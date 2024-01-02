package bubble.game.component;

public class Protocol {
    public static final int LOGIN_CODE = 100; // 로그인
    public static final int START_CODE = 101; // 게임 시작

    public static final int MOVING_TRUE = 200; // 플레이어 움직임 TRUE
    public static final int MOVING_FALSE = 201; // 플레이어 움직임 FALSE

    public static final int ADD_SCORE = 300; // 점수 추가 EnemyPosition

    public static final int ITEM_CREATE = 400; // 아이템 생성
    public static final int ADD_ITEM_SCORE = 401; // 아이템 먹으면 점수 증가
}
