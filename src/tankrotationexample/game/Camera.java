package tankrotationexample.game;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class Camera {
    private static final Set<Camera> cameras = Collections.newSetFromMap(new WeakHashMap<>());
    static Set<Camera> getCameras() {
        return Camera.cameras;
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
