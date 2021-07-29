package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;

public class GameObject {
    private static final HashSet<GameObject> gameObjects = new HashSet();
    static Set<GameObject> getGameObjects() {
        return GameObject.gameObjects;
    }
    static final void destroy(GameObject o) {
        GameObject.gameObjects.remove(o);
    }

    protected double x;
    protected double y;
    protected double angle;
    private int bbx;
    private int bby;

    protected BufferedImage img;

    GameObject(double x, double y, double angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.bbx = img.getWidth();
        this.bby = img.getHeight();

        GameObject.gameObjects.add(this);
    }

    GameObject(double x, double y, double angle, BufferedImage img, int bbx, int bby) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.bbx = bbx;
        this.bby = bby;

        GameObject.gameObjects.add(this);
    }

    final void destroy() {
        GameObject.gameObjects.remove(this);
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

    void update(long timeSinceLastTick) {
        return;
    }

    void setBoundingBox(int bbx, int bby) {
        this.bbx = bbx;
        this.bby = bby;
    }

    BufferedImage getImage() { return this.img; }

    void translateForward(double distance) {
        this.addX(distance * Math.cos(Math.toRadians(this.angle)));
        this.addY(distance * Math.sin(Math.toRadians(this.angle)));
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);

        // Draw centered bounding box if debug mode
        if (GameConstants.DEBUG_MODE) {
            g2d.setColor(new Color(255, 0, 255));
            g2d.draw(this.getCenteredBoundingBox());
        }
    }

    // Returns a Rectangle sized on the GameObject's bounding box parameters, centered around the GameObject's image
    Rectangle getCenteredBoundingBox() {
        int bbxOrigin = (int) (this.x + ((this.img.getWidth() - this.bbx) / 2));
        int bbyOrigin = (int) (this.y + ((this.img.getHeight() - this.bby) / 2));
        return new Rectangle(bbxOrigin, bbyOrigin, this.bbx, this.bby);
    }

    // Automatically set bounding box size to a square based on the GameObject's image
    protected void autoSetSquareBoundingBox() {
        int bbs = Math.max(img.getWidth(), img.getHeight());
        this.setBoundingBox(bbs, bbs);
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y;
    }
}
