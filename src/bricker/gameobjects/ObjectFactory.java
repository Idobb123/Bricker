package bricker.gameobjects;

import bricker.BrickerGameManager;
import bricker.brick_strategies.*;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;
//TODO: To document this class
//TODO: Removes constants from all classes
//TODO: note that private functions are documented in the correct format.
/**
 * A factory class that handles the creation of all the game objects.
 *
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class ObjectFactory {
    private static float BRICK_HEIGHT = 15;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private final BrickerGameManager brickerGameManager;
    private Counter bricksLeftCounter;
    private Counter strikeCounter;
    private Random rand;

    /**
     * A constructor that creates a factory that wraps the creation of game objects.
     * @param imageReader An object that reads the images.
     * @param soundReader An object that reads the sound.
     * @param inputListener The input listener instance that is in charge of processing the keyboard arguments.
     * @param windowController Contains an array of helpful, self-explanatory methods concerning the window.
     * @param brickerGameManager The bricker game manager.
     * @param bricksLeftCounter A counter containing how many bricks are still "in the game".
     * @param strikeCounter A counter containing how many strikes left.
     */
    public ObjectFactory(ImageReader imageReader,
                         SoundReader soundReader,
                         UserInputListener inputListener,
                         WindowController windowController,
                         BrickerGameManager brickerGameManager,
                         Counter bricksLeftCounter,
                         Counter strikeCounter){

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.brickerGameManager = brickerGameManager;
        this.bricksLeftCounter = bricksLeftCounter;
        this.strikeCounter = strikeCounter;
        this.rand = new Random();
    }

    /**
     * Creates a heart object instance.
     * @param heartLocation The location of the heart (center).
     * @return The created heart object instance.
     */
    public Heart createHeart(Vector2 heartLocation){
        Renderable heartImage = this.imageReader.readImage("assets/heart.png", true);
        Heart heart = new Heart(Vector2.ZERO, new Vector2(15, 15),
                heartImage, strikeCounter, brickerGameManager);
        heart.setCenter(heartLocation);
        return heart;
    }

    /**
     * Creates a text object instance.
     * @param textLocation The location of the text (center).
     * @return The created text object instance.
     */
    public GameObject createStrikeNumberDisplay(Vector2 textLocation){
        TextRenderable strikeText = new TextRenderable(String.valueOf(strikeCounter.value()));
        strikeText.setColor(getStrikeNumberDisplayColor());
        GameObject strikeNumberDisplay = new GameObject(Vector2.ZERO, new Vector2(30, 30), strikeText);
        strikeNumberDisplay.setCenter(textLocation);
        return strikeNumberDisplay;
    }

    /**
     * Creates a paddle object instance.
     * @param paddleLocation The location of the paddle (center).
     * @param windowWidth The window width.
     * @return The created paddle object instance.
     */
    public Paddle createPaddle(Vector2 paddleLocation, float windowWidth){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, inputListener, windowWidth);
        paddle.setCenter(paddleLocation);
        return paddle;
    }

    /**
     * Creates a temporary paddle instance.
     * @param paddleLocation The location of the paddle (center).
     * @param windowWidth The window width.
     * @return Returns the created TemporaryPaddle object instance.
     */
    public TemporaryPaddle createTemporaryPaddle(Vector2 paddleLocation, float windowWidth){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        TemporaryPaddle temporaryPaddle = new TemporaryPaddle(Vector2.ZERO, new Vector2(200, 20),
                paddleImage, inputListener, brickerGameManager, windowWidth );
        temporaryPaddle.setCenter(paddleLocation);
        return temporaryPaddle;
    }

    /**
     * Creates a ball instance.
     * @param ballLocation The location of the created ball (center).
     * @param velocity The velocity of the ball.
     * @param ballType The type of the ball (whether it is a puck or a regular ball).
     * @return The created ball instance.
     */
    public Ball createBall(Vector2 ballLocation, Vector2 velocity, BallType ballType){
        Renderable ballImage = null;
        Vector2 ballSize = new Vector2(20, 20);
        switch (ballType) {
            case REGULAR:
                ballImage = imageReader.readImage("assets/ball.png", true);
                break;
            case PUCK:
                ballImage = imageReader.readImage("assets/mockBall.png", true);
                ballSize = ballSize.mult(0.75f);
                break;
        }
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Ball ball = new Ball(Vector2.ZERO, ballSize, ballImage, collisionSound);
        ball.setVelocity(velocity);
        ball.setCenter(ballLocation);
        return ball;
    }

    /**
     * Creates a background object instance.
     * @param windowDimensions The window dimensions.
     * @return The created background object instance.
     */
    public GameObject createBackGround(Vector2 windowDimensions){
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,backgroundImage);
        // background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //TODO: Do we need to use this?
        return background;
    }

    /**
     * Creates a GameObject instance representing a wall.
     * @param wallLocation The location of the wall (top left corner!).
     * @param wallDimensions The wall's dimensions.
     * @return The created wall.
     */
    public GameObject createWall(Vector2 wallLocation, Vector2 wallDimensions){
        return new GameObject(wallLocation, wallDimensions, new RectangleRenderable(null));  //TODO: Change it to no color somwhow.
    }
    //TODO: Document this methods.
    private Color getStrikeNumberDisplayColor() {
        if (strikeCounter.value() == 2) {
            return Color.yellow;
        }
        else if (strikeCounter.value() == 1) {
            return Color.red;
        }
        else {
            return Color.green;
        }
    }
    public Brick createBrick(Vector2 brickLocation, float brickWidth) {
        CollisionStrategy strategy = generateStrategy();
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Brick brick = new Brick(brickLocation, new Vector2(brickWidth, BRICK_HEIGHT), brickImage,strategy, bricksLeftCounter);
        return brick;
    }

    public CollisionStrategy generateStrategy(){
        CollisionStrategy strategy = new BasicCollisionStrategy(brickerGameManager);
        if (rand.nextBoolean()) {
            return strategy;
        }

        SpecialBrickStrategyEnum[] strategyTypes = SpecialBrickStrategyEnum.values();
        int nextStrategyNumber = rand.nextInt(5);
        SpecialBrickStrategyEnum randomStrategyType = strategyTypes[nextStrategyNumber];
        return chooseStrategyBasedOnInt(randomStrategyType, strategy);
    }
    private CollisionStrategy chooseStrategyBasedOnInt(SpecialBrickStrategyEnum strategyType, CollisionStrategy strategy) {
        switch (strategyType) {
            case PUCK:
                return new PuckStrategy(strategy, brickerGameManager);
            case ADDITIONAL_PADDLE:
                return new AdditionalPaddleStrategy(strategy, brickerGameManager);
            case CAMERA_CHANGE:
                return new CameraChangeStrategy(strategy, brickerGameManager);
            case ADDITIONAL_HEART:
                return new AdditionalHeartStrategy(strategy, brickerGameManager);
            case DOUBLE_STRATEGY:
                SpecialBrickStrategyEnum[] strategyIntegers = chooseDoubleStrategies();
                for (SpecialBrickStrategyEnum currentStratInt : strategyIntegers) {
                    strategy = chooseStrategyBasedOnInt(currentStratInt, strategy);
                }
                return strategy;
            default:
                return strategy;
        }
    }
    private SpecialBrickStrategyEnum[] chooseDoubleStrategies(){
        SpecialBrickStrategyEnum[] strategyTypes = SpecialBrickStrategyEnum.values();
        SpecialBrickStrategyEnum randomStrategyType1 = strategyTypes[rand.nextInt(5)];
        SpecialBrickStrategyEnum randomStrategyType2 = strategyTypes[rand.nextInt(5)];
        if (randomStrategyType1 == SpecialBrickStrategyEnum.DOUBLE_STRATEGY || randomStrategyType2 == SpecialBrickStrategyEnum.DOUBLE_STRATEGY){
            SpecialBrickStrategyEnum[] behaviours =  {strategyTypes[rand.nextInt(4)],
                    strategyTypes[rand.nextInt(4)],
                    strategyTypes[rand.nextInt(4)]};
            return behaviours;
        }

        return new SpecialBrickStrategyEnum[]{randomStrategyType1, randomStrategyType2};
    }
}
