package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class AdditionalPaddleStrategy implements CollisionStrategy{

    private final BrickerGameManager brickerGameManager;

    public AdditionalPaddleStrategy(BrickerGameManager brickerGameManager) {

        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickerGameManager.deleteObject(object1);
        brickerGameManager.createDuplicatePaddle();
    }
}
