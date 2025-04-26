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
            testOrphanHand();
        } catch (Throwable e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testPairHand() throws ParseException {
        // seven shifted pairs only
        testSingleCase(";;11223344556677s;E,E,0,0,0,0", 88);
        testSingleCase(";;12233445566771s;E,E,0,0,0,0", 88);
        // seven pairs only
        testSingleCase(";;EE22334455s66p77m;E,E,0,0,0,0", 24);
        // mixed
        testSingleCase(";;22334455667788s;E,E,1,0,0,0", 91);
        testSingleCase(";;223344667788sEE;E,E,1,0,0,0", 31);
        testSingleCase(";;223344667788s11p;E,E,1,0,0,0", 27);
        testSingleCase(";;2233m44667788sEE;E,E,1,0,0,0", 26);
        testSingleCase(";;EEEE334455s66p77m;E,E,1,0,0,0", 26);
    }

    @Test
    public void testOrphanHand() throws ParseException {
        // thirteen orphans only
        testSingleCase(";;19m19p19sESWNPFC1s;E,E,0,0,0,0", 88);
        testSingleCase(";;19m19p11sESWNPFC9s;E,E,0,0,0,0", 88);
        // mixed
    }

    @Test
    public void testHonorsKnittedHand() throws ParseException {
        // greater honors and knitted tiles only
        testSingleCase(";;25m36p47sESWNPFC1s;E,E,0,0,0,0", 24);
        testSingleCase(";;258m9p47sESWNPFC1s;E,E,0,0,0,0", 24);
        testSingleCase(";;258m147sESWNPFC9p;E,E,0,0,0,0", 24);
        // lesser honors and knitted tiles only
        testSingleCase(";;258m36p47sESWNPF1s;E,E,0,0,0,0", 12);
        testSingleCase(";;258m36p47sESWPFC1s;E,E,0,0,0,0", 12);
        // unqualified cases
        testSingleCase(";;25m36p47sESWNPFC1m;E,E,0,0,0,0", 0);
        testSingleCase("1f;;25m36p47mESWNPFC1m;E,E,0,0,0,0", 0);
        // mixed
        testSingleCase("48f;;25m36p47sESWNPFC1s;E,E,0,0,0,0", 26);
    }

    private void testSingleCase(String cas, int expected) throws ParseException {
        final MCRHandParser parser = new MCRHandParser();
        final Hand hand = parser.parse(cas);
        final MCRCalculator calculator = new MCRCalculator();
        MCRResult result = (MCRResult) calculator.calculate(hand);
        Assertions.assertEquals(expected, result.getFanTotal());
    }

}
