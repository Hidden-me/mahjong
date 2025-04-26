package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Hand;
import net.hidme.mahjong.core.data.MCRHandParser;
import net.hidme.mahjong.core.data.MCRResult;
import net.hidme.mahjong.core.data.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class MCRCalculatorTest {

    @Test
    public void testAll() {
        try {
            testPairHand();
        } catch (Throwable e) {
            Assertions.fail(e);
        }
    }

    private void testPairHand() throws ParseException {
        // seven shifted pairs only
        testSingleCase(";;11223344556677s;E,E,1,0,0,0", 88);
        // seven pairs only
        testSingleCase(";;EE22334455s66p77m;E,E,1,0,0,0", 24);
        // mixed
        testSingleCase(";;22334455667788s;E,E,1,0,0,0", 91);
        testSingleCase(";;223344667788sEE;E,E,1,0,0,0", 31);
        testSingleCase(";;223344667788s11p;E,E,1,0,0,0", 27);
        testSingleCase(";;2233m44667788sEE;E,E,1,0,0,0", 26);
    }

    private void testSingleCase(String cas, int expected) throws ParseException {
        final MCRHandParser parser = new MCRHandParser();
        final Hand hand = parser.parse(cas);
        final MCRCalculator calculator = new MCRCalculator();
        MCRResult result = (MCRResult) calculator.calculate(hand);
        Assertions.assertEquals(expected, result.getFanTotal());
    }

}
