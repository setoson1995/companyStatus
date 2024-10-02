package service.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.dto.CompanyState;

public abstract class fnExcelAndConn {
	
	protected List<String> tmpList = new ArrayList<String>();
	protected List<CompanyState> stateList = new ArrayList<CompanyState>();
	protected List<String> vendernoList = new ArrayList<String>();
	protected HttpURLConnection conn;
	protected String filename = ""; // 파일 경로 및 이름
	protected String setJsonStr = ""; // 송신보낼 Json 문자열
	protected String getJsonStr = ""; // 수신받은 Json 문자열
	/*
	 * createConn : HttpURLConnection 세팅
	 * makeJson : Json 데이터 생성
	 * sendAndRecive : 데이터 송신 및 수신
	 * fromJsonToList : Json 데이터 List로 타입으로 변환
	 * loadExcel 엑셀 데이터 List 타입으로 변환
	 * saveExcel List 데이터 엑셀 파일로 저장
	 */
	protected abstract void work();
	protected abstract void createConn();
	protected abstract void makeData();
	protected abstract void sendAndRecive();
	protected abstract void fromDataToList();
	protected List<String> loadExcel(String filename) { //여기서 엑셀 파일 값을 불러오는 듯?
		System.out.println("loadExcel메서드");
		List<String> vendernoList = new ArrayList<String>();
		try {
			XSSFWorkbook readBook = new XSSFWorkbook(filename);
			XSSFSheet readSheet = readBook.getSheetAt(0);
			DataFormatter dataFormatter = new DataFormatter();
			for (Row row : readSheet) { // 여기서 엑셀 값의 크기대로 반복하는데, 교정이 필요한듯?
				vendernoList.add(dataFormatter.formatCellValue(row.getCell(0)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("리턴된 값 = " + vendernoList);
		return vendernoList;
	}
	
	protected void saveExcel(List<CompanyState> data) {
		XSSFWorkbook createBook = new XSSFWorkbook();
		XSSFSheet createSheet = createBook.createSheet("Data");
		FileOutputStream fos = null;
		try {
			for (int row = 0; row < data.size(); row++) {
				XSSFRow createRow = createSheet.createRow(row);
				CompanyState cs = data.get(row);
				Cell venderno = createRow.createCell(0);
				Cell state = createRow.createCell(1);
				Cell closed = createRow.createCell(2);
				Cell closeDt = createRow.createCell(3);
				Cell dt = createRow.createCell(4);
				venderno.setCellValue(cs.getVenderno());
				state.setCellValue(cs.getState());
				closed.setCellValue(cs.getClosed());
				closeDt.setCellValue(cs.getCloseDt());
				dt.setCellValue(cs.getDt());
			}
			// 조회되서 가공된 파일이 생성되는곳 설정
			fos = new FileOutputStream("D:/중요한 상자/마스타자동차 휴폐업/마스타 사업자조회_" + System.currentTimeMillis()/1000 + ".xlsx"); // "마스타 사업자조회" 이 단어는 파일 이름으로 들어감
//			fos = new FileOutputStream("C:/Users/net/Downloads/마스타 사업자조회 결과_" + System.currentTimeMillis()/1000 + ".xlsx");
//			fos = new FileOutputStream("D:/중요한 상자/마스타 사업자조회 결과_" + System.currentTimeMillis()/1000 + ".xlsx");
			
			createBook.write(fos);
			if (fos != null) {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
