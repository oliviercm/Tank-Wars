package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static javax.imageio.ImageIO.read;

public class ResourceHandler {
    private static HashMap<String, BufferedImage> images = new HashMap<>();

    public static void loadImageResource(String key, String path) {
        try {
            ResourceHandler.images.put(key, read(Objects.requireNonNull(GameWindow.class.getClassLoader().getResource(path))));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static BufferedImage getImageResource(String key) {
        return ResourceHandler.images.get(key);
    }
}
