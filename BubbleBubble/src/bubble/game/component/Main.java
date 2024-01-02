package bubble.game.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
	private Image img_icon;
	private Font font1 = new Font("d2coding", Font.CENTER_BASELINE, 20);
	private JButton startBtn;
	private JLabel img_label;
	private JTextField textField;

	private JFrame mainFrame;
	private JPanel mainPanel; // Main 클래스의 패널
	private Level level; //Level 클래스

	public Main() {	
		setFrame();
		setImage();
		setStartButton();
		mainFrame.setVisible(true);
	}
	
	public void setStartButton() {
		startBtn = new JButton("JOIN");
        startBtn.setBounds(400,440,200,50);
        startBtn.setBorderPainted(false); // 버튼 테두리 설정
        startBtn.setContentAreaFilled(false); // 버튼 영역 배경 표시 설정
        startBtn.setFocusPainted(false); // 포커스 표시 설정
        startBtn.setFont(font1); //폰트 설정
        startBtn.setForeground(Color.RED); //글자 색 설정
        mainPanel.add(startBtn); //시작 버튼 추가
        
        //시작 버튼에 대한 ActionListener 
        addActionToStartButton();
    }
	
	public void addActionToStartButton() {
	    startBtn.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
				String name = textField.getText().trim(); // textField에서 입력된 이름 가져오기
				mainFrame.getContentPane().removeAll();
				level = new Level(name, mainFrame);
				mainFrame.getContentPane().add(level);
				mainPanel.setFocusable(false);
				mainFrame.revalidate(); // 프레임을 다시 그리도록 호출
				mainFrame.repaint();
	        }
	    });
	}	
	
	public void setImage() {
		img_icon = Toolkit.getDefaultToolkit().createImage("image/game_intro.png");
		img_label = new JLabel(new ImageIcon(img_icon));
		img_label.setBounds(15, -200, 960, 960);
		mainPanel.add(img_label);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setVisible(true);
	}
	
	public void setFrame() {	
		mainFrame = new JFrame("시작 화면");
		mainFrame.setSize(1000, 800);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setAlwaysOnTop(true);

		// Main 클래스의 패널 생성
        mainPanel = new JPanel();       
        mainPanel.setLayout(null);
        mainFrame.getContentPane().add(mainPanel); // mainPanel을 프레임에 추가
        mainPanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
        mainPanel.setBackground(Color.black);
        
        //JTextField 설정
        textField = new JTextField();
        textField.setFont(font1);
        textField.setBounds(400, 400, 200, 30);
		mainPanel.add(textField);
	}

	public static void main(String[] args) { 
		new Main();
	}
}