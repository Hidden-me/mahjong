package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.icon.MahjongAtlas;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TileLabel extends JLabel {

    public TileLabel() {
        this(null, false);
    }

    public TileLabel(Tile tile, boolean sideways) {
        setOpaque(true);
        setBackground(Color.WHITE);
        setTile(tile, sideways);
    }

    public void setTile(Tile tile) {
        setTile(tile, false);
    }

    public void setTile(Tile tile, boolean sideways) {
        final Icon icon = sideways ?
                MahjongAtlas.getMahjongIconSideways(tile) :
                MahjongAtlas.getMahjongIcon(tile);
        setIcon(icon);
        if (tile == null) {
            setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        } else {
            setBorder(new LineBorder(Color.BLACK));
        }
    }

}
