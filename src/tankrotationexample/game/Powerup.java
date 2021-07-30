package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Powerup extends GameObject {
    private final PowerupType type;
    private boolean active;
    private int respawnTime = 0;

    Powerup(double x, double y, PowerupType type) {
        super(x, y, 0);
        BufferedImage powerupImage;
        switch (type) {
            case SHIELD:
                powerupImage = ResourceHandler.getImageResource("powerup_shield");
                break;
            case SHOTGUN:
                powerupImage = ResourceHandler.getImageResource("powerup_shotgun");
                break;
            case SPEED:
                powerupImage = ResourceHandler.getImageResource("powerup_speed");
                break;
            case HEALTH:
                powerupImage = ResourceHandler.getImageResource("powerup_health");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        this.active = true;
        this.img = powerupImage;
        this.type = type;
        this.setSolid(false);
        this.autoSetSquareBoundingBox();
    }

    public void update(long timeSinceLastTick) {
        super.update(timeSinceLastTick);
        this.respawnTime = (int) (Math.max(0, this.respawnTime - timeSinceLastTick));
        if (!this.active && this.respawnTime <= 0) {
            this.active = true;
        }
    }

    public void activate(Tank tank) {
        if (!this.active) {
            return;
        }
        switch (this.type) {
            case SHIELD:
                tank.setShieldDuration(600000);
                break;
            case SHOTGUN:
                tank.setShotgunDuration(10000);
                break;
            case SPEED:
                tank.setSpeedDuration(10000);
                break;
            case HEALTH:
                if (tank.getHealth() < tank.getMaxHealth()) {
                    tank.setHealth(tank.getMaxHealth());
                    break;
                } else {
                    return;
                }
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        this.active = false;
        this.respawnTime = 60000;
    }

    void drawImage(Graphics g) {
        if (this.active) {
            super.drawImage(g);
        }
    }
}
