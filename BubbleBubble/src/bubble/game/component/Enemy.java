package bubble.game.component;

import bubble.game.BubblePanel;
import bubble.game.Moveable;
import bubble.game.service.BackgroundEnemyService;
import bubble.game.state.EnemyWay;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class Enemy extends JLabel implements Moveable {

	private BubblePanel bubblePanel;
	private Player player; // 플레이어 추가. 
	
	// 위치 상태
	private int x;
	private int y;
	
	// 적군의 방향
	private EnemyWay enemyWay;

	// 움직임 상태
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private int state; // 0(살아있는 상태), 1(물방울에 갇힌 상태)
	
	// 적군 속도 상태
	private final int SPEED = 3;
	private final int JUMPSPEED = 1;

	private ImageIcon enemyR, enemyL;

	public Enemy(BubblePanel bubblePanel, EnemyWay enemyWay) {
		this.bubblePanel = bubblePanel;
		//this.player = bubblePanel.getPlayer();
		initObject();
		initSetting();
		initBackgroundEnemyService1();
		initEnemyDirection(enemyWay);
	}

	private void initObject() {
		enemyR = new ImageIcon("image/enemyR.png");
		enemyL = new ImageIcon("image/enemyL.png");
	}

	private void initSetting() {
		x = 480;
		y = 138;

		left = false;
		right = false;
		up = false;
		down = false;
		
		state = 0;

		setSize(50, 50);
		setLocation(x, y);
	}
	
	private void initEnemyDirection(EnemyWay enemyWay) {
		if(EnemyWay.RIGHT == enemyWay) {
			enemyWay = EnemyWay.RIGHT;
			setIcon(enemyR);
			right();
		}else {
			enemyWay = EnemyWay.LEFT;
			setIcon(enemyL);
			left();
		}
	}

	//적의 상태가 1이면 죽은 상태 -> true를 반환, 아니면 false를 반환
	public boolean isDead() {
		return state == 1;
	}


	private void initBackgroundEnemyService1() {
		new Thread(new BackgroundEnemyService(this)).start();
	}
	private void initBackgroundEnemyService2() {
		new Thread(new BackgroundEnemyService(this)).start();
	}

	@Override
	public void left() {
		//System.out.println("left");
		enemyWay = EnemyWay.LEFT;
		left = true;
		new Thread(()-> {
			while(left) {
				setIcon(enemyL);
				x = x - SPEED;
				setLocation(x, y);
				for (Player player : bubblePanel.getPlayers()) {
					if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
						if (player.getState() == 0 && getState() == 0)
							player.reduceLife(); //목숨 감소
						if (player.getState() == 1 && getState() == 0)
							player.die();
					}
				}
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
		enemyWay = EnemyWay.RIGHT;
		right = true;
		new Thread(()-> {
			while(right) {
				setIcon(enemyR);
				x = x + SPEED;
				setLocation(x, y);
				for (Player player : bubblePanel.getPlayers()) {
					if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
						if (player.getState() == 0 && getState() == 0)
							player.reduceLife(); //목숨 감소
						if (player.getState() == 1 && getState() == 0)
							player.die();
					}
				}
				try {
					Thread.sleep(10); // 0.01초
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}).start();
		

	}

	@Override
	public void up() {
		//System.out.println("up");
		up = true;
		new Thread(()->{
			for(int i=0; i<130/JUMPSPEED; i++) {
				y = y - JUMPSPEED;
				setLocation(x, y);
				for (Player player : bubblePanel.getPlayers()) {
					if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
						if (player.getState() == 0 && getState() == 0)
							player.reduceLife(); //목숨 감소
						if (player.getState() == 1 && getState() == 0)
							player.die();
					}
				}
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
				for (Player player : bubblePanel.getPlayers()) {
					if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
						if (player.getState() == 0 && getState() == 0)
							player.reduceLife(); //목숨 감소
					}
				}
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			down = false;
		}).start();
	}
}
