package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameObject {
    protected double x;
    protected double y;
    protected double angle;

    protected BufferedImage img;

    GameObject(double x, double y, double angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
    }

    double getX() { return this.x; }
    void setX(double x) { this.x = x; }
    void addX(double addX) { this.x += addX; }

    double getY() { return this.y; }
    void setY(double y) { this.y = y; }
    void addY(double addY) { this.y += addY; }

    double getAngle() { return this.angle; }
    void setAngle(double angle) { this.angle = angle; }
    void addAngle(double addAngle) { this.angle += addAngle; }

    void translateForward(double distance) {
        this.addX(distance * Math.cos(Math.toRadians(this.angle)));
        this.addY(distance * Math.sin(Math.toRadians(this.angle)));
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y;
    }
}
