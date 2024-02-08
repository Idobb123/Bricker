package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class AdditionalHeartStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;

    public AdditionalHeartStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        brickerGameManager.deleteObject(brick);
        brickerGameManager.addHeart();

    }
}
