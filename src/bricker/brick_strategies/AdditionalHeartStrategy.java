package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class AdditionalHeartStrategy extends StrategyDecorator{
    private BrickerGameManager brickerGameManager;

    public AdditionalHeartStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy);
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        brickerGameManager.deleteObject(brick, Layer.STATIC_OBJECTS);
        brickerGameManager.createHeart(brick.getCenter());

    }
}
