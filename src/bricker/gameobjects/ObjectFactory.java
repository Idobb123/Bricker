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

public class ObjectFactory {
    private static float BRICK_HEIGHT = 15;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private final BrickerGameManager brickerGameManager;
    private Counter brickCounter;
    private Counter strikeCounter;
    private Random rand;

    public ObjectFactory(ImageReader imageReader,
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
        this.rand = new Random();
    }
    public Heart createHeart(Vector2 heartLocation){
        Renderable heartImage = this.imageReader.readImage("assets/heart.png", true);
        Heart heart = new Heart(Vector2.ZERO, new Vector2(15, 15),
                heartImage, strikeCounter, brickerGameManager);
        heart.setCenter(heartLocation);
        return heart;
    }
    public GameObject createStrikeNumberDisplay(Vector2 textLocation){
        TextRenderable strikeText = new TextRenderable(String.valueOf(strikeCounter.value()));
        strikeText.setColor(getStrikeNumberDisplayColor());
        GameObject strikeNumberDisplay = new GameObject(Vector2.ZERO, new Vector2(30, 30), strikeText);
        strikeNumberDisplay.setCenter(textLocation);
        return strikeNumberDisplay;
    }

    public Paddle createPaddle(Vector2 paddleLocation, float windowWidth){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, inputListener, windowWidth);
        paddle.setCenter(paddleLocation);
        return paddle;
    }

    public TemporaryPaddle createTemporaryPaddle(Vector2 paddleLocation, float windowWidth){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        TemporaryPaddle temporaryPaddle = new TemporaryPaddle(Vector2.ZERO, new Vector2(200, 20),
                paddleImage, inputListener, brickerGameManager, windowWidth );
        temporaryPaddle.setCenter(paddleLocation);
        return temporaryPaddle;
    }
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
    public GameObject createBackGround(Vector2 windowDimensions){
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,backgroundImage);
        // background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return background;
    }
    public GameObject createWall(Vector2 wallLocation, Vector2 wallDimensions){
        return new GameObject(wallLocation, wallDimensions, new RectangleRenderable(null));
    }
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
        Brick brick = new Brick(brickLocation, new Vector2(brickWidth, BRICK_HEIGHT), brickImage,strategy, brickCounter);
        return brick;
    }

    public CollisionStrategy generateStrategy(){
        CollisionStrategy strategy = new BasicCollisionStrategy(brickerGameManager);
        if (rand.nextBoolean()) {
            return strategy;
        }

        BrickStrategyEnum[] strategyTypes = BrickStrategyEnum.values();
        int nextStrategyNumber = rand.nextInt(5);
        BrickStrategyEnum randomStrategyType = strategyTypes[nextStrategyNumber];
        return chooseStrategyBasedOnInt(randomStrategyType, strategy);
    }
    private CollisionStrategy chooseStrategyBasedOnInt(BrickStrategyEnum strategyType, CollisionStrategy strategy) {
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
                BrickStrategyEnum[] strategyIntegers = chooseDoubleStrategies();
                for (BrickStrategyEnum currentStratInt : strategyIntegers) {
                    strategy = chooseStrategyBasedOnInt(currentStratInt, strategy);
                }
                return strategy;
            default:
                return strategy;
        }
    }
    private BrickStrategyEnum[] chooseDoubleStrategies(){
        BrickStrategyEnum[] strategyTypes = BrickStrategyEnum.values();
        BrickStrategyEnum randomStrategyType1 = strategyTypes[rand.nextInt(5)];
        BrickStrategyEnum randomStrategyType2 = strategyTypes[rand.nextInt(5)];
        if (randomStrategyType1 == BrickStrategyEnum.DOUBLE_STRATEGY || randomStrategyType2 == BrickStrategyEnum.DOUBLE_STRATEGY){
            BrickStrategyEnum[] behaviours =  {strategyTypes[rand.nextInt(4)],
                    strategyTypes[rand.nextInt(4)],
                    strategyTypes[rand.nextInt(4)]};
            return behaviours;
        }

        return new BrickStrategyEnum[]{randomStrategyType1, randomStrategyType2};
    }
}
