package service.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import service.dto.CompanyState;
import service.excel.fnExcelAndConn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // 성우가 추가했음. 사유(org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; 예기치 않은 파일의 끝입니다.)

public class basedOnScrap extends fnExcelAndConn {

	String venderno = "";
	ObjectMapper objectMapper = new ObjectMapper(); // 성우가 추가했음.

	public basedOnScrap(String filename) {
		System.out.println("진행1");
		this.filename = filename;
		this.vendernoList = loadExcel(filename); //여기서 엑셀 파일 값을 불러옴
		work();
	}

	@Override
	protected void work() {
		System.out.println("진행2");
		try {
			makeData();
			for (int i = 0; i < tmpList.size(); i++) { // tmpList 를 스켄해야하는 수량으로하면 끝날려나?
				venderno = vendernoList.get(i); // 반복되는 횟수를 지정하는듯?
				setJsonStr = tmpList.get(i);
				createConn();
				sendAndRecive();
				System.out.println("워크1");
				fromDataToList();
				System.out.println("워크2");
				Thread.sleep(500L);
				if (getJsonStr == "" || getJsonStr == null) {
					System.out.println("국세청 차단으로 인한 1분 30초 LOCK"); // 1분 30초는 국세청에서 정한건가?
					Thread.sleep(90000L);
				}
				System.out.println(i + "건");
			}
			saveExcel(stateList);
		} catch (Exception e) {
			System.out.println("워크 케치");
			e.printStackTrace();
		}
	}

	@Override
	protected void createConn() { // API 리퀘스트 보낼 때 기본값인듯?
		System.out.println("진행3");
		try {
			URL url = new URL("https://teht.hometax.go.kr/wqAction.do?actionId=ATTABZAA001R08");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/xml");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(20000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void makeData() {
		System.out.println("진행4");
		for (String data : vendernoList) {
			StringBuilder sbXml = new StringBuilder();
			sbXml.append("<map id=\"ATTABZAA001R08\">");
			sbXml.append("<inqrTrgtClCd>1</inqrTrgtClCd><txprDscmNo>" + data + "</txprDscmNo><psbSearch>Y</psbSearch>");
			sbXml.append("<map id=\"userReqInfoVO\"></map></map>");
			tmpList.add(sbXml.toString());
		}
	}

	@Override
	protected void sendAndRecive() {
		System.out.println("진행5");
		System.out.println("진행5에서 값확인 " + setJsonStr);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			getJsonStr = "";
			osw.write(setJsonStr);
			osw.flush();
			
			int HttpResult = conn.getResponseCode();
			System.out.println("진행5에서 if문 진입전 int HttpResult 값 확인 = " + HttpResult);
			if (HttpResult == HttpURLConnection.HTTP_OK) { // 여길 안들어오네?
				System.out.println("진행5에서 if문 진입");
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line = "";
				while ((line = br.readLine()) != null) {
					System.out.println("진행5에서 line 값 " + getJsonStr);
					getJsonStr += line;
				}
				br.close();
			}
			
		} catch (

		Exception e) {
			System.out.println("진행5에서 케치에 걸리나? ");
			e.printStackTrace();
		}
	}
	
	@Override
	protected void fromDataToList() {
		System.out.println("진행6");
		System.out.println("0이야~");
		try {
			System.out.println("1이야~");
			CompanyState cs = new CompanyState();
			System.out.println("2이야~");
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(getJsonStr)));
			
			
			//xml 파일을 파싱하는 기능이라는듯?
	        System.out.println("1");
	        Element el = doc.getDocumentElement();
	        System.out.println("2");
	        Date date = new Date();
	        System.out.println("3");
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        System.out.println("4");
	        String now = formatter.format(date);
	        System.out.println("5");
	        el.getAttribute("map");
	        System.out.println("6");
	        String trtCntn = el.getElementsByTagName("trtCntn").item(0).getTextContent();
	        System.out.println("7");

	        int cutCnt = trtCntn.indexOf(" 입니다");
	        System.out.println("8");
	        if (cutCnt != -1) {
	            String result = trtCntn.substring(0, cutCnt);
	            cs.setState(result);
	            cs.setVenderno(venderno);
	            cs.setDt(now);
	            cs.setClosed("");
	            cs.setCloseDt("");
	            
	            if (trtCntn.indexOf("폐업자") > -1) {
	            	System.out.println("폐업자 if 들어옴 = " + cs);
	                cs.setClosed("폐업자");
	                cs.setCloseDt(trtCntn.substring(cutCnt - 11, cutCnt - 1));
	            }
	            if (trtCntn.indexOf("휴업자") > -1) {
	            	System.out.println("휴업자 if 들어옴 = " + cs);
	                cs.setClosed("휴업자");
	            }
	            stateList.add(cs);
	        } else {
	            System.out.println("올바른 데이터 형식이 아닙니다.");
	        }
	    } catch (Exception e) {
	    	System.out.println("fromDataToList 메서드 케치걸림 ㄷㄷ");
	        e.printStackTrace();
	    }
	}
	
//	@Override
//	protected void fromDataToList() {
//		System.out.println("진행6");
//		System.out.println("0이야~");
//	    try {
//	    	System.out.println("1이야~");
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        System.out.println("2이야~");
//	        JsonNode rootNode = objectMapper.readTree(getJsonStr);
//	        System.out.println("rootNode 값 확인 = " + rootNode.toString());
//	        System.out.println("1");
//	        if (rootNode != null && rootNode.isArray()) {
//	            for (JsonNode node : rootNode) {
//	                String trtCntn = node.get("trtCntn").asText();
//	                CompanyState cs = new CompanyState();
//	                
//	                Date date = new Date();
//	                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//	                String now = formatter.format(date);
//	                
//	                int cutCnt = trtCntn.indexOf(" 입니다");
//	                if (cutCnt != -1) {
//	                    String result = trtCntn.substring(0, cutCnt);
//	                    cs.setState(result);
//	                    cs.setVenderno(venderno);
//	                    cs.setDt(now);
//	                    cs.setClosed("");
//	                    cs.setCloseDt("");
//	                    
//	                    if (trtCntn.indexOf("폐업자") > -1) {
//	                    	System.out.println("폐업자 if 들어옴 = " + cs);
//	                        cs.setClosed("폐업자");
//	                        cs.setCloseDt(trtCntn.substring(cutCnt - 11, cutCnt - 1));
//	                    }
//	                    if (trtCntn.indexOf("휴업자") > -1) {
//	                    	System.out.println("휴업자 if 들어옴 = " + cs);
//	                        cs.setClosed("휴업자");
//	                    }
//	                    stateList.add(cs);
//	                } else {
//	                    System.out.println("올바른 데이터 형식이 아닙니다.");
//	                }
//	            }
//	        } else {
//	            System.out.println("JSON 데이터가 올바르지 않습니다.");
//	        }
//	    } catch (Exception e) {
//	    	System.out.println("fromDataToList 메서드 케치걸림 ㄷㄷ");
//	        e.printStackTrace();
//	    }
//	}


}
