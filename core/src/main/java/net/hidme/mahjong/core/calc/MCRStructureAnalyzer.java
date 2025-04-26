package net.hidme.mahjong.core.calc;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multiset;
import net.hidme.mahjong.core.data.Hand;
import net.hidme.mahjong.core.data.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MCRStructureAnalyzer {

    public List<HandStructure> getPossibleStructures(Hand hand) {
        final List<HandStructure> structures = new ArrayList<>();
        addNormalStructure(hand, structures);
        addPairStructure(hand, structures);
        addOrphanStructure(hand, structures);
        addHonorKnittedStructure(hand, structures);
        return structures;
    }

    private void addNormalStructure(Hand hand, List<HandStructure> structures) {
        ;
    }

    private void addPairStructure(Hand hand, List<HandStructure> structures) {
        if (hand.claims.length != 0) return;
        final Multiset<Tile> tiles = hand.getHandTiles();
        if (tiles.entrySet().stream().allMatch(e -> e.getCount() % 2 == 0)) {
            final List<Tile> pairs = new ArrayList<>();
            for (Multiset.Entry<Tile> entry : tiles.entrySet()) {
                if (entry.getCount() >= 2) pairs.add(entry.getElement());
                if (entry.getCount() == 4) pairs.add(entry.getElement());
            }
            structures.add(new PairHandStructure(pairs.toArray(new Tile[0])));
        }
    }

    private void addOrphanStructure(Hand hand, List<HandStructure> structures) {
        if (hand.claims.length != 0) return;
        final Multiset<Tile> tiles = hand.getHandTiles();
        if (tiles.entrySet().size() != 13) return;
        Tile doubleTile = null;
        for (Multiset.Entry<Tile> entry : tiles.entrySet()) {
            if (!entry.getElement().isOrphan()) return;
            if (entry.getCount() == 2)
                doubleTile = entry.getElement();
        }
        structures.add(new OrphanHandStructure(doubleTile));
    }

    private void addHonorKnittedStructure(Hand hand, List<HandStructure> structures) {
        if (hand.claims.length != 0) return;
        final Multiset<Tile> tiles = hand.getHandTiles();
        if (tiles.entrySet().size() != 14) return;
        final List<Tile> honors = new ArrayList<>(), knittedTiles = new ArrayList<>();
        // 369<->suit1, 147<->suit2, 258<->suit3
        BiMap<Integer, Character> knittedSuits = HashBiMap.create();
        for (Tile tile : tiles.elementSet()) {
            if (tile.isHonor()) honors.add(tile);
            else if (tile.isNumber()) {
                final char suit = tile.suit;
                final int start = tile.number % 3;
                final Character assignedSuit = knittedSuits.get(start);
                if (assignedSuit == null && knittedSuits.inverse().get(suit) == null) {
                    // new knitted-chow-suit mapping
                    knittedSuits.put(start, suit);
                } else if (assignedSuit == null || assignedSuit != suit) {
                    // the suit and knitted chow does not match
                    return;
                }
            }
        }
        structures.add(new HonorKnittedHandStructure(honors.toArray(new Tile[0]), knittedTiles.toArray(new Tile[0])));
    }

}
