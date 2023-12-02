package bubble.game.component;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import bubble.game.BubbleFrame;
import bubble.game.server.JavaChatServer;

public class Level extends JFrame {
	
	private ImageIcon img_icon = new ImageIcon("image/game_intro.png");
	private ImageIcon img1 = new ImageIcon("image/playerL.png");
	private ImageIcon img2 = new ImageIcon("image/playerR.png");
	
	private JavaChatServer server;
	
	private Font font1 = new Font("D2Coding", Font.CENTER_BASELINE, 20);
	private JFrame frame;
	private JButton startBtn;
	private JLabel img_label;
	private JLabel player_num;
	private JLabel player_name1;
	private JLabel player_name2;
	private JLabel player1_img;
	private JLabel player2_img;
	private String name;

	public Level(String playerName) {
		this.name = playerName;
		server = new JavaChatServer();
		setFrame();
		setStartButton1();
		setStartButton2();
		setStartButton3();
		setLabel();
		
	}
	
	public void setLabel() {
		player_num = new JLabel("0");
		player_num.setBounds(500,80,100,50);

		player_name1 = new JLabel("");
		player_name1.setBounds(237,180,100,50);
		
		player_name2 = new JLabel("");
		player_name2.setBounds(733,180,100,50);
	 
		player1_img = new JLabel(); // 이미지 경로 입력
		player1_img.setBounds(237, 230, img1.getIconWidth(), img1.getIconHeight()); 
		
	    player2_img = new JLabel(); // 이미지 경로 입력
	    player2_img.setBounds(733, 230, img2.getIconWidth(), img2.getIconHeight());
	    
		player_num.setForeground(Color.white); //글자 색 설정
		player_name1.setForeground(Color.white);
		player_name2.setForeground(Color.white);
		
		player_num.setFont(font1); //폰트 설정
		player_name1.setFont(font1);
		player_name2.setFont(font1); 
		
		System.out.println(name + " 응");

		if(name != null) {
			player_num.setText("1");
			player_name1.setText(name);
			player1_img.setIcon(img1); 
		}
	
		String p1_name = player_name1.getText();
		if (p1_name != null) {
			player_num.setText("2");
			player_name2.setText(name);
			player2_img.setIcon(img2);
		}		
		
		frame.getContentPane().add(player_num);
		frame.getContentPane().add(player_name1);
		frame.getContentPane().add(player_name2);
		frame.getContentPane().add(player1_img); // 첫 번째 플레이어 이미지 레이블 추가
	    frame.getContentPane().add(player2_img); // 두 번째 플레이어 이미지 레이블 추가
	    frame.setVisible(true);
			
	}
	
	public void setStartButton1() {
		startBtn = new JButton("LEVEL 1");
        startBtn.setBounds(176,482,200,50);
        startBtn.setBorderPainted(false); // 버튼 테두리 설정
        startBtn.setContentAreaFilled(false); // 버튼 영역 배경 표시 설정
        startBtn.setFocusPainted(false); // 포커스 표시 설정
        startBtn.setFont(font1); //폰트 설정
        startBtn.setForeground(Color.white); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton1();
    }
	public void addActionToStartButton1() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleFrame(1); // BubbleGame 호출
            }
        });
    }
	
	public void setStartButton2() {
		startBtn = new JButton("LEVEL 2");
        startBtn.setBounds(410,482,200,50);
        startBtn.setBorderPainted(false); // 버튼 테두리 설정
        startBtn.setContentAreaFilled(false); // 버튼 영역 배경 표시 설정
        startBtn.setFocusPainted(false); // 포커스 표시 설정
        startBtn.setFont(font1); //폰트 설정
        startBtn.setForeground(Color.white); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton2();
    }
	public void addActionToStartButton2() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleFrame(2); // BubbleGame 호출
            }
        });
    }
	
	public void setStartButton3() {
		startBtn = new JButton("LEVEL 3");
        startBtn.setBounds(651,482,200,50);
        startBtn.setBorderPainted(false); // 버튼 테두리 설정
        startBtn.setContentAreaFilled(false); // 버튼 영역 배경 표시 설정
        startBtn.setFocusPainted(false); // 포커스 표시 설정
        startBtn.setFont(font1); //폰트 설정
        startBtn.setForeground(Color.white); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton3();
    }
	public void addActionToStartButton3() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleFrame(3); // BubbleGame 호출
            }
        });
    }
	
	public void setFrame() {	
		frame = new JFrame("레벨 화면");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);
	}

	public static void main(String[] args) {
		new Level(null);
	}
}
