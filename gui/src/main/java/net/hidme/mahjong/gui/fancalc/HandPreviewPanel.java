package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.common.ClaimPanel;
import net.hidme.mahjong.gui.common.TileLabel;
import net.hidme.mahjong.gui.icon.MahjongAtlas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static net.hidme.mahjong.gui.text.Localization.textFontName;

public class HandPreviewPanel extends JPanel {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public HandPreviewPanel(FanCalcPanel fanCalcPanel, ConcurrentHand hand) {
        this.hand = hand;
        this.fanCalcPanel = fanCalcPanel;
        // claim slots
        claimSlots = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            final ClaimPanel claimPanel = new ClaimPanel(null);
            int finalI = i;
            claimPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    // remove the claim when clicked
                    hand.removeClaim(finalI);
                    fanCalcPanel.onHandUpdate();
                }
            });
            claimSlots.add(claimPanel);
            add(claimPanel);
        }
        // tile slots
        final JPanel tilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tileSlots = new LinkedList<>();
        for (int i = 0; i < 13; i++) {
            final TileLabel tileLabel = new TileLabel();
            int finalI = i;
            tileLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    // remove the tile when clicked
                    hand.removeTile(finalI);
                    fanCalcPanel.onHandUpdate();
                }
            });
            tileSlots.add(tileLabel);
            tilePanel.add(tileLabel);
        }
        add(tilePanel);
        // declared tile slot
        declaredTileSlot = new TileLabel();
        declaredTileSlot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // remove the tile when clicked
                hand.setDeclaredTile(null);
                fanCalcPanel.onHandUpdate();
            }
        });
        add(declaredTileSlot);
        // placeholder when hand is empty
        placeHolder = createPlaceHolder();
        add(placeHolder);
        onHandUpdate();
    }

    private final ConcurrentHand hand;
    private final FanCalcPanel fanCalcPanel;
    private final List<ClaimPanel> claimSlots;
    private final List<TileLabel> tileSlots;
    private final TileLabel declaredTileSlot;
    private final JLabel placeHolder;

    public void onHandUpdate() {
        // get updated model
        final HandViewModel viewModel = hand.getViewModel();
        final List<Claim> claims = viewModel.claims();
        final List<Tile> tiles = viewModel.tiles();
        final Tile declaredTile = viewModel.declaredTile();
        // update claim slots
        final Iterator<Claim> claimIterator = claims.iterator();
        for (ClaimPanel claimPanel : claimSlots) {
            if (claimIterator.hasNext()) {
                // show a slot for each claim
                final Claim claim = claimIterator.next();
                claimPanel.setClaim(claim);
                claimPanel.setVisible(true);
            } else {
                // the rest of slots are not shown
                claimPanel.setVisible(false);
            }
        }
        // update tile slots
        final Iterator<Tile> tileIterator = tiles.iterator();
        for (TileLabel tileLabel : tileSlots) {
            if (tileIterator.hasNext()) {
                // show a slot for each tile
                final Tile tile = tileIterator.next();
                tileLabel.setTile(tile);
                tileLabel.setVisible(true);
            } else {
                // the rest of slots are not shown
                tileLabel.setVisible(false);
            }
        }
        // update the slot for the declared tile
        if (declaredTile == null) {
            declaredTileSlot.setVisible(false);
        } else {
            declaredTileSlot.setTile(declaredTile);
            declaredTileSlot.setVisible(true);
        }
        // special case: empty hand
        placeHolder.setVisible(claims.isEmpty() && tiles.isEmpty() && declaredTile == null);
    }

    private JLabel createPlaceHolder() {
        final BufferedImage placeHolderImage = new BufferedImage(MahjongAtlas.MAHJONG_WIDTH * 14, MahjongAtlas.MAHJONG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = placeHolderImage.createGraphics();
        g.setFont(DEFAULT_FONT);
        g.setColor(Color.BLACK);
        final FontMetrics fm = g.getFontMetrics();
        final String str = "+";
        g.drawString(str,
                (placeHolderImage.getWidth() - fm.stringWidth(str)) / 2,
                (placeHolderImage.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
        g.dispose();
        final JLabel placeHolder = new JLabel(new ImageIcon(placeHolderImage));
        placeHolder.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        return placeHolder;
    }

}
