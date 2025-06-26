package net.hidme.mahjong.gui.fanquiz;

import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.Wind;

import javax.swing.*;
import java.awt.*;

import static net.hidme.mahjong.gui.text.Localization.*;
import static net.hidme.mahjong.gui.util.GridBagLayoutUtils.makeConstraint;

public class DetailPanel extends JPanel {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public DetailPanel() {
        setLayout(new GridBagLayout());
        pwEastButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_EAST));
        pwSouthButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_SOUTH));
        pwWestButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_WEST));
        pwNorthButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_NORTH));
        swEastButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_EAST));
        swSouthButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_SOUTH));
        swWestButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_WEST));
        swNorthButton = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_NORTH));
        selfDrawnBox = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_SELF_DRAWN));
        lastTileBox = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_LAST_TILE));
        lastDrawOrClaimBox = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_LAST_DRAW_OR_CLAIM));
        kongBox = createDetailButton(text(KEY_FAN_QUIZ_DETAIL_KONG));
        add(pwEastButton, makeConstraint(0, 0, 1, 1));
        add(pwSouthButton, makeConstraint(1, 0, 1, 1));
        add(pwWestButton, makeConstraint(2, 0, 1, 1));
        add(pwNorthButton, makeConstraint(3, 0, 1, 1));
        add(swEastButton, makeConstraint(0, 1, 1, 1));
        add(swSouthButton, makeConstraint(1, 1, 1, 1));
        add(swWestButton, makeConstraint(2, 1, 1, 1));
        add(swNorthButton, makeConstraint(3, 1, 1, 1));
        add(selfDrawnBox, makeConstraint(4, 0, 1, 2));
        add(lastTileBox, makeConstraint(5, 0, 1, 2));
        add(lastDrawOrClaimBox, makeConstraint(6, 0, 1, 2));
        add(kongBox, makeConstraint(7, 0, 1, 2));
    }

    public void updateDetails(MCRHand hand) {
        pwEastButton.setSelected(hand.prevalentWind == Wind.EAST);
        pwSouthButton.setSelected(hand.prevalentWind == Wind.SOUTH);
        pwWestButton.setSelected(hand.prevalentWind == Wind.WEST);
        pwNorthButton.setSelected(hand.prevalentWind == Wind.NORTH);
        swEastButton.setSelected(hand.seatWind == Wind.EAST);
        swSouthButton.setSelected(hand.seatWind == Wind.SOUTH);
        swWestButton.setSelected(hand.seatWind == Wind.WEST);
        swNorthButton.setSelected(hand.seatWind == Wind.NORTH);
        selfDrawnBox.setSelected(hand.selfDrawn);
        lastTileBox.setSelected(hand.lastTile);
        lastDrawOrClaimBox.setSelected(hand.lastDrawOrClaim);
        kongBox.setSelected(hand.kong);
    }

    private final JButton pwEastButton, pwSouthButton, pwWestButton, pwNorthButton,
            swEastButton, swSouthButton, swWestButton, swNorthButton,
            selfDrawnBox, lastTileBox, lastDrawOrClaimBox, kongBox;

    private JButton createDetailButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(DEFAULT_FONT);
        button.setEnabled(false);
        return button;
    }

}
