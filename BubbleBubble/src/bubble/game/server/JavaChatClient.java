package bubble.game.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class JavaChatClient {

    public static void main(String[] args) {
        String playerName = "Alice"; // 클라이언트의 이름

        try {
            Socket clientSocket = new Socket("localhost", 30000); // 서버의 IP와 포트 번호로 연결
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF("/login " + playerName); // 클라이언트의 이름을 서버로 전송하여 등록
            System.out.println(playerName + " connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
