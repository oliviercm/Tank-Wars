package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


import static javax.imageio.ImageIO.read;

/**
 *
 * @author olivec
 */
public class MainWindow extends JPanel implements Runnable {
    private final int CAMERA_COLUMNS = 2;
    private final int CAMERA_ROWS = 1;

    private BufferedImage world;
    private BufferedImage worldBackgroundImage;
    private Tank t1;
    private Launcher lf;
    private long tick = 0;
    private long lastTickTime;

    public MainWindow(Launcher lf){
        this.lf = lf;
        this.lastTickTime = System.currentTimeMillis();
    }

    @Override
    public void run(){
        try {
            this.resetGame();
            while (true) {
                this.tick++;

                long currentTime = System.currentTimeMillis();
                long timeSinceLastTick = currentTime - this.lastTickTime;
                this.lastTickTime = currentTime;

                this.t1.update(timeSinceLastTick); // update tank
                this.repaint();   // redraw game
                Thread.sleep(1000 / 144); //sleep for a few milliseconds
                /*
                * simulate an end game event
                * we will do this with by ending the game when drawn 2000 frames have been drawn
                */
//                if(this.tick > 2000){
//                    this.lf.setFrame("end");
//                    return;
//                }
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                                       GameConstants.WORLD_HEIGHT,
                                       BufferedImage.TYPE_INT_RGB);

        BufferedImage t1img = null;
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            t1img = read(Objects.requireNonNull(MainWindow.class.getClassLoader().getResource("tank1.png")));
            this.worldBackgroundImage = read(Objects.requireNonNull(MainWindow.class.getClassLoader().getResource("background.png")));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank(300, 300, 0, t1img);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.setBackground(Color.BLACK);
        this.lf.getJf().addKeyListener(tc1);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);

        Graphics2D buffer = world.createGraphics();

//        buffer.setColor(Color.GRAY);
//        buffer.fillRect(0,0,GameConstants.WORLD_WIDTH,GameConstants.WORLD_HEIGHT);

        buffer.setPaint(new TexturePaint(this.worldBackgroundImage, new Rectangle(0, 0, 320, 240)));
        buffer.fillRect(0,0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);

        this.t1.drawImage(buffer);

        drawCameraView(this.t1, world, g2d, 0, 0);
//        drawCameraView(this.t1, world, g2d, 0, 1);

//        BufferedImage minimap = world.getSubimage(0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
//        g2d.scale(0.2, 0.2);
//        g2d.drawImage(minimap, 200, 200, null);
    }

    private void drawCameraView(GameObject followObject, BufferedImage world, Graphics2D g2d, int cameraRow, int cameraColumn) {
        Point cameraPosition = new Point((int) followObject.getX(), (int) followObject.getY());
        int imageX = cameraPosition.x - (GameConstants.GAME_SCREEN_WIDTH / (2 * (this.CAMERA_COLUMNS))) + followObject.getImage().getWidth() / 2;
        int imageY = cameraPosition.y - (GameConstants.GAME_SCREEN_HEIGHT / (2 * (this.CAMERA_ROWS))) + followObject.getImage().getHeight() / 2;
        int clampedImageX = clamp(imageX, 0, GameConstants.WORLD_WIDTH);
        int clampedImageY = clamp(imageY, 0, GameConstants.WORLD_HEIGHT);
        int imageMarginX = 0;
        if (imageX < 0) {
            imageMarginX = -imageX;
        } else if (imageX > GameConstants.WORLD_WIDTH) {
            imageMarginX = imageX;
        }
        int imageSizeX = clamp(GameConstants.WORLD_WIDTH - clampedImageX, 0, GameConstants.GAME_SCREEN_WIDTH / (this.CAMERA_COLUMNS) - imageMarginX);
        int imageMarginY = 0;
        if (imageY < 0) {
            imageMarginY = -imageY;
        } else if (imageY > GameConstants.WORLD_HEIGHT) {
            imageMarginY = imageY;
        }
        int imageSizeY = clamp(GameConstants.WORLD_HEIGHT - clampedImageY, 0, GameConstants.GAME_SCREEN_HEIGHT / (this.CAMERA_ROWS) - imageMarginY);

        if (imageSizeX > 0 && imageSizeY > 0) {
            BufferedImage cameraView = world.getSubimage(clampedImageX, clampedImageY, imageSizeX, imageSizeY);
            g2d.drawImage(cameraView, cameraColumn * GameConstants.GAME_SCREEN_WIDTH / 2 + imageMarginX, cameraRow * GameConstants.GAME_SCREEN_HEIGHT / 2 + imageMarginY, null);
        }
    }

    public static int clamp(int val, int min, int max) {
        return Math.min(Math.max(min, val), max);
    }
}