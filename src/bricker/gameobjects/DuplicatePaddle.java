package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class representing a duplicate paddle.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class DuplicatePaddle extends Paddle{
    private final Counter hitCounter;
    private final BrickerGameManager brickerGameManager;
    /**
     * Construct a new DuplicatePaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener The input listener instance that is in charge of processing the keyboard arguments.
     */
    public DuplicatePaddle(Vector2 topLeftCorner, // TODO: Should we delete this?
                           Vector2 dimensions,
                           Renderable renderable,
                           UserInputListener inputListener,
                           BrickerGameManager brickerGameManager,
                           float windowWidth) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowWidth);
        this.hitCounter = new Counter();
        this.brickerGameManager = brickerGameManager;
    }
    /**
     * The function is called whenever an object collides with the duplicate paddle.
     * It calls the onCollision method of its collision strategy therefore applying the strategy.
     * The function then increments the hit counter by one.
     * if the hit counter is equal or greater than 4 (aka after 4 hits) the object uses brickerGamerManager to delete itself.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
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
