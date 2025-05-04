package net.hidme.mahjong.core.data;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import java.util.*;

import static net.hidme.mahjong.core.data.Tile.isNumberSuit;

/**
 * A claim is a chow/pung/kong.
 */
public record Claim(Type type,  // chow/pung/kong
                    Tile start,  // the smallest tile in a chow, or the tile in a pung/kong
                    int claimedIndex,  // the index of the claimed tile (0/1/2, 1/2 for chow only)
                    int claimedFrom  // 0 for self, 1 for right, 2 for opposite, 3 for left
) {
    public enum Type {
        CHOW, PUNG, KONG, KNITTED_CHOW;

        public boolean isPung() {
            return this == PUNG || this == KONG;
        }
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

    public static boolean isOfPureNumberSuit(Iterable<Claim> claims) {
        char[] suits = getSuits(claims);
        if (suits.length != 1) return false;
        return isNumberSuit(suits[0]);
    }

    public static char[] getSuits(Iterable<Claim> claims) {
        return Tile.getSuits(getTileList(claims));
    }

    public static List<Tile> getTileList(Iterable<Claim> claims) {
        final List<Tile> tiles = new ArrayList<>();
        collectTiles(claims, tiles);
        return tiles;
    }

    public static SortedMultiset<Tile> getTiles(Iterable<Claim> claims) {
        final SortedMultiset<Tile> tiles = TreeMultiset.create();
        collectTiles(claims, tiles);
        return tiles;
    }

    private static void collectTiles(Iterable<Claim> claims, Collection<Tile> tiles) {
        for (Claim claim : claims) {
            tiles.addAll(List.of(claim.getTiles()));
        }
    }

    public Tile[] getTiles() {
        return switch (type) {
            case CHOW -> new Tile[]{start, start.shift(1), start.shift(2)};
            case PUNG -> new Tile[]{start, start, start};
            case KONG -> new Tile[]{start, start, start, start};
            case KNITTED_CHOW -> new Tile[]{start, start.shift(3), start.shift(6)};
        };
    }

    public Set<Tile> getTileSet() {
        return new LinkedHashSet<>(Arrays.asList(getTiles()));
    }

}
