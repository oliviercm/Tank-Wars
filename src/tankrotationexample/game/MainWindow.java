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
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                                       GameConstants.GAME_SCREEN_HEIGHT,
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
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        Graphics2D buffer = world.createGraphics();

        buffer.setPaint(new TexturePaint(this.worldBackgroundImage, new Rectangle(0, 0, 320, 240)));
        buffer.fillRect(0,0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

        this.t1.drawImage(buffer);

        Point leftCameraPosition = new Point((int) this.t1.getX(), (int) this.t1.getY());
        int leftX = clamp(leftCameraPosition.x - (GameConstants.GAME_SCREEN_WIDTH / 4), 0, GameConstants.GAME_SCREEN_WIDTH);
        int leftY = clamp(leftCameraPosition.y - (GameConstants.GAME_SCREEN_HEIGHT / 2), 0, GameConstants.GAME_SCREEN_HEIGHT);
        int leftXSize = clamp(GameConstants.GAME_SCREEN_WIDTH - leftX, 0, GameConstants.GAME_SCREEN_WIDTH / 2);
        int leftYSize = clamp(GameConstants.GAME_SCREEN_HEIGHT - leftY, 0, GameConstants.GAME_SCREEN_HEIGHT);

        Point rightCameraPosition = new Point(700, 300);
        int rightX = clamp(rightCameraPosition.x - (GameConstants.GAME_SCREEN_WIDTH / 4), 0, GameConstants.GAME_SCREEN_WIDTH);
        int rightY = clamp(rightCameraPosition.y - (GameConstants.GAME_SCREEN_HEIGHT / 2), 0, GameConstants.GAME_SCREEN_HEIGHT);
        int rightXSize = clamp(GameConstants.GAME_SCREEN_WIDTH - rightX, 0, GameConstants.GAME_SCREEN_WIDTH / 2);
        int rightYSize = clamp(GameConstants.GAME_SCREEN_HEIGHT - rightY, 0, GameConstants.GAME_SCREEN_HEIGHT);

        BufferedImage leftHalf = world.getSubimage(
                leftX,
                leftY,
                leftXSize,
                leftYSize);
        BufferedImage rightHalf = world.getSubimage(
                rightX,
                rightY,
                rightXSize,
                rightYSize);
        g2.drawImage(leftHalf,0,0,null);
        g2.drawImage(rightHalf,GameConstants.GAME_SCREEN_WIDTH / 2,0,null);
    }

    public static int clamp(int val, int min, int max) {
        return Math.min(Math.max(min, val), max);
    }
}