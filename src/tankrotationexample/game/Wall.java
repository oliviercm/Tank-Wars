package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    Wall(double x, double y, double angle, BufferedImage img) {
        super(x, y, angle, img);
        this.setSolid(true);
    }
}
