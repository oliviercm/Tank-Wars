package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Powerup extends GameObject {
    final PowerupType type;

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
        this.img = powerupImage;
        this.type = type;
        this.setSolid(false);
        this.autoSetSquareBoundingBox();
    }
}
