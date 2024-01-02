package bubble.game;

import javax.swing.*;
import java.awt.*;

public class endingPanel extends JPanel {
    private JLabel happyEndLabel;
    private JLabel gameClearLabelLeft;
    private JLabel gameClearLabelRight;
    private Image Heart;
    private Image Gif_image;
    private Font font = new Font("Bubble Bobble", Font.CENTER_BASELINE, 20);

    public endingPanel() {
        this.setSize(1000,800);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        // HAPPY END Label 추가
        happyEndLabel = new JLabel("HAPPY END !!!");
        happyEndLabel.setFont(font.deriveFont(Font.BOLD, 50)); // 폰트 스타일 변경
        happyEndLabel.setForeground(Color.YELLOW); // 글자 색 변경
        happyEndLabel.setBounds(380, 150, 400, 50); // 원하는 위치 및 크기 설정
        this.add(happyEndLabel);

        Heart = Toolkit.getDefaultToolkit().createImage("image/EndHeart.png");
        int heartY = happyEndLabel.getY() + happyEndLabel.getHeight() + 20; // happyEndLabel 아래로 20 픽셀 이동
        ImageIcon heartIcon = new ImageIcon(Heart);
        JLabel heartLabel = new JLabel(heartIcon);
        heartLabel.setBounds(400, heartY, heartIcon.getIconWidth(), heartIcon.getIconHeight());
        this.add(heartLabel);

        gameClearLabelLeft = new JLabel("Game Clear !!!");
        gameClearLabelLeft.setFont(font.deriveFont(Font.BOLD, 30)); // 폰트 스타일 변경
        gameClearLabelLeft.setForeground(Color.GREEN); // 글자 색 변경
        gameClearLabelLeft.setBounds(happyEndLabel.getX() - 270, 350, 200, 30); // 원하는 위치 및 크기 설정
        this.add(gameClearLabelLeft);

        gameClearLabelRight = new JLabel("Game Clear !!!");
        gameClearLabelRight.setFont(font.deriveFont(Font.BOLD, 30)); // 폰트 스타일 변경
        gameClearLabelRight.setForeground(Color.GREEN); // 글자 색 변경
        gameClearLabelRight.setBounds(700, 350, 200, 30); // 원하는 위치 및 크기 설정
        this.add(gameClearLabelRight);

        Gif_image = Toolkit.getDefaultToolkit().createImage("image/GIF_IMG.gif");
        int gifY = heartLabel.getY() + heartLabel.getHeight() + 20; // heartLabel 아래로 20 픽셀 이동
        ImageIcon gifIcon = new ImageIcon(Gif_image);
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(20, 300, gifIcon.getIconWidth(), gifIcon.getIconHeight());
        this.add(gifLabel);
    }
}
