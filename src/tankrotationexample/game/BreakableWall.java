package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class BreakableWall extends Wall implements Damageable {
    private int health;

    BreakableWall(double x, double y, double angle, BufferedImage img) {
        super(x, y, angle, img);
        this.health = 50;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.onDeath();
        }
    }

    private void onDeath() {
        this.destruct();
    }
}
