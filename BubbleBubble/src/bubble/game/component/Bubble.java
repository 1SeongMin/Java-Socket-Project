package bubble.game.component;

import bubble.game.BubblePanel;
import bubble.game.Moveable;
import bubble.game.server.GameData;
import bubble.game.service.BackgroundBubbleService;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.List;

import static bubble.game.component.Protocol.ITEM_CREATE;

@Getter
@Setter
public class Bubble extends JLabel implements Moveable  {
	
	// 의존성 콤포지션
	private BubblePanel bubblePanel;
	private Player player;
	private List<Enemy> enemys;
	private Enemy removeEnemy = null; // 적 제거 변수. 
	private BackgroundBubbleService backgroundBubbleService;
	
	// 위치 상태
	private int x;
	private int y;

	// 움직임 상태
	private boolean left;
	private boolean right;
	private boolean up;
	
	// 적군을 맞춘 상태
	private int state; // 0(물방울), 1(적을 가둔 물방울)
	
	private ImageIcon bubble; // 물방울
	private ImageIcon bubbled; // 적을 가둔 물방울
	private ImageIcon bomb; // 물방울이 터진 상태

	private int PlayerNumber;
	private boolean itemCreated = false;

	public Bubble(BubblePanel bubblePanel, int PlayerNumber) {
		this.bubblePanel = bubblePanel;
		this.PlayerNumber = PlayerNumber;
		this.player =  bubblePanel.getPlayerNumber(PlayerNumber); //플레이어 번호에 따라 해당 플레이어 설정
		this.enemys = bubblePanel.getEnemys();
		initObject();
		initSetting();
	}
	
	private void initObject() {
		bubble = new ImageIcon("image/bubble.png");
		bubbled = new ImageIcon("image/bubbled.png");
		bomb = new ImageIcon("image/bomb.png");
		
		backgroundBubbleService = new BackgroundBubbleService(this);
	}
	
	private void initSetting() {
		left = false;
		right = false;
		up = false;

		x = player.getX();
		y = player.getY();
		
		setIcon(bubble);
		setSize(50, 50);
		
		state = 0;
	}

	@Override
	public void left() {
		left = true;
		Stop:for(int i=0; i<400; i++) {
			x--;
			setLocation(x, y);
			
			if(backgroundBubbleService.leftWall()) {
				left = false;
				break;
			}
			
			
			// 40과 60의 범위 절대값
			for (Enemy e : enemys) {
				if (Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
					if (e.getState() == 0) {
						attack(e);
						break Stop;
					}
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void right() {
		right = true;
		Stop:for(int i=0; i<400; i++) {
			x++;
			setLocation(x, y);
			
			if(backgroundBubbleService.rightWall()) {
				right = false;
				break;
			}
			
			// 아군과 적군의 거리가 10
			for (Enemy e : enemys) {
				if (Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
					if (e.getState() == 0) {
						attack(e);
						break Stop;
					}
				}
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void up() {
		up = true;
		while(up) {
			y--;
			setLocation(x, y);
			
			if(backgroundBubbleService.topWall()) {
				up = false;
				break;
			}
			
			try {
				if(state==0) { // 기본 물방울
					Thread.sleep(1);
				}else { // 적을 가둔 물방울
					Thread.sleep(10);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(state == 0) clearBubble(); // 천장에 버블이 도착하고 나서 3초 후에 메모리에서 소멸
		else {
			clearBubbled();
		}
	}
	
	@Override
	public void attack(Enemy e) { //적이 버블에 같힘
		//GameData data = new GameData("attack_bubble", ATTACK_MONSTER, "1");
		// Level.SendObject(data);
		state = 1;
		e.setState(1);
		setIcon(bubbled);
		removeEnemy = e;
		bubblePanel.remove(e); // 메모리에서 사라지게 한다. (가비지 컬렉션->즉시 발동하지 않음)
		bubblePanel.repaint(); // 화면 갱신
	}
	
	
	// 적에 맞지 않은 버블 지우는 함수
	private void clearBubble() {
		try {
			Thread.sleep(3000);
			setIcon(bomb);
			Thread.sleep(500);

			// 버블 객체 메모리에서 날리기 (플레이어 번호에 따라)
			if(PlayerNumber == 1) bubblePanel.getPlayer1().getBubbleList().remove(this);
			else bubblePanel.getPlayer2().getBubbleList().remove(this);

			bubblePanel.remove(this); // bubblePanel의 bubble이 메모리에서 소멸된다.
			bubblePanel.repaint(); // bubblePanel의 전체를 다시 그린다. (메모리에서 없는 건 그리지 않음)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	// 적에 맞은 버블 터지면 랜덤으로 아이템 생성하는 함수
	public synchronized void clearBubbled() {
		new Thread(() -> {
			System.out.println("clearBubbled");
			try {
				up = false;
				setIcon(bomb);

				Thread.sleep(1000);

				synchronized (this) {
					if (!itemCreated) {

						// 아이템 생성
						String[] ItemNames = {"orange", "banana", "apple", "grape"};
						String randomItemName = ItemNames[(int) (Math.random() * ItemNames.length)];

						//서버로 아이템 이름과 아이템 좌표 전송 (아이템이름#x#y)
						GameData data = new GameData("create_item", ITEM_CREATE, randomItemName + "#" + x + "#" + y
						+ "#" +PlayerNumber);
						Level.SendObject(data);

						itemCreated = true; // 생성 여부 파악
						System.out.println("아이템 생성");
					}
				}


				// 버블 객체 메모리에서 날리기 (플레이어 번호에 따라)
				if (PlayerNumber == 1) bubblePanel.getPlayer1().getBubbleList().remove(this);
				else bubblePanel.getPlayer2().getBubbleList().remove(this);

				bubblePanel.getEnemys().remove(removeEnemy); // 컨텍스트에 enemy 삭제
				bubblePanel.remove(this);
				bubblePanel.repaint();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}