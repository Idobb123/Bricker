package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A class representing a strategy that makes the camera temporarily follow the ball,
 * instead of staying still.
 * This is activated by braking a brick with this strategy.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class CameraChangeStrategy extends StrategyDecorator{

    private final BrickerGameManager BrickerGameManager;
    /**
     * Creates an instance for the camera strategy.
     * The class also applies any other strategy given.
     * @param collisionStrategy Another collision strategy to apply.
     * @param BrickerGameManager The game manager instance.
     */
    public CameraChangeStrategy(CollisionStrategy collisionStrategy,
                                BrickerGameManager BrickerGameManager) {
        super(collisionStrategy);
        this.BrickerGameManager = BrickerGameManager;
    }
    /**
     * The function is responsible for applying the class strategy on a given brick when called.
     * The function deletes the brick,
     * and sets the camera to follow the "main" ball using the brickerGameManager.
     * The function is called whenever another object collides with the brick.
     * @param brick The brick to apply the strategy on.
     * @param other The object that collided with the brick.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        if (other == BrickerGameManager.getBall() && BrickerGameManager.camera() == null)
            BrickerGameManager.setCameraToBall();
    }
}
