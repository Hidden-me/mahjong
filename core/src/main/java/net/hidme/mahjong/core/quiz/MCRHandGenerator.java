package net.hidme.mahjong.core.quiz;

import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.MCRHandParser;

import java.text.ParseException;

public class MCRHandGenerator {

    public MCRHand generate() {
        // TODO: Impl
        final MCRHandParser parser = new MCRHandParser();
        final MCRHand hand;
        try {
            hand = parser.parse("12345678f;FFFF2;678999m22sCC2s;E,S,1,0,0,0");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return hand;
    }

}
