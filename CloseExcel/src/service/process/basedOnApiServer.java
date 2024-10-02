package service.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;
import service.dto.CloseSearch;
import service.dto.CompanyState;
import service.excel.fnExcelAndConn;

public class basedOnApiServer extends fnExcelAndConn {

	public basedOnApiServer(String filename) {
		this.filename = filename;
		this.vendernoList = loadExcel(filename);
		work();
	}
	
	@Override
	protected void work() {
		makeData(); // 전송 Data 타입으로 변경
		createConn(); // Connection 생성
		sendAndRecive(); // 데이터 전송 및 수신
		fromDataToList(); // 받은 데이터 리스트 형식으로 변환
	}

	@Override
	protected void createConn() {
		try {
			URL url = new URL("https://localhost:444/api/agent/companyclosesearch");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("SBKEY", "localtest");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void makeData() {
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("auth_venderno", "0000000029");
		resultMap.put("venderno", vendernoList);
		Gson gson = new Gson();
		setJsonStr = gson.toJson(resultMap);
	}

	@Override
	protected void sendAndRecive() {
		try {
			getJsonStr = "";
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(setJsonStr);
			osw.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				getJsonStr += line;
			}
			osw.close();
			br.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void fromDataToList() {
		List<CloseSearch> closeSearchList = new ArrayList<CloseSearch>();
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(getJsonStr);
			if (jsonObject.get("Result") == "0") {
				System.out.println(jsonObject.get("Message"));
			}
			JSONObject dataObject = (JSONObject) jsonObject.get("Data");
			JSONArray dataArray = (JSONArray) dataObject.get("data");
			for (int j = 0; j < dataArray.size(); j++) {
				JSONObject obj = (JSONObject) dataArray.get(j);
				CloseSearch cs = new CloseSearch.Builder()
						.closeCode((String) obj.get("closeCode"))
						.message((String) obj.get("message"))
						.venderno((String) obj.get("venderno"))
						.closeDt((String) obj.get("closeDt"))
						.build();
				closeSearchList.add(cs);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
