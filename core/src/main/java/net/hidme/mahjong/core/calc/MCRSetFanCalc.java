package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.*;
import net.hidme.mahjong.core.util.NumberUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.hidme.mahjong.core.data.Claim.Type.CHOW;
import static net.hidme.mahjong.core.data.Claim.Type.KNITTED_CHOW;
import static net.hidme.mahjong.core.data.MCRFan.*;
import static net.hidme.mahjong.core.data.Tile.wind;
import static net.hidme.mahjong.core.util.CollectionUtils.minus;
import static net.hidme.mahjong.core.util.CollectionUtils.setOf;

/**
 * Calculator for Fans of sets.
 * Such Fans consume sets during the calculation.
 */
public class MCRSetFanCalc {

    public MCRSetFanCalc(MCRHand hand, HandStructure structure, MCRResult result) {
        this.hand = hand;
        this.structure = structure;
        this.result = result;
        usedClaims = new ArrayList<>();
        if (structure instanceof NormalHandStructure normalStruct) {
            unusedClaims = new ArrayList<>(Stream.of(normalStruct.claims)
                    .map(c -> new SuppressedClaim(c, new HashSet<>()))
                    .toList());
            pair = normalStruct.pair;
        } else {
            unusedClaims = new ArrayList<>();
            pair = null;
        }
    }

    public void calculate() {
        calculateComposedSetFans();
        calculateSingleSetFans();
    }

    private void calculateComposedSetFans() {
        // 88
        checkBigFourWinds();
        checkBigThreeDragons();
        // 64
        checkLittleFourWinds();
        checkLittleThreeDragons();
        // 48
        checkQuadrupleChow();
        checkFourPureShiftedPungs();
        // 32
        checkFourPureShiftedChows();
        // 24
        checkPureTripleChow();
        checkPureShiftedPungs();
        // 16
        checkPureStraight();
        checkPureShiftedChows();
        checkTriplePung();
        // 12
        checkKnittedStraight();
        checkBigThreeWinds();
        // 8
        checkMixedStraight();
        checkMixedTripleChow();
        checkMixedShiftedChows();
        checkMixedShiftedPungs();
        // 6
        checkMixedShiftedChows();
        checkTwoDragonPungs();
        // 2
        checkDoublePung();
        // 1
        checkTwoChowFans();
        // mark all claims as used
        usedClaims.addAll(unusedClaims);
        unusedClaims.clear();
    }

    private void calculateSingleSetFans() {
        // 2
        checkDragonPung();
        checkPrevalentWind();
        checkSeatWind();
        // 1
        checkPungOfTerminalsOrHonors();
    }

    // Some claims do not count some Fans
    // e.g. Wind pungs in small four winds do not count honor pungs
    private record SuppressedClaim(Claim claim, Set<MCRFan> suppress) {

        private Claim.Type type() {
            return claim.type();
        }

        private Tile start() {
            return claim.start();
        }

        private int claimedFrom() {
            return claim.claimedFrom();
        }

    }

    private final MCRHand hand;
    private final HandStructure structure;
    private final MCRResult result;
    private final List<SuppressedClaim> usedClaims;
    private final List<SuppressedClaim> unusedClaims;
    private final Tile pair;

    // 1

    private void checkTwoChowFans() {
        checkAddOneSetWithUsedSetOrTwoSets(
                (c1, c2) -> {
                    if (c1.type() == CHOW && c2.type() == CHOW) {
                        final Tile t1 = c1.start(), t2 = c2.start();
                        if (t1.suit == t2.suit && t1.number == t2.number)
                            return PURE_DOUBLE_CHOW;
                        if (t1.suit != t2.suit && t1.number == t2.number)
                            return MIXED_DOUBLE_CHOW;
                        if (t1.suit == t2.suit && Math.abs(t1.number - t2.number) == 3)
                            return SHORT_STRAIGHT;
                        if (t1.suit == t2.suit && (t1.number == 1 && t2.number == 7 || t1.number == 7 && t2.number == 1))
                            return TWO_TERMINAL_CHOWS;
                    }
                    return null;
                }
        );
    }

    private void checkPungOfTerminalsOrHonors() {
        checkSingleSetFan(claim ->
                claim.type().isPung() && claim.start().isOrphan(),
                PUNG_OF_TERMINALS_OR_HONORS);
    }

    // 2

