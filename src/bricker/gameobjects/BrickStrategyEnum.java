package bricker.gameobjects;

public enum BrickStrategyEnum {
    PUCK(0),
    ADDITIONAL_PADDLE(1),
    CAMERA_CHANGE(2),
    ADDITIONAL_HEART(3),
    DOUBLE_STRATEGY(4);

    private final int value;

    BrickStrategyEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
