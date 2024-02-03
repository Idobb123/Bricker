package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends GameObject {

    private final BrickerGameManager brickerGameManager;

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
                 BrickerGameManager brickerGameManager, int initialStrikes) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
    }

    public void deleteHeart() {
        brickerGameManager.deleteObject(this);
    }




}
