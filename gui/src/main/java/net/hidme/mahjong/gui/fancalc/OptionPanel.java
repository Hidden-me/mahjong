package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Wind;

import javax.swing.*;

import java.awt.*;
import java.util.Enumeration;

import static net.hidme.mahjong.gui.text.Localization.*;
import static net.hidme.mahjong.gui.text.Localization.KEY_FAN_CALC_OPTION_KONG;
import static net.hidme.mahjong.gui.text.Localization.KEY_FAN_CALC_OPTION_LAST_DRAW_OR_CLAIM;
import static net.hidme.mahjong.gui.text.Localization.KEY_FAN_CALC_OPTION_LAST_TILE;
import static net.hidme.mahjong.gui.text.Localization.text;
import static net.hidme.mahjong.gui.util.GridBagLayoutUtils.makeConstraint;

public class OptionPanel extends JPanel {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);
    private static final String CMD_WIND_EAST = "cmd_wind_east";
    private static final String CMD_WIND_SOUTH = "cmd_wind_south";
    private static final String CMD_WIND_WEST = "cmd_wind_west";
    private static final String CMD_WIND_NORTH = "cmd_wind_north";

    public OptionPanel(FanCalcPanel fanCalcPanel) {
        this.fanCalcPanel = fanCalcPanel;
        setLayout(new GridBagLayout());
        prevalentWindGroup = new ButtonGroup();
        seatWindGroup = new ButtonGroup();
        final JToggleButton pwEastButton = createWindButton(text(KEY_FAN_CALC_OPTION_PREVALENT_WIND_EAST), CMD_WIND_EAST, prevalentWindGroup);
        final JToggleButton pwSouthButton = createWindButton(text(KEY_FAN_CALC_OPTION_PREVALENT_WIND_SOUTH), CMD_WIND_SOUTH, prevalentWindGroup);
        final JToggleButton pwWestButton = createWindButton(text(KEY_FAN_CALC_OPTION_PREVALENT_WIND_WEST), CMD_WIND_WEST, prevalentWindGroup);
        final JToggleButton pwNorthButton = createWindButton(text(KEY_FAN_CALC_OPTION_PREVALENT_WIND_NORTH), CMD_WIND_NORTH, prevalentWindGroup);
        final JToggleButton swEastButton = createWindButton(text(KEY_FAN_CALC_OPTION_SEAT_WIND_EAST), CMD_WIND_EAST, seatWindGroup);
        final JToggleButton swSouthButton = createWindButton(text(KEY_FAN_CALC_OPTION_SEAT_WIND_SOUTH), CMD_WIND_SOUTH, seatWindGroup);
        final JToggleButton swWestButton = createWindButton(text(KEY_FAN_CALC_OPTION_SEAT_WIND_WEST), CMD_WIND_WEST, seatWindGroup);
        final JToggleButton swNorthButton = createWindButton(text(KEY_FAN_CALC_OPTION_SEAT_WIND_NORTH), CMD_WIND_NORTH, seatWindGroup);
        pwEastButton.setSelected(true);
        swEastButton.setSelected(true);
        selfDrawnBox = createOptionButton(text(KEY_FAN_CALC_OPTION_SELF_DRAWN));
        lastTileBox = createOptionButton(text(KEY_FAN_CALC_OPTION_LAST_TILE));
        lastDrawOrClaimBox = createOptionButton(text(KEY_FAN_CALC_OPTION_LAST_DRAW_OR_CLAIM));
        kongBox = createOptionButton(text(KEY_FAN_CALC_OPTION_KONG));
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

    public Options getOptions() {
        boolean selfDrawn = selfDrawnBox.isSelected();
        boolean lastTile = lastTileBox.isSelected();
        boolean lastDrawOrClaim = lastDrawOrClaimBox.isSelected();
        boolean kong = kongBox.isSelected();
        Wind prevalentWind = getSelectedWind(prevalentWindGroup);
        Wind seatWind = getSelectedWind(seatWindGroup);
        return new Options(selfDrawn, lastTile, lastDrawOrClaim, kong, prevalentWind, seatWind);
    }

    private final FanCalcPanel fanCalcPanel;
    private final ButtonGroup prevalentWindGroup, seatWindGroup;
    private final JToggleButton selfDrawnBox, lastTileBox, lastDrawOrClaimBox, kongBox;

    private JToggleButton createWindButton(String text, String cmd, ButtonGroup group) {
        final JToggleButton button = new JToggleButton(text);
        button.setActionCommand(cmd);
        button.setFont(DEFAULT_FONT);
        button.setFocusable(false);
        button.addActionListener(e -> fanCalcPanel.onOptionUpdate());
        group.add(button);
        return button;
    }

    private JToggleButton createOptionButton(String text) {
        final JToggleButton button = new JToggleButton(text);
        button.setFont(DEFAULT_FONT);
        button.setFocusable(false);
        button.addActionListener(e -> fanCalcPanel.onOptionUpdate());
        return button;
    }

    private Wind getSelectedWind(ButtonGroup group) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton buttonInGroup = buttons.nextElement();
            if (buttonInGroup.isSelected()) {
                final String cmd = buttonInGroup.getActionCommand();
                return switch (cmd) {
                    case CMD_WIND_EAST -> Wind.EAST;
                    case CMD_WIND_SOUTH -> Wind.SOUTH;
                    case CMD_WIND_WEST -> Wind.WEST;
                    case CMD_WIND_NORTH -> Wind.NORTH;
                    default -> throw new IllegalStateException("Illegal wind command: " + cmd);
                };
            }
        }
        throw new IllegalStateException("No wind is selected");
    }

}
