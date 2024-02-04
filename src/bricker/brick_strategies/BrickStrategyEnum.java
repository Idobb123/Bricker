package bricker.brick_strategies;

public enum BrickStrategyEnum {
    BASIC(0),
    PUCK(5),
    PADDLE(6),
    CAMERA(7),
    HEART(8),
    DOUBLE(9);
    private int value;
    BrickStrategyEnum(int value) {
        this.value = value;
    }
}
