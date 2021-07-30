package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private double speed;
    private GameObject owner;
    private int age = 0;
    private int damage;

    Bullet(double x, double y, double angle, BufferedImage img) {
        super(x, y, angle, img);
        this.damage = 25;
    }

    void setSpeed(double speed) { this.speed = speed; }

    void update(long timeSinceLastTick) {
        super.update(timeSinceLastTick);

        this.age += timeSinceLastTick;
        if (this.age >= 5000) {
            this.destruct();
        } else {
            double delta = this.speed * timeSinceLastTick;
            this.translateForward(delta);
        }

        for (GameObject go : this.getIntersectingObjects()) {
            if (go != this.owner) {
                if (go instanceof Damageable) {
                    boolean tookDamage = ((Damageable) go).takeDamage(this.damage);
                    if (tookDamage) {
                        this.afterHit();
                        break;
                    }
                } else if (go.getSolid()) {
                    this.afterHit();
                    break;
                }
            }
        }
    }

    void afterHit() {
        new Animation(this.x, this.y, 0, ResourceHandler.getImageResource("bulletexplosion"), 6, 30);
        this.destruct();
    }

    void setOwner(GameObject go) {
        this.owner = go;
    }
}
