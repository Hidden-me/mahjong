package net.hidme.mahjong.core.calc;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.Hand;
import net.hidme.mahjong.core.data.Tile;

import java.util.ArrayList;
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
        final Multiset<Tile> tiles = TreeMultiset.create(hand.getHandTiles());
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
        ;
    }

    private void addHonorKnittedStructure(Hand hand, List<HandStructure> structures) {
        ;
    }

}
