package bubble.game.component;

import javax.swing.*;
import java.awt.*;

public class Map {

    private int currentMapNumber ;
    private ImageIcon map;
    //private JLabel map;

    public Map() {
        this.currentMapNumber = 1;
        CreateMap1();
    }

    public void switchMap() {

    }

    public void CreateMap1( ) {
        map = new ImageIcon(Toolkit.getDefaultToolkit().createImage("image/round01.png"));
    }
    public ImageIcon CreateMap2( ) {
        map = new ImageIcon(Toolkit.getDefaultToolkit().createImage("image/round02.png"));
        return map;
    }
//    public JLabel getMap() {
//        return map;
//    }
    public ImageIcon getMap() {
        return map;
    }
}
