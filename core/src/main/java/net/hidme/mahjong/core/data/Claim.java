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
                    int claimedFrom,  // 0 for self, 1 for right, 2 for opposite, 3 for left
                    boolean isDeclared  // whether the claim is made upon declaration
) {
    public enum Type {
        CHOW, PUNG, KONG, KNITTED_CHOW;

        public boolean isPung() {
            return this == PUNG || this == KONG;
        }
    }

    public static final int CLAIMED_FROM_SELF = 0;
    public static final int CLAIMED_FROM_RIGHT = 1;
    public static final int CLAIMED_FROM_OPPOSITE = 2;
    public static final int CLAIMED_FROM_LEFT = 3;
    /**
     * Claimed from some other player.
     * Only use it when the claim source is really unknown
     * (e.g. when the hand is randomly generated, the claim happens upon declaration,
     * and the claim source is unknown).
     */
    public static final int CLAIMED_FROM_OTHER = 4;

    public static Claim create(Type type, Tile start, int claimedIndex, int claimedFrom) {
        return new Claim(type, start, claimedIndex, claimedFrom, false);
    }

    public static Claim create(Type type, Tile start, int claimedIndex, int claimedFrom, boolean isDeclared) {
        return new Claim(type, start, claimedIndex, claimedFrom, isDeclared);
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

    public boolean isConcealed() {
        return claimedFrom == CLAIMED_FROM_SELF;
    }

    public int size() {
        return type == Type.KONG ? 4 : 3;
    }

    /**
     * Return the tiles in this claim in the ascending order.
     */
    public Tile[] getTiles() {
        return switch (type) {
            case CHOW -> new Tile[]{start, start.shift(1), start.shift(2)};
            case PUNG -> new Tile[]{start, start, start};
            case KONG -> new Tile[]{start, start, start, start};
            case KNITTED_CHOW -> new Tile[]{start, start.shift(3), start.shift(6)};
        };
    }

    /**
     * Return the tiles in this claim in a "fancy" manner:
     * tiles are returned in the order in which they are placed in a game.
     * This order takes the claim source into consideration.
     * For example, "678p2" returns [8p, 6p, 7p] as 8p is from the player on the left.
     */
    public Tile[] getTilesFancy() {
        return switch (type) {
            case CHOW -> switch (claimedIndex) {
                case 0 -> new Tile[]{start, start.shift(1), start.shift(2)};
                case 1 -> new Tile[]{start.shift(1), start, start.shift(2)};
                case 2 -> new Tile[]{start.shift(2), start, start.shift(1)};
                default -> throw new IllegalStateException("Unexpected claimed index: " + claimedIndex);
            };
            case PUNG -> new Tile[]{start, start, start};
            case KONG -> new Tile[]{start, start, start, start};
            case KNITTED_CHOW -> throw new UnsupportedOperationException("Knitted chows in getTilesFancy");
        };
    }

    public Set<Tile> getTileSet() {
        return new LinkedHashSet<>(Arrays.asList(getTiles()));
    }

}
