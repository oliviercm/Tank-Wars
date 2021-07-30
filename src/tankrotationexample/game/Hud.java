package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Hud {
    private Tank leftPlayer;
    private Tank rightPlayer;

    Hud(Tank leftPlayer, Tank rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
    }

    // Draw world space elements (healthbars)
    public void drawWorld(Graphics2D g2d) {
        // Draw player health bars
        Hud.drawPlayerHealth(g2d, this.leftPlayer, 0);
        Hud.drawPlayerHealth(g2d, this.rightPlayer, 1);
    }

    // Draw screen space elements (lives)
    public void drawScreen(Graphics2D g2d) {
        this.drawLives(g2d, leftPlayer, 0);
        this.drawLives(g2d, rightPlayer, 1);
    }

    private void drawLives(Graphics2D g2d, Tank tank, int side) {
        BufferedImage tankImage = tank.getImage();
        for (int i = 0; i < tank.getLives(); i++) {
            g2d.drawImage(
                    tankImage,
                    (side * GameConstants.GAME_SCREEN_WIDTH / 2) + 16 + (tankImage.getWidth() + 8) * i,
                    GameConstants.GAME_SCREEN_HEIGHT - tankImage.getHeight() * 2 - 8,
                    null);
        }
    }

    private static void drawPlayerHealth(Graphics2D g2d, Tank player, int side) {
        int offset = 0;
        switch (side) {
            case 0: {
                offset = 1;
                break;
            }
            case 1: {
                offset = 3;
                break;
            }
        }
        BufferedImage healthBarImage = Hud.getImageFromHealth(player.getHealth());
        Point playerPosition = player.getBBCenter();
        int playerHealthXCoord = playerPosition.x - healthBarImage.getWidth() / 2;
        int playerHealthYCoord = playerPosition.y - healthBarImage.getHeight() - player.getImage().getHeight() - 4;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(playerHealthXCoord - 1, playerHealthYCoord - 1, healthBarImage.getWidth() + 2, healthBarImage.getHeight() + 2);
        g2d.drawImage(healthBarImage, playerHealthXCoord, playerHealthYCoord, null);
    }

    private static BufferedImage getImageFromHealth(int health) {
        if (health > 75) {
            return ResourceHandler.getImageResource("health100");
        } else if (health > 50) {
            return ResourceHandler.getImageResource("health75");
        } else if (health > 25) {
            return ResourceHandler.getImageResource("health50");
        } else if (health > 0) {
            return ResourceHandler.getImageResource("health25");
        } else {
            return ResourceHandler.getImageResource("health0");
        }
    }
}
