package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.data.Wind;

public record Options(
        boolean selfDrawn, boolean lastTile,
        boolean lastDrawOrClaim, boolean kong,
        Wind prevalentWind, Wind seatWind
) {
}
