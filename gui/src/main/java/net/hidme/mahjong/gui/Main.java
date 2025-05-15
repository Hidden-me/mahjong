package net.hidme.mahjong.gui;

import net.hidme.mahjong.core.calc.MCRCalculator;
import net.hidme.mahjong.core.data.*;

import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println("Input: ");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        MCRHandParser parser = new MCRHandParser();
        MCRHand hand = parser.parse(s);
        MCRCalculator calculator = new MCRCalculator();
        MCRResult result = (MCRResult) calculator.calculate(hand);
        MCRFanTranslator translator = new MCRFanTranslator();
        Map<MCRFan, Integer> fans = result.getFanCombination();
        for (Map.Entry<MCRFan, Integer> entry : fans.entrySet()) {
            int count = entry.getValue();
            MCRFan fan = entry.getKey();
            System.out.println(translator.translate(fan) + " (" + fan.score + ")" + (count == 1 ? "" : (" * " + count)));
        }
        System.out.println("Total: " + result.getFanTotal());
    }
}