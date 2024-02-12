package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;

public class PuckStrategy extends StrategyDecorator{
    private BrickerGameManager brickerGameManager;
    public PuckStrategy(CollisionStrategy collisionStrategy,BrickerGameManager brickerGameManager) {
        super(collisionStrategy);
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        brickerGameManager.createPuck(brick.getCenter());
        brickerGameManager.createPuck(brick.getCenter());
    }
}
