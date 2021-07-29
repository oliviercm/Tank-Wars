package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author olivec
 */
public class Tank extends GameObject {
    private final double MOVEMENT_SPEED = 0.3f;
    private final double ROTATION_SPEED = 0.2f;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    Tank(double x, double y, double angle, BufferedImage img) {
        super(x, y, angle, img);
        this.autoSetSquareBoundingBox();
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

    private void moveForwards(long timeSinceLastTick) {
        double delta = this.MOVEMENT_SPEED * timeSinceLastTick;
        this.translateForward(delta);
    }

    private void moveBackwards(long timeSinceLastTick) {
        double delta = -this.MOVEMENT_SPEED * timeSinceLastTick;
        this.translateForward(delta);
    }

    private void rotateLeft(long timeSinceLastTick) {
        double delta = -this.ROTATION_SPEED * timeSinceLastTick;
        this.addAngle(delta);
    }

    private void rotateRight(long timeSinceLastTick) {
        double delta = this.ROTATION_SPEED * timeSinceLastTick;
        this.addAngle(delta);
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y + ", angle=" + this.angle;
    }
}