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
    }

    private void testBaseFan(MCRFan baseFan) {
        testBaseFan(baseFan, 64);
    }

    private void testBaseFan(MCRFan baseFan, int loopCount) {
        try {
            final MCRHandGenerator generator = new MCRHandGenerator();
            for (int i = 0; i < loopCount; i++) {
                generator.generate(baseFan);
            }
        } catch (Throwable e) {
            Assertions.fail(e);
        }
    }

}
