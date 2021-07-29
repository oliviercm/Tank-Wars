package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private double speed;
    private GameObject owner;
    private int age = 0;

    Bullet(double x, double y, double angle, BufferedImage img) {
        super(x, y, angle, img);
    }

    void setSpeed(double speed) { this.speed = speed; }

    void update(long timeSinceLastTick) {
        super.update(timeSinceLastTick);

        this.age += timeSinceLastTick;
        if (this.age >= 2000) {
            this.destruct();
        } else {
            double delta = this.speed * timeSinceLastTick;
            this.translateForward(delta);
        }
    }

    void setOwner(GameObject go) {
        this.owner = go;
    }
}
