package net.hidme.mahjong.core.data;

import java.util.Arrays;
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
                throw new IllegalArgumentException("invalid MCR hand: " + msg);
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
        if (kong && selfDrawn && claimsOfType(Claim.Type.KONG).isEmpty())
            return "out with replacement tile (kong and selfDrawn) must not occur if there is no kong";
        // robbing the kong
        if (kong && !selfDrawn && getHandTilesWithClaims().count(declaredTile) > 1)
            return "robbing the kong (kong and not selfDrawn) must not occur if the declared tile is not unique in the hand";
        if (!selfDrawn && lastDrawOrClaim && kong)
            return "last claim and robbing the kong cannot occur at the same time";
        if (!selfDrawn && kong && !lastTile)
            return "robbing the kong must indicate last tile";
        // last tile
        if (!lastTile && Claim.getTiles(List.of(claims)).count(declaredTile) == 3)
            return "lastTile must occur if the declared tile is used thrice";
        if (lastTile && getConcealedTiles().count(declaredTile) > 0)
            return "lastTile must not occur if the declared tile is present in concealed tiles";
        return null;
    }

    @Override
    public String toString() {
        return "MCRHand{" +
                "selfDrawn=" + selfDrawn +
                ", lastTile=" + lastTile +
                ", lastDrawOrClaim=" + lastDrawOrClaim +
                ", kong=" + kong +
                ", prevalentWind=" + prevalentWind +
                ", seatWind=" + seatWind +
                ", flowers=" + Arrays.toString(flowers) +
                ", claims=" + Arrays.toString(claims) +
                ", tiles=" + Arrays.toString(tiles) +
                ", declaredTile=" + declaredTile +
                '}';
    }
}
