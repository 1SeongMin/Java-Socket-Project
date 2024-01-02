package bubble.game.component;

import bubble.game.BubblePanel;
import bubble.game.ScorePanel;
import bubble.game.server.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static bubble.game.component.Protocol.*;

public class Level extends JLayeredPane {

    private Font font = new Font("Bubble Bobble", Font.CENTER_BASELINE, 30);
    private JButton startBtn; //시작 버튼

    //대기 문구
    private JLabel WaitingPlayer;
    private Image BackGround;

    //플레이어의 이미지
    private Image Player1_img;
    private Image Player2_img;

    //플레이어의 이름
    private JLabel Player_name1;
    private JLabel Player_name2;

    //GameStarted 게임 시작 플래그
    private boolean GameStarted = false;
    private static int CurPlayerNumber; //현재 플레이어의 번호
    private static String UserName; //현재 플레이어 이름

    private static JFrame main;
    private ScorePanel scorePanel;
    private BubblePanel BubblePanel;

    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

    private String ip_addr = "127.0.0.1"; // 서버의 IP 주소
    private int port_no = 30000; //서버의 포트 번호
    private Socket ClientSocket; //서버에 접속할 클라이언트 소켓

    private ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public Level(String name, JFrame frame) {
        this.UserName = name;
        this.main = frame;
        this.setLayout(null);
        this.setBounds(0, 0, 1000, 800);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);

        scorePanel = new ScorePanel();

        setLabel();
        connectToServer();

