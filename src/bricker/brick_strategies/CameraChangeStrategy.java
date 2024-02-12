package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class CameraChangeStrategy extends StrategyDecorator{

    private final BrickerGameManager brickerGameManager;

    public CameraChangeStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy);
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
            brickerGameManager.deleteObject(brick, Layer.STATIC_OBJECTS);
            if (other == brickerGameManager.getBall() && brickerGameManager.camera() == null)
                brickerGameManager.setCameraToBall();
    }
}
