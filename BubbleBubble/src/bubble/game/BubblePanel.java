package bubble.game;

import static bubble.game.component.Protocol.ADD_ITEM_SCORE;
import static bubble.game.component.Protocol.ADD_SCORE;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import bubble.game.component.Enemy;
import bubble.game.component.GameOver;
import bubble.game.component.Item;
import bubble.game.component.Level;
import bubble.game.component.Map;
import bubble.game.component.Player;
import bubble.game.music.BGM;
import bubble.game.server.GameData;
import bubble.game.state.EnemyWay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BubblePanel extends JPanel {

    private List<Item> items = new ArrayList<>();  // 아이템 리스트 추가
    private HashMap<Integer, Integer> playerItemCounts = new HashMap<>();

    private boolean Created = false;
    private boolean twoBubbleItemEaten = false;

    private List<Enemy> enemys; // 적 -> 컬렉션으로 관리
    private BGM bgm;
    private GameOver gameOver;

    //패널, 프레임
    private BubblePanel bubblePanel = this;
    private ScorePanel scorePanel;
    private JPanel Panel;
    private JFrame frame;
    private JSplitPane SplitPane;

    //맵
    private int currentMapNumber = 1;
    private JLabel backgroundMap;
    private Map map;

    //플레이어 정보
    private Player player1; //플레이어1
    private Player player2; //플레이어2

    private int PlayerNumber; //플레이어 번호
    private String UserName; //플레이어 이름

    //플레이어의 점수
    private int scoreSum1;
    private int scoreSum2;
    private int TotalScore;

    // 플레이어1의 생명(체력) 저장하는 배열 (하트)
    private JLabel[] player1LifeLabels;

    // 플레이어2의 생명(체력) 저장하는 배열 (하트)
    private JLabel[] player2LifeLabels;

    public BubblePanel(ScorePanel scorePanel) {
        this.scorePanel = scorePanel;
        this.addKeyListener(new KeyListener());

        SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        SplitPane.setDividerLocation(40);
        SplitPane.setEnabled(false);
        SplitPane.setDividerSize(0);
        SplitPane.setBorder(null);

        SplitPane.setTopComponent(this.getScorePanel());
        SplitPane.setBottomComponent(this);
        SplitPane.setSize(1000,780);
        SplitPane.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();

        initObject();
        setVisible(true);

        GameEndingThread end = new GameEndingThread(this); //게임 엔딩을 기다리는 스레드
        end.start(); //스레드 시작 - 무한루프로 적이 모두 죽을 때 까지 기다림

        //GameStatusCheckThread gameStatusCheckThread = new GameStatusCheckThread(bubblePanel);
        //gameStatusCheckThread.start();
        
        GameOverThread over = new GameOverThread(this);
        over.start();

    }

    private class GameEndingThread extends Thread {

        private BubblePanel bubblePanel;

        public GameEndingThread(BubblePanel bubblePanel) {
            this.bubblePanel = bubblePanel;
        }

        @Override
        public void run() {
            while (true) {
                if (bubblePanel.areAllEnemiesDead()) {
                    System.out.println("모든 적 죽음");
                    try {
                        Thread.sleep(10000);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                endingPanel endingPanel = new endingPanel();
                                JFrame mainFrame = Level.getMainFrame();
                                Container contentPane = mainFrame.getContentPane();
                                contentPane.removeAll();
                                contentPane.add(endingPanel);
                                contentPane.revalidate();
                                contentPane.repaint();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }

                // 스레드 일시 중지 (게임 종료 조건을 확인하는 간격을 조절하기 위해)
                try {
                    Thread.sleep(3000); // 1초마다 확인
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }    
    
    private class GameOverThread extends Thread {

        private BubblePanel bubblePanel;

        public GameOverThread(BubblePanel bubblePanel) {
            this.bubblePanel = bubblePanel;
        }

        @Override
        public void run() {
            while (true) {
                if (bubblePanel.areAllPlayersDead()) {
                    System.out.println("모든 플레이어 죽음");
                    try {
                        Thread.sleep(10000);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                GameOverPanel GameOverPanel = new GameOverPanel();
                                JFrame mainFrame = Level.getMainFrame();
                                Container contentPane = mainFrame.getContentPane();
                                contentPane.removeAll();
                                contentPane.add(GameOverPanel);
                                contentPane.revalidate();
                                contentPane.repaint();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }

                // 스레드 일시 중지 (게임 종료 조건을 확인하는 간격을 조절하기 위해)
                try {
                    Thread.sleep(3000); // 1초마다 확인
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private class GameStatusCheckThread extends Thread {
        private BubblePanel bubblePanel;

        public GameStatusCheckThread(BubblePanel bubblePanel) {
            this.bubblePanel = bubblePanel;
        }

    @Override
    public void run() {
        while (true) {
            if (bubblePanel.areAllPlayersDead()) {
                System.out.println("모든 플레이어 사망");
                try {
                    Thread.sleep(10000);
                    updateUI(new GameOverPanel());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            } else if (bubblePanel.areAllEnemiesDead()) {
                System.out.println("모든 적 죽음");

                try {
                    Thread.sleep(10000);
                    updateUI(new endingPanel());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }

            try {
                Thread.sleep(1000); // 1초마다 확인
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUI(JPanel newPanel) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = Level.getMainFrame();
            Container contentPane = mainFrame.getContentPane();

            contentPane.removeAll();
            contentPane.add(newPanel);
            contentPane.revalidate();
            contentPane.repaint();
        });
    }
}

    //스플릿 팬 반환
    public JSplitPane getJSplitPane() {
        return SplitPane;
    }

    //스코어 패널 반환
    public JPanel getScorePanel() {
        return scorePanel;
    }

    //Label 배치, 적, 플레이어 추가 등.. 초기화
    private void initObject() {
        this.setSize(1000, 780); //1000, 640
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        //플레이어1의 생명(체력)
        player1LifeLabels = new JLabel[3];
        for (int j = 0; j < 3; j++) {
            player1LifeLabels[j] = new JLabel(new ImageIcon("image/life_heart.png"));
            player1LifeLabels[j].setBounds(20 + (j * 35), 680, player1LifeLabels[j].getIcon().getIconWidth(),
                    player1LifeLabels[j].getIcon().getIconHeight());
            this.add(player1LifeLabels[j]);
        }
        //플레이어2의 생명(체력)
        player2LifeLabels = new JLabel[3];
        for (int j = 0; j < 3; j++) {
            player2LifeLabels[j] = new JLabel(new ImageIcon("image/life_heart.png"));
            player2LifeLabels[j].setBounds(925 - (j * 35), 680, player2LifeLabels[j].getIcon().getIconWidth(),
                    player2LifeLabels[j].getIcon().getIconHeight());
            this.add(player2LifeLabels[j]);
        }

        //플레이어 추가
        UserName = Level.getUserName();
        PlayerNumber = Level.getCurPlayerNumber();
        player1 = new Player(this, 1);
        player2 = new Player(this, 2);
        this.add(player1);
        this.add(player2);

        //적 추가
        enemys = new ArrayList<Enemy>();
        enemys.add(new Enemy(this, EnemyWay.RIGHT));
        enemys.add(new Enemy(this, EnemyWay.LEFT));
        for(Enemy e : enemys) this.add(e);

        //노래 추가
        bgm = new BGM();
        bgm.playBGM("bgm.wav");

        //맵 추가
        map = new Map();
    }

    //플레이어의 번호에 따라 플레이어 반환
    public Player getPlayerNumber(int num) {
        if (num == 1) return player1;
        else return player2;
    }

    // 모든 플레이어 객체들의 목록을 반환하는 메서드
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        if (player1 != null) players.add(player1);
        if (player2 != null) players.add(player2);
        return players;
    }

    // 배경 이미지 그리기
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(currentMapNumber == 1) {
            ImageIcon backgroundImage = map.getMap();
            g.drawImage(backgroundImage.getImage(), 0, 30, 1000, 640, null);
        }
    }

    //--------------------------------------------------------------------------------------------------------

    //플레이어가 모두 죽었는지 확인
    public boolean areAllPlayersDead() {
        if (player1.getState() == 1 && player2.getState() == 1) {
            return true; // 두 플레이어가 모두 죽었으면 true
        } else {
            return false; // 두 플레이어 중 하나라도 살아있으면 false
        }
    }

    //적이 모두 죽었는지 확인
    public boolean areAllEnemiesDead() {
        for (Enemy enemy : enemys) {
            System.out.println("Enemy isDead: " + enemy.isDead());
            if (!enemy.isDead()) {
                return false; // 하나 이상의 적이 살아있으면 false 반환
            }
        }
        return true; // 모든 적이 죽었으면 true 반환
    }

    //점수 추가 되면 서버에 보냄 -> 적을 죽였을 때, 아이템 먹었을 때
    public synchronized void SendToServerScore(int PlayerNumber, int score) {
        GameData data = new GameData(UserName, ADD_SCORE, PlayerNumber + "#" + score);
        Level.SendObject(data);
    }

    //점수 설정 함수 ->
    public void  SetScore(String score[]) {
        int CurSum1 = scorePanel.getScore();
        int CurSum2 = scorePanel.getScore2();
        int TotalSum = scorePanel.getTotalScore();


        if (score[0].equals("1")) { //플레이어 1인 경우
            CurSum1 += Integer.parseInt(score[1]);
            scorePanel.updateScore(CurSum1);

        }
        else {  //플레이어 1인 경우
            CurSum2 += Integer.parseInt(score[1]);
            scorePanel.updateScore2(CurSum2);

        }
        TotalSum += CurSum1 + CurSum2;
        scorePanel.updateTotalScore(TotalSum);
    }

    //플레이어의 체력이 1 깎일 때 마다 해당 플레이어의 하트 감소
    public void removePlayerLife(int PlayerNumber,int index) {
        if(PlayerNumber == 1)  player1LifeLabels[index].setVisible(false);
        else player2LifeLabels[index].setVisible(false);
    }
    
    public void addPlayerLife(int PlayerNumber,int index) {
    	if(PlayerNumber == 1)  player1LifeLabels[index-1].setVisible(true);
        else player2LifeLabels[index-1].setVisible(true);
    }
    
    // 아이템 추가
    public void addItem(String itemData[]) {

        String itemName = itemData[0]; // 아이템 이름
        int x = Integer.parseInt(itemData[1]); // x 좌표 (문자열을 정수로 변환)
        int y = Integer.parseInt(itemData[2]); // y 좌표 (문자열을 정수로 변환)
        int playerNumber = Integer.parseInt(itemData[3]);

        // 해당 플레이어가 생성한 아이템의 수를 가져옴
        int itemCount = playerItemCounts.getOrDefault(playerNumber, 0);

        // 각 플레이어당 하나의 아이템만 생성
        if (itemCount < 1) {
            Item item = new Item(itemName, x, y);
            items.add(item);
            this.add(item);

            // 아이템 수 업데이트
            playerItemCounts.put(playerNumber, itemCount + 1);
        }
        repaint();
    }

    // 아이템 제거
    public void removeItem(Item item) {
        items.remove(item);
        remove(item);
        repaint();
    }

    // 아이템 점수 정보를 서버로 보내는 함수
    private void sendToServerItemScore(int PlayerNumber, int itemIndex, int scoretoAdd) {
        GameData data = new GameData("ItemScore", ADD_ITEM_SCORE, PlayerNumber + "#" + itemIndex);
        Level.SendObject(data);
    }

    // 아이템과 플레이어 간의 충돌을 검사하여 아이템을 먹으면 서버에 아이템 점수를 보냄
    public void checkItemCollision() {
        List<Item> itemsCopy = new ArrayList<>(items);  // 안전한 복사본 생성

        for (Item item : itemsCopy) {
            if (player1.getBounds().intersects(item.getBounds())) {
                // 아이템 먹는 처리
                String itemName = item.getItemName();
                int itemIndex = items.indexOf(item); // 아이템의 인덱스 가져오기

                int scoretoAdd = 0;
                
				switch (itemName) {
                    case "orange":
                        System.out.println("P1 아이템 획득 orange");
                        //player1.increaseLife(); // 플레이어의 생명을 증가시킴
                        sendToServerItemScore(1, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                    case "banana":
                        System.out.println("P1 아이템 획득 바나나");
                        sendToServerItemScore(1, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                    case "apple":
                        System.out.println("P1 아이템 획득 사과");
                        sendToServerItemScore(1, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                    case "grape":
                        System.out.println("P1 아이템 획득 grape");
                        sendToServerItemScore(1, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                }
                System.out.println("P1 서버로 아이템 점수 전송");
            }
            else if (player2.getBounds().intersects(item.getBounds())) {
                // 아이템 먹는 처리
                String itemName = item.getItemName();
                int itemIndex = items.indexOf(item); // 아이템의 인덱스 가져오기
                
                int scoretoAdd = 0;

                switch (itemName) {
                    case "orange":
                        System.out.println("P2 아이템 획득 orange");
                        //player2.increaseLife(); // 플레이어의 생명을 증가시킴
                        sendToServerItemScore(2, itemIndex, scoretoAdd ); // 아이템의 인덱스 전달
                        break;
                    case "banana":
                        System.out.println("P2 아이템 획득 바나나");
                        sendToServerItemScore(2, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                    case "apple":
                        System.out.println("P2 아이템 획득 사과");
                        sendToServerItemScore(2, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                    case "grape":
                        System.out.println("P2 아이템 획득 grape");
                        sendToServerItemScore(2, itemIndex, scoretoAdd); // 아이템의 인덱스 전달
                        break;
                }
                System.out.println("P2 서버로 아이템 점수 전송");
            }
        }
    }

    public void plusItemScore(String score[]) {
        int PlayerNumber = Integer.parseInt(score[0]);
        int ItemIndex = Integer.parseInt(score[1]);
        int CurSum1 = scorePanel.getScore();
        int CurSum2 = scorePanel.getScore2();
        int TotalSum = scorePanel.getTotalScore();

        if (items.size() > ItemIndex) {
            System.out.println(items.size() + " size");
            Item index = items.get(ItemIndex);

            int scoreToAdd = 0;
            
            switch (index.getItemName()) {
                case "orange":
                    scoreToAdd = 500; // orange 아이템의 점수
                    break;
                case "banana":
                    scoreToAdd = 300; // banana 아이템의 점수
                    break;
                case "apple":
                    scoreToAdd = 200; // apple 아이템의 점수
                    break;
                case "grape":
                    scoreToAdd = 400; // grape 아이템의 점수
                    break;
                // 다른 아이템에 대한 처리 추가 가능
            }

            if (PlayerNumber == 1) { // 플레이어 1인 경우
                CurSum1 += scoreToAdd;
                scorePanel.updateScore(CurSum1);
            } else {  // 플레이어 2인 경우
                CurSum2 += scoreToAdd;
                scorePanel.updateScore2(CurSum2);
            }
            removeItem(index);
        }
        TotalSum += CurSum1 + CurSum2;
        scorePanel.updateTotalScore(TotalSum);
    }


    //플레이어의 움직임을 시작하는 데 사용
    public void MovingTrue(String msg[]) {
        if (msg.length < 2) {
            // 예외 상황 처리: 메시지가 충분한 정보를 포함하지 않을 때
            System.err.println("Invalid message format: " + Arrays.toString(msg));
            return;
        }

        String key;    // msg[0] = PlayerNumber, msg[1] = KEY_CODE (VK_LEFT ...)
        Player player;

        if(msg[0].equals("1")) {
            player = player1;
        }
        else {
            player = player2;
        }

        key = msg[1];
        switch (key) {
            case "VK_LEFT":
                if (!player.isLeft() && !player.isLeftWallCrash() && player.getState() == 0)
                    player.left();
                break;
            case "VK_RIGHT":
                if (!player.isRight() && !player.isRightWallCrash() && player.getState() == 0)
                    player.right();
                break;
            case "VK_UP":
                if (!player.isUp() && !player.isDown() && player.getState() == 0)
                    player.up();
                break;
            case "VK_SPACE":
                if (player.getState() == 0)
                    player.attack();
                break;
        }
    }

    //플레이어의 움직임을 중단하는 데 사용
    public void MovingFalse(String msg[]) {
        String key;     // msg[0] = PlayerNumber, msg[1] = KEY_CODE (VK_LEFT ...)
        Player player;

        if(msg[0].equals("1")) player = player1;
        else player = player2;

        key = msg[1];
        switch (key) {
            case "VK_LEFT":
                player.setLeft(false);
                break;
            case "VK_RIGHT":
                player.setRight(false);
                break;
        }
    }

    //키 이벤트를 감지, 플레이어의 번호에 따라 해당 플레이어의 움직임을 서버에 보내는 역할 (플레이어간 동기화에 사용됨)
    //플레이어가 키를 누르면 서버에 "플레이어가 @@으로 움직이기 시작했다"는 정보를 보냄
    //플레이어가 키를 떼면 "플레이어가 @@로 움직이기를 멈췄다"는 정보를 보냄
    class KeyListener extends KeyAdapter {
        private boolean invincible = false; // 무적 상태 여부
        @Override
        public void keyPressed(KeyEvent e) {
            {
                if(!invincible) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_DOWN:
                            Level.SendObject(new GameData(UserName, 200, PlayerNumber + "#VK_DOWN"));
                            break;
                        case KeyEvent.VK_UP:
                            Level.SendObject(new GameData(UserName, 200, PlayerNumber + "#VK_UP"));
                            break;
                        case KeyEvent.VK_LEFT:
                            Level.SendObject(new GameData(UserName, 200, PlayerNumber + "#VK_LEFT"));
                            break;
                        case KeyEvent.VK_RIGHT:
                            Level.SendObject(new GameData(UserName, 200, PlayerNumber + "#VK_RIGHT"));
                            break;
                        case KeyEvent.VK_SPACE:
                            Level.SendObject(new GameData(UserName, 200, PlayerNumber + "#VK_SPACE"));
                            break;
                    }
                }
            }
            checkItemCollision();
        }

        @Override
        public void keyReleased(KeyEvent e) {

            if(!invincible) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        Level.SendObject(new GameData(UserName, 201, PlayerNumber + "#VK_LEFT"));
                        break;
                    case KeyEvent.VK_RIGHT:
                        Level.SendObject(new GameData(UserName, 201, PlayerNumber + "#VK_RIGHT"));
                        break;
                }
            }
        }
    }
}
