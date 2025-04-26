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
        final MCRStructureAnalyzer analyzer = new MCRStructureAnalyzer();
        final List<HandStructure> structures = analyzer.getPossibleStructures(hand);
        MCRResult result = new MCRResult();
        // select the largest Fan result
        for (HandStructure structure : structures) {
            final MCRResult tmpResult = calculate(hand, structure);
            if (tmpResult.getFanTotal() > result.getFanTotal())
                result = tmpResult;
        }
        return result;
    }

    // calculate Fan of a hand w.r.t a certain structure
    private MCRResult calculate(Hand hand, HandStructure structure) {
        final MCRResult result = new MCRResult();
        // check from the least Fan to the greatest Fan
        // in order to discard certain lesser Fans when a greater Fan is satisfied
        // TODO: impl all Fans
        checkSevenPairs(hand, structure, result);
        checkSevenShiftedPairs(hand, structure, result);
        return result;
    }

    private void checkSevenShiftedPairs(Hand hand, HandStructure structure, MCRResult result) {
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

    private void checkSevenPairs(Hand hand, HandStructure structure, MCRResult result) {
        if (structure instanceof PairHandStructure)
            result.addFan(SEVEN_PAIRS);
    }

}
