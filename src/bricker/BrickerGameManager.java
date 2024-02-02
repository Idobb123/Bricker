package bricker;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 200;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        gameManager.run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        //create ball
        createBall(imageReader, soundReader, windowDimensions);
        // create paddle
        createPaddle(imageReader, inputListener, windowDimensions);
        // create the wallas
        buildWalls(windowDimensions);
        // create the background
        createBackground(windowDimensions, imageReader);

        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Brick brick = new Brick(Vector2.ZERO, new Vector2(windowDimensions.x(), 15), brickImage, new BasicCollisionStrategy());
        this.gameObjects().addGameObject(brick); // TODO -> think about which layer the brick belongs to
    }

    private void createPaddle(ImageReader imageReader, UserInputListener inputListener, Vector2 windowDimensions) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, inputListener);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));
        this.gameObjects().addGameObject(paddle);
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
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
        ball.setCenter(windowDimensions.mult(0.5f));
        this.gameObjects().addGameObject(ball);
    }

    private void buildWalls(Vector2 windowDimensions) {
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(6, windowDimensions.y()), new RectangleRenderable(Color.CYAN));
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - 6, 0), new Vector2(6, windowDimensions.y()), new RectangleRenderable(Color.CYAN));
        GameObject upperWall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), 6), new RectangleRenderable(Color.CYAN));
        this.gameObjects().addGameObject(leftWall);
        this.gameObjects().addGameObject(rightWall);
        this.gameObjects().addGameObject(upperWall);
    }

    private void createBackground(Vector2 windowDimensions, ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,backgroundImage);
        // background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);

    }
}
