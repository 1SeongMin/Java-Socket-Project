package bubble.game.server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class JavaChatServer extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;
    
    private String playerName; 
    private static JavaChatServer instance;
    
    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓
    private Vector<UserService> UserVec = new Vector<>();
	// 연결된 사용자를 저장할 벡터, ArrayList와 같이 동적 배열을 만들어주는 컬렉션 객체이나 동기화로 인해 안전성 향상
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

    /**
     * Launch the application.
     */
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
    
    public static JavaChatServer getInstance() {
        if (instance == null) {
            instance = new JavaChatServer();
        }
        return instance;
    }
    
    public void addClient(String name) {
        try {
        	this.playerName = name;
            Socket clientSocket = new Socket("localhost", 30000); // 서버의 포트 번호와 연결
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF("/login " + playerName); // 클라이언트의 이름을 서버로 전송하여 등록
            System.out.println(this.playerName + "가 서버에 접속하였습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getUserName() {
    	return playerName;
    }
    public int getClientCount() {
    	return this.UserVec.size();
    }

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다. 한번 만들어서 계속 사용하는 스레드
    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting clients ...");
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    AppendText("새로운 참가자 from " + client_socket);
                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user); // 새로운 참가자 배열에 추가
                    AppendText("사용자 입장. 현재 참가자 수 " + UserVec.size());
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
        private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client_socket;
        private Vector<UserService> user_vc; // 제네릭 타입 사용
        private String UserName = "";

        public UserService(Socket client_socket) {
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            this.user_vc = UserVec;
            try {
                is = client_socket.getInputStream();
                dis = new DataInputStream(is);
                os = client_socket.getOutputStream();
                dos = new DataOutputStream(os);
                String line1 = dis.readUTF();      // 제일 처음 연결되면 SendMessage("/login " + UserName);에 의해 "/login UserName" 문자열이 들어옴
                String[] msg = line1.split(" ");   //line1이라는 문자열을 공백(" ")을 기준으로 분할
                UserName = msg[1].trim();          //분할된 문자열 배열 msg의 두 번째 요소(인덱스 1)를 가져와 trim 메소드를 사용하여 앞뒤의 공백을 제거
                AppendText("새로운 참가자 " + UserName + " 입장.");
                WriteOne("Welcome to Java chat server\n");
                WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
            } catch (Exception e) {
                AppendText("userService error");
            }
        }


        // 클라이언트로 메시지 전송
        public void WriteOne(String msg) {
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                AppendText("dos.write() error");
                try {
                    dos.close();
                    dis.close();
                    client_socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                UserVec.removeElement(this); // 에러가난 현재 객체를 벡터에서 지운다
                AppendText("사용자 퇴장. 현재 참가자 수 " + UserVec.size());
            }
        }

        
        //모든 다중 클라이언트에게 순차적으로 채팅 메시지 전달
        public void WriteAll(String str) {  
            for (int i = 0; i < user_vc.size(); i++) {
            	UserService user = user_vc.get(i);     // get(i) 메소드는 user_vc 컬렉션의 i번째 요소를 반환
                user.WriteOne(str);
            }
        }
        
        
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF(); 
                    msg = msg.trim();   //msg를 가져와 trim 메소드를 사용하여 앞뒤의 공백을 제거
                    AppendText(msg); // server 화면에 출력
                    WriteAll(msg + "\n"); // Write All
                } catch (IOException e) {
                    AppendText("dis.readUTF() error");
                    try {
                        dos.close();
                        dis.close();
                        client_socket.close();
                        UserVec.removeElement(this); // 에러가 난 현재 객체를 벡터에서 지운다
                        AppendText("사용자 퇴장. 남은 참가자 수 " + UserVec.size());
                        break;
                    } catch (Exception ee) {
                        break;
                    } 
                }
            }
        }
        
    }
}
