package bubble.game.component;

import bubble.game.BubblePanel;
import bubble.game.Moveable;
import bubble.game.service.BackgroundPlayerService;
import bubble.game.state.PlayerWay;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// class Player -> new 가능한 애들!! 게임에 존재할 수 있음. (추상메서드를 가질 수 없다.)
@Getter
@Setter
public class Player extends JLabel implements Moveable {

	private boolean shieldVisible = false; // 쉴드 이미지 표시 여부

	private BubblePanel BubblePanel;
	private List<Bubble> bubbleList;
	private GameOver gameOver;

	private int life;

	// 위치 상태
	private int x;
	private int y;

	// 플레이어의 방향
	private PlayerWay playerWay;

	// 움직임 상태
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	// 벽에 충돌한 상태
	private boolean leftWallCrash;
	private boolean rightWallCrash;

	// 플레이어 속도 상태
	private final int SPEED = 4;
	private final int JUMPSPEED = 2; // up, down

	//플레이어의 생명 상태
	private int state = 0; // 0 : 생존 , 1 : 죽음
	private boolean life_state = false;

	//플레이어의 무적 (적과 부딫히면 3초간 무적)
	private boolean invincible = false; // 무적 상태를 나타내는 플래그
	private final int INVINCIBLE_TIME = 5000; // 무적 지속 시간 (밀리초)

	private ImageIcon playerR, playerL, playerRdie, playerLdie;
	private ImageIcon shieldPlayerL, shieldPlayerR;
	private JPanel panel;
	private int PlayerNum;

	public Player(JPanel panel, int PlayerNum) {
		this.BubblePanel = (BubblePanel) panel;
		this.PlayerNum = PlayerNum;
		initObject();
		initSetting();
		initBackgroundPlayerService1();

		this.life = 3; //남은 목숨 수 설정
	}

	private void initObject() {
		playerR = new ImageIcon("image/playerR.png");
		playerL = new ImageIcon("image/playerL.png");
		playerRdie = new ImageIcon("image/playerRdie.png");
		playerLdie = new ImageIcon("image/playerLdie.png");
		shieldPlayerR = new ImageIcon("image/shieldplayerR.png");
		shieldPlayerL = new ImageIcon("image/shieldplayerL.png");
		bubbleList = new ArrayList<>();
	}

	private void initSetting() {

		if(PlayerNum == 1) { //플레이어1
			x = 65; // 80 -> 65
			y = 585; //535 -> 585

			left = false;
			right = false;
			up = false;
			down = false;

			leftWallCrash = false;
			rightWallCrash = false;

			playerWay = PlayerWay.RIGHT;
			setIcon(playerR);
			setSize(50, 50);
			setLocation(x, y);
		}
		else { //플레이어2
			x = 870;
			y = 585;

			left = false;
			right = false;
			up = false;
			down = false;

			leftWallCrash = false;
			rightWallCrash = false;

			playerWay = PlayerWay.LEFT;
			setIcon(playerL);
			setSize(50, 50);
			setLocation(x, y);
		}
	}

	// 가만히 있을 때의 이미지 설정
	private void setStillImage() {
		if (playerWay == PlayerWay.LEFT) {
			setIcon(invincible ? shieldPlayerL : playerL);
		} else {
			setIcon(invincible ? shieldPlayerR : playerR);
		}
	}

	// 무적 상태에 따라 이미지 업데이트
	private void updateImage() {
		if (!left && !right) { // 키 입력이 없을 때
			setStillImage();
		}
	}

	// 체력(생명) 감소 함수
	public synchronized void reduceLife() {
		if (!invincible) { // 무적 아닌 경우만 데미지를 받음
			if (life > 0) {
				life--;
				System.out.println("남은 체력: " + life);
				if (PlayerNum == 1) {
					BubblePanel.removePlayerLife(1, life); // Player 1의 체력 감소 처리
				} else if (PlayerNum == 2) {
					BubblePanel.removePlayerLife(2, life); // Player 2의 체력 감소 처리
				}
			} else { // 체력이 0보다 낮아지면 죽음
				setState(1);
			}

			// 무적 상태 설정
			invincible = true;

			// 이미지 업데이트
			updateImage();

			// 무적 시간이 지나면 무적 상태를 해제
			new Thread(() -> {
				try {
					Thread.sleep(INVINCIBLE_TIME);
					SwingUtilities.invokeLater(() -> {
						invincible = false;
						// 이미지 업데이트
						updateImage();
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	// 플레이어의 생명을 증가시키는 메서드
	public synchronized void increaseLife() {
	    if (life < 3) { // 최대 체력 이상으로 증가하지 않도록 체크
	        life++;
	        System.out.println("생명 증가: " + life);
	        if (PlayerNum == 1) {
	            BubblePanel.addPlayerLife(1, life); // Player 1의 생명 증가 처리
	        } else if (PlayerNum == 2) {
	            BubblePanel.addPlayerLife(2, life); // Player 2의 생명 증가 처리
	        }
	    }
	}
	
	public ImageIcon imageSetSize(ImageIcon icon, int i, int j) { // image Size Setting
		Image ximg = icon.getImage();  //ImageIcon을 Image로 변환.
		Image yimg = ximg.getScaledInstance(i, j, java.awt.Image.SCALE_SMOOTH);
		ImageIcon xyimg = new ImageIcon(yimg);
		return xyimg;
	}

	//점수 추가 -> BubblePanel에서 점수 조정
	public void plusScore(int score) {
		BubblePanel.SendToServerScore(PlayerNum, score);
	}

	private void initBackgroundPlayerService1() { //맵 1 충돌
		new Thread(new BackgroundPlayerService(this)).start();
	}

	@Override	//버블 발사
	public void attack() {
		new Thread(() -> {
			Bubble bubble = new Bubble(BubblePanel, PlayerNum);
			BubblePanel.add(bubble);
			bubbleList.add(bubble);

			if (playerWay == PlayerWay.LEFT) {
				bubble.left();
			} else {
				bubble.right();
			}
		}).start();
	}

	// 이벤트 핸들러
	@Override
	public void left() {
		playerWay = PlayerWay.LEFT;
		left = true;
		new Thread(() -> {
			while (left && getState() == 0) {
				setStillImage();
				x = x - SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			repaint();
		}).start();
	}

	@Override
	public void right() {
		playerWay = PlayerWay.RIGHT;
		right = true;
		new Thread(() -> {
			while (right && getState() == 0) {
				setStillImage();
				x = x + SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			repaint();
		}).start();
	}

	// left + up, right + up
	@Override
	public void up() {
		//System.out.println("up");
		up = true;
		new Thread(()->{
			for(int i=0; i<130/JUMPSPEED; i++) {
				y = y - JUMPSPEED;
				setLocation(x, y);
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			up = false;
			down();

		}).start();
	}

	@Override
	public void down() {
		//System.out.println("down");
		down = true;
		new Thread(()->{
			while(down) {
				y = y + JUMPSPEED;
				setLocation(x, y);
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			down = false;
		}).start();
	}

	public void die() {
		new Thread(() -> {
			setState(1);

			setIcon(PlayerWay.RIGHT == playerWay ? playerRdie : playerLdie);

			//new GameOverBGM();
			BubblePanel.getBgm().stopBGM();

			try {
				if (!isUp() && !isDown()) up();
				gameOver = new GameOver(BubblePanel);
				BubblePanel.add(gameOver);
				Thread.sleep(2000);
				BubblePanel.remove(this);
				BubblePanel.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("플레이어 사망.");
		}).start();
	}

}
