package net.hidme.mahjong.gui.common;

/**
 * Counter buttons with an integer value.
 */
public class AccumulatorButton extends CounterButton {

    public AccumulatorButton(int value, Integer minCount, Integer maxCount) {
        super(String.valueOf(value), minCount, maxCount);
        this.value = value;
    }

    public int getTotalValue() {
        return value * getCount();
    }

    private int value;

}
