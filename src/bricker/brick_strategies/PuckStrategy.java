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
    public void onCollision(GameObject object1, GameObject object2) {
        brickerGameManager.deleteObject(object1);
        brickerGameManager.createPuck(object1.getCenter());
        brickerGameManager.createPuck(object1.getCenter());
    }
}
