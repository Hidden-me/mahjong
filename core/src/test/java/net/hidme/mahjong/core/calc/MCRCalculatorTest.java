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
            // 64
            testAllTerminals();
            testLittleFourWinds();
            testLittleThreeDragons();
            testAllHonors();
            testFourConcealedPungs();
            testPureTerminalChows();
            // 48
            testQuadrupleChow();
            testFourPureShiftedPungs();
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
        testSingleCase(";2222s0,3333s0,6666p0,4444m0;EE;E,E,0,0,0,0", 152);
        testSingleCase(";2222s0,3333s0,6666p0,4444m1;EE;E,E,0,0,0,0", 104);
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
    public void testAllTerminals() throws ParseException {
        testSingleCase(";111m1;1199s111999p1s;E,E,1,0,0,0", 97);
        testSingleCase(";1111m2,111p3;1199s999p9s;E,E,1,0,0,0", 68);
        testSingleCase(";;1199m199s119999p1s;E,E,0,0,0,0", 90);
    }

    @Test
    public void testLittleFourWinds() throws ParseException {
        testSingleCase(";EEE2,SSS1;23pWWWNN1p;S,N,1,0,0,0", 77);
        testSingleCase(";NNN1,EEE3;11sSSSWW1s;W,S,0,0,0,0", 104);
        testSingleCase(";NNN3,WWW2;EESSSFFF;E,E,0,0,0,0", 130);
    }

    @Test
    public void testLittleThreeDragons() throws ParseException {
        testSingleCase(";PPP1,999s3;345mCFFFC;S,N,0,0,0,0", 67);
        testSingleCase(";CCC1,PPP3,111p2;999pFF;S,N,1,0,0,0", 104);
        testSingleCase(";EEE3,CCC2;NNFFFPPN;S,W,0,0,0,0", 128);
    }

    @Test
    public void testAllHonors() throws ParseException {
        testSingleCase(";EEE1,SSS3,FFF2;WWNNW;W,W,1,0,0,0", 135);
        testSingleCase(";;EESSWWNNPPFFCC;W,W,1,0,0,0", 89);
    }

    @Test
    public void testFourConcealedPungs() throws ParseException {
        testSingleCase(";;111444888m888s55p;W,W,0,0,0,0", 69);
        testSingleCase(";;999s11557779991p;W,W,1,0,0,0", 72);
    }

    @Test
    public void testPureTerminalChows() throws ParseException {
        testSingleCase(";123p2;12355778998p;W,W,1,0,0,0", 66);
        testSingleCase(";;11223355778899s;W,W,1,0,0,0", 68);
    }

    @Test
    public void testQuadrupleChow() throws ParseException {
        testSingleCase(";456s1,456s2,456s2;55m456s;W,W,1,1,0,0", 96);
        testSingleCase(";234s0;22333444sFF2s;W,W,1,0,0,0", 137);
    }

    @Test
    public void testFourPureShiftedPungs() throws ParseException {
        testSingleCase(";111s1,222s1,444s3;333s22p;W,W,0,0,0,0", 63);
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
