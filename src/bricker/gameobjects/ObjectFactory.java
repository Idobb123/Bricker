package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import bricker.brick_strategies.*;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;
/**
 * A factory class that handles the creation of all the game objects.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public class ObjectFactory {
    /** The height of the bricks */
    private static final float BRICK_HEIGHT = 15;
    /** The width and the height of the heart object image */
    private static final float HEART_WIDTH_AND_HEIGHT = 15;
    /** The width and the height of the strikes left text */
    private static final float STRIKE_NUMBER_WIDTH_AND_HEIGHT = 30;
    /** The width and the height of the ball object image */
    private static final float BALL_WIDTH_AND_HEIGHT = 20;
    /** The width and the height multiplying factor compared the "regular" ball size */
    private static final float PUCK_BALL_SIZE_FACTOR = 0.75f;
    /** The paddle width */
    private static final float PADDLE_WIDTH = 200;
    /** The height of the paddle */
    private static final float PADDLE_HEIGHT = 20;
    /** The amount of hearts in which the strike counter text is written in yellow */
    private static final float YELLOW_COLOR_STRIKES_LEFT_NUMBER = 2;
    /** The amount of hearts in which the strike counter text is written in red */
    private static final float RED_COLOR_STRIKES_LEFT_NUMBER = 1;
    /** The amount of "special" strategies */
    private static final int AMOUNT_OF_SPECIAL_STRATEGIES = 5;
    /** The amount of "special" strategies not count the double strategy */
    private static final int AMOUNT_OF_NON_DOUBLE_SPECIAL_STRATEGIES = 4;
    /** The image path for the heart */
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    /** The image path for the paddle */
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    /** The image path for the ball */
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    /** The image path for the puck */
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    /** The sound path for the collision the balls and pucks */
    private static final String BALL_COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    /** The image path for the background image */
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    /** The image path for the brick */
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private final BrickerGameManager BrickerGameManager;
    private Counter bricksLeftCounter;
    private Counter strikeCounter;
    private Random rand;

    /**
     * A constructor that creates a factory that wraps the creation of game objects.
     * @param imageReader An object that reads the images.
     * @param soundReader An object that reads the sound.
     * @param inputListener An input listener instance that is in charge of processing the keyboard arguments
     * @param BrickerGameManager The bricker game manager.
     * @param bricksLeftCounter A counter containing how many bricks are still "in the game".
     * @param strikeCounter A counter containing how many strikes left.
     */
    public ObjectFactory(ImageReader imageReader,
                         SoundReader soundReader,
                         UserInputListener inputListener,
                         BrickerGameManager BrickerGameManager,
                         Counter bricksLeftCounter,
                         Counter strikeCounter){

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.BrickerGameManager = BrickerGameManager;
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
        Renderable heartImage = this.imageReader.readImage(HEART_IMAGE_PATH, true);
        Heart heart = new Heart(Vector2.ZERO, new Vector2(HEART_WIDTH_AND_HEIGHT, HEART_WIDTH_AND_HEIGHT),
                heartImage, strikeCounter, BrickerGameManager);
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
        GameObject strikeNumberDisplay = new GameObject(Vector2.ZERO,
                new Vector2(STRIKE_NUMBER_WIDTH_AND_HEIGHT, STRIKE_NUMBER_WIDTH_AND_HEIGHT), strikeText);
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
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        Paddle paddle = new Paddle(Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener,
                windowWidth);
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
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        TemporaryPaddle temporaryPaddle = new TemporaryPaddle(Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, BrickerGameManager, windowWidth );
        temporaryPaddle.setCenter(paddleLocation);
        return temporaryPaddle;
    }

    /**
     * Creates a ball instance.
     * credit goes to soundbible website for the collision sound.
     * @param ballLocation The location of the created ball (center).
     * @param velocity The velocity of the ball.
     * @param ballType The type of the ball (whether it is a puck or a regular ball).
     * @return The created ball instance.
     */
    public Ball createBall(Vector2 ballLocation, Vector2 velocity, BallType ballType){
        Renderable ballImage = null;
        Vector2 ballSize = new Vector2(BALL_WIDTH_AND_HEIGHT, BALL_WIDTH_AND_HEIGHT);
        switch (ballType) {
            case REGULAR:
                ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
                break;
            case PUCK:
                ballImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
                ballSize = ballSize.mult(PUCK_BALL_SIZE_FACTOR);
                break;
        }
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
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
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,
                false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return background;
    }

    /**
     * Creates a GameObject instance representing a wall.
     * @param wallLocation The location of the wall (top left corner!).
     * @param wallDimensions The wall's dimensions.
     * @return The created wall.
     */
    public GameObject createWall(Vector2 wallLocation, Vector2 wallDimensions){
        return new GameObject(wallLocation, wallDimensions, null);
    }

    /*
     * The function returns the color of the text displaying the amount of strikes left.
     * @return A Color to write the amount of strikes in.
     */
    private Color getStrikeNumberDisplayColor() {
        if (strikeCounter.value() == YELLOW_COLOR_STRIKES_LEFT_NUMBER) {
            return Color.yellow;
        }
        else if (strikeCounter.value() == RED_COLOR_STRIKES_LEFT_NUMBER) {
            return Color.red;
        }
        else {
            return Color.green;
        }
    }

    /**
     * The function creates a brick object in the given location with the given size.
     * @param brickLocation The location of the center of the brick.
     * @param brickWidth The width of the brick
     * @return The created brick instance.
     */
    public Brick createBrick(Vector2 brickLocation, float brickWidth) {
        CollisionStrategy strategy = generateStrategy();
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        Brick brick = new Brick(brickLocation,
                new Vector2(brickWidth, BRICK_HEIGHT),
                brickImage,strategy, bricksLeftCounter);
        return brick;
    }

    /*
     * Generates a collision strategy for a single brick.
     * The strategy is chosen randomly.
     * @return The instance of the created collision strategy
     */
    private CollisionStrategy generateStrategy(){
        CollisionStrategy strategy = new BasicCollisionStrategy(BrickerGameManager);
        if (rand.nextBoolean()) {
            return strategy;
        }

        SpecialBrickStrategyEnum[] strategyTypes = SpecialBrickStrategyEnum.values();
        int nextStrategyNumber = rand.nextInt(AMOUNT_OF_SPECIAL_STRATEGIES);
        SpecialBrickStrategyEnum randomStrategyType = strategyTypes[nextStrategyNumber];
        return chooseStrategyBasedOnEnum(randomStrategyType, strategy);
    }

    /*
     * Given an Enum value that specifies what collision strategy to generate,
     * the function  generates the strategy.
     * @param strategyType The enum value specifying the strategy.
     * @param strategy Another strategy provided for the decorator design pattern.
     * @return The created strategy
     */
    private CollisionStrategy chooseStrategyBasedOnEnum(SpecialBrickStrategyEnum strategyType,
                                                        CollisionStrategy strategy) {
        switch (strategyType) {
            case PUCK:
                return new PuckStrategy(strategy, BrickerGameManager);
            case ADDITIONAL_PADDLE:
                return new AdditionalPaddleStrategy(strategy, BrickerGameManager);
            case CAMERA_CHANGE:
                return new CameraChangeStrategy(strategy, BrickerGameManager);
            case ADDITIONAL_HEART:
                return new AdditionalHeartStrategy(strategy, BrickerGameManager);
            case DOUBLE_STRATEGY:
                SpecialBrickStrategyEnum[] strategyIntegers = chooseDoubleStrategies();
                for (SpecialBrickStrategyEnum currentStratInt : strategyIntegers) {
                    strategy = chooseStrategyBasedOnEnum(currentStratInt, strategy);
                }
                return strategy;
            default:
                return strategy;
        }
    }

    /*
     * Chooses the strategies to implement in case a brick has a "double strategy".
     * @return An Enum array containing the strategies chosen.
     */
    private SpecialBrickStrategyEnum[] chooseDoubleStrategies(){
        SpecialBrickStrategyEnum[] strategyTypes = SpecialBrickStrategyEnum.values();
        SpecialBrickStrategyEnum randomStrategyType1 =
                strategyTypes[rand.nextInt(AMOUNT_OF_SPECIAL_STRATEGIES)];
        SpecialBrickStrategyEnum randomStrategyType2 =
                strategyTypes[rand.nextInt(AMOUNT_OF_SPECIAL_STRATEGIES)];
        if (randomStrategyType1 == SpecialBrickStrategyEnum.DOUBLE_STRATEGY
                || randomStrategyType2 == SpecialBrickStrategyEnum.DOUBLE_STRATEGY){
            SpecialBrickStrategyEnum[] behaviours = {
                    strategyTypes[rand.nextInt(AMOUNT_OF_NON_DOUBLE_SPECIAL_STRATEGIES)],
                    strategyTypes[rand.nextInt(AMOUNT_OF_NON_DOUBLE_SPECIAL_STRATEGIES)],
                    strategyTypes[rand.nextInt(AMOUNT_OF_NON_DOUBLE_SPECIAL_STRATEGIES)]};
            return behaviours;
        }

        return new SpecialBrickStrategyEnum[]{randomStrategyType1, randomStrategyType2};
    }
}
