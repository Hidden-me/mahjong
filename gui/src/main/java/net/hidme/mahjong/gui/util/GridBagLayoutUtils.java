package net.hidme.mahjong.gui.util;

import java.awt.*;

public interface GridBagLayoutUtils {

    static GridBagConstraints makeConstraint(int x, int y, int width, int height, double weightx, double weighty) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

    static GridBagConstraints makeConstraint(int x, int y, int width, int height) {
        return makeConstraint(x, y, width, height, 1, 1);
    }

}
