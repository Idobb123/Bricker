package bricker;

import bricker.brick_strategies.*;
import bricker.gameobjects.Brick;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;
import java.util.Vector;

public class BrickFactory {
    private Random rand = new Random();
    public Brick createBrick(Vector2 brickLocation, Vector2 brickSize, Renderable brickImage, BrickerGameManager gameManager, Counter hitCounter){
        int nextInt = rand.nextInt(10);
        CollisionStrategy strategy;

        switch (nextInt) { // TODO: Change to Enum.
//            case 5:
//                strategy = new PuckStrategy(gameManager);
//                break;
//            case 6:
//                strategy = new AdditionalPaddleStrategy(gameManager);
//                break;
//
//            case 7:
//                strategy = new CameraChangeStrategy(gameManager);
//                break;

            case 8:
                strategy = new AdditionalHeartStrategy(gameManager);
                break;

//            case 9:
//                strategy = new DoubleStrategy(gameManager);
//                break;


            default:
                strategy = new BasicCollisionStrategy(gameManager);
        }
        System.out.println(strategy);
        return new Brick(brickLocation, brickSize, brickImage, strategy, hitCounter);

    }
}
