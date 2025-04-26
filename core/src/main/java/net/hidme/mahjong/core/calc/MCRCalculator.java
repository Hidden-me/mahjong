package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.*;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static net.hidme.mahjong.core.data.MCRFan.*;

/**
 * The calculator for MCR (Mahjong Chinese Rule).
 */
public class MCRCalculator implements Calculator {

    /**
     * Calculate the Fan for a Hu hand of tiles in MCR.
     * The caller may want to cast the return value into an {@link MCRResult MCRResult} object.
     * @param hand the Hu hand of tiles whose Fan needs to be calculated
     * @return the calculation result
     */
    @Override
    public Result calculate(Hand hand) {
        if (!(hand instanceof MCRHand mcrHand))
            throw new IllegalArgumentException("incompatible hand type " + hand.getClass().getName());
        final MCRStructureAnalyzer analyzer = new MCRStructureAnalyzer();
        final List<HandStructure> structures = analyzer.getPossibleStructures(hand);
        MCRResult result = new MCRResult();
        // calculate each structure and select the largest Fan result
        for (HandStructure structure : structures) {
            final MCRResult tmpResult = calculate(mcrHand, structure);
            if (tmpResult.getFanTotal() > result.getFanTotal())
                result = tmpResult;
        }
        return result;
    }

    // calculate Fan of a hand w.r.t a certain structure
    private MCRResult calculate(MCRHand hand, HandStructure structure) {
        final MCRResult result = new MCRResult();
        // check from the least Fan to the greatest Fan
        // in order to discard certain lesser Fans when a greater Fan is satisfied
        // TODO: impl all Fans
        // 1
        checkFlowerTiles(hand, structure, result);
        // 12
        checkLesserHonorsAndKnittedTiles(hand, structure, result);
        // 24
        checkSevenPairs(hand, structure, result);
        checkGreaterHonorsAndKnittedTiles(hand, structure, result);
        // 88
        checkSevenShiftedPairs(hand, structure, result);
        checkThirteenOrphans(hand, structure, result);
        // chicken hand is checked at last
        return result;
    }

    // 1

    private void checkFlowerTiles(MCRHand hand, HandStructure structure, MCRResult result) {
        result.addFan(FLOWER_TILE, hand.flowers.length);
    }

    // 12

    private void checkLesserHonorsAndKnittedTiles(MCRHand hand, HandStructure structure, MCRResult result) {
        if (structure instanceof HonorKnittedHandStructure) {
            // add
            result.addFan(LESSER_HONORS_AND_KNITTED_TILES);
            // remove
            result.removeFan(ALL_TYPES);
            result.removeConcealedHand();
        }
    }

    // 24

    private void checkSevenPairs(MCRHand hand, HandStructure structure, MCRResult result) {
        if (structure instanceof PairHandStructure) {
            // add
            result.addFan(SEVEN_PAIRS);
            // remove
            result.removeConcealedHand();
            result.removeFan(SINGLE_WAIT);
        }
    }

    private void checkGreaterHonorsAndKnittedTiles(MCRHand hand, HandStructure structure, MCRResult result) {
        if (!(structure instanceof HonorKnittedHandStructure honorKnittedHandStructure)) return;
        if (honorKnittedHandStructure.honors.length == 7) {
            // add
            result.addFan(GREATER_HONORS_AND_KNITTED_TILES);
            // remove
            result.removeFan(LESSER_HONORS_AND_KNITTED_TILES);
            result.removeFan(ALL_TYPES);
            result.removeConcealedHand();
        }
    }

    // 88

    private void checkSevenShiftedPairs(MCRHand hand, HandStructure structure, MCRResult result) {
        if (!(structure instanceof PairHandStructure pairHandStructure)) return;
        final SortedSet<Tile> sortedPairs = new TreeSet<>(List.of(pairHandStructure.pairs));
        if (sortedPairs.size() != 7) return;
        Tile last = null;
        for (Tile tile : sortedPairs) {
            if (!tile.isNumber()) return;
            if (last != null) {
                if (tile.suit != last.suit || tile.number != last.number + 1) return;
            }
            last = tile;
        }
        // add
        result.addFan(SEVEN_SHIFTED_PAIRS);
        // remove
        result.removeFan(FULL_FLUSH);
        result.removeFan(SEVEN_PAIRS);
        result.removeConcealedHand();
        result.removeFan(NO_HONORS);
        result.removeFan(SINGLE_WAIT);
    }

    private void checkThirteenOrphans(MCRHand hand, HandStructure structure, MCRResult result) {
        if (structure instanceof OrphanHandStructure) {
            // add
            result.addFan(THIRTEEN_ORPHANS);
            // remove
            result.removeFan(ALL_TERMINALS_AND_HONORS);
            result.removeFan(ALL_TYPES);
            result.removeConcealedHand();
            result.removeFan(SINGLE_WAIT);
        }
    }

}
