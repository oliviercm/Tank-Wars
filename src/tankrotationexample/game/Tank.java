package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author olivec
 */
public class Tank {
    private double x;
    private double y;
    private double angle;

    private final float MOVEMENT_SPEED = 0.3f;
    private final float ROTATION_SPEED = 0.2f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    Tank(double x, double y, double angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
    }

    void setX(double x){
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
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

    private void moveForwards(long timeSinceLastTick) {
        double delta = timeSinceLastTick * this.MOVEMENT_SPEED;
        this.x += delta * Math.cos(Math.toRadians(this.angle));
        this.y += delta * Math.sin(Math.toRadians(this.angle));
        checkBorder();
    }

    private void moveBackwards(long timeSinceLastTick) {
        this.x -= this.MOVEMENT_SPEED * Math.cos(Math.toRadians(this.angle)) * timeSinceLastTick;
        this.y -= this.MOVEMENT_SPEED * Math.sin(Math.toRadians(this.angle)) * timeSinceLastTick;
        checkBorder();
    }

    private void rotateLeft(long timeSinceLastTick) {
        double delta = this.ROTATION_SPEED * timeSinceLastTick;
        this.angle -= delta;
    }

    private void rotateRight(long timeSinceLastTick) {
        double delta = this.ROTATION_SPEED * timeSinceLastTick;
        this.angle += delta;
    }

    private void checkBorder() {
        if (this.x < 30) {
            this.x = 30;
        }
        if (this.x >= GameConstants.GAME_SCREEN_WIDTH - 88) {
            this.x = GameConstants.GAME_SCREEN_WIDTH - 88;
        }
        if (this.y < 40) {
            this.y = 40;
        }
        if (this.y >= GameConstants.GAME_SCREEN_HEIGHT - 80) {
            this.y = GameConstants.GAME_SCREEN_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", angle=" + this.angle;
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }
}