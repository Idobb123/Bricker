package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class representing a heart object.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class Heart extends GameObject {
    private static final String ORIGINAL_PADDLE_TAG = "originalPaddle";
    private Counter strikeCounter;
    private BrickerGameManager brickerGameManager;


    /**
     * Construct a new Heart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param strikeCounter A counter containing how many strikes left.
     * @param brickerGameManager The bricker game manager.
     */
    public Heart(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 Counter strikeCounter,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.strikeCounter = strikeCounter;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Decrements the strike counter and deletes itself using brickerGameManager.
     */
    public void deleteHeart() {
        strikeCounter.decrement();
        brickerGameManager.deleteObject(this, Layer.UI);
    }

    /**
     * The function is responsible for adding a heart and deleting itself using the brickerGameManger.
     * The function is only called when the object collides with the original paddle.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        brickerGameManager.addHeart();
        brickerGameManager.deleteObject(this);
    }

    /**
     * The function is called whenever two objects are in the "same place".
     * It returns a boolean specifying whether this should be considered a collision or not.
     * It only returns true if the other object is the "original" paddle.
     * @param other The other GameObject that is in the "same place".
     * @return A boolean specifying whether it should collide with the object or not.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other.getTag()).equals(ORIGINAL_PADDLE_TAG);
    }
}
