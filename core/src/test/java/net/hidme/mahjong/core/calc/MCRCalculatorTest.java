package net.hidme.mahjong.core.calc;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.hidme.mahjong.core.data.Hand;
import net.hidme.mahjong.core.data.MCRFan;
import net.hidme.mahjong.core.data.MCRHandParser;
import net.hidme.mahjong.core.data.MCRResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Map;

import static net.hidme.mahjong.core.data.MCRFan.*;

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
            // 32
            testFourPureShiftedChows();
            testThreeKongs();
            testPureTerminalsAndHonors();
            // 24
            testSevenPairs();
            testGreaterHonorsAndKnittedTiles();
            testAllEvenPungs();
            testFullFlush();
            testPureTripleChow();
            testPureShiftedPungs();
            testUpperTiles();
            testMiddleTiles();
            testLowerTiles();
            // 16
            testPureStraight();
            testThreeSuitedTerminalChows();
            testPureShiftedChows();
            testAllFive();
            testTriplePung();
            testThreeConcealedPungs();
            // 12
            testLesserHonorsAndKnittedTiles();
            testKnittedStraight();
            testUpperFour();
            testLowerFour();
            testBigThreeWinds();
            // 8
            testMixedStraight();
            testReversibleTiles();
            testMixedTripleChow();
            testMixedShiftedPungs();
            testChickenHand();
            testLastTileDraw();
            testLastTileClaim();
            testOutWithReplacementTile();
            testRobbingTheKong();
            // 6
            testAllPungs();
            testHalfFlush();
            testMixedShiftedChows();
            testAllTypes();
            testMeldedHand();
            testTwoConcealedKongs();
            testTwoDragonPungs();
            // 5
            testConcealedAndMeldedKongs();
            // 4
            testOutsideHand();
            testFullyConcealedHand();
            testTwoMeldedKongs();
            testLastTile();
            // 2
            testDragonPung();
            testPrevalentWind();
            testSeatWind();
            testConcealedHand();
            testAllChows();
            testTileHog();
            testDoublePung();
            testTwoConcealedPungs();
            testConcealedKong();
            testAllSimples();
            // 1
            testTwoChowFans();
            testPungOfTerminalsOrHonors();
            testMeldedKong();
            testOneVoidedSuit();
            testNoHonors();
            testUniqueWait();
            testSelfDrawn();
            testFlowerTiles();
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
        testSingleCase(";;11123456789998m;S,W,0,0,0,0", 92);
        testSingleCase(";;11123456789997p;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789996s;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789995m;E,E,0,0,0,0", 91);
        testSingleCase(";;11123456789994p;S,W,0,0,0,0", 89);
        testSingleCase(";;11123456789993s;E,E,0,0,0,0", 89);
        testSingleCase(";;11123456789992m;E,E,0,0,0,0", 92);
        testSingleCase(";;11123456789991p;E,E,0,0,0,0", 106);
        testSingleCase(";123s1;11456789999s;S,W,0,0,0,0", 43);
        testSingleCase(";;11112345678999s;E,E,0,0,0,0", 45);
    }

    @Test
    public void testFourKongs() throws ParseException {
        testSingleCase(";2222s1,3333s1,6666p1,4444m0;EE;E,E,0,0,0,0", 88);
        testSingleCase(";2222s0,3333s0,6666p0,4444m0;EE;E,E,0,0,0,0", 152);
        testSingleCase(";2222s0,3333s0,6666p0,4444m1;EE;S,W,0,0,0,0", 104);
    }

    @Test
    public void testSevenShiftedPairs() throws ParseException {
        testSingleCase(";;11223344556677s;S,W,0,0,0,0", 88);
        testSingleCase(";;12233445566771s;E,E,0,0,0,0", 88);
        testSingleCase(";;22334455667788s;E,E,1,0,0,0", 91);
    }

    @Test
    public void testThirteenOrphans() throws ParseException {
        testSingleCase(";;19m19p19sESWNPFC1s;S,W,0,0,0,0", 88);
        testSingleCase(";;19m19p11sESWNPFC9s;E,E,0,0,0,0", 88);
        testSingleCase(";;19m119p19sESWNPFC;E,E,1,0,0,0", 89);
    }

    @Test
    public void testAllTerminals() throws ParseException {
        testSingleCase(";111m1;1199s111999p1s;S,W,1,0,0,0", 97);
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
    public void testFourPureShiftedChows() throws ParseException {
        testSingleCase(";123m2,345m1;67789mCC5m;W,W,1,0,0,0", 39);
        testSingleCase(";123p0,234p1;9s344556p9s;W,W,0,0,0,0", 36);
    }

    @Test
    public void testThreeKongs() throws ParseException {
        testSingleCase(";2222s1,3333s1,6666p1;444m99s;E,E,0,0,0,0", 40);
        testSingleCase(";2222s0,3333s1,6666p2;444m99s;E,E,0,0,0,0", 42);
        testSingleCase(";2222s0,3333s0,6666p3;444m99s;S,W,0,0,0,0", 56);
        testSingleCase(";2222s0,3333s0,6666p0;444m99s;E,E,0,0,0,0", 98);
        testSingleCase(";2222s0,3333s0,6666p0;44m99s4m;E,E,0,0,0,0", 57);
        testSingleCase(";2222s0,3333s0,6666p0;44m99s4m;E,E,1,0,0,0", 98);
    }

    @Test
    public void testPureTerminalsAndHonors() throws ParseException {
        testSingleCase(";NNN2,999s1,CCC1;999m11p;E,E,1,0,0,0", 44);
        testSingleCase(";;99m11pSWWNNPPFFS;S,W,0,0,0,0", 57);
    }

    @Test
    public void testSevenPairs() throws ParseException {
        testSingleCase(";;EE22334455s66p77m;S,W,0,0,0,0", 24);
        testSingleCase(";;223344667788sEE;E,E,1,0,0,0", 31);
        testSingleCase(";;223344667788s11p;E,E,1,0,0,0", 27);
        testSingleCase(";;2233m44667788sEE;E,E,1,0,0,0", 26);
        testSingleCase(";;EEEE334455s66p77m;S,W,0,0,0,0", 26);
    }

    @Test
    public void testGreaterHonorsAndKnittedTiles() throws ParseException {
        testSingleCase(";;25m36p47sESWNPFC1s;E,E,0,0,0,0", 24);
        testSingleCase(";;258m9p47sESWNPFC1s;S,W,0,0,0,0", 24);
        testSingleCase(";;258m147sESWNPFC9p;E,E,0,0,0,0", 24);
        testSingleCase("48f;;25m36p47sESWNPFC1s;E,E,0,0,0,0", 26);
        testSingleCase(";;14m369p25sESWNFPC;E,E,1,0,0,0", 25);
    }

    @Test
    public void testAllEvenPungs() throws ParseException {
        testSingleCase(";444m1,666m3;22244s88p4s;E,E,0,0,0,0", 26);
        testSingleCase(";444m1,666m3;22244s88p4s;E,E,1,0,0,0", 29);
        testSingleCase(";444m1,666m3;222444s88p;S,W,1,0,0,0", 30);
        testSingleCase(";444m1,666m3;222444s77p;E,E,0,0,0,0", 13);
        testSingleCase(";;22224444s666688p;E,E,0,0,0,0", 33);
    }

    @Test
    public void testFullFlush() throws ParseException {
        testSingleCase(";123s2,333s1;45678996s;S,W,0,0,0,0", 27);
        testSingleCase(";;11224455668899p;E,E,1,0,0,0", 49);
    }

    @Test
    public void testPureTripleChow() throws ParseException {
        testSingleCase(";456p1,456p0;77m456pCCC;S,W,0,0,0,0", 27);
        testSingleCase(";789s0;44778899s567p;E,E,0,0,0,0", 27);
        testSingleCase(";;22233344m234s88p4m;E,E,0,0,0,0", 31);
    }

    @Test
    public void testPureShiftedPungs() throws ParseException {
        testSingleCase(";666p1;678s77888pWW7p;S,W,0,0,0,0", 25);
        testSingleCase(";234s1,111m1,222m2;33m11s3m;E,E,1,0,0,0", 39);
        testSingleCase(";;222333444m234s88p;E,E,1,0,0,0", 47);
        testSingleCase(";;22233344m234s88p4m;E,E,1,0,0,0", 46);
        testSingleCase(";;22233344m222s88p4m;E,E,0,0,0,0", 52);
    }

    @Test
    public void testUpperTiles() throws ParseException {
        testSingleCase(";;778899m777s89997p;S,W,1,0,0,0", 30);
        testSingleCase(";789m0,999s1;789s78999p;E,E,0,0,0,0", 39);
    }

    @Test
    public void testMiddleTiles() throws ParseException {
        testSingleCase(";;44666m456s45556p4m;S,W,1,0,0,0", 31);
        testSingleCase(";444m2,666m1;4466s444p4s;E,E,0,0,0,0", 64);
    }

    @Test
    public void testLowerTiles() throws ParseException {
        testSingleCase(";111p1,111s2,222s1;11m33s1m;S,W,1,0,0,0", 50);
        testSingleCase(";;112m33s11223333p2m;E,E,0,0,0,0", 50);
    }

    @Test
    public void testPureStraight() throws ParseException {
        testSingleCase(";;12345566789s22p4s;E,E,1,0,0,0", 24);
        testSingleCase(";123m1,789m0;888s46m88p5m;S,W,0,0,0,0", 18);
    }

    @Test
    public void testThreeSuitedTerminalChows() throws ParseException {
        testSingleCase(";123m0,789m1,123p2;55s897p;S,W,1,0,0,0", 18);
        testSingleCase(";123m0,789m1,123p2,789p0;55s;E,E,0,0,0,0", 22);
    }

    @Test
    public void testPureShiftedChows() throws ParseException {
        testSingleCase(";123m0,345m0;567m34sCC5s;S,W,0,0,0,0", 18);
        testSingleCase(";234p0;12334567pEE5p;E,E,1,0,0,0", 24);
    }

    @Test
    public void testAllFive() throws ParseException {
        testSingleCase(";345m2;567m34s45556p5s;S,W,0,0,0,0", 25);
        testSingleCase(";345m2;567m3455s456m5s;E,E,0,0,0,0", 36);
        testSingleCase(";345m2;455667m3455s5s;E,E,0,0,0,0", 36);
        testSingleCase(";456s1;34555m567pPP5m;E,E,0,0,0,0", 8);
    }

    @Test
    public void testTriplePung() throws ParseException {
        testSingleCase(";1111p1,1111m0;11134445s;E,E,1,0,0,0", 28);
        testSingleCase(";1111p0,1111m0,1111s0;999s99p;E,E,1,0,0,0", 178);
    }

    @Test
    public void testThreeConcealedPungs() throws ParseException {
        testSingleCase(";;44m77s444567pFFF7s;S,W,1,0,0,0", 22);
        testSingleCase(";;1156667m222s888p6m;E,E,0,0,0,0", 21);
        testSingleCase(";;444666888s33777p;E,E,0,0,0,0", 27);
    }

    @Test
    public void testLesserHonorsAndKnittedTiles() throws ParseException {
        testSingleCase(";;147m369p25sESWFP8s;S,W,1,0,0,0", 25);
        testSingleCase(";;258m36p47sESWNPF1s;E,E,0,0,0,0", 12);
        testSingleCase(";;258m36p47sESWPFC1s;E,E,0,0,0,0", 12);
        testSingleCase(";;25m36p47sESWNPFC1m;E,E,0,0,0,0", 0);
        testSingleCase("1f;;25m36p47mESWNPFC1m;E,E,0,0,0,0", 0);
    }

    @Test
    public void testKnittedStraight() throws ParseException {
        testSingleCase(";;369m25s147pSWNFP8s;S,W,0,0,0,0", 24);
        testSingleCase(";234p1;258m1478s369p8s;E,E,0,0,0,0", 15);
        testSingleCase(";;12355s258m147s369p;E,E,0,0,0,0", 16);
        testSingleCase(";;123s258m147s36999p;E,E,0,0,0,0", 17);
        testSingleCase(";;33s258m14789s369p7s;E,E,0,0,0,0", 17);
        testSingleCase(";;44s258m13457s369p4s;E,E,0,0,0,0", 19);
    }

    @Test
    public void testUpperFour() throws ParseException {
        testSingleCase(";678p0;789m6777s999p6s;E,E,0,0,0,0", 15);
        testSingleCase(";666m1,777m3,999m2;78886m;E,E,0,1,0,0", 45);
    }

    @Test
    public void testLowerFour() throws ParseException {
        testSingleCase(";234m0,234m0;12333s342p;E,E,0,0,0,0", 16);
        testSingleCase(";123p0;22244s23334p4s;E,E,1,0,0,0", 25);
    }

    @Test
    public void testBigThreeWinds() throws ParseException {
        testSingleCase(";EEE1,SSS3;78m55pWWW9m;N,N,1,0,0,0", 14);
        testSingleCase(";EEE3,SSS2;99m55pWWW9m;N,N,1,0,0,0", 23);
        testSingleCase(";EEE3,SSS2;99m55pWWW9m;E,E,1,0,0,0", 27);
        testSingleCase(";EEE3,SSS2;99m55pWWW9m;S,S,1,0,0,0", 27);
        testSingleCase(";EEE3,SSS2;99m55pWWW9m;W,W,1,0,0,0", 27);
        testSingleCase(";EEE3,SSS2;99m55pWWW9m;E,S,1,0,0,0", 27);
        testSingleCase(";EEE3,SSS2;99m55pWWW9m;E,N,1,0,0,0", 25);
    }

    @Test
    public void testMixedStraight() throws ParseException {
        testSingleCase(";;123456m1178s456p9s;E,E,0,0,0,0", 13);
        testSingleCase(";456s1,567p0;6678m123p9m;E,E,0,0,0,0", 10);
        testSingleCase(";888p1,789m0;123s45pPP6p;S,W,0,0,0,0", 8);
    }

    @Test
    public void testReversibleTiles() throws ParseException {
        testSingleCase(";456s0,123p0,888p1;55s231p;E,E,0,0,0,0", 10);
        testSingleCase(";234p1;8899s123345p9s;E,E,0,0,0,0", 26);
        testSingleCase(";;223344558pPPPP8p;S,W,1,0,0,0", 41);
    }

    @Test
    public void testMixedTripleChow() throws ParseException {
        testSingleCase(";567m1;11m566778s567p;N,E,0,0,0,0", 10);
        testSingleCase(";234m0;23344s11234p2s;S,W,0,0,0,0", 23);
    }

    @Test
    public void testMixedShiftedPungs() throws ParseException {
        testSingleCase(";555m1;678m666s77997p;N,E,0,0,0,0", 9);
        testSingleCase(";444s3;333m55pWWCCC5p;S,W,1,0,0,0", 39);
    }

    @Test
    public void testChickenHand() throws ParseException {
        testSingleCase(";123m0,345m0;56s678pWW7s;N,E,0,0,0,0", 8);
        testSingleCase(";888p1,789m0;123s45pPP3p;S,W,0,0,0,0", 8);
    }

    @Test
    public void testLastTileDraw() throws ParseException {
        testSingleCase(";123m0,345m0;56s678pWW7s;N,E,1,0,1,0", 8);
        testSingleCase(";888p1,789m0;123s45pPP3p;S,W,1,0,1,0", 8);
    }

    @Test
    public void testLastTileClaim() throws ParseException {
        testSingleCase(";123m0,345m0;56s678pWW7s;N,E,0,0,1,0", 8);
        testSingleCase(";888p1,789m0;123s45pPP3p;S,W,0,0,1,0", 8);
    }

    @Test
    public void testOutWithReplacementTile() throws ParseException {
        testSingleCase(";1111m0,345m0;56s678pWW7s;N,E,1,0,0,1", 11);
        testSingleCase(";8888p1,789m0;123s45pPP3p;S,W,1,0,0,1", 9);
    }

    @Test
    public void testRobbingTheKong() throws ParseException {
        testSingleCase(";1111m0,345m0;56s678pWW7s;N,E,0,1,0,1", 11);
        testSingleCase(";8888p1,789m0;123s45pPP3p;S,W,0,1,0,1", 9);
    }

    @Test
    public void testAllPungs() throws ParseException {
        testSingleCase(";333p1,444s2;11188mSS8m;N,E,1,0,0,0", 10);
    }

    @Test
    public void testHalfFlush() throws ParseException {
        testSingleCase(";FFF3,789s2;11345sNN1s;N,E,0,0,0,0", 9);
        testSingleCase(";CCC1,789m0;12345mEE6m;N,E,1,0,0,0", 25);
        testSingleCase(";;112255889pPPPP9p;S,W,0,0,0,0", 40);
    }

    @Test
    public void testMixedShiftedChows() throws ParseException {
        testSingleCase(";234p0;23m345s45677p1m;N,E,0,0,0,0", 8);
        testSingleCase(";CCCC0;567m68s456pSS7s;S,W,0,0,0,0", 19);
    }

    @Test
    public void testAllTypes() throws ParseException {
        testSingleCase(";678m2,444s1,FFF2;234pWW;N,E,1,0,0,0", 10);
        testSingleCase(";;3355m55s4477pNNCC;N,E,0,0,0,0", 30);
        testSingleCase(";PPP1;147m39s258pEE6s;N,E,0,0,0,0", 20);
    }

    @Test
    public void testMeldedHand() throws ParseException {
        testSingleCase(";234m0,345p1,234s0,CCCC1;11m;N,E,0,0,0,0", 10);
    }

    @Test
    public void testTwoConcealedKongs() throws ParseException {
        testSingleCase(";PPPP0,1111s0,345m2;67899p;N,E,0,0,0,0", 9);
        testSingleCase(";6666s0,5555p0;55777m345s;N,E,1,0,0,0", 36);
    }

    @Test
    public void testTwoDragonPungs() throws ParseException {
        testSingleCase(";PPP3,FFF1;999s33354p;N,E,1,0,0,0", 9);
    }

    @Test
    public void testConcealedAndMeldedKongs() throws ParseException {
        testSingleCase(";2222s0,3333s1,666p2;444m99s;E,E,0,0,0,0", 15);
    }

    @Test
    public void testOutsideHand() throws ParseException {
        testSingleCase(";123p0,CCC2;999m79998p;N,E,1,0,0,0", 10);
    }

    @Test
    public void testFullyConcealedHand() throws ParseException {
        testSingleCase(";;234678m44556s55p3s;N,E,1,0,0,0", 8);
        testSingleCase(";;12789m33s234999p3m;E,E,1,0,0,0", 8);
        testSingleCase(";;234678m22s55pFFF5p;W,S,1,0,0,0", 8);
    }

    @Test
    public void testTwoMeldedKongs() throws ParseException {
        testSingleCase(";PPPP2,2222s1;456s24663p;N,E,0,0,0,0", 8);
        testSingleCase(";CCC1,4444p3,4444m2;11133m;N,E,0,0,0,0", 16);
    }

    @Test
    public void testLastTile() throws ParseException {
        testSingleCase(";123p0,888m1;67m678sFF8m;N,E,1,1,0,0", 8);
    }

    @Test
    public void testDragonPung() throws ParseException {
        testSingleCase(";PPP2,789m2;34s123pSS5s;N,E,0,0,0,0", 8);
        testSingleCase(";;3368m345678pFFF7m;N,E,0,0,0,0", 8);
    }

    @Test
    public void testPrevalentWind() throws ParseException {
        testSingleCase(";EEEE0;888m789s45674p;E,S,0,0,0,0", 8);
        testSingleCase(";;234567m66789sNNN;N,N,0,0,0,0", 8);
    }

    @Test
    public void testSeatWind() throws ParseException {
        testSingleCase(";SSS3;345m11567s456p;N,S,0,0,0,0", 8);
        testSingleCase(";WWW2,FFF1;78m44678p6m;W,W,0,0,0,0", 8);
    }

    @Test
    public void testConcealedHand() throws ParseException {
        testSingleCase(";;234567m66s345678p;N,E,0,0,0,0", 8);
        testSingleCase(";;33m12456s123456p3s;E,E,0,0,0,0", 8);
        testSingleCase(";;345678999m22354p;W,S,0,0,0,0", 8);
    }

    @Test
    public void testAllChows() throws ParseException {
        testSingleCase(";;345m12344s234576p;N,E,0,0,0,0", 12);
        testSingleCase(";345p0;345567m55s687p;E,E,1,0,0,0", 8);
        testSingleCase(";345p0;345m567sWW687p;E,E,1,0,0,0", 4);
    }

    @Test
    public void testTileHog() throws ParseException {
        testSingleCase(";999m1;22789m89sCCC7s;N,E,0,0,0,0", 8);
        testSingleCase(";111s2,123s1;33455553s;E,E,1,0,0,0", 32);
        testSingleCase(";;22448p55m88sPPPP8p;E,E,1,0,0,0", 27);
    }

    @Test
    public void testDoublePung() throws ParseException {
        testSingleCase(";234m1,555m1;567m55566s;N,E,0,0,0,0", 8);
        testSingleCase(";999p2,999s3;35s22345p4s;E,E,0,0,0,0", 8);
        testSingleCase(";222m1,555m3;8m222555s8m;E,E,0,0,0,0", 16);
    }

    @Test
    public void testTwoConcealedPungs() throws ParseException {
        testSingleCase(";;1235789m666888s5m;N,E,0,0,0,0", 8);
    }

    @Test
    public void testConcealedKong() throws ParseException {
        testSingleCase(";CCCC0;56m888s11234p7m;N,E,0,0,0,0", 8);
    }

    @Test
    public void testAllSimples() throws ParseException {
        testSingleCase(";345m0;22256s22456p7s;N,E,0,0,0,0", 8);
        testSingleCase(";;44558m225566s22p8m;S,W,1,0,0,0", 27);
    }

    @Test
    public void testTwoChowFans() throws ParseException {
        testSingleCase(";234p1;22667788s243p;N,E,0,0,0,0", 8);
        testSingleCase(";;234678m55s234678p;N,E,0,0,0,0", 8);
        testSingleCase(";234s0,567s0;88s345687p;N,E,0,0,0,0", 8);
        testSingleCase(";123m2;55789m127893s;N,E,1,0,0,0", 8);
    }

    @Test
    public void testPungOfTerminalsOrHonors() throws ParseException {
        testSingleCase(";WWW1,NNNN2,111p3;55m999s;E,S,1,0,0,0", 12);
    }

    @Test
    public void testMeldedKong() throws ParseException {
        testSingleCase(";9999s1,123p0,456s1;44s465p;E,S,1,0,0,0", 8);
    }

    @Test
    public void testOneVoidedSuit() throws ParseException {
        testSingleCase(";;55678m123457896p;E,S,1,0,0,0", 23);
        testSingleCase(";;556699m22334455p;E,S,0,0,0,0", 26);
    }

    @Test
    public void testNoHonors() throws ParseException {
        testSingleCase(";9999s0,123p1,456s1;44s465p;E,S,0,0,0,0", 8);
    }

    @Test
    public void testUniqueWait() throws ParseException {
        testSingleCase(";9999p0,123p1,456p1;12333s;E,S,0,0,0,0", 8);
        testSingleCase(";9999s0,123p1,123s1;77897p;E,S,0,0,0,0", 8);
        testSingleCase(";9999s0,123p1,123s1;66798p;E,S,0,0,0,0", 8);
        testSingleCase(";9999s0,123p1,123s1;66132p;E,S,0,0,0,0", 8);
        testSingleCase(";9999s0,123p1,123s1;66243p;E,S,0,0,0,0", 7);
        testSingleCase(";9999s0,123p1,123s1;66354p;E,S,0,0,0,0", 7);
        testSingleCase(";9999s0,123p1,123s1;33465p;E,S,0,0,0,0", 8);
        testSingleCase(";9999s0,123p1,123s1;66576p;E,S,0,0,0,0", 7);
        testSingleCase(";9999s0,123p1,123s1;44687p;E,S,0,0,0,0", 7);
        testSingleCase(";9999s0,123p1,123s1;77798p;E,S,0,0,0,0", 7);
        testSingleCase(";FFF1,234p0;234mNPPPN;E,S,0,0,0,0", 9);
        testSingleCase(";9999s0,123s1,123s1,123s1;44s;E,S,0,0,0,0", 52);
        // specific type of unique wait
        testSingleCase(";456s0,567s2,678s0;22354s;E,S,0,0,0,0", CLOSED_WAIT);
        testSingleCase(";456s0,567s2,678s0;12883s;E,S,0,0,0,0", EDGE_WAIT);
        testSingleCase(";456s0,567s2,678s0;11897s;E,S,0,0,0,0", EDGE_WAIT);
        testSingleCase(";456s0,567s2,678s0;12355s;E,S,0,0,0,0", SINGLE_WAIT);
    }

    @Test
    public void testSelfDrawn() throws ParseException {
        testSingleCase(";FFFF2;678999m22sCC2s;E,S,1,0,0,0", 8);
    }

    @Test
    public void testFlowerTiles() throws ParseException {
        testSingleCase("18f;FFFF2;678999m22sCC2s;E,S,1,0,0,0", 10);
        testSingleCase("12345678f;FFFF2;678999m22sCC2s;E,S,1,0,0,0", 16);
    }

    private void testSingleCase(String cas, int expected) throws ParseException {
        final MCRHandParser parser = new MCRHandParser();
        final Hand hand = parser.parse(cas);
        final MCRCalculator calculator = new MCRCalculator();
        MCRResult result = (MCRResult) calculator.calculate(hand);
        Assertions.assertEquals(expected, result.getFanTotal());
    }

    private void testSingleCase(String cas, MCRFan fanToContain) throws ParseException {
        final Multiset<MCRFan> expected = HashMultiset.create();
        expected.add(fanToContain);
        testSingleCase(cas, expected);
    }

    private void testSingleCase(String cas, Multiset<MCRFan> expectedToContain) throws ParseException {
        final MCRHandParser parser = new MCRHandParser();
        final Hand hand = parser.parse(cas);
        final MCRCalculator calculator = new MCRCalculator();
        MCRResult result = (MCRResult) calculator.calculate(hand);
        final Map<MCRFan, Integer> fans = result.getFanCombination();
        for (Multiset.Entry<MCRFan> entry : expectedToContain.entrySet()) {
            final MCRFan fan = entry.getElement();
            final int count = entry.getCount();
            Assertions.assertTrue(fans.containsKey(fan) && fans.get(fan) == count);
        }
    }

}
