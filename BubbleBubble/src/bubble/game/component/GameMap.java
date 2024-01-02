package bubble.game.component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameMap extends JPanel {

    private JLabel backgroundMap;
    private ArrayList<Enemy> enemyList;
    private Player player; // Player 추가

    public GameMap(ArrayList<Enemy> enemyList, Player player) {
        this.enemyList = enemyList;
        this.player = player;
        initializeComponents();
    }

    private void initializeComponents() {
        backgroundMap = new JLabel(new ImageIcon("image/round01_back.png"));
        add(backgroundMap);

        // Player 추가
        add(player);

        // Enemy 리스트에 있는 각각의 Enemy를 패널에 추가
        for (Enemy enemy : enemyList) {
            add(enemy);
        }
    }

    // 적이 모두 죽었는지 확인
    public boolean enemyDie() {
        return enemyList.isEmpty(); // 간단한 방법으로 리스트가 비어있는지 확인합니다.
    }

    // 배경을 변경
    public void changeBackground() {
        if (enemyDie()) {
            backgroundMap.setIcon(new ImageIcon("image/round02.png"));
        }
    }

    // 패널을 반환하는 메서드
    public JPanel getGameMapPanel() {
        return this;
    }
}
