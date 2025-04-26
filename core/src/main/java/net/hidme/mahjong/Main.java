package net.hidme.mahjong;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.calc.MCRCalculator;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.MCRHandParser;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
//        Multiset<Integer> set = TreeMultiset.create();
//        set.add(100);
//        set.add(100);
//        set.add(200);
//        for (Multiset.Entry<Integer> e : set.entrySet()) {
//            System.out.println(e);
//        }
        MCRHandParser parser = new MCRHandParser();
        MCRHand hand = parser.parse("12f;;2233m44668888sEE;E,E,1,0,0,0");
        MCRCalculator calculator = new MCRCalculator();
        System.out.println(calculator.calculate(hand));
    }
}