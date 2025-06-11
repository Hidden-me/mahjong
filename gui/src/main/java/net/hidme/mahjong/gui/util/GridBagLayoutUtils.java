package net.hidme.mahjong.gui.util;

import java.awt.*;

public interface GridBagLayoutUtils {

    static GridBagConstraints makeConstraint(int x, int y, int width, int height) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

}
