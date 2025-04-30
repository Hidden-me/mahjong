package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Hand;
import net.hidme.mahjong.core.data.MCRHandParser;
import net.hidme.mahjong.core.data.MCRResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class MCRCalculatorTest {

    @Test
    public void testAll() {
        try {
            // 88
            testBigFourWinds();
            testBigThreeDragons();
            testAllGreen();
            testNineGates();
            testFourKongs();
            testSevenShiftedPairs();
            testThirteenOrphans();
        } catch (Throwable e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testBigFourWinds() throws ParseException {
        testSingleCase(";EEE3,SSSS2;55pWWWNNN;E,E,1,0,0,0", 98);
        testSingleCase(";WWW2,NNN1;11sEEESSS;E,E,0,0,0,0", 126);
        testSingleCase(";NNN3,EEE1;SSSWWFFW;E,E,0,0,0,0", 152);
    }

    @Test
    public void testBigThreeDragons() throws ParseException {
        testSingleCase(";PPP2,FFF1;66999sCCC;E,E,1,0,0,0", 104);
        testSingleCase(";CCC3,FFFF0,999m2;11pPPP;E,E,0,0,0,0", 123);
        testSingleCase(";FFF1,EEE3;WWCCCPPP;E,W,0,0,0,0", 154);
    }

    @Test
    public void testAllGreen() throws ParseException {
        testSingleCase(";234s1,666s2;33888sFFF;E,E,1,0,0,0", 93);
        testSingleCase(";;22333344668888s;E,E,0,0,0,0", 142);
    }

    @Test
    public void testNineGates() throws ParseException {
        testSingleCase(";;11123456789999s;E,E,0,0,0,0", 106);
        testSingleCase(";;11123456789998m;E,E,0,0,0,0", 92);
        testSingleCase(";;11123456789997p;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789996s;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789995m;E,E,0,0,0,0", 91);
        testSingleCase(";;11123456789994p;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789993s;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789992m;E,E,0,0,0,0", 92);
        testSingleCase(";;11123456789991p;E,E,0,0,0,0", 106);
        testSingleCase(";123s1;11456789999s;E,E,0,0,0,0", 43);
    }

    @Test
    public void testFourKongs() throws ParseException {
        testSingleCase(";2222s1,3333s1,6666p1,4444m0;EE;E,E,0,0,0,0", 88);
    }

    @Test
    public void testSevenShiftedPairs() throws ParseException {
        testSingleCase(";;11223344556677s;E,E,0,0,0,0", 88);
        testSingleCase(";;12233445566771s;E,E,0,0,0,0", 88);
        testSingleCase(";;22334455667788s;E,E,1,0,0,0", 91);
    }

    @Test
    public void testThirteenOrphans() throws ParseException {
        testSingleCase(";;19m19p19sESWNPFC1s;E,E,0,0,0,0", 88);
        testSingleCase(";;19m19p11sESWNPFC9s;E,E,0,0,0,0", 88);
        testSingleCase(";;19m119p19sESWNPFC;E,E,1,0,0,0", 89);
    }

    @Test
    public void testPairHand() throws ParseException {
        // seven shifted pairs only
        // seven pairs only
        testSingleCase(";;EE22334455s66p77m;E,E,0,0,0,0", 24);
        // mixed
//        testSingleCase(";;223344667788sEE;E,E,1,0,0,0", 31);
//        testSingleCase(";;223344667788s11p;E,E,1,0,0,0", 27);
//        testSingleCase(";;2233m44667788sEE;E,E,1,0,0,0", 26);
//        testSingleCase(";;EEEE334455s66p77m;E,E,1,0,0,0", 26);
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
