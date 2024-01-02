
package bubble.game.server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import static bubble.game.component.Protocol.*;

public class JavaChatServer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;

    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓

    //연결된 사용자를 저장할 벡터,  ArrayList와 같이 동적 배열을 만들어주는 컬렉션 객체이나 동기화로 인해 안전성 향상
    private Vector<UserService> UserVec = new Vector<>();
    private ArrayList<String> nameList = new ArrayList<>(); //연결된 클라이언트의 이름을 저장할 ArrayList

    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private int UserCount = 0; // 연결된 사용자 수를 나타내는 변수 추가

    public static void main(String[] args) {   // 스윙 비주얼 디자이너를 이용해 GUI를 만들면 자동으로 생성되는 main 함수
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JavaChatServer frame = new JavaChatServer();      // JavaChatServer 클래스의 객체 생성
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JavaChatServer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 386);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 244);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblNewLabel = new JLabel("Port Number");
        lblNewLabel.setBounds(12, 264, 87, 26);
        contentPane.add(lblNewLabel);

        txtPortNumber = new JTextField();
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setText("30000");
        txtPortNumber.setBounds(111, 264, 199, 26);
        contentPane.add(txtPortNumber);
        txtPortNumber.setColumns(10);

        JButton btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
                } catch (NumberFormatException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                AppendText("Chat Server Running..");
                btnServerStart.setText("Chat Server Running..");
                btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
                txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
                AcceptServer accept_server = new AcceptServer();   // 멀티 스레드 객체 생성
                accept_server.start();
            }
        });
        btnServerStart.setBounds(12, 300, 300, 35);
        contentPane.add(btnServerStart);
    }

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다. 한번 만들어서 계속 사용하는 스레드
    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting clients ...");
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket); //클라이언트의 소켓 정보
                    AppendText("사용자 입장. 현재 참가자 수 " + UserVec.size());
                    UserVec.add(new_user); // 새로운 참가자 배열에 추가
                    new_user.start(); // 만든 객체의 스레드 실행
                } catch (IOException e) {
                    AppendText("!!!! accept 에러 발생... !!!!");
                }
            }
        }
    }

    //JtextArea에 문자열을 출력해 주는 기능을 수행하는 함수
    public void AppendText(String str) {
        textArea.append(str + "\n");   //전달된 문자열 str을 textArea에 추가
        textArea.setCaretPosition(textArea.getText().length());  // textArea의 커서(캐럿) 위치를 텍스트 영역의 마지막으로 이동
    }

    // User 당 생성되는 Thread, 유저의 수만큼 스레스 생성
    // Read One 에서 대기 -> Write All
    class UserService extends Thread {
        // 매개변수로 넘어온 자료 저장
        private Socket client_socket;

        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        private Vector<UserService> user_vc; // 제네릭 타입 사용
        private String UserName = "";

        public UserService(Socket client_socket) {
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            this.user_vc = UserVec;
            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());

            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        //모든 다중 클라이언트에게 순차적으로 채팅 메시지 전달
        public void WriteAll(String str, int protocol)  {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = user_vc.get(i);     // get(i) 메소드는 user_vc 컬렉션의 i번째 요소를 반환
                user.WriteOne(str, protocol);
            }
        }

        // 클라이언트로 메시지 전송
        public void WriteOne(String msg, int protocol) {
            try {
                GameData data = new GameData("sendMsg", protocol, msg);
                if(data != null) {
                    oos.writeObject(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    ois.close();
                    oos.close();
                    client_socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                UserVec.removeElement(this); //현재 객체를 벡터에서 삭제
                UserCount--;
                AppendText("사용자 " + UserName + " 퇴장. 남은 참가자 수 " + UserVec.size());
            }
        }

        //다른 플레이어에게 전송
        public void WriteOtherPlayers(String str, int protocol) {
            // 추가적인 로직 처리 또는 클라이언트에 메시지 전송 등의 작업
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = user_vc.get(i);     // get(i) 메소드는 user_vc 컬렉션의 i번째 요소를 반환
                if (user != this) user.WriteOne(str, protocol); //자신 제외한 플레이어에게 메시지 전송
            }
        }

        //플레이어 두 명이 게임 -> 게임에 참여함 알려줌
        public void WriteTwoPlayer() {
            // 추가적인 로직 처리 또는 클라이언트에 메시지 전송 등의 작업
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = user_vc.get(i);     // get(i) 메소드는 user_vc 컬렉션의 i번째 요소를 반환
                if (user != this) {
                    user.WriteOne("P1 " + UserName, LOGIN_CODE); //현재 사용자에게 전송
                    WriteOne("P2 " + user.UserName, LOGIN_CODE); //다른 사용자에게 전송
                }
            }
        }

        public void run() {
            while (true) {
                try {
                    Object obj = null;
                    GameData data = null;
                    if (socket == null) break;
                    try {
                        obj = ois.readObject(); //저장된 데이터 읽어와서 객체로 역 직렬화
                        if (obj == null) break; //서버에서 받은 데이터 없으면 break
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                    if (obj == null) break; //서버에서 받은 데이터 없으면 break
                    if (obj instanceof GameData) data = (GameData) obj; //obj instanceof GameData 없으면 오류 발생?

                    switch (data.getProtocol()) {
                        case LOGIN_CODE:
                            //로그인에 대한 처리 (100)
                            UserName = data.getId();
                            UserCount++;
                            AppendText("새로운 참가자 " + UserName + "가 로그인 했습니다.");

                            if (UserCount >= 1) {
                                //연결된 사용자가 2명 이상일 때
                                WriteTwoPlayer();
                            }
                            break;
                        case START_CODE:
                            //게임 시작에 대한 처리 (101)
                            WriteOtherPlayers("Start", START_CODE);
                            break;

                        case MOVING_TRUE:
                            //플레이어 움직임 True 처리 (200)
                            WriteOtherPlayers(data.getMsg(), MOVING_TRUE);
                            WriteOne(data.getMsg(), MOVING_TRUE);
                            break;
                        case MOVING_FALSE:
                            //플레이어 움직임 False 처리 (201)
                            WriteOtherPlayers(data.getMsg(),MOVING_FALSE);
                            WriteOne(data.getMsg(),MOVING_FALSE);
                            break;

                        case ADD_SCORE:
                            //점수 획득에 대한 처리 (300)
                            WriteOtherPlayers(data.getMsg(),ADD_SCORE);
                            WriteOne(data.getMsg(),ADD_SCORE);
                            break;

                        case ITEM_CREATE:
                            //아이템 생성에 대한 처리 (400)
                            WriteOtherPlayers(data.getMsg(),ITEM_CREATE);
                            WriteOne(data.getMsg(),ITEM_CREATE);
                            break;
                        case ADD_ITEM_SCORE:
                            //아이템 먹으면 점수 증가 처리 (401)
                            WriteOtherPlayers(data.getMsg(),ADD_ITEM_SCORE);
                            WriteOne(data.getMsg(),ADD_ITEM_SCORE);
                            break;
                    }

                } catch (IOException e) {
                    AppendText("dis.readUTF() error");
                    try {
                        ois.close();
                        oos.close();
                        client_socket.close();
                        UserVec.removeElement(this); //현재 객체를 벡터에서 삭제
                        UserCount--;
                        AppendText("사용자 " + UserName + " 퇴장. 남은 참가자 수 " + UserVec.size());
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
}
