package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface for all the brick strategies to implement.
 * @author Ido Ben Zvi Brenner & Adam Leon Fleisher
 */
public interface CollisionStrategy {
    /**
     * The function is responsible for applying the class strategy on a given brick when called.
     * The function is called whenever another object collides with the brick.
     * @param brick The brick to apply the strategy on.
     * @param other The object that collided with the brick.
     */
    public void onCollision(GameObject brick, GameObject other);
}
