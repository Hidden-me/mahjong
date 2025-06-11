package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.core.data.Wind;

import java.util.List;

/**
 * Intermediate representation between ConcurrentHand and MCRHand.
 * Useful for view rendering.
 */
public record HandViewModel(
        List<Claim> claims,
        List<Tile> tiles,
        Tile declaredTile
) {

    public MCRHand toMCRHand(Options options) {
        try {
            return new MCRHand(
                    new Tile[0],
                    claims.toArray(new Claim[0]),
                    tiles.toArray(new Tile[0]),
                    declaredTile,
                    options.selfDrawn(), options.lastTile(),
                    options.lastDrawOrClaim(), options.kong(),
                    options.prevalentWind(), options.seatWind());
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}
