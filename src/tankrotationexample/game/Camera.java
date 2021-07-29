package tankrotationexample.game;

import java.util.HashSet;
import java.util.Set;

public class Camera {
    private static final HashSet<Camera> cameras = new HashSet();
    static Set<Camera> getCameras() {
        return Camera.cameras;
    }
    static void destroy(Camera o) {
        Camera.cameras.remove(o);
    }

    private final GameObject followObject;
    private final int cameraX;
    private final int cameraY;

    Camera(GameObject followObject, int cameraX, int cameraY) {
        this.followObject = followObject;
        this.cameraX = cameraX;
        this.cameraY = cameraY;

        Camera.cameras.add(this);
    }

    void destroy() {
        Camera.cameras.remove(this);
    }

    GameObject getFollowObject() {
        return this.followObject;
    }

    int getCameraX() {
        return this.cameraX;
    }

    int getCameraY() {
        return this.cameraY;
    }
}
