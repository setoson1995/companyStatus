package service.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.dto.CompanyState;
import service.excel.ClosedCompany;
import tool.Utill;

public class basedOnApi2 implements ClosedCompany {

	@Override
	public void run(String filepath) {
		List<CompanyState> CompanyStateList = new ArrayList<CompanyState>();
		List<CompanyState> tempList = new ArrayList<CompanyState>();
		List<String> vendernoList = Utill.loadExcel(filepath);
		
		int quo = vendernoList.size() / 100;
		int rem = vendernoList.size() % 100;
		for (int i = 0; i <= quo; i++) {
			String setJsondata = "";
			HttpURLConnection conn = createConn();
			if (i != quo) {
				setJsondata = convertSendData(vendernoList.subList(i * 100, (i + 1) * 100)); // 길이 i * 100 의 데이터 조회
			} else {
				setJsondata = convertSendData(vendernoList.subList(i * 100, i * 100 + rem)); // 100 이하 나머지 데이터 조회
			}
			String getJsondata = SendAndRecipt(setJsondata, conn); // 데이터 조회
			tempList = convertReciptData(getJsondata); // Json 데이터 List 형식으로 변환
			CompanyStateList.addAll(tempList);
		}
		Utill.saveExcel(CompanyStateList); // 엑셀 파일 저장
	}

	@Override
	public HttpURLConnection createConn() {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=Mo9C4whIEnLm1FdqcjUKDQai4tEqpArojEpssvE5zg4eCi8PgGvdqFG2gcp2iP%2FKGU60jdNf4p8wgUJA8oFG%2BA%3D%3D");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Authorization", "zV7Au5dWmOA+TY+TDuteVsO50XOrDt17+IDNyHzuSndviY9OrehvzQMJg50Wqs/Je29U0VZW/g0/TDXN27IBNQ==");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(20000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	@Override
	public String convertSendData(Object obj) {
		String setJsonStr = "";
		if (obj instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) obj;
			setJsonStr = "{  \"b_no\": [    \"";
			for (int i = 0; i <  list.size(); i++) {
				if (i == list.size() - 1) {
					setJsonStr += list.get(i) + "\"  ]}";
				} else {
					setJsonStr += list.get(i) + "\", \"";
				}
			}
		} else {
			new Exception("basedOnApi2.convertSendData.error");
		}
		return setJsonStr;
	}
	
	@Override
	public String SendAndRecipt(String str, HttpURLConnection conn) {
		String getJsonStr = "";
		try {
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			osw.write(str);
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
		return getJsonStr;
	}

	
	@Override
	public List<CompanyState> convertReciptData(String getJsonStr) {
		List<CompanyState> companyStateList = new ArrayList<CompanyState>();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String now = formatter.format(date);
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(getJsonStr);
			JSONArray dataArray = (JSONArray) jsonObject.get("data");
			for (int j = 0; j < dataArray.size(); j++) {
				JSONObject obj = (JSONObject) dataArray.get(j);
				CompanyState cs = new CompanyState.Builder()
						.venderno((String) obj.get("b_no"))
						.state((String)obj.get("b_stt") + " (" + (String) obj.get("tax_type") + ")")
						.closed((String) obj.get("b_stt"))
						.closeDt((String) obj.get("end_dt"))
						.dt(now)
						.builder();
				companyStateList.add(cs);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return companyStateList;
	}

}
