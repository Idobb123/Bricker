package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class AdditionalPaddleStrategy extends StrategyDecorator{

    private final BrickerGameManager brickerGameManager;

    public AdditionalPaddleStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        brickerGameManager.deleteObject(brick, Layer.STATIC_OBJECTS);
        brickerGameManager.createDuplicatePaddle();
    }
}
