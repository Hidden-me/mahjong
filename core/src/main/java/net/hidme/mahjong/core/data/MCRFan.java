package net.hidme.mahjong.core.data;

public enum MCRFan {
    BIG_FOUR_WINDS(88),
    BIG_THREE_DRAGONS(88),
    ALL_GREEN(88),
    NINE_GATES(88),
    FOUR_KONGS(88),
    SEVEN_SHIFTED_PAIRS(88),
    THIRTEEN_ORPHANS(88),

    ALL_TERMINALS(64),
    LITTLE_FOUR_WINDS(64),
    LITTLE_THREE_DRAGONS(64),
    ALL_HONORS(64),
    FOUR_CONCEALED_PUNGS(64),
    PURE_TERMINAL_CHOWS(64),

    QUADRUPLE_CHOW(48),
    FOUR_PURE_SHIFTED_PUNGS(48),

    FOUR_PURE_SHIFTED_CHOWS(32),
    THREE_KONGS(32),
    ALL_TERMINALS_AND_HONORS(32),

    SEVEN_PAIRS(24),
    GREATER_HONORS_AND_KNITTED_TILES(24),
    ALL_EVEN_PUNGS(24),
    FULL_FLUSH(24),
    PURE_TRIPLE_CHOW(24),
    PURE_SHIFTED_PUNGS(24),
    UPPER_TILES(24),
    MIDDLE_TILES(24),
    LOWER_TILES(24),

    PURE_STRAIGHT(16),
    THREE_SUITED_TERMINAL_CHOWS(16),
    PURE_SHIFTED_CHOWS(16),
    ALL_FIVE(16),
    TRIPLE_PUNG(16),
    THREE_CONCEALED_PUNGS(16),

    LESSER_HONORS_AND_KNITTED_TILES(12),
    KNITTED_STRAIGHT(12),
    UPPER_FOUR(12),
    LOWER_FOUR(12),
    BIG_THREE_WINDS(12),

    MIXED_STRAIGHT(8),
    REVERSIBLE_TILES(8),
    MIXED_TRIPLE_CHOW(8),
    MIXED_SHIFTED_PUNGS(8),
    CHICKEN_HAND(8),
    LAST_TILE_DRAW(8),
    LAST_TILE_CLAIM(8),
    OUT_WITH_REPLACEMENT_TILE(8),
    ROBBING_THE_KONG(8),

    ALL_PUNGS(6),
    HALF_FLUSH(6),
    MIXED_SHIFTED_CHOWS(6),
    ALL_TYPES(6),
    MELDED_HAND(6),
    TWO_CONCEALED_KONGS(6),
    TWO_DRAGON_PUNGS(6),

    CONCEALED_AND_MELDED_KONGS(5),

    OUTSIDE_HAND(4),
    FULLY_CONCEALED_HAND(4),
    TWO_MELDED_KONGS(4),
    LAST_TILE(4),

    DRAGON_PUNG(2),
    PREVALENT_WIND(2),
    SEAT_WIND(2),
    CONCEALED_HAND(2),
    ALL_CHOWS(2),
    TILE_HOG(2),
    DOUBLE_PUNG(2),
    TWO_CONCEALED_PUNGS(2),
    CONCEALED_KONG(2),
    ALL_SIMPLES(2),

    PURE_DOUBLE_CHOW(1),
    MIXED_DOUBLE_CHOW(1),
    SHORT_STRAIGHT(1),
    TWO_TERMINAL_CHOWS(1),
    PUNG_OF_TERMINALS_OR_HONORS(1),
    MELDED_KONG(1),
    ONE_VOIDED_SUIT(1),
    NO_HONORS(1),
    EDGE_WAIT(1),
    CLOSED_WAIT(1),
    SINGLE_WAIT(1),
    SELF_DRAWN(1),
    FLOWER_TILE(1)
    ;

    MCRFan(int score) {
        this.score = score;
    }

    public final int score;
}
