package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A class representing a strategy that creates an extra paddle by braking a brick with this strategy.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class AdditionalPaddleStrategy extends StrategyDecorator{

    private final BrickerGameManager BrickerGameManager;
    /**
     * Creates an instance for the additional paddle strategy.
     * The class also applies any other strategy given.
     * @param collisionStrategy Another collision strategy to apply.
     * @param BrickerGameManager The game manager instance.
     */
    public AdditionalPaddleStrategy(CollisionStrategy collisionStrategy,
                                    BrickerGameManager BrickerGameManager) {
        super(collisionStrategy);
        this.BrickerGameManager = BrickerGameManager;
    }
    /**
     * The function is responsible for applying the class strategy on a given brick when called.
     * The function deletes the brick and creates an extra paddle object using the brickerGameManager.
     * The function is called whenever another object collides with the brick.
     * @param brick The brick to apply the strategy on.
     * @param other The object that collided with the brick.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        BrickerGameManager.createTemporaryPaddle();
    }
}
