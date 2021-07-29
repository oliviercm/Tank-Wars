package tankrotationexample.game;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Camera {
    private static final Set<Camera> cameras = Collections.newSetFromMap(new ConcurrentHashMap<>());
    static Camera[] getCameras() {
        return Camera.cameras.toArray(new Camera[0]);
    }
    static final void destroy(Camera o) {
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
