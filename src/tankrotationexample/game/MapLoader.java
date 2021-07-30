package tankrotationexample.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MapLoader {
    public static void loadMap(String path) {
        try {
            InputStreamReader isr = new InputStreamReader(GameWindow.class.getClassLoader().getResourceAsStream(path));
            BufferedReader mapReader = new BufferedReader(isr);

            String row = mapReader.readLine();
            if (row == null) {
                throw new IOException("Map file does not contain any data");
            }
            String[] mapInfo = row.split("\t");
            int mapCols = Integer.parseInt(mapInfo[0]);
            int mapRows = Integer.parseInt(mapInfo[1]);

            for (int curRow = 0; curRow < mapRows; curRow++) {
                row =  mapReader.readLine();
                mapInfo = row.split("\t");
                for (int curCol = 0; curCol < mapCols; curCol++) {
                    switch (mapInfo[curCol]) {
                        case "1": { // Unbreakable wall
                            new Wall(32 * curCol, 32 * curRow, 0, ResourceHandler.getImageResource("wall1"));
                            break;
                        }
                        case "2": { // Breakable wall
                            new BreakableWall(32 * curCol, 32 * curRow, 0, ResourceHandler.getImageResource("wall2"));
                            break;
                        }
                        case "3": { // Shield powerup
                            new Powerup(32 * curCol, 32 * curRow, PowerupType.SHIELD);
                            break;
                        }
                        case "4": { // Shotgun Powerup
                            new Powerup(32 * curCol, 32 * curRow, PowerupType.SHOTGUN);
                            break;
                        }
                        case "5": { // Speed Powerup
                            new Powerup(32 * curCol, 32 * curRow, PowerupType.SPEED);
                            break;
                        }
                        case "6": { // Health Powerup
                            new Powerup(32 * curCol, 32 * curRow, PowerupType.HEALTH);
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
