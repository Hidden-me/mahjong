package net.hidme;

import net.hidme.core.data.MCRHand;
import net.hidme.core.data.MCRHandParser;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        MCRHandParser parser = new MCRHandParser();
        MCRHand hand = parser.parse("12f;9999m0,432p2;11m234567891s;E,E,1,0,0,1");
        System.out.println(hand);
    }
}