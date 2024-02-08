package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class DuplicatePaddle extends Paddle{
    private final Counter hitCounter;
    private final BrickerGameManager brickerGameManager;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener
     */
    public DuplicatePaddle(Vector2 topLeftCorner,
                           Vector2 dimensions,
                           Renderable renderable,
                           UserInputListener inputListener,
                           BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener);
        this.hitCounter = new Counter();
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) // cyber TODO: use tags instead of instanceof
            hitCounter.increment();
        if (hitCounter.value() >= 4) {
            brickerGameManager.deleteObject(this);
        }
    }
}
