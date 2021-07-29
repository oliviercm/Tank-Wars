package tankrotationexample.game;

import java.awt.image.BufferedImage;

public abstract class DamageableObject extends GameObject {
    int health;

    DamageableObject(double x, double y, double angle, BufferedImage img, int health) {
        super(x, y, angle, img);
        this.health = health;
    }

    int getHealth() { return this.health; };
    void setHealth(int health) { this.health = health; };

    void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.onDeath();
        }
    }

    protected void onDeath() {
        this.destruct();
    }
}
