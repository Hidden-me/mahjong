package net.hidme.mahjong.core.calc;

import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.*;
import net.hidme.mahjong.core.util.NumberUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.hidme.mahjong.core.data.Claim.Type.CHOW;
import static net.hidme.mahjong.core.data.MCRFan.*;
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
        // mark all claims as used
        usedClaims.addAll(unusedClaims);
        unusedClaims.clear();
    }

    private void calculateSingleSetFans() {
        // 2
        checkDragonPung();
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
    private void checkPungOfTerminalsOrHonors() {
        checkSingleSetFan(claim -> claim.start().isOrphan(), PUNG_OF_TERMINALS_OR_HONORS);
    }

    // 2

    private void checkDragonPung() {
        checkSingleSetFan(claim -> claim.start().isOrphan(), DRAGON_PUNG, PUNG_OF_TERMINALS_OR_HONORS);
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
                    if (claims.stream().anyMatch(c -> c.type().isPung()))
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
                setOf(PREVALENT_WIND, SEAT_WIND, PUNG_OF_TERMINALS_OR_HONORS)
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
                fan);
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
        final List<SuppressedClaim> toConsume = target.apply(unusedClaims);
        if (toConsume != null) {
            unusedClaims.removeAll(toConsume);
            for (SuppressedClaim claim : toConsume) {
                final Set<MCRFan> suppress = suppressor.apply(claim);
                claim.suppress.addAll(suppress);
                usedClaims.add(claim);
            }
            return true;
        }
        return false;
    }

}
