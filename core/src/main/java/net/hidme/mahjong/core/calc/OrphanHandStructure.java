package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Tile;

/**
 * The hand structure for thirteen orphans.
 */
public class OrphanHandStructure implements HandStructure {
    // the only double tile in 13 orphans
    Tile doubleTile;

    public OrphanHandStructure(Tile doubleTile) {
        this.doubleTile = doubleTile;
    }
}
