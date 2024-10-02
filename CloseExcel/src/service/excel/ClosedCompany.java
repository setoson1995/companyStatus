package service.excel;

import java.net.HttpURLConnection;

public interface ClosedCompany {

	public void run(String path);
	public String convertSendData(Object obj);
	public HttpURLConnection createConn();
	public String SendAndRecipt(String str, HttpURLConnection conn);
	public Object convertReciptData(String obj);
	
}
