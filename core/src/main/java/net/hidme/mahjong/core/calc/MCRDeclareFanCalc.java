package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.MCRResult;

/**
 * Calculator for Fans of the declared tile.
 */
public class MCRDeclareFanCalc {

    public MCRDeclareFanCalc(MCRHand hand, HandStructure structure, MCRResult result) {
        this.hand = hand;
        this.structure = structure;
        this.result = result;
    }

    public void calculate() {
        ;
    }

    private final MCRHand hand;
    private final HandStructure structure;
    private final MCRResult result;

}
