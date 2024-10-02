package tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.dto.CompanyState;

public class Utill {
	public static List<String> loadExcel(String filename) {
		List<String> vendernoList = new ArrayList<String>();
		try {
			XSSFWorkbook readBook = new XSSFWorkbook(filename);
			XSSFSheet readSheet = readBook.getSheetAt(0);
			DataFormatter dataFormatter = new DataFormatter();
			for (Row row : readSheet) {
				vendernoList.add(dataFormatter.formatCellValue(row.getCell(0)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vendernoList;
	}
	
	public static void saveExcel(List<CompanyState> data) {
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
			fos = new FileOutputStream("C:/Users/net/Downloads/휴폐업파일.xlsx");
			createBook.write(fos);
			if (fos != null) {
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
