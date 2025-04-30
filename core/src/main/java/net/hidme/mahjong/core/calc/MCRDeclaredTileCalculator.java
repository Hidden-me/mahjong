package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.Tile;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MCRDeclaredTileCalculator {

    /**
     * Initialize the calculator with a hand of 13 tiles (possibly including claims).
     */
    public MCRDeclaredTileCalculator(Claim[] claims, Tile[] tiles) {
        this.claims = claims;
        this.tiles = tiles;
    }

    /**
     * Get the set of tiles that lead to winning.
     * @param allowsInvalidTileSet whether a winning hand is allowed to use arbitrarily many
     *                             of each tile (i.e. a non-flower tile shows up more than 4 times
     *                             or a flower tile shows up more than twice)
     */
    public Set<Tile> calculate(boolean allowsInvalidTileSet) {
        final Set<Tile> declaredTiles = new LinkedHashSet<>();
        for (Tile tile : Tile.values()) {
            try {
                final MCRHand mcrHand = new MCRHand(new Tile[0], claims, tiles, tile, allowsInvalidTileSet);
                final MCRStructureAnalyzer analyzer = new MCRStructureAnalyzer();
                if (!analyzer.getPossibleStructures(mcrHand).isEmpty())
                    declaredTiles.add(tile);
            } catch (Throwable ignored) {}
        }
        return declaredTiles;
    }

    private final Claim[] claims;
    private final Tile[] tiles;

}
