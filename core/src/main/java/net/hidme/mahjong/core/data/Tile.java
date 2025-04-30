package net.hidme.mahjong.core.data;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public enum Tile {

    M1(1, 'm'),
    M2(2, 'm'),
    M3(3, 'm'),
    M4(4, 'm'),
    M5(5, 'm'),
    M6(6, 'm'),
    M7(7, 'm'),
    M8(8, 'm'),
    M9(9, 'm'),

    P1(1, 'p'),
    P2(2, 'p'),
    P3(3, 'p'),
    P4(4, 'p'),
    P5(5, 'p'),
    P6(6, 'p'),
    P7(7, 'p'),
    P8(8, 'p'),
    P9(9, 'p'),

    S1(1, 's'),
    S2(2, 's'),
    S3(3, 's'),
    S4(4, 's'),
    S5(5, 's'),
    S6(6, 's'),
    S7(7, 's'),
    S8(8, 's'),
    S9(9, 's'),

    E(1, 'w'),
    S(2, 'w'),
    W(3, 'w'),
    N(4, 'w'),

    P(1, 'd'),
    F(2, 'd'),
    C(3, 'd'),

    // 春夏秋冬梅兰竹菊
    F1(1, 'f'),
    F2(2, 'f'),
    F3(3, 'f'),
    F4(4, 'f'),
    F5(5, 'f'),
    F6(6, 'f'),
    F7(7, 'f'),
    F8(8, 'f'),
    ;

    Tile(int number, char suit) {
        this.number = number;
        this.suit = suit;
    }

    // tile index within a suit
    public final int number;
    // "m/s/p" for non-honor tiles, "w" for wind tiles, "d" for dragon tiles, and "f" for flower tiles
    public final char suit;

    public boolean isNumber() {
        return isNumberSuit(suit);
    }

    public boolean isHonor() {
        return isHonorSuit(suit);
    }

    public boolean isFlower() {
        return suit == 'f';
    }

    public boolean isTerminal() {
        return isNumber() && (number == 1 || number == 9);
    }

    public boolean isSimple() {
        return isNumber() && (number != 1 && number != 9);
    }

    public boolean isOrphan() {
        return isHonor() || isTerminal();
    }

    public boolean isWind() {
        return suit == 'w';
    }

    public boolean isDragon() {
        return suit == 'd';
    }

    public Tile next() {
        return shift(1);
    }

    public Tile shift(int offset) {
        return getInstance(number + offset, suit);
    }

    public static boolean isNumberSuit(char suit) {
        return suit == 'm' || suit == 's' || suit == 'p';
    }

    public static boolean isHonorSuit(char suit) {
        return suit == 'w' || suit == 'd';
    }

    public static Tile getInstance(int number, char suit) {
        if (suit == 'm') return MANS[number - 1];
        if (suit == 'p') return DOTS[number - 1];
        if (suit == 's') return BAMBOOS[number - 1];
        if (suit == 'f') return FLOWERS[number - 1];
        if (suit == 'w') return WINDS[number - 1];
        if (suit == 'd') return DRAGONS[number - 1];
        throw new IllegalArgumentException("The tile (" + number + "," + suit + ") is invalid");
    }

    public static Tile parseHonor(char honor) throws ParseException {
        return switch (honor) {
            case 'E' -> E;
            case 'S' -> S;
            case 'W' -> W;
            case 'N' -> N;
            case 'P' -> P;
            case 'F' -> F;
            case 'C' -> C;
            default -> throw new ParseException(String.valueOf(honor), 0);
        };
    }

    /**
     * Get tiles of the specified suit from {@code tiles}.
     */
    public static SortedMultiset<Tile> getTilesOfSuit(SortedMultiset<Tile> tiles, char suit) {
        return TreeMultiset.create(
                tiles.stream().filter(t -> t.suit == suit).toList());
    }

    public static char[] getSuits(Collection<Tile> tiles) {
        final Set<Character> suits = getSuitSet(tiles);
        final char[] ret = new char[suits.size()];
        int i = 0;
        for (Character suit : suits)
            ret[i++] = suit;
        return ret;
    }

    public static Set<Character> getSuitSet(Collection<Tile> tiles) {
        final Set<Character> suits = new HashSet<>();
        for (Tile tile : tiles) {
            suits.add(tile.suit);
        }
        return suits;
    }

    public static Tile wind(Wind wind) {
        return switch (wind) {
            case EAST -> E;
            case SOUTH -> S;
            case WEST -> W;
            case NORTH -> N;
        };
    }

    private static final Tile[] MANS = {M1, M2, M3, M4, M5, M6, M7, M8, M9};
    private static final Tile[] BAMBOOS = {S1, S2, S3, S4, S5, S6, S7, S8, S9};
    private static final Tile[] DOTS = {P1, P2, P3, P4, P5, P6, P7, P8, P9};
    private static final Tile[] WINDS = {E, S, W, N};
    private static final Tile[] DRAGONS = {P, F, C};
    private static final Tile[] FLOWERS = {F1, F2, F3, F4, F5, F6, F7, F8};

}
