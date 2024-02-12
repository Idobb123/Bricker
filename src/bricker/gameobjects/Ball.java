package bricker.gameobjects;
//TODO: We should not reset the camera if the ball is gone mid camera special event.
//TODO: Document all the constants.
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class representing a ball in the game.
 * The class represents both the "main" ball and the "puck" balls.
 */
public class Ball  extends GameObject {

    private final Sound collisionSound;
    private int collisionCounter;


    /**
     * Construct a new ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner,
                Vector2 dimensions,
                Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }
    /**
     * The function is called whenever the ball collides with another object.
     * The function calculates and applies the new speed of the ball, and plays the ball collisionSound.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionSound.play();
    }
    /**
     * A getter function that returns the amount of times the ball has collided with other objects.
     * @return The amount of time collided with other objects since the ball's creation.
     */
    public int getCollisionCounter() {
        return this.collisionCounter;
    }
}
