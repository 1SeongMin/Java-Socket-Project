package bubble.game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bubble.game.component.Enemy;
import bubble.game.component.GameOver;
import bubble.game.component.Player;
import bubble.game.music.BGM;
import bubble.game.state.EnemyWay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BubbleFrame extends JFrame {
 
	private BubbleFrame mContext = this;
	private JLabel backgroundMap;
	private Player player;
	private List<Enemy> enemys; // 컬렉션으로 관리
	private BGM bgm;
	private GameOver gameOver;
	private static int i;
	private int sum; //적을 죽이면 추가되는 점수
	
	//플레이어 1의 이름과 점수
	private JLabel player1;
	private JLabel player1_score;
	
	//플레이어 2의 이름과 점수
	private JLabel player2;
	private JLabel player2_score;
	
	//두 플레이어의 총 점수
	private JLabel score;
	private JLabel total_score ;

	// 플레이어1의 목숨 저장하는 배열 (하트)
	private JLabel[] player1LifeLabels;
	
	// 플레이어1의 목숨 저장하는 배열 (하트)
	private JLabel[] player2LifeLabels;

	public BubbleFrame(int i) {
		this.i = i;
		initObject();
		initSetting();
		initListener();
		setVisible(true);
	}

	private void initObject() {
		if(i == 1) backgroundMap = new JLabel(new ImageIcon("image/round01.png"));
		else if (i == 2 ) backgroundMap = new JLabel(new ImageIcon("image/round02.png"));
		else backgroundMap = new JLabel(new ImageIcon("image/round03.png"));
		
		setContentPane(backgroundMap);
		player = new Player(mContext);
		add(player);
		enemys = new ArrayList<Enemy>();
		enemys.add(new Enemy(mContext, EnemyWay.RIGHT));
		enemys.add(new Enemy(mContext, EnemyWay.LEFT));
		for(Enemy e : enemys) add(e);
		bgm = new BGM();
		bgm.playBGM("bgm.wav");
	}

	private void initSetting() {
		setSize(1000, 730); //1000, 640
		setBackground(new Color(0, 0, 0));
		setLayout(null); // absoulte 레이아웃 (자유롭게 그림을 그릴 수 있다)
		setLocationRelativeTo(null); // JFrame 가운데 배치하기
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x버튼으로 창을 끌 때 JVM 같이 종료하기
		
		//윗 줄, 플레이어 1 이름
		player1 = new JLabel("name1");
		player1.setForeground(new Color(0, 255, 0));
		player1.setFont(new Font("D2Coding", Font.PLAIN, 20));
		player1.setBounds(10, 5, 50, 20);
		add(player1);
		
		//플레이어 2 이름
		player2 = new JLabel("name2");
		player2.setForeground(new Color(255, 0, 255));
		player2.setFont(new Font("D2Coding", Font.PLAIN, 20));
		player2.setBounds(925, 5, 50, 20);
		add(player2);
		
		score = new JLabel("T Score");
		score.setForeground(new Color(255, 0, 0));
		score.setFont(new Font("D2Coding", Font.PLAIN, 20));
		score.setBounds(470, 5, 50, 20);
		add(score);
		
		//아랫줄 , 플레이어1 점수
		player1_score = new JLabel("0");
		player1_score.setForeground(new Color(255, 255, 255));
		player1_score.setFont(new Font("D2Coding", Font.PLAIN, 20));
		player1_score.setBounds(10, 30, 70, 20);
		add(player1_score);
		
		//플레이어2 점수
		player2_score = new JLabel("0");
		player2_score.setForeground(new Color(255, 255, 255));
		player2_score.setFont(new Font("D2Coding", Font.PLAIN, 20));
		player2_score.setBounds(925, 30, 70, 20);
		add(player2_score);
		
		//플레이어1, 2의 총 점수
		total_score = new JLabel("T score");
		total_score.setForeground(new Color(255, 255, 255));
		total_score.setFont(new Font("D2Coding", Font.PLAIN, 20));
		total_score.setBounds(465, 30, 70, 20);
		add(total_score);
		
		//플레이어1의 생명
		player1LifeLabels = new JLabel[3];
		 for (int j = 0; j < 3; j++) {
			 player1LifeLabels[j] = new JLabel(new ImageIcon("image/life_heart.png"));
			 player1LifeLabels[j].setBounds(20 + (j * 35), 650, player1LifeLabels[j].getIcon().getIconWidth(), player1LifeLabels[j].getIcon().getIconHeight());
	            add(player1LifeLabels[j]);
	        }
		
		//플레이어2의 생명
		JLabel player2_life1 = new JLabel(new ImageIcon("image/life_heart.png"));
		player2_life1.setBounds(925, 685, player2_life1.getIcon().getIconWidth(), player2_life1.getIcon().getIconHeight());
		add(player2_life1);
		JLabel player2_life2 = new JLabel(new ImageIcon("image/life_heart.png"));
		player2_life2.setBounds(890, 685, player2_life2.getIcon().getIconWidth(), player2_life2.getIcon().getIconHeight());
		add(player2_life2);
		JLabel player2_life3 = new JLabel(new ImageIcon("image/life_heart.png"));
		player2_life3.setBounds(855, 685, player2_life3.getIcon().getIconWidth(), player2_life3.getIcon().getIconHeight());
		add(player2_life3);
	}
	
	public void addScore(int score) {
		sum += score;
		player1_score.setText(String.valueOf(sum));
	}
	//게임의 레벨을 반환하는 함수
	public int getLevel() {
		return this.i;
	}
	
	//체력이 감소하면 하트를 지우는 함수
	public void removePlayerLife(int index) {
        player1LifeLabels[index].setVisible(false);
	}

	private void initListener() {
		addKeyListener(new KeyAdapter() {

			// 키보드 클릭 이벤트 핸들러
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT: 
					if (!player.isLeft() && !player.isLeftWallCrash() && player.getState() == 0)
						player.left();
					break;
				case KeyEvent.VK_RIGHT:
					if (!player.isRight() && !player.isRightWallCrash() && player.getState() == 0)
						player.right();
					break;
				case KeyEvent.VK_UP:
					if(!player.isUp() && !player.isDown() && player.getState() == 0)
						player.up();
					break;
				case KeyEvent.VK_SPACE:
					if(player.getState() == 0)
						player.attack();
					break;
				}
			}

			// 키보드 해제 이벤트 핸들러
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					player.setLeft(false);
					break;
				case KeyEvent.VK_RIGHT:
					player.setRight(false);
					break;
				}
			}

		});
	}

	public static void main(String[] args) {
		new BubbleFrame(i);
	}
}
