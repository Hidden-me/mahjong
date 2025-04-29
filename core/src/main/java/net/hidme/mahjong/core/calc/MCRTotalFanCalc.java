package net.hidme.mahjong.core.calc;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.*;

import java.util.*;
import java.util.stream.Stream;

import static net.hidme.mahjong.core.data.Claim.Type.*;
import static net.hidme.mahjong.core.data.MCRFan.*;
import static net.hidme.mahjong.core.data.Tile.*;

/**
 * Calculator for Fans of total properties.
 * Such Fans do not consume sets during the calculation.
 */
public class MCRTotalFanCalc {

    public MCRTotalFanCalc(MCRHand hand, HandStructure structure, MCRResult result) {
        this.hand = hand;
        this.structure = structure;
        this.result = result;
    }

    public void calculate() {
        // 88
        checkAllGreen();
        checkNineGates();
        checkFourKongs();
        checkSevenShiftedPairs();
        checkThirteenOrphans();
        // 64
        checkAllTerminals();
        checkAllHonors();
        checkFourConcealedPungs();
        checkPureTerminalChows();
        // 32
        // 24
        checkSevenPairs();
        checkGreaterHonorsAndKnittedTiles();
        // 12
        checkLesserHonorsAndKnittedTiles();
    }

    private final MCRHand hand;
    private final HandStructure structure;
    private final MCRResult result;

    // 12

    private void checkLesserHonorsAndKnittedTiles() {
        if (structure instanceof HonorKnittedHandStructure) {
            result.addFan(LESSER_HONORS_AND_KNITTED_TILES);
        }
    }

    // 24

    private void checkSevenPairs() {
        if (structure instanceof PairHandStructure) {
            result.addFan(SEVEN_PAIRS);
        }
    }

    private void checkGreaterHonorsAndKnittedTiles() {
        if (!(structure instanceof HonorKnittedHandStructure honorKnittedHandStructure)) return;
        if (honorKnittedHandStructure.honors.length == 7) {
            result.addFan(GREATER_HONORS_AND_KNITTED_TILES);
        }
    }

    // 32

    private void checkAllTerminalsAndHonors() {
        if (hand.getHandTilesWithClaims().stream().allMatch(Tile::isOrphan)) {
            result.addFan(ALL_TERMINALS_AND_HONORS);
        }
    }

    // 64

    private void checkAllTerminals() {
        if (hand.getHandTilesWithClaims().stream().allMatch(Tile::isTerminal)) {
            result.addFan(ALL_TERMINALS);
        }
    }

    private void checkAllHonors() {
        if (hand.getHandTilesWithClaims().stream().allMatch(Tile::isHonor)) {
            result.addFan(ALL_HONORS);
        }
    }

    private void checkFourConcealedPungs() {
        checkConcealedPungs(4);
    }

    private void checkPureTerminalChows() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (!hand.isOfPureNumberSuit()) return;
        if (normalStruct.pair.number != 5) return;
        if (!normalStruct.chowsOnly()) return;
        final SortedMultiset<Integer> starts = normalStruct.getClaimStartNumbers();
        if (starts.count(1) == 2 && starts.count(7) == 2) {
            result.addFan(PURE_TERMINAL_CHOWS);
        }
    }

    // 88

    private static final Set<Tile> GREEN_TILES = Set.of(S2, S3, S4, S6, S8, F);

    private void checkAllGreen() {
        if (GREEN_TILES.containsAll(hand.getHandTilesWithClaims())) {
            result.addFan(ALL_GREEN);
        }
    }

    private static final int[] NINE_GATE_NUMBERS = {1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 9};

    private void checkNineGates() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (hand.hasClaim()) return;
        // all tiles should be of one number suit
        if (!hand.isOfPureNumberSuit()) return;
        // check tile pattern
        final SortedMultiset<Tile> tiles = TreeMultiset.create(List.of(hand.tiles));
        int i = 0;
        for (Tile tile : tiles) {
            if (tile.number != NINE_GATE_NUMBERS[i++]) return;
        }
        result.addFan(NINE_GATES);
    }

    private void checkFourKongs() {
        checkKongs(4);
    }

    private void checkSevenShiftedPairs() {
        if (!(structure instanceof PairHandStructure pairHandStructure)) return;
        final SortedSet<Tile> sortedPairs = new TreeSet<>(List.of(pairHandStructure.pairs));
        if (sortedPairs.size() != 7) return;
        Tile last = null;
        for (Tile tile : sortedPairs) {
            if (!tile.isNumber()) return;
            if (last != null) {
                if (tile.suit != last.suit || tile.number != last.number + 1) return;
            }
            last = tile;
        }
        result.addFan(SEVEN_SHIFTED_PAIRS);
    }

    private void checkThirteenOrphans() {
        if (structure instanceof OrphanHandStructure) {
            result.addFan(THIRTEEN_ORPHANS);
        }
    }

    // utilities

    private static final MCRFan[] FAN_CONCEALED_PUNGS = {null, null, TWO_CONCEALED_PUNGS, THREE_CONCEALED_PUNGS, FOUR_CONCEALED_PUNGS};
    private static final MCRFan[] FAN_KONGS = {null, null, null, THREE_KONGS, FOUR_KONGS};

    // pungCount should be 2/3/4
    private void checkConcealedPungs(int pungCount) {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (Stream.of(normalStruct.claims)
                .filter(c -> c.type() == PUNG && c.claimedFrom() == 0)
                .count() == pungCount) {
            result.addFan(FAN_CONCEALED_PUNGS[pungCount]);
        }
    }

    // kongCount should be 3 or 4
    private void checkKongs(int kongCount) {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (Stream.of(normalStruct.claims)
                .filter(c -> c.type() == KONG)
                .count() == kongCount) {
            result.addFan(FAN_KONGS[kongCount]);
        }
    }

}
