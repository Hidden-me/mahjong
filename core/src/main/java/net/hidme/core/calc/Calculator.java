package net.hidme.core.calc;

import net.hidme.core.data.Hand;
import net.hidme.core.data.Result;

/**
 * A calculator calculates the score for a Hu hand of tiles.
 * Different subclasses do calculation according to different rules.
 */
public interface Calculator {

    /**
     * Calculate the score for a Hu hand of tiles.
     * Since the calculation result format may be different from rule to rule,
     * the caller may want to cast the return value into the class they want.
     * @param hand the Hu hand of tiles whose score needs to be calculated
     * @return the calculation result according to the rule
     */
    Result calculate(Hand hand);

}