        this.setVisible(true);
    }

    //버튼 클릭하면 게임 시작 데이터 서버에 보내고 게임 화면 생성 및 전환
    private void addActionToStartButton() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameData data = new GameData(UserName, START_CODE, "start");
                SendObject(data);

                Container contentPane = main.getContentPane();
                contentPane.removeAll();
                if (BubblePanel == null) {
                    BubblePanel = new BubblePanel(scorePanel);
                }
                contentPane.add(BubblePanel.getJSplitPane());

                contentPane.revalidate(); // 프레임을 다시 그리도록 호출
                contentPane.repaint();
            }
        });
    }

    public static  JFrame getMainFrame() {
        return main;
    }

    private void setLabel() {
        WaitingPlayer = new JLabel("Player2 Waiting...");
        WaitingPlayer.setBounds(310, 370, 400, 80);
        WaitingPlayer.setFont(font);
        WaitingPlayer.setForeground(new Color(255, 180, 0));
        WaitingPlayer.setHorizontalAlignment(JTextField.CENTER);
        this.add(WaitingPlayer);

        Player1_img = Toolkit.getDefaultToolkit().createImage("image/playerR.png");
        BackGround = Toolkit.getDefaultToolkit().createImage("image/Level_Gif.gif");

        //플레이어 1 이름
        Player_name1 = new JLabel(UserName);
        Player_name1.setBounds(180, 450, 200, 40);
        Player_name1.setFont(font);
        Player_name1.setForeground(new Color(0, 255, 0));
        Player_name1.setHorizontalAlignment(JTextField.RIGHT);
        this.add(Player_name1);
        scorePanel.setPlayerNameLabel1(UserName);

        //플레이어 2 이름
        Player_name2 = new JLabel("");
        Player_name2.setBounds(460, 450, 200, 40);
        Player_name2.setFont(font);
        Player_name2.setForeground(new Color(255, 0, 255));
        Player_name2.setHorizontalAlignment(JTextField.RIGHT);

        startBtn = new JButton("Start");
        startBtn.setBounds(410, 482, 200, 50);
        startBtn.setBorderPainted(false);
        startBtn.setContentAreaFilled(false);
        startBtn.setFocusPainted(false);
        startBtn.setFont(font);
        startBtn.setForeground(Color.white);

        // 시작 버튼에 대한 ActionListener
        addActionToStartButton();
    }

    //플레이어 1, 2 이미지 그리기
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BackGround, 300, 70, 400, 300, this);
        g.drawImage(Player1_img, 325, 500, 70, 70, this);
        g.drawImage(Player2_img, 615, 500, 70, 70, this);

    }

    //클라이언트를 생성해서 서버에 접속
    private void connectToServer() {
        try {
            ClientSocket = new Socket(ip_addr, port_no);

            oos = new ObjectOutputStream(ClientSocket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(ClientSocket.getInputStream());

            GameData dataMsg = new GameData(UserName, LOGIN_CODE, "Login"); //로그인 정보를 직렬화
            SendObject(dataMsg); //서버로 전송

            Receive_Service service = new Receive_Service(); //UserService
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //서버로 메시지 전송 (다른 클래스에서 Level 생성하지 않고 사용하기 위해 static 사용)
    static public void SendObject(Object object) {
        try {
            oos.writeObject(object); //객체가 직렬화되어 스트림에 쓰여짐 -> 서버에서 읽음(역 직렬화)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public int getCurPlayerNumber() { //현재 (자신)의 플레이어 번호 반환
        return CurPlayerNumber;
    } //현재 플레이어의 번호 반환
    static public String getUserName() {
        return UserName;
    } //현재 플레이어 이름 반환

    //플레이어 2명 접속 함수
    public void Join_Player(String Player[]) {
        WaitingPlayer.setText("All Players connected!!");
        Player2_img = Toolkit.getDefaultToolkit().createImage("image/playerL.png");
        this.add(Player_name2);

        if (Player[0].trim().equals("P1")) {
            Player_name2.setText(Player[1]);
            scorePanel.setPlayerNameLabel2(Player[1]);
            this.add(startBtn);
            CurPlayerNumber = 1;
        } else if ((Player[0].trim().equals("P2"))) {
            Player_name1.setText(Player[1]);
            Player_name2.setText(UserName);
            scorePanel.setPlayerNameLabel1(Player[1]);
            scorePanel.setPlayerNameLabel2(UserName);
            CurPlayerNumber = 2;
        }
    }

    //서버로부터 메시지를 수신해서 프로토콜에 해당하는 작업 수행
    class Receive_Service extends Thread {
        public void run() {
            while (true) {
                try {
                    Object obj = null;
                    GameData data = null;
                    try {
                        obj = ois.readObject(); //저장된 데이터 읽어와서 객체로 역 직렬화
                        if (obj == null) break; //서버에서 받은 데이터 없으면 break
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                    if (obj == null) break; //서버에서 받은 데이터 없으면 break
                    if (obj instanceof GameData) data = (GameData) obj; //obj instanceof GameData 없으면 오류 발생?

                    // 받은 데이터에 따라 동작 수행
                    switch(data.getProtocol()) {
                        case LOGIN_CODE:
                            // 로그인에 대한 처리 (100)
                            Join_Player(data.getMsg().split(" "));
                            break;
                        case START_CODE:
                            // 게임 시작에 대한 처리 (101)
                            if(!GameStarted) {
                                if (BubblePanel == null) {
                                    BubblePanel = new BubblePanel(scorePanel);
                                    Container contentPane = main.getContentPane();
                                    contentPane.removeAll();
                                    contentPane.add(BubblePanel.getJSplitPane());
                                    contentPane.revalidate();
                                    contentPane.repaint();
                                }
                            }
                            break;

                        case MOVING_TRUE:                   //PlayerNumber + #Key (분리)
                            BubblePanel.MovingTrue(data.getMsg().split("#"));
                            //플레이어 움직임 True 처리 (200)
                            break;
                        case MOVING_FALSE:                  //PlayerNumber + #Key (분리)
                            BubblePanel.MovingFalse(data.getMsg().split("#"));
                            //플레이어 움직임 False 처리 (201)
                            break;

                        case ADD_SCORE:
                            //점수 획득에 대한 처리 (300)        //PlayerNumber + 점수
                            BubblePanel.SetScore(data.getMsg().split("#"));
                            break;

                        case ITEM_CREATE:
                            //아이템 생성에 대한 처리 (400)       (아이템이름 + #x + #y)
                            BubblePanel.addItem(data.getMsg().split("#"));
                            break;
                        case ADD_ITEM_SCORE:
                            //아이템 먹으면 점수 증가 처리 (401)
                            BubblePanel.plusItemScore(data.getMsg().split("#"));
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        ois.close();
                        oos.close();
                        ClientSocket.close();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
}


