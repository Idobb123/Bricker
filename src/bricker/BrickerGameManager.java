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
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {
    private static final String LOSING_PROMPT = "You lose! Play again?";
    private static final String WINNING_PROMPT = "You win! Play again?";
    private static int DEFAULT_STRIKES_LEFT = 3;
    private static float WALL_WIDTH = 6;
    private static float BRICK_SPACE = 2;
    private static float BRICK_HEIGHT = 15;
    private int bricksPerRow;
    private int numberOfRows;

    private static final float BALL_SPEED = 200;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private Ball ball;
    private int strikesLeft;

    private Counter brickCounter;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.strikesLeft = DEFAULT_STRIKES_LEFT;
        this.bricksPerRow= 8;
        this.numberOfRows = 7;
        this.brickCounter = new Counter(bricksPerRow * numberOfRows);
    }
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksPerRow, int numberOfRows) {
        super(windowTitle, windowDimensions);
        this.strikesLeft = DEFAULT_STRIKES_LEFT;
        this.bricksPerRow= bricksPerRow;
        this.numberOfRows = numberOfRows;
        this.brickCounter = new Counter(bricksPerRow * numberOfRows);
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker",
                new Vector2(700, 500),2,1);
        gameManager.run();
    }

    /**
     * removes the given game object from the game
     * @param object the given game object
     */
    public void deleteObject (GameObject object) {
            this.gameObjects().removeGameObject(object);
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController; // like in the videos, but for everything

        Vector2 windowDimensions = windowController.getWindowDimensions();

        //create ball
        createBall(imageReader, soundReader, windowDimensions);
        // create paddle
        createPaddle(imageReader, inputListener, windowDimensions);
        // create the wallas
        buildWalls(windowDimensions);
        // create the background
        createBackground(windowDimensions, imageReader);
        // Create the bricks
        createBricks(imageReader, windowDimensions);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkStrikes();
        checkWinCondition();
    }

    private void checkStrikes() {
        double ballHeight = ball.getCenter().y();
        if(ballHeight > windowController.getWindowDimensions().y()) { // that is, we lost the ball
            this.strikesLeft -= 1;
            deleteObject(this.ball);
            createBall(imageReader, soundReader, windowController.getWindowDimensions());
        }
        if(this.strikesLeft <= 0) {
            String prompt = LOSING_PROMPT;
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    private void checkWinCondition() {
        boolean winningKeyPressed = inputListener.isKeyPressed(KeyEvent.VK_W);
        if(this.brickCounter.value() == 0 || winningKeyPressed) {
            String prompt = WINNING_PROMPT;
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
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
        ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
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
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()), new RectangleRenderable(Color.CYAN));
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

    private void createBricks(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Vector2 curBrickLocation;// = new Vector2(WALL_WIDTH +BRICK_SPACE, WALL_WIDTH + BRICK_SPACE);
//        System.out.println(curBrickLocation);
        float brickWidth = (windowDimensions.x() - 2*(WALL_WIDTH+BRICK_SPACE)) / bricksPerRow - BRICK_SPACE;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <bricksPerRow; j++) {
                curBrickLocation = new Vector2(WALL_WIDTH + BRICK_SPACE + (brickWidth + BRICK_SPACE) * j,
                        WALL_WIDTH + BRICK_SPACE + (BRICK_HEIGHT + BRICK_SPACE) * i);
                Brick brick = new Brick(curBrickLocation, new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, new BasicCollisionStrategy(this), brickCounter);
                this.gameObjects().addGameObject(brick); // TODO -> think about which layer the brick belongs to

            }
        }
    }
}
