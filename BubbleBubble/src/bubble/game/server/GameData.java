package bubble.game.server;

import javax.swing.*;
import java.io.Serializable;

// 예시: 게임 데이터를 직렬화하는 클래스
public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private int protocol; //100 -> 로그인 관련, 200 -> 플레이어 관련, 300 -> 버블 관련
    private String msg;
    private ImageIcon img;

    private int playerScore;
    private int playerLevel;
    private boolean isGamePaused;

    // 기본 생성자 및 게임 데이터 초기화
    public GameData(String id, int protocol, String msg) {
        this.id = id;
        this.protocol = protocol;
        this.msg = msg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setData(String data) {
        this.msg = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setImg(ImageIcon img) {
        this.img = img;
    }
}

