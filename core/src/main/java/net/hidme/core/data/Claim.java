package net.hidme.core.data;

import java.util.Collection;
import java.util.HashSet;

/**
 * A claim is a chow/pung/kong.
 */
public record Claim(Type type,  // chow/pung/kong
                    Tile start,  // the smallest tile in a chow, or the tile in a pung/kong
                    int claimedIndex,  // the index of the claimed tile (0/1/2, 1/2 for chow only)
                    int claimedFrom  // 3 for left, 1 for right, 2 for opposite
) {
    public enum Type {
        CHOW, PUNG, KONG
    }

    public static Type getType(Collection<Tile> tiles) {
        if (tiles.size() == 3) {
            // pung
            if (new HashSet<>(tiles).size() == 1) return Type.PUNG;
            // chow
            if (tiles.stream().anyMatch(t -> !t.isNumber()))
                throw new IllegalArgumentException("A chow must consist of number tiles");
            int sum = tiles.stream().mapToInt(t -> t.number).sum();  // sum/3 is the center of chow
            // all tiles in a chow must be within [center-1, center+1]
            if (sum % 3 != 0 || tiles.stream().anyMatch(t -> Math.abs(t.number - (sum / 3)) > 1))
                throw new IllegalArgumentException("Not a valid chow");
            return Type.CHOW;
        } else if (tiles.size() == 4) {
            // kong
            if (new HashSet<>(tiles).size() != 1)
                throw new IllegalArgumentException("A 4-tile claim must be a kong and contain the same tile");
            return Type.KONG;
        }
        throw new IllegalArgumentException(tiles.size() + " tile(s) must not be a claim");
    }

}
