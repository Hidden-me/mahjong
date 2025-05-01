package net.hidme.mahjong.core.data;

import java.util.List;

/**
 * A hand of tiles along with the ambient situation in MCR.
 */
public class MCRHand extends Hand {

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile, boolean allowsInvalidTileSet,
                   boolean allowsConflictingOptions) {
        this(flowers, claims, tiles, declaredTile, allowsInvalidTileSet,
                allowsConflictingOptions, false, false, false, false,
                Wind.EAST, Wind.EAST);
    }

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile,
                   boolean selfDrawn, boolean lastTile,
                   boolean lastDrawOrClaim, boolean kong,
                   Wind prevalentWind, Wind seatWind) {
        this(flowers, claims, tiles, declaredTile, false,
                false, selfDrawn, lastTile, lastDrawOrClaim, kong, prevalentWind, seatWind);
    }

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile, boolean allowsInvalidTileSet,
                   boolean allowsConflictingOptions,
                   boolean selfDrawn, boolean lastTile,
                   boolean lastDrawOrClaim, boolean kong,
                   Wind prevalentWind, Wind seatWind) {
        super(flowers, claims, tiles, declaredTile, allowsInvalidTileSet);
        this.selfDrawn = selfDrawn;
        this.lastTile = lastTile;
        this.lastDrawOrClaim = lastDrawOrClaim;
        this.kong = kong;
        this.prevalentWind = prevalentWind;
        this.seatWind = seatWind;
        if (!allowsConflictingOptions) {
            final String msg = checkValid();
            if (msg != null)
                throw new IllegalArgumentException("invalid MCR handL: " + msg);
        }
    }

    public final boolean selfDrawn, lastTile, lastDrawOrClaim, kong;
    public final Wind prevalentWind, seatWind;

    // return error message
    private String checkValid() {
        // 14 effective tiles
        if (claims.length * 3 + tiles.length != 13)
            return "not 14 effective tiles";
        // out with replacement tile
        if (claimsOfType(Claim.Type.KONG).isEmpty() && kong && selfDrawn)
            return "out with replacement tile (kong and selfDrawn) must not occur if there is no kong";
        // last tile
        if (Claim.getTiles(List.of(claims)).count(declaredTile) == 3 && !lastTile)
            return "lastTile must occur if the declared tile is used thrice";
        return null;
    }

}
