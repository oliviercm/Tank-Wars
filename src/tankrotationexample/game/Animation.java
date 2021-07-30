package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Animation extends GameObject {
    final private BufferedImage strip;
    final private int framesPerSecond;
    final private int frames;
    private int elapsedTicks;

    Animation(double x, double y, double angle, BufferedImage strip, int frames, int framesPerSecond) {
        super(x, y, angle);
        this.strip = strip;
        this.frames = frames;
        this.framesPerSecond = framesPerSecond;
        this.elapsedTicks = 0;
        this.img = this.strip.getSubimage(0, 0, this.strip.getWidth() / this.frames, this.strip.getHeight());
        this.setSolid(false);
    }

    public void update(long timeSinceLastTick) {
        super.update(timeSinceLastTick);
        double ticksPerFrame = 1000 / this.framesPerSecond;
        int currentFrame = (int) (Math.floor(this.elapsedTicks / ticksPerFrame));
        if (currentFrame >= this.frames) {
            this.destruct();
            return;
        }
        this.img = this.strip.getSubimage(this.strip.getWidth() / this.frames * currentFrame, 0, this.strip.getWidth() / this.frames, this.strip.getHeight());
        this.elapsedTicks += timeSinceLastTick;
    }
}
