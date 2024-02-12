package bricker;

import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * A class that manages the run of a Bricker game. relies upon the GameManager of the DanoGameLabs library.
 * Manages all of the objects of the game, including the background and the UI
 */
public class BrickerGameManager extends GameManager {
    private static final String LOSING_PROMPT = "You lose! Play again?";
    private static final String WINNING_PROMPT = "You win! Play again?";
    private static final int TARGET_FRAME_RATE = 40;
    private static final float HEART_FALLING_SPEED = 100;
    private static final String ORIGINAL_PADDLE_TAG = "originalPaddle";
    private static final String DUPLICATE_PADDLE_TAG = "duplicatePaddle";
    private static final String ORIGINAL_BALL_TAG = "originalBall";
    private static int DEFAULT_STRIKES_LEFT = 3;
    private static float WALL_WIDTH = 15;
    private static float BRICK_SPACE = 2;
    private static float BRICK_HEIGHT = 15;
    private static int MAX_HEARTS = 4;
    private static float BALL_SPEED = 300;
    private Random rand = new Random();
    private int bricksPerRow;
    private int numberOfRows;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private Ball ball;
    private Counter strikeCounter;
    private Counter brickCounter;
    private Heart[] hearts;
    private GameObject strikeNumberDisplay;
    private ObjectFactory objectFactory;
    private int cameraSetBricksLeft;

    /**
     *  Constructs a new BrickerGameManager instance
     * @param windowTitle
     * @param windowDimensions
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow= 8;
        this.numberOfRows = 7;

    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksPerRow, int numberOfRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow= bricksPerRow;
        this.numberOfRows = numberOfRows;
    }

    public static void main(String[] args) {
        BrickerGameManager gameManager = new BrickerGameManager("Bricker",
                new Vector2(700, 500));
//        BrickerGameManager gameManager = new BrickerGameManager("Bricker",
//                new Vector2(700, 500),3 ,3 );
        gameManager.run();
    }

    /**
     * removes the given game object from the game
     * @param object the given game object
     */
    public void deleteObject (GameObject object) {
            this.gameObjects().removeGameObject(object);
    }

    /**
     * overloading the previous method with the layered version
     * @param object
     * @param layer
     */
    public void deleteObject (GameObject object, int layer) {
        this.gameObjects().removeGameObject(object, layer);
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
        this.strikeCounter = new Counter(DEFAULT_STRIKES_LEFT);
        this.brickCounter = new Counter(bricksPerRow * numberOfRows);
        this.hearts = new Heart[MAX_HEARTS];
        this.objectFactory = new ObjectFactory(imageReader, soundReader, inputListener, windowController, this, brickCounter, strikeCounter);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        windowController.setTargetFramerate(TARGET_FRAME_RATE);

        //create ball
        createBall(windowDimensions);
        // create paddle
        createPaddle(windowDimensions);
        // create the wallas
        buildWalls(windowDimensions);
        // create the background
        createBackground(windowDimensions);
        // Create the bricks
        createBricks(windowDimensions);
        // create the initial first three hearts
        createInitialHearts(windowDimensions);
        // create the initial strike counter
        createStrikeNumberDisplay(windowDimensions);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkStrikes();
        checkWinCondition();
        setCameraToDefault();
    }

