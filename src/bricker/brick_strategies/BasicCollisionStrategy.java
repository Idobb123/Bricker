package bricker.brick_strategies;
import bricker.BrickerGameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy{

    private BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {

        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        deleteBrick(object1);
    }

    private void deleteBrick(GameObject object) {
        brickerGameManager.deleteObject(object);
    }

}
