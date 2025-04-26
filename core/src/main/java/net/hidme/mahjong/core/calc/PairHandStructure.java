package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Tile;

/**
 * The hand structure for seven pairs.
 */
public class PairHandStructure implements HandStructure {
    // an array of length 7
    Tile[] pairs;

    public PairHandStructure(Tile[] pairs) {
        this.pairs = pairs;
    }
}
