package bubble.game;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    private JLabel playerNameLabel1;
    private JLabel playerNameLabel2;
    private JLabel scoreLabel;
    private JLabel scoreLabel2;
    private JLabel totalScoreLabel;
    private Font font = new Font("Bubble Bobble", Font.CENTER_BASELINE, 30);

    private int scoreSum1 = 0; // 플레이어 1의 누적 점수
    private int scoreSum2 = 0; // 플레이어 2의 누적 점수
    private int totalScore = 0; // 전체 누적 점수

    public ScorePanel() {
        initUI();
    }

    private void initUI() {
        this.setSize(1000,40);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        //이름1
        playerNameLabel1 = new JLabel("");
        playerNameLabel1.setFont(font);
        playerNameLabel1.setForeground(Color.GREEN);
        playerNameLabel1.setBounds(10, 5, 100, 30); // X좌표, Y좌표, 너비, 높이
        this.add(playerNameLabel1);

        //점수1
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(font);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(150, 5, 100, 30); // X좌표, Y좌표, 너비, 높이
        this.add(scoreLabel);

        totalScoreLabel = new JLabel("0");
        totalScoreLabel.setFont(font);
        totalScoreLabel.setForeground(Color.WHITE);
        totalScoreLabel.setBounds(480, 5, 100, 30); // X좌표, Y좌표, 너비, 높이
        this.add(totalScoreLabel);

        //이름2
        playerNameLabel2 = new JLabel("");
        playerNameLabel2.setFont(font);
        playerNameLabel2.setForeground(new Color(255, 0, 255));
        playerNameLabel2.setBounds(930, 5, 100, 30); // X좌표, Y좌표, 너비, 높이
        this.add(playerNameLabel2);

        //점수2
        scoreLabel2 = new JLabel("0");
        scoreLabel2.setFont(font);
        scoreLabel2.setForeground(Color.WHITE);
        scoreLabel2.setBounds(800, 5, 100, 30); // X좌표, Y좌표, 너비, 높이
        this.add(scoreLabel2);

        setVisible(true);
    }

    public void updateScore(int score) {
        this.scoreSum1 += score;
        scoreLabel.setText(String.valueOf(score));
    }
    public void updateScore2(int score) {
        this.scoreSum2 = score;
        scoreLabel2.setText(String.valueOf(score));
    }
    public void updateTotalScore(int score) {
        //totalScore = sum1 + sum2; // totalScore 필드에 누적 점수 저장
        totalScoreLabel.setText(String.valueOf(score));
    }

    public int getScore() { return scoreSum1;  }
    public int getScore2() { return scoreSum2; }
    public int getTotalScore() { return totalScore; }

    public void setPlayerNameLabel1(String PlayerName1) { playerNameLabel1.setText(PlayerName1); }
    public void setPlayerNameLabel2(String PlayerName2) { playerNameLabel2.setText(PlayerName2); }
}
