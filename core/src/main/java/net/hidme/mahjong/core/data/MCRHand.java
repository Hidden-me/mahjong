package net.hidme.mahjong.core.data;

/**
 * A hand of tiles along with the ambient situation in MCR.
 */
public class MCRHand extends Hand {

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile, boolean allowsInvalidTileSet) {
        this(flowers, claims, tiles, declaredTile, allowsInvalidTileSet,
                false, false, false, false,
                Wind.EAST, Wind.EAST);
    }

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile,
                   boolean selfDrawn, boolean lastTile,
                   boolean lastDrawOrClaim, boolean kong,
                   Wind prevalentWind, Wind seatWind) {
        this(flowers, claims, tiles, declaredTile, false,
                selfDrawn, lastTile, lastDrawOrClaim, kong, prevalentWind, seatWind);
    }

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile, boolean allowsInvalidTileSet,
                   boolean selfDrawn, boolean lastTile,
                   boolean lastDrawOrClaim, boolean kong,
                   Wind prevalentWind, Wind seatWind) {
        super(flowers, claims, tiles, declaredTile, allowsInvalidTileSet);
        if (!isValid())
            throw new IllegalArgumentException("invalid MCR hand");
        this.selfDrawn = selfDrawn;
        this.lastTile = lastTile;
        this.lastDrawOrClaim = lastDrawOrClaim;
        this.kong = kong;
        this.prevalentWind = prevalentWind;
        this.seatWind = seatWind;
    }

    public final boolean selfDrawn, lastTile, lastDrawOrClaim, kong;
    public final Wind prevalentWind, seatWind;

    // 14 effective tiles
    private boolean isValid() {
        return claims.length * 3 + tiles.length == 13;
    }

}
