package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class representing a brick in the game.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class Brick extends GameObject {

    private CollisionStrategy collisionStrategy;
    private final Counter hitCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy,
                 Counter hitCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.hitCounter = hitCounter;
    }
    /**
     * The function is called whenever an object collides with the brick.
     * It calls the onCollision method of its collision strategy therefore applying the strategy.
     * The function the decrease the hit counter by one.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionStrategy.onCollision(this,other);
        hitCounter.decrement();
//        System.out.println(this);
//        System.out.println(other);
//        System.out.println(hitCounter.value());
    }
}
