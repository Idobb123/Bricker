package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class representing a temporary paddle.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class TemporaryPaddle extends Paddle{
    /** the object tag for the main ball */
    private static final int MAXIMAL_HITS_ALLOWED = 4;
    /** The tag of the "regular" ball */
    private static final String ORIGINAL_BALL_TAG = "regularBall";
    private final Counter hitCounter;
    private final BrickerGameManager BrickerGameManager;
    /**
     * Constructs a new TemporaryPaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener An input listener instance that is in charge of processing the keyboard arguments
     * @param BrickerGameManager The bricker game manager.
     * @param windowWidth The width of the game window.
     */
    public TemporaryPaddle(Vector2 topLeftCorner,
                           Vector2 dimensions,
                           Renderable renderable,
                           UserInputListener inputListener,
                           BrickerGameManager BrickerGameManager,
                           float windowWidth) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowWidth);
        this.hitCounter = new Counter();
        this.BrickerGameManager = BrickerGameManager;
    }
    /**
     * The function is called whenever an object collides with the temporary paddle.
     * It calls the onCollisionEnter of it super class.
     * After that, if the object that collided with the TemporaryPaddle is the original ball.
     * it increases the hitCounter.
     * In case it reached the maximal hits allowed (4) it deletes itself using the bricker game manager.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(ORIGINAL_BALL_TAG))
            hitCounter.increment();
        if (hitCounter.value() >= MAXIMAL_HITS_ALLOWED) {
            BrickerGameManager.deleteObject(this);
        }
    }
}
