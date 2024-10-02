package main;

import java.io.File;

public class CheckWritePermission {
// 파일 읽고 쓰기 권한 확인 클레스
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// 저장되어야 하는 디렉토리 경로
//        String directoryPath = "D:/중요한 상자/마스타자동차 휴폐업/";
        String directoryPath = "C:/Users/user-pc/Downloads/";
        
        // 디렉토리 객체 생성
        File directory = new File(directoryPath);

        // 디렉토리에 쓰기 권한이 있는지 확인
        boolean canWrite = directory.canWrite();

        // 쓰기 권한 결과 출력
        if (canWrite) {
            System.out.println("쓰기 권한이 있습니다.");
        } else {
            System.out.println("쓰기 권한이 없습니다.");
        }
		
	}

}
