package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;

public class PuckStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;
    public PuckStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        brickerGameManager.deleteObject(brick);
        brickerGameManager.createPuck(brick.getCenter());
        brickerGameManager.createPuck(brick.getCenter());
    }
}
