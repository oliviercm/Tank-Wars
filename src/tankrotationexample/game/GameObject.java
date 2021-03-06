package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GameObject {
    private static final Set<GameObject> gameObjects = Collections.newSetFromMap(new ConcurrentHashMap<>());
    static GameObject[] getGameObjects() {
        return GameObject.gameObjects.toArray(new GameObject[0]);
    }
    static final void destroy(GameObject o) {
        GameObject.gameObjects.remove(o);
    }

    protected double x;
    protected double y;
    protected double angle;
    private int bbx;
    private int bby;
    private boolean isSolid = true; // Whether this object blocks the movement of other objects

    protected BufferedImage img;

    GameObject(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bbx = 0;
        this.bby = 0;

        GameObject.gameObjects.add(this);
    }

    GameObject(double x, double y, double angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.bbx = img.getWidth();
        this.bby = img.getHeight();

        GameObject.gameObjects.add(this);
    }

    final void destruct() {
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

    boolean getSolid() { return this.isSolid; }
    void setSolid(boolean solid) { this.isSolid = solid; }

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

    // Acts like translateForward, but implements sliding against other objects such as walls instead of moving inside them
    void moveForward(double distance) {
        double prevX = this.x;
        this.addX(distance * Math.cos(Math.toRadians(this.angle)));
        if (this.isColliding()) {
            this.setX(prevX);
        }

        double prevY = this.y;
        this.addY(distance * Math.sin(Math.toRadians(this.angle)));
        if (this.isColliding()) {
            this.setY(prevY);
        }
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);

        // Draw centered bounding box if debug mode
        if (GameConstants.DEBUG_MODE) {
            g2d.setColor(new Color(255, 0, 255));
            g2d.draw(this.getBoundingBox());
        }
    }

    // Returns a point at the center of the GameObject's bounding box
    Point getBBCenter() {
        Point boundingBoxOrigin = this.getBoundingBoxOrigin();
        return new Point(boundingBoxOrigin.x + this.bbx / 2, boundingBoxOrigin.y + this.bby / 2);
    }

    // Returns the origin of the bounding box's origin (top left corner) based on the GameObject's image
    Point getBoundingBoxOrigin() {
        return new Point((int) (this.x + ((this.img.getWidth() - this.bbx) / 2)), (int) (this.y + ((this.img.getHeight() - this.bby) / 2)));
    }

    // Returns a Rectangle sized on the GameObject's bounding box parameters, centered around the GameObject's image
    Rectangle getBoundingBox() {
        Point boundingBoxOrigin = this.getBoundingBoxOrigin();
        return new Rectangle(boundingBoxOrigin.x, boundingBoxOrigin.y, this.bbx, this.bby);
    }

    // Automatically set bounding box size to a square based on the GameObject's image
    protected void autoSetSquareBoundingBox() {
        int bbs = Math.max(img.getWidth(), img.getHeight());
        this.setBoundingBox(bbs, bbs);
    }

    ArrayList<GameObject> getIntersectingObjects() {
        ArrayList<GameObject> intersecting = new ArrayList<>();
        for (GameObject go : GameObject.getGameObjects()) {
            if (go != this && this.getBoundingBox().intersects(go.getBoundingBox())) {
                intersecting.add(go);
            }
        }
        return intersecting;
    }

    private boolean isColliding() {
        for (GameObject go : this.getIntersectingObjects()) {
            if (go.getSolid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "x=" + this.x + ", y=" + this.y;
    }
}
