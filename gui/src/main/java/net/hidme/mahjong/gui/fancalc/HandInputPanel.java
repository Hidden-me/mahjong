package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.icon.MahjongAtlas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Enumeration;

import static net.hidme.mahjong.gui.text.Localization.*;

public class HandInputPanel extends JPanel {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);
    private static final String CMD_TILE = "cmd_tile";
    private static final String CMD_CHOW = "cmd_chow";
    private static final String CMD_PUNG = "cmd_pung";
    private static final String CMD_MELDED_KONG = "cmd_melded_kong";
    private static final String CMD_CONCEALED_KONG = "cmd_concealed_kong";

    public HandInputPanel(FanCalcPanel fanCalcPanel, ConcurrentHand hand) {
        this.hand = hand;
        this.fanCalcPanel = fanCalcPanel;
        setBorder(new EmptyBorder(20, 50, 20, 50));
        setLayout(new BorderLayout());
        // input mode buttons
        inputModes = new ButtonGroup();
        final JPanel modePanel = createInputModePanel(inputModes);
        add(modePanel, BorderLayout.WEST);
        // tile buttons
        final JPanel buttonPanel = createButtonPanel();
        add(buttonPanel);
        // extra options
        optionPanel = new OptionPanel();
        add(optionPanel, BorderLayout.EAST);
    }

    private ConcurrentHand hand;
    private final FanCalcPanel fanCalcPanel;
    private final ButtonGroup inputModes;
    private final OptionPanel optionPanel;

    private JPanel createInputModePanel(ButtonGroup inputModes) {
        final JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridLayout(6, 1));
        final JLabel modeTitle = new JLabel(text(KEY_FAN_CALC_TITLE_INPUT_MODE));
        modeTitle.setFont(DEFAULT_FONT);
        final JRadioButton tileButton = createInputModeButton(inputModes, text(KEY_FAN_CALC_BUTTON_TILE), CMD_TILE);
        final JRadioButton chowButton = createInputModeButton(inputModes, text(KEY_FAN_CALC_BUTTON_CHOW), CMD_CHOW);
        final JRadioButton pungButton = createInputModeButton(inputModes, text(KEY_FAN_CALC_BUTTON_PUNG),CMD_PUNG);
        final JRadioButton meldedKongButton = createInputModeButton(inputModes, text(KEY_FAN_CALC_BUTTON_MELDED_KONG), CMD_MELDED_KONG);
        final JRadioButton concealedKongButton = createInputModeButton(inputModes, text(KEY_FAN_CALC_BUTTON_CONCEALED_KONG), CMD_CONCEALED_KONG);
        modePanel.add(modeTitle);
        modePanel.add(tileButton);
        modePanel.add(chowButton);
        modePanel.add(pungButton);
        modePanel.add(meldedKongButton);
        modePanel.add(concealedKongButton);
        tileButton.setSelected(true);
        return modePanel;
    }

    private JRadioButton createInputModeButton(ButtonGroup inputModes, String text, String cmd) {
        final JRadioButton button = new JRadioButton(text);
        button.setFont(DEFAULT_FONT);
        button.setActionCommand(cmd);
        inputModes.add(button);
        return button;
    }

    private JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(new EmptyBorder(40, 20, 20, 20));
        int row = 0, col = 0;
        // tile buttons
        for (Tile tile : Tile.values()) {
            // skip flower tiles
            if (tile.isFlower()) continue;
            // for each tile, create a button
            final JButton button = createTileButton(tile);
            final GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = col;
            gbc.gridy = row;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.BOTH;
            buttonPanel.add(button, gbc);
            if (++col == 9) {
                row++;
                col = 0;
            }
        }
        // the "clear" button
        final JButton clearButton = createClearButton();
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        buttonPanel.add(clearButton, gbc);
        return buttonPanel;
    }

    private JButton createTileButton(Tile tile) {
        final JButton button = new JButton(MahjongAtlas.getMahjongIcon(tile));
        button.addActionListener(e -> {
            for (Enumeration<AbstractButton> buttons = inputModes.getElements(); buttons.hasMoreElements();) {
                AbstractButton buttonInGroup = buttons.nextElement();
                if (buttonInGroup.isSelected()) {
                    final String cmd = buttonInGroup.getActionCommand();
                    switch (cmd) {
                        case CMD_TILE -> hand.addTile(tile);
                        case CMD_CHOW -> {
                            if (tile.isNumber()) {
                                final Tile startTile = tile.number <= 7 ? tile : Tile.getInstance(7, tile.suit);
                                hand.addClaim(new Claim(Claim.Type.CHOW, startTile, 0, 3));
                            }
                        }
                        case CMD_PUNG -> hand.addClaim(new Claim(Claim.Type.PUNG, tile, 0, 2));
                        case CMD_MELDED_KONG -> hand.addClaim(new Claim(Claim.Type.KONG, tile, 0, 2));
                        case CMD_CONCEALED_KONG -> hand.addClaim(new Claim(Claim.Type.KONG, tile, 0, 0));
                    }
                }
            }
            fanCalcPanel.onHandUpdate();
        });
        return button;
    }

    private JButton createClearButton() {
        final JButton button = new JButton(text(KEY_FAN_CALC_BUTTON_CLEAR));
        button.setFont(DEFAULT_FONT);
        button.addActionListener(e -> {
            hand.clear();
            fanCalcPanel.onHandUpdate();
        });
        return button;
    }

}
