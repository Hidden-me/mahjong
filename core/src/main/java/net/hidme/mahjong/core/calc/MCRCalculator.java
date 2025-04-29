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
        final List<HandStructure> structures = analyzer.getPossibleStructures(mcrHand);
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
        // first check total properties
        new MCRTotalFanCalc(hand, structure, result).calculate();
        // then check Fans over sets
        new MCRSetFanCalc(hand, structure, result).calculate();
        // after that, check Fans about the declared tile
        new MCRDeclareFanCalc(hand, structure, result).calculate();
        // resolve Fan conflicts
        MCRFanConflictResolver.resolveConflict(result);
        // chicken hand is checked at last
        if (result.isEmpty())
            result.addFan(CHICKEN_HAND);
        // add flowers
        result.addFan(FLOWER_TILE, hand.flowers.length);
        return result;
    }

}
