package main;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import gui.AgentFrame;
import service.dto.CompanyState;

public class Main_loadExcel {
	public static void main(String[] args) {
		System.out.println("진행0");
			new AgentFrame();
		}
}

//@Override
//protected void fromDataToList() {
//	System.out.println("진행6");
//	System.out.println("0이야~");
//	try {
//		System.out.println("1이야~");
//		CompanyState cs = new CompanyState();
//		System.out.println("2이야~");
//		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(getJsonStr)));
//		
//		
//		//xml 파일을 파싱하는 기능이라는듯?
//        System.out.println("1");
//        Element el = doc.getDocumentElement();
//        System.out.println("2");
//        Date date = new Date();
//        System.out.println("3");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println("4");
//        String now = formatter.format(date);
//        System.out.println("5");
//        el.getAttribute("map");
//        System.out.println("6");
//        String trtCntn = el.getElementsByTagName("trtCntn").item(0).getTextContent();
//        System.out.println("7");
//
//        int cutCnt = trtCntn.indexOf(" 입니다");
//        System.out.println("8");
//        if (cutCnt != -1) {
//            String result = trtCntn.substring(0, cutCnt);
//            cs.setState(result);
//            cs.setVenderno(venderno);
//            cs.setDt(now);
//            cs.setClosed("");
//            cs.setCloseDt("");
//            
//            if (trtCntn.indexOf("폐업자") > -1) {
//            	System.out.println("폐업자 if 들어옴 = " + cs);
//                cs.setClosed("폐업자");
//                cs.setCloseDt(trtCntn.substring(cutCnt - 11, cutCnt - 1));
//            }
//            if (trtCntn.indexOf("휴업자") > -1) {
//            	System.out.println("휴업자 if 들어옴 = " + cs);
//                cs.setClosed("휴업자");
//            }
//            stateList.add(cs);
//        } else {
//            System.out.println("올바른 데이터 형식이 아닙니다.");
//        }
//    } catch (Exception e) {
//    	System.out.println("fromDataToList 메서드 케치걸림 ㄷㄷ");
//        e.printStackTrace();
//    }
//}