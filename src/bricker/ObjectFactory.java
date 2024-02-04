package bricker;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Heart;
import bricker.gameobjects.Paddle;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

public class ObjectFactory {
    private static float BALL_SPEED = 300;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private final BrickerGameManager brickerGameManager;
    private Counter brickCounter;
    private Counter strikeCounter;

    ObjectFactory(ImageReader imageReader,
                  SoundReader soundReader,
                  UserInputListener inputListener,
                  WindowController windowController,
                  BrickerGameManager brickerGameManager,
                  Counter brickCounter,
                  Counter strikeCounter){

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.brickerGameManager = brickerGameManager;
        this.brickCounter = brickCounter;
        this.strikeCounter = strikeCounter;
    }
    public Heart createHeart(Vector2 heartLocation){
        Renderable heartImage = this.imageReader.readImage("assets/heart.png", true);
        Heart heart = new Heart(Vector2.ZERO, new Vector2(15, 15),
                heartImage, strikeCounter, brickerGameManager);
        heart.setCenter(heartLocation);
        return heart;

    }
    public Paddle createPaddle(Vector2 paddleLocation){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, inputListener);
        paddle.setCenter(paddleLocation);
        return paddle;
    }
    public Ball createBall(Vector2 ballLocation){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
        float ballSpeedX = BALL_SPEED;
        float ballSpeedY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballSpeedX *= -1;
        }
        if (rand.nextBoolean()) {
            ballSpeedY *= -1;
        }
        ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
        ball.setCenter(ballLocation);
        return ball;
    }

}
