package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;

public class NormalHandStructure implements HandStructure {
    Claim[] claims;
    Tile pair;

    public NormalHandStructure(Claim[] claims, Tile pair) {
        this.claims = claims;
        this.pair = pair;
    }
}
