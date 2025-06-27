package net.hidme.mahjong.gui.common;

import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.icon.MahjongAtlas;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TileLabel extends JLabel {

    private static final Color COLOR_BACK = new Color(53, 122, 171);

    public TileLabel() {
        this(null, false);
    }

    public TileLabel(Tile tile, boolean sideways) {
        this(tile, sideways, false);
    }

    public TileLabel(Tile tile, boolean sideways, boolean concealed) {
        setOpaque(true);
        setTile(tile, sideways, concealed);
    }

    public void setTile(Tile tile) {
        setTile(tile, false);
    }

    public void setTile(Tile tile, boolean sideways) {
        setTile(tile, sideways, false);
    }

    public void setTile(Tile tile, boolean sideways, boolean concealed) {
        if (tile == null) {
            setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        } else {
            setBorder(new LineBorder(Color.BLACK));
        }
        if (concealed) {
            setBackground(COLOR_BACK);
            final Icon icon = sideways ?
                    MahjongAtlas.getMahjongIconSideways(null) :
                    MahjongAtlas.getMahjongIcon(null);
            setIcon(icon);
        } else {
            setBackground(Color.WHITE);
            final Icon icon = sideways ?
                    MahjongAtlas.getMahjongIconSideways(tile) :
                    MahjongAtlas.getMahjongIcon(tile);
            setIcon(icon);
        }
    }

}
