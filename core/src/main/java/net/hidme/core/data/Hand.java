package net.hidme.core.data;

/**
 * A hand of tiles.
 */
public abstract class Hand {

    public Hand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile) {
        this.flowers = flowers;
        this.claims = claims;
        this.tiles = tiles;
        this.declaredTile = declaredTile;
    }

    public final Tile[] flowers;
    public final Claim[] claims;
    public final Tile[] tiles;
    public final Tile declaredTile;

}
