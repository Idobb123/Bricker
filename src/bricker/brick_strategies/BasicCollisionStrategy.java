package bricker.brick_strategies;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

/**
 * A class representing a "basic strategy",
 * this means it only destroys the brick without anything "special" happening.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class BasicCollisionStrategy implements CollisionStrategy{

    private BrickerGameManager BrickerGameManager;
    /**
     * Creates an instance for the "basic" strategy.
     * @param BrickerGameManager The game manager instance.
     */
    public BasicCollisionStrategy(BrickerGameManager BrickerGameManager) {
        this.BrickerGameManager = BrickerGameManager;
    }
    /**
     * The function is responsible for applying the class strategy on a given brick when called.
     * The function deletes the brick using the brickerGameManager.
     * The function is called whenever another object collides with the brick.
     * @param brick The brick to delete.
     * @param other The object that collided with the brick.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        BrickerGameManager.deleteObject(brick, Layer.STATIC_OBJECTS);

    }
}
