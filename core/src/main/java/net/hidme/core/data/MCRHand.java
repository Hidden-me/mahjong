package net.hidme.core.data;

/**
 * A hand of tiles along with the ambient situation in MCR.
 */
public class MCRHand extends Hand {

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile) {
        this(flowers, claims, tiles, declaredTile,
                false, false, false, false,
                Wind.EAST, Wind.EAST);
    }

    public MCRHand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile,
                   boolean selfDrawn, boolean lastTile,
                   boolean lastDrawOrClaim, boolean kong,
                   Wind prevalentWind, Wind seatWind) {
        super(flowers, claims, tiles, declaredTile);
        this.selfDrawn = selfDrawn;
        this.lastTile = lastTile;
        this.lastDrawOrClaim = lastDrawOrClaim;
        this.kong = kong;
        this.prevalentWind = prevalentWind;
        this.seatWind = seatWind;
    }

    public final boolean selfDrawn, lastTile, lastDrawOrClaim, kong;
    public final Wind prevalentWind, seatWind;

}
