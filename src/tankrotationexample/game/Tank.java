package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author olivec
 */
public class Tank extends GameObject implements Damageable {
    private final double MOVEMENT_SPEED = 0.25f;
    private final double ROTATION_SPEED = 0.2f;
    private final int MAX_TANK_HEALTH = 100;

    private int health;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private BufferedImage bulletImage;

    Tank(double x, double y, double angle, BufferedImage img, BufferedImage bullet) {
        super(x, y, angle, img);
        this.bulletImage = bullet;
        this.autoSetSquareBoundingBox();
        this.health = this.MAX_TANK_HEALTH;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update(long timeSinceLastTick) {
        if (this.isDead()) {
            return;
        }
        super.update(timeSinceLastTick);
        if (this.UpPressed) {
            this.moveForwards(timeSinceLastTick);
        }
        if (this.DownPressed) {
            this.moveBackwards(timeSinceLastTick);
        }
        if (this.LeftPressed) {
            this.rotateLeft(timeSinceLastTick);
        }
        if (this.RightPressed) {
            this.rotateRight(timeSinceLastTick);
        }
    }

    void shoot() {
        if (this.isDead()) {
            return;
        }

        Bullet bullet = new Bullet(this.x, this.y, this.getAngle(), this.bulletImage);

        final int bulletBBSize = 16;
        Point tankCenter = this.getBBCenter();
        Point bulletBBOrigin = new Point(tankCenter.x - this.bulletImage.getWidth() / 2, tankCenter.y - this.bulletImage.getHeight() / 2);

        bullet.setBoundingBox(bulletBBSize, bulletBBSize);
        bullet.setX(bulletBBOrigin.x);
        bullet.setY(bulletBBOrigin.y);
        bullet.setSpeed(0.5);
        bullet.setOwner(this);
        bullet.setSolid(false);
        return;
    }

    private void moveForwards(long timeSinceLastTick) {
        double delta = this.MOVEMENT_SPEED * timeSinceLastTick;
        this.moveForward(delta);
    }

    private void moveBackwards(long timeSinceLastTick) {
        double delta = -this.MOVEMENT_SPEED * timeSinceLastTick;
        this.moveForward(delta);
    }

    private void rotateLeft(long timeSinceLastTick) {
        double delta = -this.ROTATION_SPEED * timeSinceLastTick;
        this.addAngle(delta);
    }

    private void rotateRight(long timeSinceLastTick) {
        double delta = this.ROTATION_SPEED * timeSinceLastTick;
        this.addAngle(delta);
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.isDead()) {
            this.destruct();
        }
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", angle=" + this.angle;
    }
}