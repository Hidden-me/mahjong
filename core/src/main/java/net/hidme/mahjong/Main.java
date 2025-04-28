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
//        set.add(100);
//        set.add(200);
//        set.remove(100);
//        for (Multiset.Entry<Integer> e : set.entrySet()) {
//            System.out.println(e);
//        }
//        System.out.println(set.size());
        MCRHandParser parser = new MCRHandParser();
        MCRCalculator calculator = new MCRCalculator();
        calculator.calculate(parser.parse("12f;;147m258p123469sEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;147m258p368sCCCEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;147m259p368sCCCEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;148m259p367sCCCEE;E,E,1,0,0,0"));

        calculator.calculate(parser.parse("12f;;369s12222358m147p;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;369s122358m11147p;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;369s122358m147pEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;147m258p369sCCCEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;258m147p369sPPPEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;;369s258m147p123mEE;E,E,1,0,0,0"));

        calculator.calculate(parser.parse("12f;;111122223333pEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;123p1;111222333pEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;123p1,123p1;112233pEE;E,E,1,0,0,0"));
        calculator.calculate(parser.parse("12f;123p1,111p1;222333pEE;E,E,1,0,0,0"));
    }
}