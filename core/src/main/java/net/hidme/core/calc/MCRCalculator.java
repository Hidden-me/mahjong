package net.hidme.core.calc;

import net.hidme.core.data.Hand;
import net.hidme.core.data.Result;

/**
 * The calculator for MCR (Mahjong Chinese Rule).
 */
public class MCRCalculator implements Calculator {

    /**
     * Calculate the Fan for a Hu hand of tiles in MCR.
     * The caller may want to cast the return value into an {@link net.hidme.core.data.MCRResult MCRResult} object.
     * @param hand the Hu hand of tiles whose Fan needs to be calculated
     * @return the calculation result
     */
    @Override
    public Result calculate(Hand hand) {
        return null;
    }

}
