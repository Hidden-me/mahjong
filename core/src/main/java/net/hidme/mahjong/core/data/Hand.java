package net.hidme.mahjong.core.data;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.hidme.mahjong.core.data.Tile.isNumberSuit;

/**
 * A hand of tiles.
 */
public abstract class Hand {

    public final Tile[] flowers;
    public final Claim[] claims;
    public final Tile[] tiles;
    public final Tile declaredTile;

    public Hand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile) {
        this(flowers, claims, tiles, declaredTile, false);
    }

    public Hand(Tile[] flowers, Claim[] claims, Tile[] tiles, Tile declaredTile, boolean allowsInvalidTileSet) {
        this.flowers = flowers;
        this.claims = claims;
        this.tiles = tiles;
        this.declaredTile = declaredTile;
        if (!allowsInvalidTileSet && !isValid())
            throw new IllegalArgumentException("invalid hand");
    }

    /**
     * Get hand tiles, including the declared tile.
     * Claims are not included.
     * Tiles are sorted.
     */
    public SortedMultiset<Tile> getHandTiles() {
        final SortedMultiset<Tile> ret = TreeMultiset.create(List.of(tiles));
        ret.add(declaredTile);
        return ret;
    }

    /**
     * Get hand tiles, including the declared tile and claims.
     */
    public SortedMultiset<Tile> getHandTilesWithClaims() {
        final SortedMultiset<Tile> ret = getHandTiles();
        for (Claim claim : claims) {
            ret.addAll(List.of(claim.getTiles()));
        }
        return ret;
    }

    /**
     * Get hand tiles, including the declared tile, claims, and flowers.
     */
    public SortedMultiset<Tile> getAllTiles() {
        final SortedMultiset<Tile> ret = getHandTilesWithClaims();
        ret.addAll(List.of(flowers));
        return ret;
    }

    public SortedMultiset<Tile> getHandTilesOfSuit(char suit) {
        return Tile.getTilesOfSuit(getHandTiles(), suit);
    }

    /**
     * Get concealed tiles (excluding the declared tile).
     */
    public SortedMultiset<Tile> getConcealedTiles() {
        final SortedMultiset<Tile> tiles = TreeMultiset.create(List.of(this.tiles));
        for (Claim claim : claims) {
            if (claim.isConcealed())
                tiles.addAll(List.of(claim.getTiles()));
        }
        return tiles;
    }

    public boolean hasClaim() {
        return claims.length != 0;
    }

    public List<Claim> claimsOfType(Claim.Type type) {
        final List<Claim> claims = new ArrayList<>();
        for (Claim claim : this.claims) {
            if (claim.type() == type) claims.add(claim);
        }
        return claims;
    }

    public boolean isOfPureNumberSuit() {
        char[] suits = Tile.getSuits(getHandTilesWithClaims());
        if (suits.length != 1) return false;
        return isNumberSuit(suits[0]);
    }

    public boolean isOfNumberSuits() {
        char[] suits = Tile.getSuits(getHandTilesWithClaims());
        for (char suit : suits) {
            if (!isNumberSuit(suit)) return false;
        }
        return true;
    }

    public boolean isConcealed() {
        for (Claim claim : claims) {
            if (!claim.isConcealed()) return false;
        }
        return true;
    }

    public void sort() {
        // sort tiles
        Arrays.sort(tiles);
    }

    // each tile is finite
    private boolean isValid() {
        if (declaredTile == null) return false;
        final SortedMultiset<Tile> normalTiles = getHandTilesWithClaims();
        for (Multiset.Entry<Tile> entry : normalTiles.entrySet()) {
            if (entry.getCount() < 0 || entry.getCount() > 4) {
                return false;
            }
        }
        for (Multiset.Entry<Tile> entry : HashMultiset.create(List.of(flowers)).entrySet()) {
            if (entry.getCount() < 0 || entry.getCount() > 1) {
                return false;
            }
        }
        return true;
    }

}
