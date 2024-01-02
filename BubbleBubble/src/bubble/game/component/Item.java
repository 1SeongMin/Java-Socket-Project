package bubble.game.component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

public class Item extends JLabel {

    private String ItemName; // 아이템의 이름
    private int x;
    private int y;
    private int score; // 아이템의 점수

    public Item(String ItemName, int x, int y) {
        this.ItemName = ItemName;
        this.x = x;
        this.y = y;

        // 아이템의 종류에 따라 점수 설정
        //this.score = calculateScore();

        // 아이템 이미지 초기화
        ImageIcon itemIcon = new ImageIcon("image/" + ItemName + ".png");
        Image resizedImage = itemIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(resizedImage));
        setSize(50, 50);
        setLocation(x, y);
    }
    public String getItemName() {
        return ItemName;
    }
    public int getScore() {
        return score;
    }
    public int getX() { return x; }
    public int getY() { return y; }

}
