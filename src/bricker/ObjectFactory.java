package bricker;

import bricker.gameobjects.Heart;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ObjectFactory {
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
}
