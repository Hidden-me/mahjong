package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;

import java.util.LinkedList;
import java.util.List;

/**
 * For concurrent updates/queries of the same hand.
 */
public class ConcurrentHand {

    public ConcurrentHand() {
        claims = new LinkedList<>();
        tiles = new LinkedList<>();
    }

    /**
     * Get a copy of view model of this hand for rendering.
     */
    public synchronized HandViewModel getViewModel() {
        // copy each list
        return new HandViewModel(new LinkedList<>(claims),
                new LinkedList<>(tiles),
                declaredTile);
    }

    public synchronized void addClaim(Claim claim) {
        final int sizeTotal = sizeTotal(), sizeWithoutDeclaredTile = sizeWithoutDeclaredTile();
        if (sizeTotal >= 12) return;
        if (sizeWithoutDeclaredTile == 11) {
            // adding a claim causes the last tile to be the declared one
            declaredTile = tiles.removeLast();
        }
        claims.add(claim);
    }

    public synchronized void addTile(Tile tile) {
        final int sizeTotal = sizeTotal(), sizeWithoutDeclaredTile = sizeWithoutDeclaredTile();
        if (sizeTotal >= 14) return;
        if (sizeWithoutDeclaredTile >= 13) declaredTile = tile;
        else tiles.add(tile);
    }

    public synchronized void setDeclaredTile(Tile tile) {
        declaredTile = tile;
    }

    public synchronized void removeClaim(int index) {
        claims.remove(index);
    }

    public synchronized void removeTile(int index) {
        tiles.remove(index);
    }

    public synchronized void clear() {
        claims.clear();
        tiles.clear();
        declaredTile = null;
    }

    public synchronized int size() {
        return sizeTotal();
    }

    private int sizeTotal() {
        return sizeWithoutDeclaredTile() + (declaredTile == null ? 0 : 1);
    }

    private int sizeWithoutDeclaredTile() {
        return claims.size() * 3 + tiles.size();
    }

    private List<Claim> claims;
    private List<Tile> tiles;
    private Tile declaredTile;

}
