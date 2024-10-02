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
import service.dto.CloseSearch;
import service.dto.CompanyState;
import service.excel.fnExcelAndConn;

public class basedOnApi extends fnExcelAndConn {

	public basedOnApi(String filename) {
		this.filename = filename;
		this.vendernoList = loadExcel(filename);
		work();
	}

	@Override
	protected void work() {
		int quo = vendernoList.size() / 100;
		int rem = vendernoList.size() % 100;
		for (int i = 0; i <= quo; i++) {
			createConn();
			if (i != quo) {
				tmpList = vendernoList.subList(i * 100, (i + 1) * 100);
			} else {
				tmpList = vendernoList.subList(i * 100, i * 100 + rem);
			}
			makeData();
			sendAndRecive();
			fromDataToList();
		}
		saveExcel(stateList);
	}

	@Override
	protected void createConn() {
		try {
			URL url = new URL(
					"https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=Mo9C4whIEnLm1FdqcjUKDQai4tEqpArojEpssvE5zg4eCi8PgGvdqFG2gcp2iP%2FKGU60jdNf4p8wgUJA8oFG%2BA%3D%3D");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Authorization",
					"zV7Au5dWmOA+TY+TDuteVsO50XOrDt17+IDNyHzuSndviY9OrehvzQMJg50Wqs/Je29U0VZW/g0/TDXN27IBNQ==");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(20000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void makeData() {
		setJsonStr = "{  \"b_no\": [    \"";
		for (int i = 0; i < tmpList.size(); i++) {
			if (i == tmpList.size() - 1) {
				setJsonStr += tmpList.get(i) + "\"  ]}";
			} else {
				setJsonStr += tmpList.get(i) + "\", \"";
			}
		}
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
		try {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String now = formatter.format(date);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(getJsonStr);
			JSONArray dataArray = (JSONArray) jsonObject.get("data");
			for (int j = 0; j < dataArray.size(); j++) {
				JSONObject obj = (JSONObject) dataArray.get(j);
				CompanyState cs = new CompanyState();
				cs.setVenderno((String)obj.get("b_no"));
				if("03".equals(obj.get("b_stt_cd"))) {
					String enddt = (String)obj.get("end_dt");
					cs.setState(obj.get("b_stt") + " (" + obj.get("tax_type") + " 폐업일자:" + enddt.substring(0, 4) + "-" + enddt.substring(4, 6) + "-" + enddt.substring(6, 8) + ")");
					cs.setClosed((String)obj.get("b_stt"));
					cs.setCloseDt(enddt.substring(0, 4) + "-" + enddt.substring(4, 6) + "-" + enddt.substring(6, 8));
				} else if ("02".equals(obj.get("b_stt_cd"))){
					cs.setState(obj.get("b_stt") + " (" + obj.get("tax_type") + ")");
					cs.setClosed((String)obj.get("b_stt"));
					cs.setCloseDt("");
				} else {
					cs.setState((String)obj.get("tax_type"));
					cs.setClosed("");
					cs.setCloseDt("");
				}
				cs.setDt(now);
				stateList.add(cs);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
