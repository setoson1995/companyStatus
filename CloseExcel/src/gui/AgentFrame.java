package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import service.process.basedOnApiServer;
import service.excel.ClosedCompany;
import service.process.basedOnApi;
import service.process.basedOnApi2;
import service.process.basedOnScrap;


public class AgentFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JPanel pane1 = new JPanel();
	JPanel pane2 = new JPanel();
	JPanel pane3 = new JPanel();
	JButton upload = new JButton("업로드");
	JButton download = new JButton("휴폐업 조회");
	JTextField path1 = new JTextField(20);
	JTextField path2 = new JTextField(20);

	public AgentFrame() {
		super("에이전트");
		Container container = getContentPane();
		setSize(300, 400);
		
		 // 화면의 크기를 가져옴
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 화면의 중앙에 프레임이 위치하도록 설정
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		upload.addActionListener(this);
		download.addActionListener(this);
		pane1.setLayout(new GridLayout(1, 1, 0, 0));
		pane2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));
		pane2.add(path1);
		pane2.add(upload);
		pane2.add(download);
		pane1.add(pane2);
		pane1.add(pane3);
		container.add(pane1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == upload) {
			path1.setText(selectFileDirectory());
		}
		if (obj == download) {
//			new basedOnScrap(path1.getText()); //여기는 속도느린 스크레핑
			new basedOnApi(path1.getText()); // 빠른 API
		}
	}

	public static String selectFileDirectory() {
		Component com = null;
		JFileChooser fchooser = null;

		fchooser = new JFileChooser();

		fchooser.setDialogTitle("저장");

		fchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int rval = fchooser.showOpenDialog(com);

		if (rval == JFileChooser.APPROVE_OPTION) {
			return fchooser.getSelectedFile().getPath();
		} else if (rval == JFileChooser.CANCEL_OPTION) {
			return "";
		}
		return "";
	}
	
	public static String selectDirectory() {
		Component com = null;
		JFileChooser fchooser = null;

		fchooser = new JFileChooser();

		fchooser.setDialogTitle("저장");

		fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int rval = fchooser.showOpenDialog(com);

		if (rval == JFileChooser.APPROVE_OPTION) {
			return fchooser.getSelectedFile().getPath() + "\\";
		} else if (rval == JFileChooser.CANCEL_OPTION) {
			return "";
		}
		return "";
	}
}