    private void checkDragonPung() {
        checkSingleSetFan(claim -> claim.start().isDragon(), DRAGON_PUNG, PUNG_OF_TERMINALS_OR_HONORS);
    }

    private void checkPrevalentWind() {
        checkSingleSetFan(claim -> claim.start() == wind(hand.prevalentWind), PREVALENT_WIND, PUNG_OF_TERMINALS_OR_HONORS);
    }

    private void checkSeatWind() {
        checkSingleSetFan(claim -> claim.start() == wind(hand.seatWind), SEAT_WIND, PUNG_OF_TERMINALS_OR_HONORS);
    }

    private void checkDoublePung() {
        checkAddOneSetWithUsedSetOrTwoSets(
                (c1, c2) -> {
                    if (c1.type().isPung() && c2.type().isPung()) {
                        final Tile t1 = c1.start(), t2 = c2.start();
                        if (!t1.isNumber() || !t2.isNumber()) return false;
                        return t1.number == t2.number;
                    }
                    return false;
                },
                DOUBLE_PUNG
        );
    }

    // 6

    private void checkMixedShiftedChows() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().anyMatch(c -> c.type() != CHOW))
                        return null;
                    if (claims.stream().map(c -> c.start().suit)
                            .collect(Collectors.toSet())
                            .size() != 3)
                        return null;
                    final List<Integer> numberSeq = claims.stream().map(c -> c.start().number).toList();
                    if (NumberUtils.isUnorderedArithSeq(numberSeq, 1))
                        return claims;
                    return null;
                },
                MIXED_SHIFTED_CHOWS
        );
    }

    private void checkTwoDragonPungs() {
        checkAddSuppress(
                claims -> {
                    final List<SuppressedClaim> target = claims.stream()
                            .filter(c -> c.start().isDragon()).toList();
                    if (target.size() == 2) return target;
                    return null;
                },
                TWO_DRAGON_PUNGS,
                setOf(DRAGON_PUNG, PUNG_OF_TERMINALS_OR_HONORS)
        );
    }

    // 8

    private void checkMixedStraight() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().map(c -> c.start().suit)
                            .collect(Collectors.toSet())
                            .size() != 3)
                        return null;
                    if (claims.stream().anyMatch(c -> c.type() != CHOW))
                        return null;
                    final Set<Integer> starts = claims.stream()
                            .map(c -> c.start().number).collect(Collectors.toSet());
                    if (starts.equals(Set.of(1, 4, 7))) return claims;
                    return null;
                },
                MIXED_STRAIGHT
        );
    }

    private void checkMixedTripleChow() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().map(c -> c.start().suit)
                            .collect(Collectors.toSet())
                            .size() != 3)
                        return null;
                    final int start = claims.getFirst().start().number;
                    if (claims.stream().allMatch(c -> c.type() == CHOW && c.start().number == start))
                        return claims;
                    return null;
                },
                MIXED_TRIPLE_CHOW
        );
    }

    private void checkMixedShiftedPungs() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().anyMatch(c -> !c.type().isPung()))
                        return null;
                    // only number pungs are considered
                    if (!isOfNumberSuits(claims))
                        return null;
                    if (claims.stream().map(c -> c.start().suit)
                            .collect(Collectors.toSet())
                            .size() != 3)
                        return null;
                    final List<Integer> numberSeq = claims.stream().map(c -> c.start().number).toList();
                    if (NumberUtils.isUnorderedArithSeq(numberSeq, 1))
                        return claims;
                    return null;
                },
                MIXED_SHIFTED_PUNGS
        );
    }

    // 12

    private void checkKnittedStraight() {
        checkAdd(
                claims -> {
                    final List<SuppressedClaim> target = claims.stream()
                            .filter(c -> c.type() == KNITTED_CHOW).toList();
                    if (target.size() != 3) return null;
                    final Set<Integer> starts = target.stream()
                            .map(c -> c.start().number)
                            .collect(Collectors.toSet());
                    final Set<Character> suits = target.stream()
                            .map(c -> c.start().suit)
                            .collect(Collectors.toSet());
                    if (starts.size() == 3 && suits.size() == 3) return target;
                    return null;
                },
                KNITTED_STRAIGHT
        );
        if (structure instanceof HonorKnittedHandStructure knittedStruct) {
            if (knittedStruct.knittedTiles.length == 9)
                result.addFan(KNITTED_STRAIGHT);
        }
    }

    private void checkBigThreeWinds() {
        checkAddSuppress(
                claims -> {
                    final List<SuppressedClaim> target = claims.stream()
                            .filter(c -> c.start().isWind()).toList();
                    if (target.size() == 3) return target;
                    return null;
                },
                BIG_THREE_WINDS,
                setOf(PUNG_OF_TERMINALS_OR_HONORS)
        );
    }

    // 16

    private void checkPureStraight() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().anyMatch(c -> c.type() != CHOW))
                        return null;
                    if (!isOfPureNumberSuit(claims)) return null;
                    final Set<Integer> starts = claims.stream()
                            .map(c -> c.start().number).collect(Collectors.toSet());
                    if (starts.equals(Set.of(1, 4, 7))) return claims;
                    return null;
                },
                PURE_STRAIGHT
        );
    }

    private void checkPureShiftedChows() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().anyMatch(c -> c.type() != CHOW))
                        return null;
                    if (!isOfPureNumberSuit(claims)) return null;
                    final List<Integer> numberSeq = claims.stream().map(c -> c.start().number).toList();
                    if (NumberUtils.isUnorderedArithSeq(numberSeq, 1)
                            || NumberUtils.isUnorderedArithSeq(numberSeq, 2))
                        return claims;
                    return null;
                },
                PURE_SHIFTED_CHOWS
        );
    }

    private void checkTriplePung() {
        checkAddThreeSets(
                claims -> {
                    // only number pungs are considered
                    if (!isOfNumberSuits(claims))
                        return null;
                    final int start = claims.getFirst().start().number;
                    if (claims.stream().allMatch(c -> c.type().isPung() && c.start().number == start))
                        return claims;
                    return null;
                },
                TRIPLE_PUNG
        );
    }

    // 24

    private void checkPureTripleChow() {
        checkAddThreeSets(
                claims -> {
                    final Tile start = claims.getFirst().start();
                    if (claims.stream().allMatch(c -> c.type() == CHOW && c.start() == start))
                        return claims;
                    return null;
                },
                PURE_TRIPLE_CHOW
        );
    }

    private void checkPureShiftedPungs() {
        checkAddThreeSets(
                claims -> {
                    if (claims.stream().anyMatch(c -> !c.type().isPung()))
                        return null;
                    if (!isOfPureNumberSuit(claims)) return null;
                    final List<Integer> numberSeq = claims.stream().map(c -> c.start().number).toList();
                    if (NumberUtils.isUnorderedArithSeq(numberSeq, 1))
                        return claims;
                    return null;
                },
                PURE_SHIFTED_PUNGS
        );
    }

    // 32

    private void checkFourPureShiftedChows() {
        checkAdd(
                claims -> {
                    if (claims.size() != 4) return null;
                    if (claims.stream().anyMatch(c -> c.type() != CHOW))
                        return null;
                    if (!isOfPureNumberSuit(claims)) return null;
                    final List<Integer> numberSeq = claims.stream().map(c -> c.start().number).toList();
                    if (NumberUtils.isUnorderedArithSeq(numberSeq, 1)
                            || NumberUtils.isUnorderedArithSeq(numberSeq, 2))
                        return claims;
                    return null;
                },
                FOUR_PURE_SHIFTED_CHOWS
        );
    }

    // 48

    private void checkQuadrupleChow() {
        checkAdd(
                claims -> {
                    if (claims.size() != 4) return null;
                    final Tile start = claims.getFirst().start();
                    if (claims.stream().allMatch(c -> c.type() == CHOW && c.start() == start))
                        return claims;
                    return null;
                },
                QUADRUPLE_CHOW
        );
    }

    private void checkFourPureShiftedPungs() {
        checkAdd(
                claims -> {
                    if (claims.size() != 4) return null;
                    if (claims.stream().anyMatch(c -> !c.type().isPung()))
                        return null;
                    if (!isOfPureNumberSuit(claims)) return null;
                    final List<Integer> numberSeq = claims.stream().map(c -> c.start().number).toList();
                    if (NumberUtils.isUnorderedArithSeq(numberSeq, 1))
                        return claims;
                    return null;
                },
                FOUR_PURE_SHIFTED_PUNGS
        );
    }

    // 64

    private void checkLittleFourWinds() {
        checkAddSuppress(
                claims -> {
                    if (pair == null || !pair.isWind()) return null;
                    final List<SuppressedClaim> target = claims.stream()
                            .filter(c -> c.start().isWind()).toList();
                    if (target.size() == 3) return target;
                    return null;
                },
                LITTLE_FOUR_WINDS,
                setOf(PUNG_OF_TERMINALS_OR_HONORS)
        );
    }

    private void checkLittleThreeDragons() {
        checkAddSuppress(
                claims -> {
                    if (pair == null || !pair.isDragon()) return null;
                    final List<SuppressedClaim> target = claims.stream()
                            .filter(c -> c.start().isDragon()).toList();
                    if (target.size() == 2) return target;
                    return null;
                },
                LITTLE_THREE_DRAGONS,
                setOf(DRAGON_PUNG, PUNG_OF_TERMINALS_OR_HONORS)
        );
    }

    // 88

    private void checkBigFourWinds() {
        checkAddSuppress(
                claims -> {
                    if (claims.size() != 4) return null;
                    if (claims.stream().allMatch(c -> c.start().isWind()))
                        return claims;
                    else return null;
                },
                BIG_FOUR_WINDS,
                setOf(PREVALENT_WIND, SEAT_WIND, PUNG_OF_TERMINALS_OR_HONORS)
        );
    }

    private void checkBigThreeDragons() {
        checkAddSuppress(
                claims -> {
                    final List<SuppressedClaim> target = claims.stream()
                            .filter(c -> c.start().isDragon()).toList();
                    if (target.size() == 3) return target;
                    return null;
                },
                BIG_THREE_DRAGONS,
                setOf(DRAGON_PUNG, PUNG_OF_TERMINALS_OR_HONORS)
        );
    }

    // utilities

    private static boolean isOfPureNumberSuit(List<SuppressedClaim> claims) {
        return Claim.isOfPureNumberSuit(claims.stream().map(SuppressedClaim::claim).toList());
    }

    private static boolean isOfNumberSuits(List<SuppressedClaim> claims) {
        return claims.stream().allMatch(c -> Tile.isNumberSuit(c.start().suit));
    }

    private void checkSingleSetFan(Predicate<SuppressedClaim> target, MCRFan fan) {
        checkSingleSetFan(target, fan, setOf());
    }

    private void checkSingleSetFan(Predicate<SuppressedClaim> target, MCRFan fan, MCRFan suppress) {
        checkSingleSetFan(target, fan, setOf(suppress));
    }

    // check single-set Fan (excluding suppress) after all sets are consumed
    // suppress a set if the set satisfies the Fan
    private void checkSingleSetFan(Predicate<SuppressedClaim> target, MCRFan fan, Set<MCRFan> suppress) {
        for (SuppressedClaim suppressedClaim : usedClaims) {
            if (suppressedClaim.suppress.contains(fan)) continue;
            if (target.test(suppressedClaim)) {
                result.addFan(fan);
                suppressedClaim.suppress.addAll(suppress);
            }
        }
    }

    /**
     * Check whether there is a used set and an unused set (or two unused sets) that match a 2-set Fan.
     * If so, add this Fan and consume the unused set(s) until the Fan is not met.
     * You may assume that the input of "target" must be 2 sets.
     */
    private void checkAddOneSetWithUsedSetOrTwoSets(BiPredicate<SuppressedClaim, SuppressedClaim> target,
                                                    MCRFan fan) {
        checkAddOneSetWithUsedSetOrTwoSets((c1, c2) -> target.test(c1, c2) ? fan : null);
    }

    /**
     * Check whether there is a used set and an unused set (or two unused sets) that match some 2-set Fan specified by "target".
     * If given a pair of sets as mentioned, "target" returns a non-null Fan,
     * then add this Fan and consume the unused set(s), keep finding such a pair until "target" returns null for all pairs.
     * "One used set + one unused set" is considered prior to "two unused sets".
     * You may assume that the input of "target" must be 2 sets.
     */
    private void checkAddOneSetWithUsedSetOrTwoSets(BiFunction<SuppressedClaim, SuppressedClaim, MCRFan> target) {
        final AtomicReference<MCRFan> fanRef = new AtomicReference<>();
        boolean matched;
        do {
            // try to match and consume unused claims
            matched = consumeUnusedClaims(claims -> {
                // try "one used set + one unused set" first
                List<SuppressedClaim> result = oneSetWithUsedSetFuncWrapper(target, claims, fanRef);
                if (result != null) return result;
                // then try "two unused sets"
                result = twoSetFuncWrapper(target, claims, fanRef);
                return result;
            }, claim -> setOf());
            if (matched) result.addFan(fanRef.get());
        } while (matched);
    }

    // given unused claims
    // this method checks each pair of used and unused claim
    // returns the unused set to consume
    // and sets fanRef to the Fan to add
    private List<SuppressedClaim> oneSetWithUsedSetFuncWrapper(BiFunction<SuppressedClaim, SuppressedClaim, MCRFan> target,
                                                               List<SuppressedClaim> claims, AtomicReference<MCRFan> fanRef) {
        for (SuppressedClaim unusedClaim : claims) {
            for (SuppressedClaim usedClaim : usedClaims) {
                final MCRFan fan = target.apply(unusedClaim, usedClaim);
                if (fan != null) {
                    fanRef.set(fan);
                    return new ArrayList<>(List.of(unusedClaim));
                }
            }
        }
        return null;
    }

    private List<SuppressedClaim> twoSetFuncWrapper(BiFunction<SuppressedClaim, SuppressedClaim, MCRFan> target,
                                                    List<SuppressedClaim> claims, AtomicReference<MCRFan> fanRef) {
        if (claims.size() <= 1) return null;
        for (int i = 0, bound = claims.size() - 1; i < bound; i++) {
            for (int j = i + 1, bound1 = claims.size(); j < bound1; j++) {
                final MCRFan fan = target.apply(claims.get(i), claims.get(j));
                if (fan != null) {
                    fanRef.set(fan);
                    return new ArrayList<>(List.of(claims.get(i), claims.get(j)));
                }
            }
        }
        return null;
    }

    /**
     * Check whether the required sets of a 3-set Fan exist.
     * If so, add this Fan and consume involved sets.
     * You may assume that the input of target must be 3 sets.
     */
    private void checkAddThreeSets(Function<List<SuppressedClaim>, List<SuppressedClaim>> target,
                                   MCRFan fan) {
        checkAdd(
                claims -> {
                    if (claims.size() == 4) {
                        for (int i = 0; i < 4; i++) {
                            final List<SuppressedClaim> threeSets = minus(claims, i);
                            final List<SuppressedClaim> ret = target.apply(threeSets);
                            if (ret != null) return ret;
                        }
                    } else if (claims.size() == 3) {
                        return target.apply(claims);
                    }
                    return null;
                },
                fan
        );
    }

    private void checkAdd(Function<List<SuppressedClaim>, List<SuppressedClaim>> target,
                          MCRFan fan) {
        checkAddSuppress(target, fan, claim -> setOf());
    }

    private void checkAddSuppress(Function<List<SuppressedClaim>, List<SuppressedClaim>> target,
                                  MCRFan fan,
                                  MCRFan suppress) {
        checkAddSuppress(target, fan, claim -> setOf(suppress));
    }

    private void checkAddSuppress(Function<List<SuppressedClaim>, List<SuppressedClaim>> target,
                                  MCRFan fan,
                                  Set<MCRFan> suppress) {
        checkAddSuppress(target, fan, claim -> suppress);
    }

    // check whether the required sets of a Fan exist
    // if so, add this Fan (maybe duplicable), consume involved sets,
    // and suppress Fans for each consumed set (specified by suppressor)
    private void checkAddSuppress(Function<List<SuppressedClaim>, List<SuppressedClaim>> target,
                                  MCRFan fan,
                                  Function<SuppressedClaim, Set<MCRFan>> suppressor) {
        while (consumeUnusedClaims(target, suppressor))
            result.addFan(fan);
    }

    // consume a subset of unused claims and apply suppress
    private boolean consumeUnusedClaims(Function<List<SuppressedClaim>, List<SuppressedClaim>> target,
                                        Function<SuppressedClaim, Set<MCRFan>> suppressor) {
        final List<SuppressedClaim> result = target.apply(unusedClaims);
        if (result == null) return false;
        final List<SuppressedClaim> toConsume = new ArrayList<>(result);
        for (SuppressedClaim claim : toConsume) {
            unusedClaims.remove(claim);
            final Set<MCRFan> suppress = suppressor.apply(claim);
            claim.suppress.addAll(suppress);
            usedClaims.add(claim);
        }
        return true;
    }

}
