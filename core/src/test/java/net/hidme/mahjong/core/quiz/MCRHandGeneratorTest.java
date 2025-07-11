package net.hidme.mahjong.core.quiz;

import net.hidme.mahjong.core.data.MCRFan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.hidme.mahjong.core.data.MCRFan.*;

public class MCRHandGeneratorTest {

    @Test
    public void testAll() {
        // 88
        testBaseFan(BIG_FOUR_WINDS);
        testBaseFan(BIG_THREE_DRAGONS);
        testBaseFan(ALL_GREEN);
        testBaseFan(NINE_GATES);
        testBaseFan(FOUR_KONGS);
        testBaseFan(SEVEN_SHIFTED_PAIRS);
        testBaseFan(THIRTEEN_ORPHANS);
        // 64
        testBaseFan(ALL_TERMINALS);
        testBaseFan(LITTLE_FOUR_WINDS);
        testBaseFan(LITTLE_THREE_DRAGONS);
        testBaseFan(ALL_HONORS);
        testBaseFan(FOUR_CONCEALED_PUNGS);
        testBaseFan(PURE_TERMINAL_CHOWS);
        // 48
        testBaseFan(QUADRUPLE_CHOW);
        testBaseFan(FOUR_PURE_SHIFTED_PUNGS);
        // 32
        testBaseFan(FOUR_PURE_SHIFTED_CHOWS);
        testBaseFan(THREE_KONGS);
        testBaseFan(ALL_TERMINALS_AND_HONORS);
        // 24
        testBaseFan(SEVEN_PAIRS);
        testBaseFan(GREATER_HONORS_AND_KNITTED_TILES);
        testBaseFan(ALL_EVEN_PUNGS);
        testBaseFan(FULL_FLUSH);
        testBaseFan(PURE_TRIPLE_CHOW);
        testBaseFan(PURE_SHIFTED_PUNGS);
        testBaseFan(UPPER_TILES);
        testBaseFan(MIDDLE_TILES);
        testBaseFan(LOWER_TILES);
        // 16
        testBaseFan(PURE_STRAIGHT);
        testBaseFan(THREE_SUITED_TERMINAL_CHOWS);
        testBaseFan(PURE_SHIFTED_CHOWS);
        testBaseFan(ALL_FIVE);
        testBaseFan(TRIPLE_PUNG);
        testBaseFan(THREE_CONCEALED_PUNGS);
        // 12
        testBaseFan(LESSER_HONORS_AND_KNITTED_TILES);
        testBaseFan(KNITTED_STRAIGHT);
        testBaseFan(UPPER_FOUR);
        testBaseFan(LOWER_FOUR);
        testBaseFan(BIG_THREE_WINDS);
        // 8
        testBaseFan(MIXED_STRAIGHT);
        testBaseFan(REVERSIBLE_TILES);
        testBaseFan(MIXED_TRIPLE_CHOW);
        testBaseFan(MIXED_SHIFTED_PUNGS);
        testBaseFan(CHICKEN_HAND);
    }

    private void testBaseFan(MCRFan baseFan) {
        testBaseFan(baseFan, 64);
    }

    private void testBaseFan(MCRFan baseFan, int loopCount) {
        try {
            final MCRHandGenerator generator = new MCRHandGenerator();
            generator.debugMode = true;
            for (int i = 0; i < loopCount; i++) {
                generator.generate(baseFan);
            }
        } catch (Throwable e) {
            Assertions.fail(e);
        }
    }

}
