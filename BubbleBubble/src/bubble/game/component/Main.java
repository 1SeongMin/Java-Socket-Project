package bubble.game.component;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import bubble.game.BubbleFrame;
import bubble.game.server.JavaChatServer;

public class Main extends JFrame {
	
	private ImageIcon img_icon = new ImageIcon("image/game_intro.png");
	private Font font1 = new Font("D2Coding", Font.CENTER_BASELINE, 20);
	private JFrame frame;
	private JButton startBtn;
	private JLabel img_label;
	private JTextField textField;
	
	private String playerName;
	private static JavaChatServer server;
	private Level levelInstance;
	private Level levelPanel;
	
	public Main(JavaChatServer server) {
		this.server = server;
		setFrame();
		setImage();
		setStartButton();
	}
	
	public void setStartButton() {
		startBtn = new JButton("Game Start");
        startBtn.setBounds(400,450,200,50);
        startBtn.setBorderPainted(false); // 버튼 테두리 설정
        startBtn.setContentAreaFilled(false); // 버튼 영역 배경 표시 설정
        startBtn.setFocusPainted(false); // 포커스 표시 설정
        startBtn.setFont(font1); //폰트 설정
        startBtn.setForeground(Color.RED); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton();
    }
	
	 public void addActionToStartButton() {
	        startBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                playerName = textField.getText(); // 텍스트 필드에서 입력된 이름 가져오기

	                if (!playerName.isEmpty()) { // 이름 있는 경우만 서버에 클라이언트 추가
	                    server.addClient(playerName); // 클라이언트 추가

	                    if (levelInstance == null) { // Level 인스턴스가 없는 경우에만 생성
	                        levelInstance = new Level(server); // Level 인스턴스 생성
	                    }

	                    // 레벨 창에 클라이언트 추가
	                    levelInstance.addPlayer(playerName); // 이 부분은 Level 클래스에 해당하는 메서드로 변경해야 함
	                }
	                frame.dispose(); // 시작 화면 프레임 닫기
	            }
	        });
	    }
	
	public void setImage() {
		img_label = new JLabel(img_icon);
		img_label.setBounds(15, -200, img_icon.getIconWidth(), img_icon.getIconWidth());
		frame.getContentPane().add(img_label);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setVisible(true);
	}
	
	public void setFrame() {	
		frame = new JFrame("시작 화면");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //JTextField 설정
        textField = new JTextField();
        textField.setFont(font1);
        textField.setBounds(400, 400, 200, 30);
        frame.getContentPane().add(textField);
	}

	public static void main(String[] args) {
		JavaChatServer server = JavaChatServer.getInstance(); 
		new Main(server);
	}
}
