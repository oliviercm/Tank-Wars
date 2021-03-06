package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author olivec
 */
public class GameWindow extends JPanel implements Runnable {
    private ArrayList<Tank> tanks = new ArrayList<>();
    private final ArrayList<Camera> cameras = new ArrayList<>();
    private final int CAMERA_COLUMNS = 2;
    private final int CAMERA_ROWS = 1;

    private Hud hud;
    private BufferedImage world;
    private final Color worldBackgroundColor = new Color(140, 132, 87);
    private final Launcher lf;
    private long lastTickTime;

    public GameWindow(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        this.lastTickTime = System.currentTimeMillis();
        while (true) {
            long currentTime = System.currentTimeMillis();
            long timeSinceLastTick = currentTime - this.lastTickTime;
            this.lastTickTime = currentTime;

            // Run update on all GameObjects
            for (GameObject go : GameObject.getGameObjects()) {
                go.update(timeSinceLastTick);
            }

            this.repaint(); // redraw game

            // Check for game over condition
            for (Tank tank : this.tanks) {
                if (tank.getGameOver()) {
                    this.lf.setFrame("end");
                    return;
                }
            }
        }
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        ResourceHandler.loadImageResource("tank1", "tank1.png");
        ResourceHandler.loadImageResource("tank1transparent", "tank1transparent.png");
        ResourceHandler.loadImageResource("tank2", "tank2.png");
        ResourceHandler.loadImageResource("tank2transparent", "tank2transparent.png");
        ResourceHandler.loadImageResource("tankexplosion", "largeexplosion_strip.png");
        ResourceHandler.loadImageResource("bullet", "bullet1.png");
        ResourceHandler.loadImageResource("bulletexplosion", "smallexplosion_strip.png");
        ResourceHandler.loadImageResource("wall1", "wall1.png");
        ResourceHandler.loadImageResource("wall2", "wall2.png");
        ResourceHandler.loadImageResource("health100", "health100.png");
        ResourceHandler.loadImageResource("health75", "health75.png");
        ResourceHandler.loadImageResource("health50", "health50.png");
        ResourceHandler.loadImageResource("health25", "health25.png");
        ResourceHandler.loadImageResource("health0", "health0.png");
        ResourceHandler.loadImageResource("shield1", "shield1.png");
        ResourceHandler.loadImageResource("shield2", "shield2.png");
        ResourceHandler.loadImageResource("powerup_shield", "powerup_shield.png");
        ResourceHandler.loadImageResource("powerup_shotgun", "powerup_shotgun.png");
        ResourceHandler.loadImageResource("powerup_speed", "powerup_speed.png");
        ResourceHandler.loadImageResource("powerup_health", "powerup_health.png");

        MapLoader.loadMap("maps/map1");

        // Create player 1 and assign to camera
        Tank tank1 = new Tank(64, GameConstants.GAME_SCREEN_WIDTH / 2 - 24, 0,
                ResourceHandler.getImageResource("tank1"),
                ResourceHandler.getImageResource("tank1transparent"),
                ResourceHandler.getImageResource("bullet"),
                ResourceHandler.getImageResource("shield1"));
        this.tanks.add(tank1);
        this.cameras.add(new Camera(tank1, 0, 0));
        TankControl tc1 = new TankControl(tank1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        // Create player 2 and assign to camera
        Tank tank2 = new Tank(GameConstants.GAME_SCREEN_WIDTH - 64 - 48, GameConstants.GAME_SCREEN_WIDTH / 2 - 24, 180,
                ResourceHandler.getImageResource("tank2"),
                ResourceHandler.getImageResource("tank2transparent"),
                ResourceHandler.getImageResource("bullet"),
                ResourceHandler.getImageResource("shield2"));
        this.tanks.add(tank2);
        this.cameras.add(new Camera(tank2, 0, 1));
        TankControl tc2 = new TankControl(tank2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        // Create Hud handler
        this.hud = new Hud(tank1, tank2);

        this.setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);

        // Fill the entire screen with a flat background color
        g2d.setColor(this.worldBackgroundColor);
        g2d.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

        // Create a temporary buffer to draw graphics onto
        Graphics2D buffer = world.createGraphics();

        // Draw background into buffer
        buffer.setColor(this.worldBackgroundColor);
        buffer.fillRect(0,0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);

        // Draw all GameObjects into buffer
        for (GameObject go : GameObject.getGameObjects()) {
            go.drawImage(buffer);
        }

        // Draw HUD
        this.hud.drawWorld(buffer);

        // Draw all split screen camera views
        for (Camera cam : Camera.getCameras()) {
            drawCameraView(cam.getFollowObject(), world, g2d, cam.getCameraX(), cam.getCameraY());
        }

        // Set colors and thickness for lines separating split screen cameras and minimap
        final int borderThickness = 2;
        final Color borderColor = Color.WHITE;
        g2d.setColor(borderColor);

        // Draw borders separating split screens
        for (int i = 0; i < this.CAMERA_COLUMNS - 1; i++) {
            g2d.fill(new Rectangle((i + 1) * GameConstants.GAME_SCREEN_WIDTH / CAMERA_COLUMNS - borderThickness / 2, 0, borderThickness, GameConstants.GAME_SCREEN_HEIGHT));
        }
        for (int i = 0; i < this.CAMERA_ROWS - 1; i++) {
            g2d.fill(new Rectangle(0, (i + 1) * GameConstants.GAME_SCREEN_HEIGHT / CAMERA_ROWS - borderThickness / 2, GameConstants.GAME_SCREEN_WIDTH, borderThickness));
        }

        // Draw minimap and border
        BufferedImage minimap = world.getSubimage(0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        final double minimapScale = 1f / 6f;
        g2d.scale(minimapScale, minimapScale);
        g2d.drawImage(minimap, (int) ((GameConstants.GAME_SCREEN_WIDTH / minimapScale / 2) - (minimap.getWidth() / 2)), 0, null);
        g2d.setStroke(new BasicStroke((int) (borderThickness / minimapScale)));
        g2d.drawRect((int) ((GameConstants.GAME_SCREEN_WIDTH / minimapScale / 2) - (minimap.getWidth() / 2)), -(int) (borderThickness / minimapScale * 2), minimap.getWidth(), minimap.getHeight() + (int) (borderThickness / minimapScale * 2));
        g2d.scale(1 / minimapScale, 1 / minimapScale);

        // Draw lives
        this.hud.drawScreen(g2d);
    }

    // Draws a centered "view" based on the location of a GameObject onto the passed Graphics.
    private void drawCameraView(GameObject followObject, BufferedImage world, Graphics2D g2d, int cameraRow, int cameraColumn) {
        // Get the position of the object to center the camera view on
        Point cameraPosition = new Point((int) followObject.getX(), (int) followObject.getY());
        // Find the top-left corner to start cutting the image from
        int imageX = cameraPosition.x - (GameConstants.GAME_SCREEN_WIDTH / (2 * (this.CAMERA_COLUMNS))) + followObject.getImage().getWidth() / 2;
        int imageY = cameraPosition.y - (GameConstants.GAME_SCREEN_HEIGHT / (2 * (this.CAMERA_ROWS))) + followObject.getImage().getHeight() / 2;
        // Find the bottom-right corner to end cutting the image from, making sure that it doesn't bypass the size of the image
        int clampedImageX = Util.clamp(imageX, 0, GameConstants.WORLD_WIDTH);
        int clampedImageY = Util.clamp(imageY, 0, GameConstants.WORLD_HEIGHT);
        // Find the X offset (margin) that should be used to display the image (if the subimage had to be clamped)
        int imageMarginX = 0;
        if (imageX < 0) {
            imageMarginX = -imageX;
        } else if (imageX > GameConstants.WORLD_WIDTH) {
            imageMarginX = imageX;
        }
        int imageSizeX = Util.clamp(GameConstants.WORLD_WIDTH - clampedImageX, 0, GameConstants.GAME_SCREEN_WIDTH / (this.CAMERA_COLUMNS) - imageMarginX);
        // Find the Y offset (margin) that should be used to display the image (if the subimage had to be clamped)
        int imageMarginY = 0;
        if (imageY < 0) {
            imageMarginY = -imageY;
        } else if (imageY > GameConstants.WORLD_HEIGHT) {
            imageMarginY = imageY;
        }
        int imageSizeY = Util.clamp(GameConstants.WORLD_HEIGHT - clampedImageY, 0, GameConstants.GAME_SCREEN_HEIGHT / (this.CAMERA_ROWS) - imageMarginY);

        // Draw the image, using the calculated margins
        if (imageSizeX > 0 && imageSizeY > 0) {
            BufferedImage cameraView = world.getSubimage(clampedImageX, clampedImageY, imageSizeX, imageSizeY);
            g2d.drawImage(cameraView, cameraColumn * GameConstants.GAME_SCREEN_WIDTH / 2 + imageMarginX, cameraRow * GameConstants.GAME_SCREEN_HEIGHT / 2 + imageMarginY, null);
        }
    }
}