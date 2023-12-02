package bubble.game.server;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

public class JavaChatServer extends JFrame { // 서버화면

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;
    private String playerName = "";

    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓

    private Vector<Socket> userSockets = new Vector<>(); // 연결된 사용자 소켓 저장

    public static void main(String[] args) { // 스윙 비주얼 디자이너를 이용해 GUI를 만들면 자동으로 생성되는 main 함수
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JavaChatServer frame = new JavaChatServer(); // JavaChatServer 클래스의 객체 생성
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
        btnServerStart.addActionListener(e -> {
            try {
                socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText())); // 서버 소켓 생성
            } catch (NumberFormatException | IOException e1) {
                e1.printStackTrace();
            }
            AppendText("Chat Server Running..");
            btnServerStart.setText("Chat Server Running..");
            btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
            txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
            AcceptServer accept_server = new AcceptServer(); // 멀티 스레드 객체 생성
            accept_server.start();
        });
        btnServerStart.setBounds(12, 300, 300, 35);
        contentPane.add(btnServerStart);
    }

    public void addClient(String playerName) {
    	
        try {
        	this.playerName = playerName;
        	 System.out.println(this.playerName + ".");
            Socket clientSocket = new Socket("localhost", 30000); // 서버의 포트 번호와 연결
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF("/login " + playerName); // 클라이언트의 이름을 서버로 전송하여 등록
            System.out.println(this.playerName + "가 서버에 접속하였습니다.");
            userSockets.add(clientSocket);
            // 여기서 필요한 작업을 수행하세요
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getUserName() {
    	return this.playerName;
    }

    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting clients ...");
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    AppendText("사용자 입장. 현재 참가자 수 " + userSockets.size());
                    userSockets.add(client_socket);
                    UserHandler userHandler = new UserHandler(client_socket);
                    userHandler.start();
                } catch (IOException e) {
                    AppendText("!!!! accept 에러 발생... !!!!");
                }
            }
        }
    }

    public void AppendText(String str) {
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    class UserHandler extends Thread {
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client_socket;

        public UserHandler(Socket client_socket) {
            this.client_socket = client_socket;
            try {
                dis = new DataInputStream(client_socket.getInputStream());
                dos = new DataOutputStream(client_socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    msg = msg.trim();
                    AppendText(msg);
                } catch (IOException e) {
                    try {
                        dos.close();
                        dis.close();
                        client_socket.close();
                        userSockets.remove(client_socket);
                        AppendText("사용자 퇴장. 남은 참가자 수 " + userSockets.size());
                        break;
                    } catch (IOException ex) {
                        break;
                    }
                }
            }
        }
    }
}
