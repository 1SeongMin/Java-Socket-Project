package bubble.game.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.music.GameOverBGM;
import bubble.game.service.BackgroundPlayerService;
import bubble.game.service.BackgroundPlayerService2;
import bubble.game.service.BackgroundPlayerService3;
import bubble.game.state.PlayerWay;
import lombok.Getter;
import lombok.Setter;

// class Player -> new 가능한 애들!! 게임에 존재할 수 있음. (추상메서드를 가질 수 없다.)
@Getter
@Setter
public class Player extends JLabel implements Moveable {

	private BubbleFrame bubbleFrame;
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
	private final int INVINCIBLE_TIME = 3000; // 무적 지속 시간 (밀리초)


	private ImageIcon playerR, playerL, playerRdie, playerLdie;

	public Player(BubbleFrame bubbleFrame) {
		this.bubbleFrame = bubbleFrame;
		initObject();
		initSetting();
		initBackgroundPlayerService();
		this.life = 3; //남은 목숨 수 설정
	}

	private void initObject() {
		playerR = new ImageIcon("image/playerR.png");
		playerL = new ImageIcon("image/playerL.png");
		playerRdie = new ImageIcon("image/playerRdie.png");
		playerLdie = new ImageIcon("image/playerLdie.png");
		bubbleList = new ArrayList<>();
	}

	private void initSetting() {
		
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
	
	//목숨 감소 함수
	public void reduceLife() {
        if (!invincible) { // 무적 아닌 경우만 데미지를 받음
            if (life > 0) {
                life--;
                System.out.println("남은 체력: " + life);
                bubbleFrame.removePlayerLife(life);
            } else { //체력이 0보다 낮아지면 죽음
                setState(1);
            }

            // 무적 상태 설정
            invincible = true;

            // 무적 시간이 지나면 무적 상태를 해제
            new Thread(() -> {
                try {
                    Thread.sleep(INVINCIBLE_TIME);
                    invincible = false; // 무적 상태 해제
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
	public void plusScore(int score) {
		bubbleFrame.addScore(score);
	}
	
	//레벨에 맞게 백그라운드 서비스 스레드로 동자 시키는 함수
	private void initBackgroundPlayerService() {
		if(bubbleFrame.getLevel() ==1) new Thread(new BackgroundPlayerService(this)).start();
		else if(bubbleFrame.getLevel() ==2) new Thread(new BackgroundPlayerService2(this)).start();
		//else new Thread(new BackgroundPlayerService3(this)).start();
	}
	
	@Override
	public void attack() {
		new Thread(()->{
			Bubble bubble = new Bubble(bubbleFrame);
			bubbleFrame.add(bubble);
			bubbleList.add(bubble);
			if(playerWay == PlayerWay.LEFT) {
				bubble.left();
			}else {
				bubble.right();
			}
		}).start();
	}

	// 이벤트 핸들러
	@Override
	public void left() {
		//System.out.println("left");
		playerWay = PlayerWay.LEFT;
		left = true;
		new Thread(()-> {
			while(left && getState() == 0) {
				setIcon(playerL);
				x = x - SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10); // 0.01초
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}).start();

	}

	@Override
	public void right() {
		//System.out.println("right");
		playerWay = PlayerWay.RIGHT;
		right = true;
		new Thread(()-> {
			while(right && getState() == 0) {
				setIcon(playerR);
				x = x + SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10); // 0.01초
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
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
			setState(1); //플레이어를 사망 상태로 설정
			setIcon(PlayerWay.RIGHT == playerWay ? playerRdie : playerLdie);
			new GameOverBGM();
			bubbleFrame.getBgm().stopBGM(); //음악이 멈춤

			try {				
				if(!isUp() && !isDown()) up();
				gameOver = new GameOver(bubbleFrame);
				bubbleFrame.add(gameOver);
				Thread.sleep(2000);
				bubbleFrame.remove(this);
				bubbleFrame.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("플레이어 사망.");
		}).start();
	}
}
