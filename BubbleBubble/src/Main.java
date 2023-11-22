import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {
	
	private ImageIcon img_icon = new ImageIcon("image/game_intro.png");
	private Font font1 = new Font("D2Coding", Font.CENTER_BASELINE, 20);
	private JFrame frame;
	private JButton startBtn;
	private JLabel img_label;
	private JTextField textField;
	
	public Main() {
		setFrame();
		setStartButton();
		setImage();
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
                frame.dispose(); // 시작 화면 프레임 닫기
                new BubbleGame(); // BubbleGame 호출
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
		new Main();
	}
}