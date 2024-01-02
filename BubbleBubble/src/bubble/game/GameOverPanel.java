package bubble.game;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    private JButton gameClearLabelLeft;
    private JButton gameClearLabelRight;
    private Image gifImage1;
    private Image gifImage2;
    private Image gifImage3;
    private Font font = new Font("Bubble Bobble", Font.CENTER_BASELINE, 20);

    public GameOverPanel() {
        this.setSize(1000, 800);
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        // GAME OVER Label 추가
        ImageIcon gameOverIcon = new ImageIcon("image/GameOver.png");
        Image resizedGameOverImage = gameOverIcon.getImage().getScaledInstance(
                (int) (gameOverIcon.getIconWidth() * 1.5),
                (int) (gameOverIcon.getIconHeight() * 1.5),
                Image.SCALE_DEFAULT
        );
        JLabel gameOverLabel = new JLabel(new ImageIcon(resizedGameOverImage));
        gameOverLabel.setBounds(370, 100, (int) (gameOverIcon.getIconWidth() * 1.5), (int) (gameOverIcon.getIconHeight() * 1.5));
        this.add(gameOverLabel);

        // RETRY 버튼 추가
        gameClearLabelLeft = new JButton("RETRY");
        gameClearLabelLeft.setFont(font.deriveFont(Font.BOLD, 30));
        gameClearLabelLeft.setForeground(Color.GREEN);
        gameClearLabelLeft.setBounds(200, 500, 200, 30);
        this.add(gameClearLabelLeft);

        // MENU 버튼 추가
        gameClearLabelRight = new JButton("MENU");
        gameClearLabelRight.setFont(font.deriveFont(Font.BOLD, 30));
        gameClearLabelRight.setForeground(Color.GREEN);
        gameClearLabelRight.setBounds(600, 500, 200, 30);
        this.add(gameClearLabelRight);

        // Gif 이미지 추가 1
        gifImage1 = Toolkit.getDefaultToolkit().createImage("image/GameOverGif3.gif");
        ImageIcon gifIcon1 = new ImageIcon(gifImage1.getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        JLabel gifLabel1 = new JLabel(gifIcon1);
        gifLabel1.setBounds(800, 600, 150, 150);  // Set the size to 150*150
        this.add(gifLabel1);

        // Gif 이미지 추가 2
        gifImage2 = Toolkit.getDefaultToolkit().createImage("image/GameOverGif3.gif");
        ImageIcon gifIcon2 = new ImageIcon(gifImage2.getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        JLabel gifLabel2 = new JLabel(gifIcon2);
        gifLabel2.setBounds(30, 600, 150, 150);  // Set the size to 150*150
        this.add(gifLabel2);

        // Gif 이미지 추가 3
        gifImage3 = Toolkit.getDefaultToolkit().createImage("image/GameOverGif2.gif");
        ImageIcon gifIcon3 = new ImageIcon(gifImage3);
        JLabel gifLabel3 = new JLabel(gifIcon3);
        gifLabel3.setBounds(450, 480, gifIcon3.getIconWidth(), gifIcon3.getIconHeight());
        this.add(gifLabel3);
    }
}