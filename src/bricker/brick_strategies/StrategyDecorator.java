package bricker.brick_strategies;

import danogl.GameObject;

//TODO -> if protected is possible than change brickgamemanager to protected and simplify
public abstract class StrategyDecorator implements CollisionStrategy {
    private CollisionStrategy collisionStrategy;

    public StrategyDecorator(CollisionStrategy collisionStrategy) {
        this.collisionStrategy = collisionStrategy;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        collisionStrategy.onCollision(brick, other);
    }
}
