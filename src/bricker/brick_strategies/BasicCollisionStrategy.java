package bricker.brick_strategies;
//import bricker.BrickerGameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy{

//    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
//
//    }
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        System.out.println("collision with brick detected");
    }
}
