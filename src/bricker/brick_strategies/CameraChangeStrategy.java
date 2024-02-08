package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class CameraChangeStrategy implements CollisionStrategy{

    private final BrickerGameManager brickerGameManager;

    public CameraChangeStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
            brickerGameManager.deleteObject(brick);
            if (other == brickerGameManager.getBall() && brickerGameManager.camera() == null)
                brickerGameManager.setCameraToBall();
    }
}
