package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Wind;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import static net.hidme.mahjong.gui.text.Localization.*;
import static net.hidme.mahjong.gui.text.Localization.KEY_FAN_CALC_OPTION_KONG;
import static net.hidme.mahjong.gui.text.Localization.KEY_FAN_CALC_OPTION_LAST_DRAW_OR_CLAIM;
import static net.hidme.mahjong.gui.text.Localization.KEY_FAN_CALC_OPTION_LAST_TILE;
import static net.hidme.mahjong.gui.text.Localization.text;

public class OptionPanel extends JPanel {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public OptionPanel() {
        setLayout(new GridLayout(7, 1));
        final JLabel title = new JLabel(text(KEY_FAN_CALC_TITLE_OPTIONS));
        title.setFont(DEFAULT_FONT);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        prevalentWindComboBox = createWindComboBox();
        seatWindComboBox = createWindComboBox();
        final JPanel prevalentWindPanel = createWindOptionPanel(text(KEY_FAN_CALC_OPTION_PREVALENT_WIND), prevalentWindComboBox);
        final JPanel seatWindPanel = createWindOptionPanel(text(KEY_FAN_CALC_OPTION_SEAT_WIND), seatWindComboBox);
        selfDrawnBox = createOptionBox(text(KEY_FAN_CALC_OPTION_SELF_DRAWN));
        lastTileBox = createOptionBox(text(KEY_FAN_CALC_OPTION_LAST_TILE));
        lastDrawOrClaimBox = createOptionBox(text(KEY_FAN_CALC_OPTION_LAST_DRAW_OR_CLAIM));
        kongBox = createOptionBox(text(KEY_FAN_CALC_OPTION_KONG));
        add(title);
        add(prevalentWindPanel);
        add(seatWindPanel);
        add(selfDrawnBox);
        add(lastTileBox);
        add(lastDrawOrClaimBox);
        add(kongBox);
    }

    private final JComboBox<String> prevalentWindComboBox, seatWindComboBox;
    private final JCheckBox selfDrawnBox, lastTileBox, lastDrawOrClaimBox, kongBox;

    private JPanel createWindOptionPanel(String hint, JComboBox<String> comboBox) {
        final JPanel panel = new JPanel();
        final JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(DEFAULT_FONT);
        panel.add(hintLabel);
        panel.add(comboBox);
        return panel;
    }

    private JComboBox<String> createWindComboBox() {
        final JComboBox<String> comboBox = new JComboBox<>(new String[]{
                text(KEY_FAN_CALC_OPTION_WIND_EAST),
                text(KEY_FAN_CALC_OPTION_WIND_SOUTH),
                text(KEY_FAN_CALC_OPTION_WIND_WEST),
                text(KEY_FAN_CALC_OPTION_WIND_NORTH)
        });
        comboBox.setFont(DEFAULT_FONT);
        return comboBox;
    }

    private JCheckBox createOptionBox(String text) {
        final JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(DEFAULT_FONT);
        return checkBox;
    }

}
