package net.hidme.mahjong.gui.common;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.icon.MahjongAtlas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClaimPanel extends JPanel {

    public ClaimPanel(Claim claim) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // one label for each tile in the claim
        slots = new LinkedList<>();
        struts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final TileLabel slot = new TileLabel();
            slots.add(slot);
            final Component strut = Box.createVerticalStrut(MahjongAtlas.MAHJONG_HEIGHT - MahjongAtlas.MAHJONG_WIDTH);
            struts.add(strut);
            // box:
            // +-----------------------------------------+
            // | strut (only visible for sideways tiles) |
            // +-----------------------------------------+
            // | slot                                    |
            // +-----------------------------------------+
            final JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.add(strut);
            box.add(slot);
            add(box);
        }
        setClaim(claim);
    }

    public void setClaim(Claim claim) {
        // empty (null) claim
        if (claim == null) {
            for (TileLabel slot : slots) {
                slot.setVisible(false);
            }
            return;
        }
        // get tiles in the claim
        final Tile[] tiles = claim.getTilesFancy();
        final int sidewaysTileIndex = getSidewaysTileIndex(claim);
        final int claimSize = claim.size();
        // given the claim tiles, update slots
        final boolean isConcealedKong = claim.type() == Claim.Type.KONG && claim.isConcealed();
        int i = 0;
        for (TileLabel slot : slots) {
            if (i >= claimSize) {
                slot.setVisible(false);
            } else {
                final boolean sideways = sidewaysTileIndex == i;
                slot.setTile(tiles[i], sideways, isConcealedKong && (i == 0 || i == 3));
                struts.get(i).setVisible(sideways);
                slot.setVisible(true);
            }
            i++;
        }
    }

    private final List<TileLabel> slots;
    private final List<Component> struts;  // for vertical alignment

    private int getSidewaysTileIndex(Claim claim) {
        return switch (claim.claimedFrom()) {
            case 0 -> -1;
            case 1 -> claim.size() - 1;
            case 2 -> 1;
            case 3 -> 0;
            default -> throw new IllegalArgumentException("Unexpected claim source: " + claim.claimedFrom());
        };
    }

}
