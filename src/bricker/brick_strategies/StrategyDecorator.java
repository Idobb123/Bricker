package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A strategy decorator used for easily creating multiple strategies for a single brick.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public abstract class StrategyDecorator implements CollisionStrategy {
    private CollisionStrategy collisionStrategy;

    /**
     * A constructor that creates an instance of the decorator.
     * @param collisionStrategy The collision strategy the decorator holds using composition.
     */
    public StrategyDecorator(CollisionStrategy collisionStrategy) {
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * The function applies its collisionStrategy on the given brick.
     * The function is called whenever another object collides with the brick.
     * @param brick The brick to apply the strategy on.
     * @param other The object that collided with the brick.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        collisionStrategy.onCollision(brick, other);
    }
}
