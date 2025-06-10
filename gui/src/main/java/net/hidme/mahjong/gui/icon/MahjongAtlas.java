package net.hidme.mahjong.gui.icon;

import net.hidme.mahjong.core.data.Tile;
import net.hidme.mahjong.gui.util.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static net.hidme.mahjong.core.data.Tile.*;

public class MahjongAtlas {

    public static final int MAHJONG_WIDTH = 45;
    public static final int MAHJONG_HEIGHT = 62;

    public static Icon getMahjongIcon(Tile tile) {
        if (tile == null) return EMPTY_TILE_ICON;
        return ICON_MAP.get(tile);
    }

    public static Icon getMahjongIconSideways(Tile tile) {
        if (tile == null) return SIDEWAYS_EMPTY_TILE_ICON;
        return SIDEWAYS_ICON_MAP.get(tile);
    }

    private static final String PATH = "/mahjong_atlas.png";
    private static final Map<Tile, Icon> ICON_MAP, SIDEWAYS_ICON_MAP;
    private static final Icon EMPTY_TILE_ICON, SIDEWAYS_EMPTY_TILE_ICON;

    static {
        try {
            final BufferedImage atlas = ImageIO.read(MahjongAtlas.class.getResourceAsStream(PATH));
            EMPTY_TILE_ICON = getSubimage(atlas, 6, 4);
            SIDEWAYS_EMPTY_TILE_ICON = ImageUtils.rotate90CounterClockwise(EMPTY_TILE_ICON);
            ICON_MAP = new HashMap<>();
            ICON_MAP.put(S1, getSubimage(atlas, 0, 0));
            ICON_MAP.put(S2, getSubimage(atlas, 1, 0));
            ICON_MAP.put(S3, getSubimage(atlas, 2, 0));
            ICON_MAP.put(S4, getSubimage(atlas, 3, 0));
            ICON_MAP.put(S5, getSubimage(atlas, 4, 0));
            ICON_MAP.put(S6, getSubimage(atlas, 5, 0));
            ICON_MAP.put(S7, getSubimage(atlas, 6, 0));
            ICON_MAP.put(S8, getSubimage(atlas, 7, 0));
            ICON_MAP.put(S9, getSubimage(atlas, 8, 0));
            ICON_MAP.put(M1, getSubimage(atlas, 0, 1));
            ICON_MAP.put(M2, getSubimage(atlas, 1, 1));
            ICON_MAP.put(M3, getSubimage(atlas, 2, 1));
            ICON_MAP.put(M4, getSubimage(atlas, 3, 1));
            ICON_MAP.put(M5, getSubimage(atlas, 4, 1));
            ICON_MAP.put(M6, getSubimage(atlas, 5, 1));
            ICON_MAP.put(M7, getSubimage(atlas, 6, 1));
            ICON_MAP.put(M8, getSubimage(atlas, 7, 1));
            ICON_MAP.put(M9, getSubimage(atlas, 8, 1));
            ICON_MAP.put(P1, getSubimage(atlas, 0, 2));
            ICON_MAP.put(P2, getSubimage(atlas, 1, 2));
            ICON_MAP.put(P3, getSubimage(atlas, 2, 2));
            ICON_MAP.put(P4, getSubimage(atlas, 3, 2));
            ICON_MAP.put(P5, getSubimage(atlas, 4, 2));
            ICON_MAP.put(P6, getSubimage(atlas, 5, 2));
            ICON_MAP.put(P7, getSubimage(atlas, 6, 2));
            ICON_MAP.put(P8, getSubimage(atlas, 7, 2));
            ICON_MAP.put(P9, getSubimage(atlas, 8, 2));
            ICON_MAP.put(E, getSubimage(atlas, 0, 3));
            ICON_MAP.put(S, getSubimage(atlas, 1, 3));
            ICON_MAP.put(W, getSubimage(atlas, 2, 3));
            ICON_MAP.put(N, getSubimage(atlas, 3, 3));
            ICON_MAP.put(C, getSubimage(atlas, 4, 3));
            ICON_MAP.put(F, getSubimage(atlas, 5, 3));
            ICON_MAP.put(P, getSubimage(atlas, 6, 3));
            ICON_MAP.put(F1, getSubimage(atlas, 7, 3));
            ICON_MAP.put(F4, getSubimage(atlas, 8, 3));
            ICON_MAP.put(F2, getSubimage(atlas, 0, 4));
            ICON_MAP.put(F3, getSubimage(atlas, 1, 4));
            ICON_MAP.put(F5, getSubimage(atlas, 2, 4));
            ICON_MAP.put(F6, getSubimage(atlas, 3, 4));
            ICON_MAP.put(F7, getSubimage(atlas, 4, 4));
            ICON_MAP.put(F8, getSubimage(atlas, 5, 4));
            SIDEWAYS_ICON_MAP = new HashMap<>();
            for (Map.Entry<Tile, Icon> entry : ICON_MAP.entrySet()) {
                SIDEWAYS_ICON_MAP.put(entry.getKey(), ImageUtils.rotate90CounterClockwise(entry.getValue()));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static Icon getSubimage(BufferedImage atlas, int indexX, int indexY) {
        final BufferedImage rawImage = atlas.getSubimage(indexX * 111, indexY * 154, 111, 154);
        return new ImageIcon(rawImage.getScaledInstance(MAHJONG_WIDTH, MAHJONG_HEIGHT, BufferedImage.SCALE_SMOOTH));
    }

}
