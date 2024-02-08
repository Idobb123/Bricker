package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {

    private Counter strikeCounter;
    private BrickerGameManager brickerGameManager;
    private GameObject[] hearts;



    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Heart(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 Counter strikeCounter,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.strikeCounter = strikeCounter;
        this.brickerGameManager = brickerGameManager;
    }

    public void deleteHeart() {
        strikeCounter.decrement();
        brickerGameManager.deleteObject(this, Layer.UI);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

    }

}
