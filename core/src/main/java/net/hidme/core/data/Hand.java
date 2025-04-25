package net.hidme.core.data;

/**
 * A hand of cards.
 */
public abstract class Hand {

    public Hand(short[] cards) {
        this.cards = cards;
        this.count = cards.length;
    }

    protected final short[] cards;
    protected final int count;

}
