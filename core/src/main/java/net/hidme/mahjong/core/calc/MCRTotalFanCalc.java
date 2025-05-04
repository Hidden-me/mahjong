package net.hidme.mahjong.core.calc;

import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
        checkThreeKongs();
        checkAllTerminalsAndHonors();
        // 24
        checkSevenPairs();
        checkGreaterHonorsAndKnittedTiles();
        checkAllEvenPungs();
        checkFullFlush();
        checkUpperTiles();
        checkMiddleTiles();
        checkLowerTiles();
        // 16
        checkThreeSuitedTerminalChows();
        checkAllFive();
        checkThreeConcealedPungs();
        // 12
        checkLesserHonorsAndKnittedTiles();
        checkUpperFour();
        checkLowerFour();
        // 8
        checkReversibleTiles();
        checkLastTileDraw();
        checkLastTileClaim();
        checkOutWithReplacementTile();
        checkRobbingTheKong();
        // 6
        checkAllPungs();
        checkHalfFlush();
        checkAllTypes();
        checkMeldedHand();
        checkTwoConcealedKongs();
        // 5
        checkConcealedAndMeldedKongs();
        // 4
        checkOutsideHand();
        checkFullyConcealedHand();
        checkTwoMeldedKongs();
        checkLastTile();
        // 2
        checkConcealedHand();
        checkAllChows();
        checkTileHog();
        checkTwoConcealedPungs();
        checkConcealedKong();
        checkAllSimples();
        // 1
        checkMeldedKong();
        checkOneVoidedSuit();
        checkNoHonors();
        checkUniqueWait();
        checkSelfDrawn();
    }

    private final MCRHand hand;
    private final HandStructure structure;
    private final MCRResult result;

    // 1

    private void checkMeldedKong() {
        checkOneKong(0);
    }

    private void checkOneVoidedSuit() {
        final Set<Character> suits = getSuitSet(hand.getHandTilesWithClaims());
        suits.removeAll(List.of('w', 'd'));
        if (suits.size() == 2)
            result.addFan(ONE_VOIDED_SUIT);
    }

    private void checkNoHonors() {
        if (hand.getHandTilesWithClaims().stream().noneMatch(Tile::isHonor))
            result.addFan(NO_HONORS);
    }

    private void checkUniqueWait() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        final MCRDeclaredTileCalculator declaredTileCalculator = new MCRDeclaredTileCalculator(hand.claims, hand.tiles);
        final Set<Tile> declaredTiles = declaredTileCalculator.calculate(true);
        assert declaredTiles.contains(hand.declaredTile);
        if (declaredTiles.size() != 1) return;
        final Tile declaredTile = hand.declaredTile;
        // classify unique wait
        for (Claim claim : normalStruct.claims) {
            if (claim.type() != CHOW) continue;
            if (!Set.of(claim.getTiles()).contains(declaredTile)) continue;
            final int pos = declaredTile.number - claim.start().number;
            if (pos == 0 || pos == 2) {
                result.addFan(EDGE_WAIT);
            } else {
                assert pos == 1;
                result.addFan(CLOSED_WAIT);
            }
            return;
        }
        if (declaredTile == normalStruct.pair) {
            result.addFan(SINGLE_WAIT);
        }
        // special unique wait (e.g. thirteen orphans / honors and knitted tiles)
    }

    private void checkSelfDrawn() {
        if (hand.selfDrawn)
            result.addFan(SELF_DRAWN);
    }

    // 2

    private void checkConcealedHand() {
        if (hand.isConcealed())
            result.addFan(CONCEALED_HAND);
    }

    private void checkAllChows() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (normalStruct.chowsOrKnittedChowsOnly() && !normalStruct.pair.isHonor())
            result.addFan(ALL_CHOWS);
    }

    private void checkTileHog() {
        final Set<Tile> kongTiles = Stream.of(hand.claims)
                .filter(c -> c.type() == KONG)
                .map(Claim::start)
                .collect(Collectors.toSet());
        for (Multiset.Entry<Tile> entry : hand.getHandTilesWithClaims().entrySet()) {
            if (entry.getCount() == 4 && !kongTiles.contains(entry.getElement()))
                result.addFan(TILE_HOG);
        }
    }

    private void checkTwoConcealedPungs() {
        checkConcealedPungs(2);
    }

    private void checkConcealedKong() {
        checkOneKong(1);
    }

    private void checkAllSimples() {
        if (hand.getHandTilesWithClaims().stream().noneMatch(Tile::isOrphan))
            result.addFan(ALL_SIMPLES);
    }

    // 4

    private void checkOutsideHand() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (!normalStruct.pair.isOrphan()) return;
        if (Stream.of(normalStruct.claims)
                .allMatch(c -> Stream.of(c.getTiles()).anyMatch(Tile::isOrphan)))
            result.addFan(OUTSIDE_HAND);
    }

    private void checkFullyConcealedHand() {
        if (hand.isConcealed() && hand.selfDrawn)
            result.addFan(FULLY_CONCEALED_HAND);
    }

    private void checkTwoMeldedKongs() {
        checkTwoKongs(0);
    }

    private void checkLastTile() {
        if (hand.lastTile)
            result.addFan(LAST_TILE);
    }

    // 5

    private void checkConcealedAndMeldedKongs() {
        checkTwoKongs(1);
    }

    // 6

    private void checkAllPungs() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (normalStruct.pungsOnly())
            result.addFan(ALL_PUNGS);
    }

    private void checkHalfFlush() {
        final Set<Character> suits = getSuitSet(hand.getHandTilesWithClaims());
        if (!suits.contains('w') && !suits.contains('d')) return;
        suits.removeAll(List.of('w', 'd'));
        if (suits.size() == 1)
            result.addFan(HALF_FLUSH);
    }

    private void checkAllTypes() {
        if (getSuits(hand.getHandTilesWithClaims()).length == 5)
            result.addFan(ALL_TYPES);
    }

    private void checkMeldedHand() {
        if (hand.claims.length == 4 && !hand.selfDrawn) {
            for (Claim claim : hand.claims) {
                if (claim.claimedFrom() == 0) return;
            }
            result.addFan(MELDED_HAND);
        }
    }

    private void checkTwoConcealedKongs() {
        checkTwoKongs(2);
    }

    // 8

    private static final Set<Tile> REVERSIBLE_TILE_SET = Set.of(
            P1, P2, P3, P4, P5, P8, P9, S2, S4, S5, S6, S8, S9, P
    );

    private void checkReversibleTiles() {
        if (REVERSIBLE_TILE_SET.containsAll(hand.getHandTilesWithClaims()))
            result.addFan(REVERSIBLE_TILES);
    }

    private void checkLastTileDraw() {
        if (hand.lastDrawOrClaim && hand.selfDrawn)
            result.addFan(LAST_TILE_DRAW);
    }

    private void checkLastTileClaim() {
        if (hand.lastDrawOrClaim && !hand.selfDrawn)
            result.addFan(LAST_TILE_CLAIM);
    }

    private void checkOutWithReplacementTile() {
        if (hand.kong && hand.selfDrawn)
            result.addFan(OUT_WITH_REPLACEMENT_TILE);
    }

    private void checkRobbingTheKong() {
        if (hand.kong && !hand.selfDrawn)
            result.addFan(ROBBING_THE_KONG);
    }

    // 12

    private void checkLesserHonorsAndKnittedTiles() {
        if (structure instanceof HonorKnittedHandStructure) {
            result.addFan(LESSER_HONORS_AND_KNITTED_TILES);
        }
    }

    private void checkUpperFour() {
        checkNumberHand(i -> i >= 6, UPPER_FOUR);
    }

    private void checkLowerFour() {
        checkNumberHand(i -> i <= 4, LOWER_FOUR);
    }

    // 16

    private void checkThreeSuitedTerminalChows() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (!normalStruct.pair.isNumber() || normalStruct.pair.number != 5) return;
        final char pairSuit = normalStruct.pair.suit;
        // suits of 123 and suits of 789
        final Set<Character> chow1Suits = new HashSet<>(), chow7Suits = new HashSet<>();
        for (Claim claim : normalStruct.claims) {
            if (claim.type() != CHOW) return;
            final Tile start = claim.start();
            if (start.suit == pairSuit) return;
            if (start.number == 1) chow1Suits.add(start.suit);
            else if (start.number == 7) chow7Suits.add(start.suit);
        }
        if (chow1Suits.size() == 2 && chow7Suits.size() == 2)
            result.addFan(THREE_SUITED_TERMINAL_CHOWS);
    }

    private void checkAllFive() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        for (Claim claim : normalStruct.claims) {
            if (Stream.of(claim.getTiles()).noneMatch(t -> t.isNumber() && t.number == 5)) return;
        }
        result.addFan(ALL_FIVE);
    }

    private void checkThreeConcealedPungs() {
        checkConcealedPungs(3);
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

    private void checkAllEvenPungs() {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        for (Claim claim : normalStruct.claims) {
            if (!claim.type().isPung()) return;
        }
        checkNumberHand(i -> i % 2 == 0, ALL_EVEN_PUNGS);
    }

    private void checkFullFlush() {
        if (hand.isOfPureNumberSuit()) {
            result.addFan(FULL_FLUSH);
        }
    }

    private void checkUpperTiles() {
        checkNumberHand(i -> i >= 7, UPPER_TILES);
    }

    private void checkMiddleTiles() {
        checkNumberHand(i -> i >= 4 && i <= 6, MIDDLE_TILES);
    }

    private void checkLowerTiles() {
        checkNumberHand(i -> i <= 3, LOWER_TILES);
    }

    // 32

    private void checkThreeKongs() {
        checkKongs(3);
    }

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

    private static final MCRFan[] FAN_ONE_KONG = {MELDED_KONG, CONCEALED_KONG};

    private void checkOneKong(int concealedCount) {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        // exactly 1 kong
        final List<Claim> claims = Stream.of(normalStruct.claims)
                .filter(c -> c.type() == KONG).toList();
        if (claims.size() != 1) return;
        // check the count of concealed kongs
        final Claim claim = claims.getFirst();
        if ((claim.claimedFrom() == 0 ? 1 : 0) == concealedCount) {
            result.addFan(FAN_ONE_KONG[concealedCount]);
        }
    }

    private static final MCRFan[] FAN_TWO_KONGS = {TWO_MELDED_KONGS, CONCEALED_AND_MELDED_KONGS, TWO_CONCEALED_KONGS};

    private void checkTwoKongs(int concealedCount) {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        // exactly 2 kongs
        if (Stream.of(normalStruct.claims)
                .filter(c -> c.type() == KONG)
                .count() != 2) {
            return;
        }
        // check the count of concealed kongs
        if (Stream.of(normalStruct.claims)
                .filter(c -> c.type() == KONG && c.claimedFrom() == 0)
                .count() == concealedCount) {
            result.addFan(FAN_TWO_KONGS[concealedCount]);
        }
    }

    private void checkNumberHand(Predicate<Integer> numberRequirement, MCRFan fan) {
        if (!hand.isOfNumberSuits()) return;
        if (hand.getHandTilesWithClaims().stream().allMatch(t -> numberRequirement.test(t.number))) {
            result.addFan(fan);
        }
    }

    private static final MCRFan[] FAN_CONCEALED_PUNGS = {null, null, TWO_CONCEALED_PUNGS, THREE_CONCEALED_PUNGS, FOUR_CONCEALED_PUNGS};
    private static final MCRFan[] FAN_KONGS = {null, null, null, THREE_KONGS, FOUR_KONGS};

    // pungCount should be 2/3/4
    private void checkConcealedPungs(int pungCount) {
        if (!(structure instanceof NormalHandStructure normalStruct)) return;
        if (Stream.of(normalStruct.claims)
                .filter(c -> c.type().isPung() && c.claimedFrom() == 0)
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
