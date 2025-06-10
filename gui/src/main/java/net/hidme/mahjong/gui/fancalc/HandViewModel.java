package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;

import java.util.List;

public record HandViewModel(
        List<Claim> claims,
        List<Tile> tiles,
        Tile declaredTile
) {
}