    private void checkStrikes() {
        if(isBallOutOfBounds()) { // that is, we lost the ball
            hearts[strikeCounter.value() - 1].deleteHeart();
            deleteObject(this.strikeNumberDisplay, Layer.UI);
            createStrikeNumberDisplay(windowController.getWindowDimensions());
            deleteObject(this.ball);
            this.setCamera(null);
            createBall(windowController.getWindowDimensions());
        }
        if(this.strikeCounter.value() <= 0) {
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

    private void createPaddle(Vector2 windowDimensions) {
        Vector2 paddleLocation = new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30);
        GameObject paddle = objectFactory.createPaddle(paddleLocation, windowDimensions.x());
        paddle.setTag(ORIGINAL_PADDLE_TAG);
        this.gameObjects().addGameObject(paddle);
    }

    private void createBall(Vector2 windowDimensions) {
        Vector2 ballLocation = windowDimensions.mult(0.5f);
        float ballSpeedX = BALL_SPEED;
        float ballSpeedY = BALL_SPEED;
        if (this.rand.nextBoolean()) {
            ballSpeedX *= -1;
        }
        if (this.rand.nextBoolean()) {
            ballSpeedY *= -1;
        }
        Vector2 velocity = new Vector2(ballSpeedX, ballSpeedY);
        this.ball = objectFactory.createBall(ballLocation, velocity, BallType.REGULAR);
        this.ball.setTag(ORIGINAL_BALL_TAG);
        this.gameObjects().addGameObject(ball);
    }

    private void buildWalls(Vector2 windowDimensions) {
        GameObject leftWall = objectFactory.createWall(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()));
        GameObject rightWall = objectFactory.createWall(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()));
        GameObject upperWall = objectFactory.createWall(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH));
        this.gameObjects().addGameObject(leftWall);
        this.gameObjects().addGameObject(rightWall);
        this.gameObjects().addGameObject(upperWall);
    }

    private void createBackground(Vector2 windowDimensions) {
        GameObject background = objectFactory.createBackGround(windowDimensions);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBricks(Vector2 windowDimensions) {
        Vector2 curBrickLocation;
        float brickWidth = (windowDimensions.x() - 2*(WALL_WIDTH+BRICK_SPACE)) / bricksPerRow - BRICK_SPACE;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j <bricksPerRow; j++) {
                curBrickLocation = new Vector2(WALL_WIDTH + BRICK_SPACE + (brickWidth + BRICK_SPACE) * j,
                        WALL_WIDTH + BRICK_SPACE + (BRICK_HEIGHT + BRICK_SPACE) * i);
                Brick brick = this.objectFactory.createBrick(curBrickLocation, brickWidth);
                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createInitialHearts(Vector2 windowDimensions) {
        Heart heart;
        for (int i = 0; i < strikeCounter.value(); i++) {
            heart = objectFactory.createHeart(new Vector2(40 + i*20 ,windowDimensions.y() - 20 ));
            hearts[i] = heart;
            this.gameObjects().addGameObject(hearts[i], Layer.UI);
        }
    }

    private void createStrikeNumberDisplay(Vector2 windowDimensions) {
        this.strikeNumberDisplay = objectFactory.createStrikeNumberDisplay(new Vector2(15, windowDimensions.y() - 40));
        this.gameObjects().addGameObject(strikeNumberDisplay, Layer.UI);
    }

    public void addHeart() {
        Vector2 windowDimensions = windowController.getWindowDimensions();
        int heartIndex = strikeCounter.value();
        if (heartIndex >= MAX_HEARTS) {
            return;
        }
        Vector2 heartLocation = new Vector2(40 + heartIndex * 20, windowDimensions.y() - 20);
        hearts[heartIndex] = objectFactory.createHeart(heartLocation);
        this.gameObjects().addGameObject(hearts[heartIndex], Layer.UI);
        this.strikeCounter.increment();
        deleteObject(this.strikeNumberDisplay, Layer.UI);
        createStrikeNumberDisplay(windowController.getWindowDimensions());
    }

    public void createHeart(Vector2 heartLocation) {
        Heart heart = objectFactory.createHeart(heartLocation);
        heart.setVelocity(new Vector2(0, HEART_FALLING_SPEED));
        this.gameObjects().addGameObject(heart);
    }

    public void createPuck(Vector2 puckLocation) {
        double angle = this.rand.nextDouble() * Math.PI;
        float ballSpeedX = (float)Math.cos(angle) * BALL_SPEED;
        float ballSpeedY = (float)Math.sin(angle) * BALL_SPEED;
        Vector2 velocity = new Vector2(ballSpeedX, ballSpeedY);
        Ball ball = objectFactory.createBall(puckLocation, velocity, BallType.PUCK);
        this.gameObjects().addGameObject(ball);
    }

    public void createDuplicatePaddle() { // TODO: maybe delete duplicatePaddle class, problems with making the original disappear
        if (checkForDuplicatePaddle()) {
            return;
        }
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Vector2 paddleLocation = new Vector2(windowDimensions.x() / 2, windowDimensions.y()/2);
        GameObject duplicatePaddle = objectFactory.createDuplicatePaddle(paddleLocation, windowDimensions.x());
        duplicatePaddle.setTag(DUPLICATE_PADDLE_TAG);
        this.gameObjects().addGameObject(duplicatePaddle);
    }

    private boolean checkForDuplicatePaddle() {
        for (GameObject object : this.gameObjects()) {
            if (object.getTag().equals(DUPLICATE_PADDLE_TAG)) {
                return true;
            }
        }
        return false;
    }

    public void setCameraToBall() {
        this.cameraSetBricksLeft  = this.ball.getCollisionCounter();
        this.setCamera(new Camera(ball,
                Vector2.ZERO,
                windowController.getWindowDimensions().mult(1.2f),
                windowController.getWindowDimensions()));
    }

    public void setCameraToDefault() {
        if (this.ball.getCollisionCounter() - cameraSetBricksLeft >= 4) {
            this.setCamera(null);
        }
    }

    private boolean isBallOutOfBounds() {
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowController.getWindowDimensions().y()) {
            return true;
        }
        return false;
    }

    public Ball getBall(){
        return this.ball;
    }

}
