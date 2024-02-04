package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class AdditionalHeartStrategy implements CollisionStrategy{
    private BrickerGameManager brickerGameManager;

    public AdditionalHeartStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        deleteBrick(object1);
        brickerGameManager.addHeart();

    }
    private void deleteBrick(GameObject object) {
        brickerGameManager.deleteObject(object);
    }
}
