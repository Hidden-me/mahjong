package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Tile;

/**
 * The hand structure for honors and knitted tiles.
 */
public class HonorKnittedHandStructure implements HandStructure {
    Tile[] honors, knittedTiles;

    public HonorKnittedHandStructure(Tile[] honors, Tile[] knittedTiles) {
        this.honors = honors;
        this.knittedTiles = knittedTiles;
    }
}
