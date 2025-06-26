package net.hidme.mahjong.gui.fanquiz;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.common.ClaimPanel;
import net.hidme.mahjong.gui.fancalc.*;
import net.hidme.mahjong.gui.icon.MahjongAtlas;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import static net.hidme.mahjong.gui.text.Localization.textFontName;

public class HandViewPanel extends JPanel {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public HandViewPanel() {
        // claim slots
        claimSlots = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            final ClaimPanel claimPanel = new ClaimPanel(null);
            claimSlots.add(claimPanel);
            add(claimPanel);
        }
        // tile slots
        final JPanel tilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tileSlots = new LinkedList<>();
        for (int i = 0; i < 13; i++) {
            final TileLabel tileLabel = new TileLabel();
            tileSlots.add(tileLabel);
            tilePanel.add(tileLabel);
        }
        add(tilePanel);
        // declared tile slot
        declaredTileSlot = new TileLabel();
        add(declaredTileSlot);
        // placeholder when hand is empty
        placeHolder = createPlaceHolder();
        add(placeHolder);
        reset();
    }

    public void updateHand(MCRHand hand) {
        if (hand == null) {
            reset();
        } else {
            updateHandNonNull(hand);
        }
    }

    private final List<ClaimPanel> claimSlots;
    private final List<TileLabel> tileSlots;
    private final TileLabel declaredTileSlot;
    private final JLabel placeHolder;

    private void reset() {
        placeHolder.setVisible(true);
        for (ClaimPanel claimPanel : claimSlots) {
            claimPanel.setVisible(false);
        }
        for (TileLabel tileLabel : tileSlots) {
            tileLabel.setVisible(false);
        }
        declaredTileSlot.setVisible(false);
    }

    private void updateHandNonNull(MCRHand hand) {
        placeHolder.setVisible(false);
        // get updated model
        final Claim[] claims = hand.claims;
        final Tile[] tiles = hand.tiles;
        final Tile declaredTile = hand.declaredTile;
        // update claim slots
        int i = 0;
        final int claimCount = claims.length, tileCount = tiles.length;
        for (ClaimPanel claimPanel : claimSlots) {
            if (i < claimCount) {
                // show a slot for each claim
                final Claim claim = claims[i++];
                claimPanel.setClaim(claim);
                claimPanel.setVisible(true);
            } else {
                // the rest of slots are not shown
                claimPanel.setVisible(false);
            }
        }
        // update tile slots
        i = 0;
        for (TileLabel tileLabel : tileSlots) {
            if (i < tileCount) {
                // show a slot for each tile
                final Tile tile = tiles[i++];
                tileLabel.setTile(tile);
                tileLabel.setVisible(true);
            } else {
                // the rest of slots are not shown
                tileLabel.setVisible(false);
            }
        }
        // update the slot for the declared tile
        declaredTileSlot.setTile(declaredTile);
        declaredTileSlot.setVisible(true);
    }

    private JLabel createPlaceHolder() {
        final BufferedImage placeHolderImage = new BufferedImage(MahjongAtlas.MAHJONG_WIDTH * 14, MahjongAtlas.MAHJONG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = placeHolderImage.createGraphics();
        g.setFont(DEFAULT_FONT);
        g.setColor(Color.BLACK);
        g.dispose();
        final JLabel placeHolder = new JLabel(new ImageIcon(placeHolderImage));
        placeHolder.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        return placeHolder;
    }

}
