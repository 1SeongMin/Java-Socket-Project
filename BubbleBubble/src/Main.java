import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {

	public static void main(String[] args) {
		JFrame frame = new JFrame("시작 화면");
	        
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        //버튼
        JButton startBtn = new JButton("게임 시작");
        JButton rankingBtn = new JButton("순위 표");
        startBtn.setBounds(300,170,100,30);
        rankingBtn.setBounds(300, 230,100,30);
        
        frame.getContentPane().add(startBtn);
        frame.getContentPane().add(rankingBtn);
        
        //시작 화면 이미지 추가해야 함!!

        frame.setVisible(true);
	}
}