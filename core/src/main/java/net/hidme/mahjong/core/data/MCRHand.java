package net.hidme.mahjong.core.data;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;

import java.util.List;

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
