package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author olivec
 */
public class Tank extends GameObject implements Damageable {
    private final double MOVEMENT_SPEED = 0.25f;
    private final double ROTATION_SPEED = 0.2f;
    private final int MAX_TANK_HEALTH = 100;

    private final double spawnX;
    private final double spawnY;
    private final double spawnAngle;
    private int health;
    private int lives;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private BufferedImage tankImage;
    private BufferedImage tankTransparentImage;
    private BufferedImage bulletImage;
    private BufferedImage shieldImage;

    private int shieldDuration = 0;
    private int invulnerableDuration = 0;
    private int shotgunDuration = 0;


    Tank(double x, double y, double angle, BufferedImage tankImg, BufferedImage tankTransparentImg, BufferedImage bullet, BufferedImage shield) {
        super(x, y, angle, tankImg);
        this.spawnX = x;
        this.spawnY = y;
        this.spawnAngle = angle;
        this.tankImage = tankImg;
        this.tankTransparentImage = tankTransparentImg;
        this.bulletImage = bullet;
        this.shieldImage = shield;
        this.autoSetSquareBoundingBox();
        this.health = this.MAX_TANK_HEALTH;
        this.invulnerableDuration = 4000;
        this.lives = 3;
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
        this.invulnerableDuration = (int) (Math.max(0, this.invulnerableDuration - timeSinceLastTick));
        this.shieldDuration = (int) (Math.max(0, this.shieldDuration - timeSinceLastTick));
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

        this.createBullet(0);

        if (this.hasShotgun()) {
            final int shotgunSpread = 25;
            this.createBullet(-shotgunSpread);
            this.createBullet(shotgunSpread);
        }

        return;
    }

    void createBullet(int offsetAngle) {
        Bullet bullet = new Bullet(this.x, this.y, this.getAngle() + offsetAngle, this.bulletImage);

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

    public boolean takeDamage(int damage) {
        if (this.hasInvulnerability()) {
            return false;
        }
        this.health -= damage;
        if (this.isDead()) {
            if (this.lives > 0) {
                this.respawn();
                this.lives--;
            } else {
                this.destruct();
            }
        }
        return true;
    }

    private void respawn() {
        this.setX(this.spawnX);
        this.setY(this.spawnY);
        this.setAngle(this.spawnAngle);
        this.setHealth(this.MAX_TANK_HEALTH);
        this.invulnerableDuration = 3000;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

    void drawImage(Graphics g) {
        if (this.hasInvulnerability()) {
            final int flickerDuration = 200;
            if (this.invulnerableDuration % flickerDuration < flickerDuration / 2) {
                this.img = this.tankImage;
            } else {
                this.img = this.tankTransparentImage;
            }
        } else {
            this.img = this.tankImage;
        }

        super.drawImage(g);

        // Draw shield graphics
        if (this.hasShield()) {
            Point center = this.getBBCenter();
            g.drawImage(this.shieldImage, (int) (center.x - this.shieldImage.getWidth() / 2), (int) (center.y - this.shieldImage.getHeight() / 2), null);
        }
    }

    boolean hasShield() {
        return this.shieldDuration > 0;
    }

    boolean hasInvulnerability() {
        return this.invulnerableDuration > 0;
    }

    boolean hasShotgun() {
        return this.shotgunDuration > 0;
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", angle=" + this.angle;
    }
}