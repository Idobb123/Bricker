package bricker.gameobjects;

public enum BrickStrategyEnum {
    PUCK(0),
    PADDLE(1),
    CAMERA(2),
    HEART(3),
    DOUBLE(4);
    private int value;
    BrickStrategyEnum(int value) {
        this.value = value;
    }
}
