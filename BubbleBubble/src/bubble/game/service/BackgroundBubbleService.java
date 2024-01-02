package bubble.game.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import bubble.game.component.Bubble;

public class BackgroundBubbleService {

    private BufferedImage image;
    private Bubble bubble;

    public BackgroundBubbleService(Bubble bubble) {
        this.bubble = bubble;
        try {
            image = ImageIO.read(new File("image/round01_back.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean leftWall() {
        int x = bubble.getX();
        int y = bubble.getY();
        
        if (isWithinBounds(x - 5, y - 25)) {
            Color leftColor = new Color(image.getRGB(x - 5, y - 25));
            return isWallColor(leftColor);
        }
        return false;
    }

    public boolean rightWall() {
        int x = bubble.getX();
        int y = bubble.getY();
        
        if (isWithinBounds(x + 80, y - 25)) {
            Color rightColor = new Color(image.getRGB(x + 80, y - 25));
            return isWallColor(rightColor);
        }
        return false;
    }

    public boolean topWall() {
        int x = bubble.getX();
        int y = bubble.getY();
        
        if (isWithinBounds(x + 25, y - 85)) {
            Color topColor = new Color(image.getRGB(x + 25, y - 85));
            return isWallColor(topColor);
        }
        return false;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight();
    }

    // 벽 색상 여부 확인
    private boolean isWallColor(Color color) {
        // 여기에서 정확한 벽의 색상을 판단하는 로직을 구현해야 합니다.
        // 현재는 빨간색(RGB: 255, 0, 0)으로 판단하고 있습니다.
        return color.getRed() == 255 && color.getGreen() == 0 && color.getBlue() == 0;
    }
}
