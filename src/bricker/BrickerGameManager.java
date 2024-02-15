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
//TODO: Create a main method that follows the rules in the ex description.
/**
 * A class that manages the run of a Bricker game. relies upon the GameManager of the DanoGameLabs library.
 * Manages all the objects of the game, including the background and the UI
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class BrickerGameManager extends GameManager {
    /** the prompt displayed to the user upon losing*/
    private static final String LOSING_PROMPT = "You lose! Play again?";
    /** the prompt displayed to the user upon winning*/
    private static final String WINNING_PROMPT = "You win! Play again?";
    /** the frame-rate for the game*/
    private static final int TARGET_FRAME_RATE = 100;
    /** the speed that a heart will fall from a brick*/
    private static final float HEART_FALLING_SPEED = 100; //TODO: put the constants in a separate class.
    /** the object tag for the player's paddle*/
    private static final String ORIGINAL_PADDLE_TAG = "originalPaddle";
    /** the object tag for the paddle generated from the strategy */
    private static final String TEMPORARY_PADDLE_TAG = "temporaryPaddle";
    /** the object tag for the main ball */
    private static final String ORIGINAL_BALL_TAG = "regularBall";
    /** the default number of pricks per row */
    private static final int DEFAULT_BRICKS_PER_ROW = 8;
    /** the default number of rows */
    private static final int DEFAULT_NUM_OF_ROWS = 7;
    /** the windows width, that is, the x axis */
    private static final float DEFAULT_WINDOW_WIDTH = 700;
    /** the windows height, that is, the y axis */
    private static final float DEFAULT_WINDOW_HEIGHT = 500;
    /** the title for the game */
    private static final String WINDOW_TITLE = "Bricker";

    /** how high the paddle is from the bottom of the screen in pixels */
    private static final int PADDLE_Y_OFFSET = 30;
    /** multiplication factor for windowDimensions to get the middle of the screen */
    private static final float MIDDLE_OF_THE_SCREEN_FACTOR = 0.5f;
    /** the offset of the heart UI from the left of the screen*/
    private static final float HEART_X_OFFSET = 40;
    /** the space between the hearts in the UI */
    private static final float HEART_X_SPACE = 20;
    /** the offset of the heart UI from the bottom of the screen */
    private static final float HEART_Y_OFFSET = 20;
    /** the offset of the heart counter UI from the left of the screen*/
    private static final float NUMBER_COUNTER_X_OFFSET = 15;
    /** the offset of the heart counter UI from the bottom of the screen */
    private static final float NUMBER_COUNTER_Y_OFFSET = 40;
    /** the multiplication factor for the camera ball change zoom*/
    private static final float CAMERA_ZOOM_FARCTOR = 1.2f;
    /** the amount of bricks that need to be destroyed for the camera to change back*/
    private static final int BRICKS_FOR_CAMERA_CHANGE_BACK = 4;
    /** the default number strikes (hearts) the player starts with */
    private static final int DEFAULT_STRIKES_LEFT = 3;
    /** the width of the games' walls*/
    private static final float WALL_WIDTH = 15;
    /** the space between each of the bricks*/
    private static final float BRICK_SPACE = 2;
    /** the height of the bricks */
    private static final float BRICK_HEIGHT = 15;
    /** the maximum number of hearts a player can have */
    private static final int MAX_HEARTS = 4;
    /** the speed of the ball */
    private static final float BALL_SPEED = 200;
    private Random rand = new Random();
    private int bricksPerRow;
    private int numberOfRows;
    private WindowController windowController;
    private UserInputListener inputListener;
    private Ball ball;
    private Counter strikeCounter;
    private Counter bricksLeftCounter;
    private Heart[] hearts;
    private GameObject strikeNumberDisplay;
    private ObjectFactory objectFactory;
    private int cameraSetBricksLeft;

    /**
     *  Constructs a new BrickerGameManager instance with the default number of bricks per row (8) and the
     *  default number of rows(7)
     * @param windowTitle the title of the game window
     * @param windowDimensions the dimensions of the game window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow= DEFAULT_BRICKS_PER_ROW;
        this.numberOfRows = DEFAULT_NUM_OF_ROWS;

    }

    /**
     * Constructs a new BrickerGameManager instance with a given number of bricks per row and a given
     * number of rows
     * @param windowTitle the title of the game window
     * @param windowDimensions the dimensions of the game window
     * @param bricksPerRow the number of bricks per row
     * @param numberOfRows the number of rows of bricks to create in the game
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksPerRow, int numberOfRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow= bricksPerRow;
        this.numberOfRows = numberOfRows;
    }

    /**
     * creates and runs a new game of bricker with the number of bricks per row and the number of rows
     * specified in the command line argument
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BrickerGameManager gameManager;
        if(args.length == 2){
            int columns = Integer.parseInt(args[0]);
            int rows = Integer.parseInt(args[1]);
            gameManager = new BrickerGameManager(WINDOW_TITLE,
                    new Vector2(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT), columns, rows);
        }else {
            gameManager = new BrickerGameManager(WINDOW_TITLE,
                    new Vector2(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        }

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

    /**
     * initializes a game of bricker by creating all of the initial objects of the game, including UI
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        this.windowController = windowController; // like in the videos, but for everything
        this.strikeCounter = new Counter(DEFAULT_STRIKES_LEFT);
        this.bricksLeftCounter = new Counter(bricksPerRow * numberOfRows);
        this.hearts = new Heart[MAX_HEARTS];
        this.objectFactory = new ObjectFactory(imageReader, soundReader, inputListener, this, bricksLeftCounter, strikeCounter);

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

    /**
     * an override of the update function from DanoGameLabs.
     * This version also checks if the losing or winning condition was reached.
     * furthermore, checks if the camera needs to be set to default or the ball to be set back on the screen
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        manageBallOutOfBounds();
        checkStrikes();
        checkWinCondition();
        setCameraToDefault();
    }

    /**
     * adds a heart (and strike) to the players count and also updates the hearts and strike number UI
     */
    public void addHeart() {
        Vector2 windowDimensions = windowController.getWindowDimensions();
        int heartIndex = strikeCounter.value();
        if (heartIndex >= MAX_HEARTS) {
            return;
        }
        Vector2 heartLocation = new Vector2(HEART_X_OFFSET + heartIndex * HEART_X_SPACE,
                windowDimensions.y() - HEART_Y_OFFSET);
        hearts[heartIndex] = objectFactory.createHeart(heartLocation);
        this.gameObjects().addGameObject(hearts[heartIndex], Layer.UI);
        this.strikeCounter.increment();
        deleteObject(this.strikeNumberDisplay, Layer.UI);
        createStrikeNumberDisplay(windowController.getWindowDimensions());
    }

    /**
     * creates a falling heart in a given location
     * @param heartLocation the given location
     */
    public void createFallingHeart(Vector2 heartLocation) {
        Heart heart = objectFactory.createHeart(heartLocation);
        heart.setVelocity(new Vector2(0, HEART_FALLING_SPEED));
        this.gameObjects().addGameObject(heart);
    }

    /**
     * creates pucks which are smaller versions of the ball in a given location
     * @param puckLocation the location to create the puck in
     */
    public void createPuck(Vector2 puckLocation) {
        double angle = this.rand.nextDouble() * Math.PI;
        float ballSpeedX = (float)Math.cos(angle) * BALL_SPEED;
        float ballSpeedY = (float)Math.sin(angle) * BALL_SPEED;
        Vector2 velocity = new Vector2(ballSpeedX, ballSpeedY);
        Ball ball = objectFactory.createBall(puckLocation, velocity, BallType.PUCK);
        this.gameObjects().addGameObject(ball);
    }

    /**
     * creates a temporary paddle in the middle of the screens. the paddle disappears after a few
     * collision with the main ball
     */
    public void createTemporaryPaddle() {
        if (checkForTemporaryPaddle()) {
            return;
        }
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Vector2 paddleLocation = windowDimensions.mult(MIDDLE_OF_THE_SCREEN_FACTOR);
        GameObject temporaryPaddle = objectFactory.createTemporaryPaddle(paddleLocation, windowDimensions.x());
        temporaryPaddle.setTag(TEMPORARY_PADDLE_TAG);
        this.gameObjects().addGameObject(temporaryPaddle);
    }

    /*
     * sets the camera to focus on the ball and increase the zoom by a bit
     */
    public void setCameraToBall() {
        this.cameraSetBricksLeft  = this.ball.getCollisionCounter();
        this.setCamera(new Camera(ball,
                Vector2.ZERO,
                windowController.getWindowDimensions().mult(CAMERA_ZOOM_FARCTOR),
                windowController.getWindowDimensions()));
    }

    /*
     * sets the camera back to default
     */
    private void setCameraToDefault() {
        if (this.ball.getCollisionCounter() - cameraSetBricksLeft >= BRICKS_FOR_CAMERA_CHANGE_BACK) {
            this.setCamera(null);
        }
    }

    /**
     * @return the main ball of the game
     */
    public Ball getBall(){
        return this.ball;
    }

    /*
     * checks if no more strikes(hearts) are left. if so, present the losing prompt and ask the player if
     * he wants to play again.
     */
    private void checkStrikes() {
        if(this.strikeCounter.value() <= 0) {
            String prompt = LOSING_PROMPT;
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    /*
     * manages everything that needs to happen when the ball leaves the screen from the bottom.
     * reduces the number of hearts, update the numerical counter deletes the previous ball and creates a
     * new one
     */
    private void manageBallOutOfBounds() {
        if(isBallOutOfBounds()) { // that is, we lost the ball
            hearts[strikeCounter.value() - 1].deleteHeart();
            deleteObject(this.strikeNumberDisplay, Layer.UI);
            createStrikeNumberDisplay(windowController.getWindowDimensions());
            deleteObject(this.ball);
            this.setCamera(null);
            createBall(windowController.getWindowDimensions());
        }
    }

    /*
     * checks if the winning condition for the game has been reached.
     * either the player presses W or there are no more bricks left on the screen
     */
    private void checkWinCondition() {
        boolean winningKeyPressed = inputListener.isKeyPressed(KeyEvent.VK_W);
        if(this.bricksLeftCounter.value() == 0 || winningKeyPressed) {
            String prompt = WINNING_PROMPT;
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    /*
     * creates the initial paddle for the game
     * @param windowDimensions the dimensions of the game window
     */
    private void createPaddle(Vector2 windowDimensions) {
        Vector2 paddleLocation = new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - PADDLE_Y_OFFSET);
        GameObject paddle = objectFactory.createPaddle(paddleLocation, windowDimensions.x());
        paddle.setTag(ORIGINAL_PADDLE_TAG);
        this.gameObjects().addGameObject(paddle);
    }

    /*
     * creates a ball in the middle of the game screen
     * @param windowDimensions the dimensions of the window
     */
    private void createBall(Vector2 windowDimensions) {
        Vector2 ballLocation = windowDimensions.mult(MIDDLE_OF_THE_SCREEN_FACTOR);
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
    /*
     * builds the walls in the initialization of the game
     * @param windowDimensions the dimensions of the wall
     */
    private void buildWalls(Vector2 windowDimensions) {
        GameObject leftWall = objectFactory.createWall(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()));
        GameObject rightWall = objectFactory.createWall(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()));
        GameObject upperWall = objectFactory.createWall(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH));
        this.gameObjects().addGameObject(leftWall);
        this.gameObjects().addGameObject(rightWall);
        this.gameObjects().addGameObject(upperWall);
    }

    /*
     * creates the background for the game in the initialization
     * @param windowDimensions
     */
    private void createBackground(Vector2 windowDimensions) {
        GameObject background = objectFactory.createBackGround(windowDimensions);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /*
     * create the bricks according to the given number of rows and number of bricks per row. used in the
     * initialization of the game
     * @param windowDimensions the dimensions of the game window
     */
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

    /*
     * creates the initial hearts for the game, including their UI
     * @param windowDimensions
     */
    private void createInitialHearts(Vector2 windowDimensions) {
        Heart heart;
        for (int i = 0; i < strikeCounter.value(); i++) {
            heart = objectFactory.createHeart(new Vector2(HEART_X_OFFSET + i*HEART_X_SPACE,
                    windowDimensions.y() - HEART_Y_OFFSET));
            hearts[i] = heart;
            this.gameObjects().addGameObject(hearts[i], Layer.UI);
        }
    }

    /*
     * creates the strike (hearts) number display for the game
     * @param windowDimensions
     */
    private void createStrikeNumberDisplay(Vector2 windowDimensions) {
        this.strikeNumberDisplay = objectFactory.createStrikeNumberDisplay(new Vector2(NUMBER_COUNTER_X_OFFSET,
                windowDimensions.y() - NUMBER_COUNTER_Y_OFFSET));
        this.gameObjects().addGameObject(strikeNumberDisplay, Layer.UI);
    }

    /*
     * check if a temporary paddle already exists in the game
     * @return true if there is temporary object in the game, false otherwise
     */
    private boolean checkForTemporaryPaddle() {
        for (GameObject object : this.gameObjects()) {
            if (object.getTag().equals(TEMPORARY_PADDLE_TAG)) {
                return true;
            }
        }
        return false;
    }


    /*
     * check if the ball is out of bounds (from the bottom)
     * @return true if the ball is out of bounds, false otherwise
     */
    private boolean isBallOutOfBounds() {
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowController.getWindowDimensions().y()) {
            return true;
        }
        return false;
    }

}
