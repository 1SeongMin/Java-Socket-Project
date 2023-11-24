package bubble.game.component;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import bubble.game.BubbleFrame;

public class Level extends JFrame {
	
	private ImageIcon img_icon = new ImageIcon("image/game_intro.png");
	private Font font1 = new Font("D2Coding", Font.CENTER_BASELINE, 20);
	private JFrame frame;
	private JButton startBtn;
	private JLabel img_label;
	
	public Level() {
		setFrame();
		setStartButton1();
		setStartButton2();
		setStartButton3();
		setImage();
	}
	
	public void setStartButton1() {
		startBtn = new JButton("LEVEL 1");
        startBtn.setBounds(176,482,200,50);
        startBtn.setBorderPainted(false); // 버튼 테두리 설정
        startBtn.setContentAreaFilled(false); // 버튼 영역 배경 표시 설정
        startBtn.setFocusPainted(false); // 포커스 표시 설정
        startBtn.setFont(font1); //폰트 설정
        startBtn.setForeground(new Color(0, 0, 255)); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton1();
    }
	public void addActionToStartButton1() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleFrame(); // BubbleGame 호출
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
        startBtn.setForeground(new Color(0, 0, 255)); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton2();
    }
	public void addActionToStartButton2() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleFrame(); // BubbleGame 호출
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
        startBtn.setForeground(new Color(0, 0, 255)); //글자 색 설정
        frame.getContentPane().add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton3();
    }
	public void addActionToStartButton3() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleFrame(); // BubbleGame 호출
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
		frame = new JFrame("레벨 화면");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
	}

	public static void main(String[] args) {
		new Level();
	}
}