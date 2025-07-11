package net.hidme.mahjong.core.quiz;

import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.*;
import net.hidme.mahjong.core.util.PoolRandom;
import net.hidme.mahjong.core.util.WeightRandom;

import java.util.*;

import static net.hidme.mahjong.core.data.Claim.*;
import static net.hidme.mahjong.core.data.Claim.Type.*;
import static net.hidme.mahjong.core.data.MCRFan.*;
import static net.hidme.mahjong.core.data.Tile.*;
import static net.hidme.mahjong.core.util.CollectionUtils.intersect;

public class MCRHandGenerator {

    public MCRHandGenerator() {
        fanRandom = new WeightRandom<>(FANS, FAN_WEIGHTS);
        simpleRandom = new Random();
        windRandom = new WeightRandom<>(Wind.values());
        suitRandom = new WeightRandom<>(NUMBER_SUIT_LIST);
        usedTiles = TreeMultiset.create();
        effectiveTileCount = 0;
        claims = new ArrayList<>();
        tiles = new ArrayList<>();
        // debug mode
        // 1. the same weight for each claim type
        debugMode = false;
    }

    public MCRHand generate() {
        return generate(getRandomFan());
    }

    MCRHand generate(MCRFan baseFan) {
        reset();
        return generateOnBaseFan(baseFan);
    }

    private static final MCRFan[] FANS;
    private static final int[] FAN_WEIGHTS;
    private static final List<Character> NUMBER_SUIT_LIST;
    private static final Tile[] ORPHANS, WINDS, DRAGONS;
    private static final Set<Tile>[] SUIT_TILE_SETS;
    private static final Set<Tile> TERMINAL_SET, HONOR_SET, ORPHAN_SET,
            NUMBER_SET,
            EVEN_SET,
            FIVE_SET, AROUND_FIVE_SET,
            REVERSIBLE_TILE_SET;
    private static final Set<Claim.Type> RANDOM_CLAIM_TYPES;

    static {
        FANS = MCRFan.values();
        // weights for fans
        FAN_WEIGHTS = new int[FANS.length];
        for (int i = 0; i < FANS.length; i++) {
            if (FANS[i] == LAST_TILE_DRAW || FANS[i] == LAST_TILE_CLAIM
                    || FANS[i] == OUT_WITH_REPLACEMENT_TILE || FANS[i] == ROBBING_THE_KONG
                    || FANS[i] == FLOWER_TILE) {
                FAN_WEIGHTS[i] = 0;
                continue;
            }
            switch (FANS[i].score) {
                case 88, 64 -> FAN_WEIGHTS[i] = 1;
                case 48 -> FAN_WEIGHTS[i] = 2;
                case 32 -> FAN_WEIGHTS[i] = 3;
                case 24 -> FAN_WEIGHTS[i] = 8;
                case 16, 12 -> FAN_WEIGHTS[i] = 16;
                case 8 -> FAN_WEIGHTS[i] = 32;
                case 6, 5 -> FAN_WEIGHTS[i] = 48;
                case 4, 2, 1 -> FAN_WEIGHTS[i] = 64;
            }
        }
        // suit list for 3 number suits
        NUMBER_SUIT_LIST = List.of('m', 'p', 's');
        // tile sets
        ORPHANS = new Tile[]{M1, M9, P1, P9, S1, S9, E, S, W, N, C, F, P};
        WINDS = new Tile[]{E, S, W, N};
        DRAGONS = new Tile[]{C, F, P};
        SUIT_TILE_SETS = new Set[]{
                Set.of(M1, M2, M3, M4, M5, M6, M7, M8, M9),
                Set.of(P1, P2, P3, P4, P5, P6, P7, P8, P9),
                Set.of(S1, S2, S3, S4, S5, S6, S7, S8, S9)
        };
        TERMINAL_SET = Set.of(M1, M9, P1, P9, S1, S9);
        HONOR_SET = Set.of(E, S, W, N, C, F, P);
        ORPHAN_SET = Set.of(M1, M9, P1, P9, S1, S9, E, S, W, N, C, F, P);
        NUMBER_SET = Set.of(
                M1, M2, M3, M4, M5, M6, M7, M8, M9,
                P1, P2, P3, P4, P5, P6, P7, P8, P9,
                S1, S2, S3, S4, S5, S6, S7, S8, S9
        );
        EVEN_SET = Set.of(M2, M4, M6, M8, P2, P4, P6, P8, S2, S4, S6, S8);
        FIVE_SET = Set.of(M5, P5, S5);
        REVERSIBLE_TILE_SET = Set.of(P1, P2, P3, P4, P5, P8, P9, S2, S4, S5, S6, S8, S9, P);
        AROUND_FIVE_SET = Set.of(M3, M4, M5, M6, M7, P3, P4, P5, P6, P7, S3, S4, S5, S6, S7);
        RANDOM_CLAIM_TYPES = Set.of(CHOW, PUNG, KONG);
    }

    private final WeightRandom<MCRFan> fanRandom;
    private final Random simpleRandom;
    private final WeightRandom<Wind> windRandom;
    private final WeightRandom<Character> suitRandom;
    private final SortedMultiset<Tile> usedTiles;
    private int effectiveTileCount;
    private final List<Claim> claims;
    private final List<Tile> tiles;
    private Tile declaredTile;
    private boolean selfDrawn, lastTile, lastDrawOrClaim, kong;
    private Wind prevalentWind, seatWind;

    boolean debugMode;

    private void reset() {
        usedTiles.clear();
        effectiveTileCount = 0;
        claims.clear();
        tiles.clear();
        declaredTile = null;
    }

    private MCRFan getRandomFan() {
        return fanRandom.next();
    }

    private MCRHand generateOnBaseFan(MCRFan baseFan) {
        // options must be generated beforehand
        // since they should allow the base Fan to override them
        generateRandomOptions();
        if (baseFan.score < 4) {
            // small base fan uses a special generation method
            generateOnSmallBaseFan();
        } else {
            // normal base fan (>= 4)
            generateOnNormalBaseFan(baseFan);
        }
        fixHand();
        return getHand();
    }

    private void generateRandomOptions() {
        // assign random options
        selfDrawn = simpleRandom.nextInt(3) == 0;
        lastTile = simpleRandom.nextInt(4) == 0;
        lastDrawOrClaim = simpleRandom.nextInt(8) == 0;
        kong = simpleRandom.nextInt(4) == 0;
        prevalentWind = windRandom.next();
        seatWind = windRandom.next();
    }

    private void generateOnNormalBaseFan(MCRFan baseFan) {
        switch (baseFan) {
            case BIG_FOUR_WINDS -> generateBigFourWinds();
            case BIG_THREE_DRAGONS -> generateBigThreeDragons();
            case ALL_GREEN -> generateAllGreen();
            case NINE_GATES -> generateNineGates();
            case FOUR_KONGS -> generateFourKongs();
            case SEVEN_SHIFTED_PAIRS -> generateSevenShiftedPairs();
            case THIRTEEN_ORPHANS -> generateThirteenOrphans();
            case ALL_TERMINALS -> generateAllTerminals();
            case LITTLE_FOUR_WINDS -> generateLittleFourWinds();
            case LITTLE_THREE_DRAGONS -> generateLittleThreeDragons();
            case ALL_HONORS -> generateAllHonors();
            case FOUR_CONCEALED_PUNGS -> generateFourConcealedPungs();
            case PURE_TERMINAL_CHOWS -> generatePureTerminalChows();
            case QUADRUPLE_CHOW -> generateQuadrupleChow();
            case FOUR_PURE_SHIFTED_PUNGS -> generateFourPureShiftedPungs();
            case FOUR_PURE_SHIFTED_CHOWS -> generateFourPureShiftedChows();
            case THREE_KONGS -> generateThreeKongs();
            case ALL_TERMINALS_AND_HONORS -> generateAllTerminalsAndHonors();
            case SEVEN_PAIRS -> generateSevenPairs();
            case GREATER_HONORS_AND_KNITTED_TILES -> generateGreaterHonorsAndKnittedTiles();
            case ALL_EVEN_PUNGS -> generateAllEvenPungs();
            case FULL_FLUSH -> generateFullFlush();
            case PURE_TRIPLE_CHOW -> generatePureTripleChow();
            case PURE_SHIFTED_PUNGS -> generatePureShiftedPungs();
            case UPPER_TILES -> generateUpperTiles();
            case MIDDLE_TILES -> generateMiddleTiles();
            case LOWER_TILES -> generateLowerTiles();
            case PURE_STRAIGHT -> generatePureStraight();
            case THREE_SUITED_TERMINAL_CHOWS -> generateThreeSuitedTerminalChows();
            case PURE_SHIFTED_CHOWS -> generatePureShiftedChows();
            case ALL_FIVE -> generateAllFive();
            case TRIPLE_PUNG -> generateTriplePung();
            case THREE_CONCEALED_PUNGS -> generateThreeConcealedPungs();
            case LESSER_HONORS_AND_KNITTED_TILES -> generateLesserHonorsAndKnittedTiles();
            case KNITTED_STRAIGHT -> generateKnittedStraight();
            case UPPER_FOUR -> generateUpperFour();
            case LOWER_FOUR -> generateLowerFour();
            case BIG_THREE_WINDS -> generateBigThreeWinds();
            case MIXED_STRAIGHT -> generateMixedStraight();
            case REVERSIBLE_TILES -> generateReversibleTiles();
            case MIXED_TRIPLE_CHOW -> generateMixedTripleChow();
            case MIXED_SHIFTED_PUNGS -> generateMixedShiftedPungs();
            case CHICKEN_HAND -> generateChickenHand();
            case ALL_PUNGS -> generateBigFourWinds();
            case HALF_FLUSH -> generateBigFourWinds();
            case MIXED_SHIFTED_CHOWS -> generateBigFourWinds();
            case ALL_TYPES -> generateBigFourWinds();
            case MELDED_HAND -> generateBigFourWinds();
            case TWO_CONCEALED_KONGS -> generateBigFourWinds();
            case TWO_DRAGON_PUNGS -> generateBigFourWinds();
            case CONCEALED_AND_MELDED_KONGS -> generateBigFourWinds();
            case OUTSIDE_HAND -> generateBigFourWinds();
            case FULLY_CONCEALED_HAND -> generateBigFourWinds();
            case TWO_MELDED_KONGS -> generateBigFourWinds();
            case LAST_TILE -> generateBigFourWinds();
            default -> throw new IllegalStateException("Unexpected fan: " + baseFan);
        }
    }

    private void generateOnSmallBaseFan() {
        // TODO: impl
        generateOnNormalBaseFan(BIG_FOUR_WINDS);
    }

    private void generateBigFourWinds() {
        appendPungKongOrTiles(E);
        appendPungKongOrTiles(S);
        appendPungKongOrTiles(W);
        appendPungKongOrTiles(N);
        generatePair();
    }

    private void generateBigThreeDragons() {
        appendPungKongOrTiles(C);
        appendPungKongOrTiles(F);
        appendPungKongOrTiles(P);
        generateClaim();
        generatePair();
    }

    private void generateAllGreen() {
        final Set<Tile> range = Set.of(S2, S3, S4, S6, S8, F);
        generateClaim(range);
        generateClaim(range);
        generateClaim(range);
        generateClaim(range);
        generatePair(range);
    }

    private void generateNineGates() {
        final char suit = suitRandom.next();
        appendNonDeclaredTile(getInstance(1, suit));
        appendNonDeclaredTile(getInstance(1, suit));
        appendNonDeclaredTile(getInstance(1, suit));
        appendNonDeclaredTile(getInstance(2, suit));
        appendNonDeclaredTile(getInstance(3, suit));
        appendNonDeclaredTile(getInstance(4, suit));
        appendNonDeclaredTile(getInstance(5, suit));
        appendNonDeclaredTile(getInstance(6, suit));
        appendNonDeclaredTile(getInstance(7, suit));
        appendNonDeclaredTile(getInstance(8, suit));
        appendNonDeclaredTile(getInstance(9, suit));
        appendNonDeclaredTile(getInstance(9, suit));
        appendNonDeclaredTile(getInstance(9, suit));
        appendDeclaredTile(getInstance(simpleRandom.nextInt(1, 10), suit));
    }

    private void generateFourKongs() {
        for (int i = 0; i < 4; i++) {
            generateClaim(KONG);
        }
        generatePair();
    }

    private void generateSevenShiftedPairs() {
        final int startIndex = simpleRandom.nextInt(1, 4);
        final char suit = suitRandom.next();
        for (int i = 0; i < 7; i++) {
            appendPair(getInstance(startIndex + i, suit));
        }
    }

    private void generateThirteenOrphans() {
        for (Tile tile : ORPHANS) {
            appendTile(tile);
        }
        appendTile(ORPHANS[simpleRandom.nextInt(ORPHANS.length)]);
    }

    private void generateAllTerminals() {
        for (int i = 0; i < 4; i++) {
            generateClaim(TERMINAL_SET);
        }
        generatePair(TERMINAL_SET);
    }

    private void generateLittleFourWinds() {
        final int windPairIndex = simpleRandom.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == windPairIndex) appendPair(WINDS[i]);
            else appendPungKongOrTiles(WINDS[i]);
        }
        generateClaim();
    }

    private void generateLittleThreeDragons() {
        final int dragonPairIndex = simpleRandom.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == dragonPairIndex) appendPair(DRAGONS[i]);
            else appendPungKongOrTiles(DRAGONS[i]);
        }
        generateClaim();
        generateClaim();
    }

    private void generateAllHonors() {
        for (int i = 0; i < 4; i++) {
            generateClaim(HONOR_SET);
        }
        generatePair(HONOR_SET);
    }

    private void generateFourConcealedPungs() {
        for (int i = 0; i < 4; i++) {
            generateConcealedPungOrKong();
        }
        generatePair();
    }

    private void generatePureTerminalChows() {
        final char suit = suitRandom.next();
        appendClaimOrTiles(CHOW, getInstance(1, suit));
        appendClaimOrTiles(CHOW, getInstance(1, suit));
        appendClaimOrTiles(CHOW, getInstance(7, suit));
        appendClaimOrTiles(CHOW, getInstance(7, suit));
        appendPair(getInstance(5, suit));
    }

    private void generateQuadrupleChow() {
        final int number = simpleRandom.nextInt(1, 8);
        final char suit = suitRandom.next();
        for (int i = 0; i < 4; i++) {
            appendClaimOrTiles(CHOW, getInstance(number, suit));
        }
        generatePair();
    }

    private void generateFourPureShiftedPungs() {
        final int number = simpleRandom.nextInt(1, 7);
        final char suit = suitRandom.next();
        for (int i = 0; i < 4; i++) {
            appendPungKongOrTiles(getInstance(number + i, suit));
        }
        generatePair();
    }

    private void generateFourPureShiftedChows() {
        final int diff = simpleRandom.nextInt(1, 3);
        final int number = simpleRandom.nextInt(1, 8 - 3 * diff);
        final char suit = suitRandom.next();
        for (int i = 0; i < 4; i++) {
            appendClaimOrTiles(CHOW, getInstance(number + i * diff, suit));
        }
        generatePair();
    }

    private void generateThreeKongs() {
        for (int i = 0; i < 3; i++) {
            generateClaim(KONG);
        }
        generateClaim();
        generatePair();
    }

    private void generateAllTerminalsAndHonors() {
        for (int i = 0; i < 4; i++) {
            generateClaim(ORPHAN_SET);
        }
        generatePair(ORPHAN_SET);
    }

    private void generateSevenPairs() {
        for (int i = 0; i < 7; i++) {
            generatePair();
        }
    }

    private void generateGreaterHonorsAndKnittedTiles() {
        for (Tile tile : HONOR_SET) {
            appendTile(tile);
        }
        // shuffle the suits for knitted tiles
        final List<Character> suits = getShuffledSuits();
        // pick 2 tiles absent in the hand
        final int absent1 = simpleRandom.nextInt(1, 10);
        int absent2;
        do {
            absent2 = simpleRandom.nextInt(1, 10);
        } while (absent1 == absent2);
        // add the 7 tiles left to the hand
        for (int i = 1; i <= 9; i++) {
            if (i == absent1 || i == absent2) continue;
            appendTile(getInstance(i, suits.get(i % 3)));
        }
    }

    private void generateAllEvenPungs() {
        for (int i = 0; i < 4; i++) {
            generatePungOrKong(EVEN_SET);
        }
        generatePair(EVEN_SET);
    }

    private void generateFullFlush() {
        final Set<Tile> tileRange = getRandomSuitTileSet();
        for (int i = 0; i < 4; i++) {
            generateClaim(tileRange);
        }
        generatePair(tileRange);
    }

    private void generatePureTripleChow() {
        final char suit = suitRandom.next();
        final int number = simpleRandom.nextInt(1, 8);
        for (int i = 0; i < 3; i++) {
            appendClaimOrTiles(CHOW, getInstance(number, suit));
        }
        generateClaim();
        generatePair();
    }

    private void generatePureShiftedPungs() {
        final char suit = suitRandom.next();
        final int number = simpleRandom.nextInt(1, 8);
        for (int i = 0; i < 3; i++) {
            appendPungKongOrTiles(getInstance(number + i, suit));
        }
        generateClaim();
        generatePair();
    }

    private void generateUpperTiles() {
        generateNumberHand(7, 9);
    }

    private void generateMiddleTiles() {
        generateNumberHand(4, 6);
    }

    private void generateLowerTiles() {
        generateNumberHand(1, 3);
    }

    private void generatePureStraight() {
        final char suit = suitRandom.next();
        for (int i = 0; i < 3; i++) {
            appendClaimOrTiles(CHOW, getInstance(i * 3 + 1, suit));
        }
        generateClaim();
        generatePair();
    }

    private void generateThreeSuitedTerminalChows() {
        final List<Character> suits = getShuffledSuits();
        appendClaimOrTiles(CHOW, getInstance(1, suits.getFirst()));
        appendClaimOrTiles(CHOW, getInstance(7, suits.getFirst()));
        appendClaimOrTiles(CHOW, getInstance(1, suits.getLast()));
        appendClaimOrTiles(CHOW, getInstance(7, suits.getLast()));
        appendPair(getInstance(5, suits.get(1)));
    }

    private void generatePureShiftedChows() {
        final int diff = simpleRandom.nextInt(1, 3);
        final int number = simpleRandom.nextInt(1, 8 - 2 * diff);
        final char suit = suitRandom.next();
        for (int i = 0; i < 3; i++) {
            appendClaimOrTiles(CHOW, getInstance(number + i * diff, suit));
        }
        generateClaim();
        generatePair();
    }

    private void generateAllFive() {
        // the pair has to be generated first to ensure that there are enough 5-tiles for the pair
        generatePair(FIVE_SET);
        // generate claims
        for (int i = 0; i < 4; i++) {
            // PUNGs/KONGs are available only when there are enough tiles
            final boolean mustBeChow = getUsableTiles(FIVE_SET, 3).isEmpty();
            if (mustBeChow || simpleRandom.nextInt(4) > 0) {
                generateChow(AROUND_FIVE_SET);
            } else {
                generatePungOrKong(FIVE_SET);
            }
        }
    }

    private void generateTriplePung() {
        final int number = simpleRandom.nextInt(1, 10);
        appendPungKongOrTiles(getInstance(number, 'm'));
        appendPungKongOrTiles(getInstance(number, 'p'));
        appendPungKongOrTiles(getInstance(number, 's'));
        generateClaim();
        generatePair();
    }

    private void generateThreeConcealedPungs() {
        for (int i = 0; i < 3; i++) {
            generateConcealedPungOrKong();
        }
        generateClaim();
        generatePair();
    }

    private void generateLesserHonorsAndKnittedTiles() {
        // get the pool of honors and knitted tiles
        final Set<Tile> tileRange = new HashSet<>(HONOR_SET);
        final List<Character> suits = getShuffledSuits();
        for (int i = 1; i <= 9; i++) {
            tileRange.add(getInstance(i, suits.get(i % 3)));
        }
        final PoolRandom<Tile> tilePool = new PoolRandom<>(tileRange);
        // randomly pick tiles from the pool
        for (int i = 0; i < 14; i++) {
            appendTile(tilePool.next());
        }
    }

    private void generateKnittedStraight() {
        // add knitted tiles
        final List<Character> suits = getShuffledSuits();
        for (int i = 1; i <= 9; i++) {
            appendTile(getInstance(i, suits.get(i % 3)));
        }
        // generate other tiles
        generateClaim();
        generatePair();
    }

    private void generateUpperFour() {
        generateNumberHand(6, 9);
    }

    private void generateLowerFour() {
        generateNumberHand(1, 4);
    }

    private void generateBigThreeWinds() {
        final int skipIndex = simpleRandom.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == skipIndex) continue;
            appendPungKongOrTiles(WINDS[i]);
        }
        generateClaim();
        generatePair();
    }

    private void generateMixedStraight() {
        final List<Character> suits = getShuffledSuits();
        for (int i = 0; i < 3; i++) {
            appendClaimOrTiles(CHOW, getInstance(i * 3 + 1, suits.get(i)));
        }
        generateClaim();
        generatePair();
    }

    private void generateReversibleTiles() {
        for (int i = 0; i < 4; i++) {
            generateClaim(REVERSIBLE_TILE_SET);
        }
        generatePair(REVERSIBLE_TILE_SET);
    }

    private void generateMixedTripleChow() {
        final List<Character> suits = getShuffledSuits();
        final int number = simpleRandom.nextInt(1, 8);
        for (int i = 0; i < 3; i++) {
            appendClaimOrTiles(CHOW, getInstance(number, suits.get(i)));
        }
        generateClaim();
        generatePair();
    }

    private void generateMixedShiftedPungs() {
        final List<Character> suits = getShuffledSuits();
        final int number = simpleRandom.nextInt(1, 8);
        for (int i = 0; i < 3; i++) {
            appendPungKongOrTiles(getInstance(number + i, suits.get(i)));
        }
        generateClaim();
        generatePair();
    }

    private void generateChickenHand() {
        // honor pair
        final Tile pairTile = getRandomTile(HONOR_SET);
        appendNonDeclaredTile(pairTile);
        appendNonDeclaredTile(pairTile);
        // claims/tiles
        final List<Claim> claims = new ArrayList<>();
        int concealedCount = 0, pungCount = 0;
        final WeightRandom<Claim.Type> claimTypeRandom = getClaimTypeRandom(Set.of(CHOW, PUNG));
        for (int i = 0; i < 4; i++) {
            // decide whether the claim is concealed
            // when there are 3 melded claims, the last one has to be concealed
            // when there is no declared tile, the last claim has to be tiles
            // (the concealed declared tile will turn into a melded one at last)
            // when there are 3 concealed claims (tiles), the last one has to be melded
            // (to make it not conflict with the second rule,
            // we force the third claim after 2 concealed claims to be melded)
            final boolean isConcealed = (i == 3 && concealedCount == 0)
                    || lastClaimMustBeTiles()
                    || (!(i == 2 && concealedCount == 2) && simpleRandom.nextBoolean());
            // generate the claim type and the start tile
            // when there is a concealed pung, there must not be another one
            // when there are 3 pungs, the last claim must not be a pung
            final Claim.Type claimType = (isConcealed && containsConcealedPung(claims))
                    || (pungCount == 3) ?
                    CHOW : claimTypeRandom.next();
            if (claimType == PUNG) pungCount++;
            final Set<Tile> tileRange = getTileRangeInChickenHand(claimType, claims);
            System.out.println(tileRange);
            System.out.println(claimType);
            final Set<Tile> actualTileRange = intersect(tileRange,
                    getUsableTilesByClaimType(ALL_TILE_SET, claimType));
            final Tile start = getRandomTile(actualTileRange);
            // append
            if (isConcealed) {
                if (claimType == CHOW)
                    appendConcealedChowWithoutUniqueWait(start);
                else
                    appendClaimOrTilesConcealed(claimType, start);
                concealedCount++;
            } else {
                appendClaim(claimType, start, false, true);
            }
            claims.add(Claim.create(claimType, start, 0,
                    isConcealed ? CLAIMED_FROM_SELF : CLAIMED_FROM_OTHER));
            System.out.println(claims);
        }
        // reset options
        selfDrawn = false;
        lastTile = false;
        lastDrawOrClaim = false;
        kong = false;
    }

    // utilities

    private boolean containsConcealedPung(List<Claim> claims) {
        for (Claim claim : claims) {
            if (claim.isConcealed() && claim.type() == PUNG)
                return true;
        }
        return false;
    }

    private void appendConcealedChowWithoutUniqueWait(Tile start) {
        final int number = start.number;
        final char suit = start.suit;
        // some tiles must not be declared to avoid unique wait
        if (number == 1) {
            // only 1 can be declared
            appendNonDeclaredTile(getInstance(2, suit));
            appendNonDeclaredTile(getInstance(3, suit));
            appendTile(start);
        } else if (number == 7) {
            // only 9 can be declared
            appendNonDeclaredTile(start);
            appendNonDeclaredTile(getInstance(8, suit));
            appendTile(getInstance(9, suit));
        } else {
            appendNonDeclaredTile(getInstance(number + 1, suit));
            appendTile(start);
            appendTile(getInstance(number + 2, suit));
        }
    }

    /**
     * Given the tiles/claims already generated,
     * get the range of tiles applicable to the specified claim type
     * in order to make a chicken hand.
     */
    private Set<Tile> getTileRangeInChickenHand(Claim.Type claimType, List<Claim> claims) {
        // decide which suits are available
        // there should be at most 1 suit that has >1 claims (tiles)
        final Set<Character> possibleSuits = getPossibleSuitsInChickenHand(claims);
        // initial tile range
        final Set<Tile> tileRange = new TreeSet<>();
        for (char suit : possibleSuits) {
            tileRange.addAll(SUIT_TILE_SETS[getSuitIndex(suit)]);
        }
        if (claimType == CHOW) {
            // remove impossible tiles by chow relations
            removeTilesByTwoChowRelations(tileRange, claims);
            removeTilesByThreeChowRelations(tileRange, claims);
        } else {
            // remove terminals
            for (char suit : possibleSuits) {
                tileRange.remove(getInstance(1, suit));
                tileRange.remove(getInstance(9, suit));
            }
            // remove impossible tiles by pung relations
            removeTilesByTwoPungRelations(tileRange, claims);
            removeTilesByThreePungRelations(tileRange, claims);
        }
        // remove impossible tiles by tile hog
        removeTilesByTileHog(tileRange, claimType, claims);
        return tileRange;
    }

    private void removeTilesByTileHog(Set<Tile> tileRange, Claim.Type claimType, List<Claim> claims) {
        final int requiredCount, tileOffsetMin;
        if (claimType == CHOW) {
            requiredCount = 1;
            // chows starting at (i-2) to (i) contains the tile i
            tileOffsetMin = -2;
        } else {
            requiredCount = 3;
            tileOffsetMin = 0;
        }
        // count tiles used in claims
        final SortedMultiset<Tile> usedTiles = TreeMultiset.create();
        for (Claim claim : claims) {
            usedTiles.addAll(Arrays.asList(claim.getTiles()));
        }
        // remove tiles that happen to make a tile hog
        for (Multiset.Entry<Tile> entry : usedTiles.entrySet()) {
            final Tile tile = entry.getElement();
            if (entry.getCount() + requiredCount == 4) {
                for (int i = Math.max(1, tile.number + tileOffsetMin); i <= tile.number; i++) {
                    tileRange.remove(getInstance(i, tile.suit));
                }
            }
        }
    }

    private static Set<Character> getPossibleSuitsInChickenHand(List<Claim> claims) {
        final Set<Character> possibleSuits = new HashSet<>(NUMBER_SUIT_LIST);
        final Set<Character> visitedSuits = new HashSet<>();
        boolean containsDupSuit = false;
        for (Claim claim : claims) {
            final char suit = claim.start().suit;
            if (visitedSuits.contains(suit)) {
                possibleSuits.remove(suit);
                containsDupSuit = true;
            }
            visitedSuits.add(suit);
        }
        if (containsDupSuit) {
            possibleSuits.removeAll(visitedSuits);
        }
        return possibleSuits;
    }

    private void removeTilesByTwoChowRelations(Set<Tile> tileRange, List<Claim> claims) {
        for (Claim claim : claims) {
            final char suit = claim.start().suit;
            final int number = claim.start().number;
            // short straight
            if (number >= 4) tileRange.remove(getInstance(number - 3, suit));
            if (number <= 4) tileRange.remove(getInstance(number + 3, suit));
            // double chow
            for (char s : NUMBER_SUIT_LIST) {
                tileRange.remove(getInstance(number, s));
            }
            // two terminal chows
            if (number == 1) tileRange.remove(getInstance(7, suit));
            else if (number == 7) tileRange.remove(getInstance(1, suit));
        }
    }

    private void removeTilesByThreeChowRelations(Set<Tile> tileRange, List<Claim> claims) {
        final List<Tile> starts = claims.stream()
                .filter(c -> c.type() == CHOW)
                .map(Claim::start)
                .toList();
        if (starts.size() < 2) return;
        for (int i = 0, bound = starts.size(); i < bound - 1; i++) {
            for (int j = i + 1; j < bound; j++) {
                final char suit1 = starts.get(i).suit;
                final int number1 = starts.get(i).number;
                final char suit2 = starts.get(j).suit;
                final int number2 = starts.get(j).number;
                final int small = Math.min(number1, number2);
                final int large = Math.max(number1, number2);
                if (suit1 != suit2) {
                    final char suit3 = getLastSuit(suit1, suit2);
                    // mixed shifted chows
                    switch (large - small) {
                        case 1 -> {
                            if (small >= 2) tileRange.remove(getInstance(small - 1, suit3));
                            if (large <= 6) tileRange.remove(getInstance(large + 1, suit3));
                        }
                        case 2 -> tileRange.remove(getInstance(small + 1, suit3));
                    }
                    // mixed straight (1, 4, 7)
                    if (large % 3 == 1 && small % 3 == 1) {
                        // large must not be the same as small by hypothesis
                        tileRange.remove(getInstance(12 - large - small, suit3));
                    }
                }
                // pure chows has been forbidden by preventing 3 claims in the same suit
                // triple chow has been forbidden by preventing double chow
            }
        }
    }

    private char getLastSuit(char suit1, char suit2) {
        if (suit1 == suit2)
            throw new IllegalArgumentException("Suits should be different");
        final Set<Character> suits = new HashSet<>(NUMBER_SUIT_LIST);
        suits.remove(suit1);
        suits.remove(suit2);
        return suits.stream().toList().getFirst();
    }

    private void removeTilesByTwoPungRelations(Set<Tile> tileRange, List<Claim> claims) {
        for (Claim claim : claims) {
            final int number = claim.start().number;
            // double pung
            for (char s : NUMBER_SUIT_LIST) {
                tileRange.remove(getInstance(number, s));
            }
        }
    }

    private void removeTilesByThreePungRelations(Set<Tile> tileRange, List<Claim> claims) {
        final List<Tile> starts = claims.stream()
                .filter(c -> c.type() == PUNG)
                .map(Claim::start)
                .sorted()
                .toList();
        if (starts.size() < 2) return;
        for (int i = 0, bound = starts.size(); i < bound - 1; i++) {
            for (int j = i + 1; j < bound; j++) {
                final char suit1 = starts.get(i).suit;
                final int number1 = starts.get(i).number;
                final char suit2 = starts.get(j).suit;
                final int number2 = starts.get(j).number;
                final int small = Math.min(number1, number2);
                final int large = Math.max(number1, number2);
                if (suit1 != suit2) {
                    final char suit3 = getLastSuit(suit1, suit2);
                    // mixed shifted pungs
                    switch (large - small) {
                        case 1 -> {
                            if (small >= 2) tileRange.remove(getInstance(small - 1, suit3));
                            if (large <= 8) tileRange.remove(getInstance(large + 1, suit3));
                        }
                        case 2 -> tileRange.remove(getInstance(small + 1, suit3));
                    }
                }
                // triple pung has been forbidden by preventing double pung
            }
        }
    }

    private List<Character> getShuffledSuits() {
        final List<Character> suits = new ArrayList<>(NUMBER_SUIT_LIST);
        Collections.shuffle(suits);
        return suits;
    }

    private void generateNumberHand(int min, int max) {
        final Set<Tile> tileRange = new HashSet<>();
        for (int i = min; i <= max; i++) {
            tileRange.add(getInstance(i, 'm'));
            tileRange.add(getInstance(i, 'p'));
            tileRange.add(getInstance(i, 's'));
        }
        for (int i = 0; i < 4; i++) {
            generateClaim(tileRange);
        }
        generatePair(tileRange);
    }

    private void generateClaim() {
        // generate a claim or 3 tiles
        generateClaim(ALL_TILE_SET);
    }

    private void generatePair() {
        // generate a pair
        generatePair(ALL_TILE_SET);
    }

    private void generateClaim(Set<Tile> tileRange) {
        generateClaim(tileRange, RANDOM_CLAIM_TYPES, false, false);
    }

    private void generateClaim(Claim.Type claimType) {
        generateClaim(ALL_TILE_SET, Set.of(claimType), false, false);
    }

    private void generateChow(Set<Tile> tileRange) {
        generateClaim(tileRange, Set.of(CHOW), false, false);
    }

    private void generatePungOrKong(Set<Tile> tileRange) {
        generateClaim(tileRange, Set.of(PUNG, KONG), false, false);
    }

    private void generateConcealedPungOrKong() {
        generateClaim(ALL_TILE_SET, Set.of(PUNG, KONG), true, false);
    }

    /**
     * Generate and append a random CHOW/PUNG/KONG.
     * Please make sure that claimTypeRange is a subset of RANDOM_CLAIM_TYPES.
     */
    private void generateClaim(Set<Tile> tileRange, Set<Claim.Type> claimTypeRange, boolean mustBeConcealed, boolean mustBeMelded) {
        // generate a claim or 3 tiles
        Set<Tile> newRange;
        Claim.Type actualClaimType;
        // try to find a possible claim type
        final Set<Claim.Type> possibleTypes = new HashSet<>(claimTypeRange);
        // when this is the last claim and has to be 3 tiles, KONG is forbidden
        if (lastClaimMustBeTiles()) possibleTypes.remove(KONG);
        // try to find a possible claim type
        do {
            final WeightRandom<Claim.Type> claimTypeRandom = getClaimTypeRandom(possibleTypes);
            actualClaimType = claimTypeRandom.next();
            newRange = getUsableTilesByClaimType(tileRange, actualClaimType);
            possibleTypes.remove(actualClaimType);
        } while (newRange.isEmpty());
        // append the claim/tiles
        final Tile start = getRandomTile(newRange);
        if (mustBeConcealed)
            appendClaimOrTilesConcealed(actualClaimType, start);
        else if (mustBeMelded)
            appendClaim(actualClaimType, start, false, true);
        else
            appendClaimOrTiles(actualClaimType, start);
    }

    private void generatePair(Set<Tile> range) {
        // generate a pair
        appendPair(getRandomTile(getUsableTiles(range, 2)));
    }

    private Tile getRandomTile(Set<Tile> range) {
        final WeightRandom<Tile> tileRandom = new WeightRandom<>(range.toArray(new Tile[0]));
        return tileRandom.next();
    }

    private Set<Tile> getUsableTilesByClaimType(Set<Tile> range, Claim.Type claimType) {
        return switch (claimType) {
            // how many tiles are required for each distinct tile in the claim
            case PUNG -> getUsableTiles(range, 3);
            case KONG -> getUsableTiles(range, 4);
            case CHOW -> getUsableTilesForChow(range, 1, 2);
            case KNITTED_CHOW -> getUsableTilesForChow(range, 3, 6);
        };
    }

    /**
     * Get the tiles whose remaining count is not less than requiredCount.
     */
    private Set<Tile> getUsableTiles(Set<Tile> range, int requiredCount) {
        final Set<Tile> tiles = new HashSet<>();
        for (Tile tile : range) {
            if (tile.isFlower()) continue;
            if (usedTiles.count(tile) <= 4 - requiredCount)
                tiles.add(tile);
        }
        return tiles;
    }

    /**
     * Get the tiles with which a chow can be made.
     * Here a chow refers to three non-honor/flower tiles T, T.shift(offset1), and T.shift(offset2),
     * where offset1 and offset2 are positive and different.
     */
    private Set<Tile> getUsableTilesForChow(Set<Tile> range, int offset1, int offset2) {
        if (offset1 <= 0 || offset2 <= 0 || offset1 == offset2)
            throw new IllegalArgumentException("Offsets should be positive and different");
        final Set<Tile> tiles = new HashSet<>();
        for (Tile tile : range) {
            if (tile.isFlower() || tile.isHonor()) continue;
            if (tile.number + offset1 <= 9
                    && tile.number + offset2 <= 9
                    && range.contains(tile.shift(offset1))
                    && range.contains(tile.shift(offset2))
                    && usedTiles.count(tile) <= 3
                    && usedTiles.count(tile.shift(offset1)) <= 3
                    && usedTiles.count(tile.shift(offset2)) <= 3)
                tiles.add(tile);
        }
        return tiles;
    }

    private void appendPungKongOrTiles(Tile start) {
        appendClaimOrTiles(getClaimTypeRandom(Set.of(PUNG, KONG)).next(), start);
    }

    private void appendClaimOrTiles(Claim.Type type, Tile start) {
        // when this is the last claim to add and the declared tile is null
        // the claim has to be added as 3 tiles
        if (type == KONG)
            appendKong(start, false, false);
        else
            appendClaimOrTiles(type, start, lastClaimMustBeTiles());
    }

    /**
     * Whether there is only one claim left and the claim must be 3 tiles.
     */
    private boolean lastClaimMustBeTiles() {
        return effectiveTileCount == 11 && declaredTile == null;
    }

    private void appendClaimOrTilesConcealed(Claim.Type type, Tile start) {
        // add the triple as a concealed claim (kong only) or 3 concealed tiles
        final Tile declaredTileBefore = declaredTile;
        if (type == KONG)
            appendKong(start, true, false);
        else
            appendClaimOrTiles(type, start, true);
        // if the claim includes the declared tile, the declared tile should be concealed
        if (declaredTileBefore == null && declaredTile != null) selfDrawn = true;
    }

    /**
     * Append a CHOW/PUNG as a claim or 3 tiles.
     * This is not applicable to KONGs.
     * For KONGs, use appendKong.
     */
    private void appendClaimOrTiles(Claim.Type type, Tile start, boolean mustBeTiles) {
        // add a claim or 3 tiles
        final Claim tmpClaim = Claim.create(type, start, 0, 0);
        // kong must be a claim
        if (type == KONG) {
            throw new IllegalArgumentException("This method is not applicable to KONGs");
        }
        // knitted chow must not be a claim
        // other types of claims decide if they are concealed randomly
        if (type != KNITTED_CHOW && !mustBeTiles && simpleRandom.nextBoolean()) {
            // claim
            appendClaim(type, start, false, false);
        } else {
            // non-claim tiles
            for (Tile tile : tmpClaim.getTiles()) {
                appendTile(tile);
            }
        }
    }

    private void appendKong(Tile start, boolean mustBeConcealed, boolean mustBeMelded) {
        appendClaim(KONG, start, mustBeConcealed, mustBeMelded);
    }

    private void appendPair(Tile tile) {
        appendTile(tile);
        appendTile(tile);
    }

    private void appendClaim(Claim.Type type, Tile start, boolean mustBeConcealed, boolean mustBeMelded) {
        if (mustBeConcealed && mustBeMelded)
            throw new IllegalArgumentException("The arguments mustBeConcealed and mustBeMelded should not be both true");
        if (mustBeConcealed && type != KONG)
            throw new IllegalArgumentException("Non-kong claims cannot be concealed");
        switch (type) {
            case CHOW -> {
                claims.add(Claim.create(type, start, simpleRandom.nextInt(3), CLAIMED_FROM_LEFT));
                usedTiles.add(start);
                usedTiles.add(start.shift(1));
                usedTiles.add(start.shift(2));
            }
            case PUNG -> {
                claims.add(Claim.create(type, start, 0, simpleRandom.nextInt(1, 4)));
                usedTiles.add(start, 3);
            }
            case KONG -> {
                // mustBeConcealed/mustBeMelded only works for KONG
                claims.add(Claim.create(type, start, 0,
                        mustBeConcealed ? 0 :
                                (mustBeMelded ?
                                        simpleRandom.nextInt(1, 4) :
                                        simpleRandom.nextInt(0, 4))
                ));
                usedTiles.add(start, 4);
            }
        }
        effectiveTileCount += 3;
    }

    private void appendTile(Tile tile) {
        if (declaredTile == null && (effectiveTileCount == 13 || simpleRandom.nextInt(4) == 0)) {
            // 1/4 chance for declared tile
            // or the last tile must be the declared tile
            appendDeclaredTile(tile);
        } else {
            // non-declared tile
            appendNonDeclaredTile(tile);
        }
    }

    private void appendDeclaredTile(Tile tile) {
        declaredTile = tile;
        usedTiles.add(tile);
        effectiveTileCount++;
    }

    private void appendNonDeclaredTile(Tile tile) {
        tiles.add(tile);
        usedTiles.add(tile);
        effectiveTileCount++;
    }

    private void fixHand() {
        final MCRHand hand = getTmpHand();
        // make sure that options do not conflict with the hand of tiles
        // fix lastTile
        if (Claim.getTiles(claims).count(declaredTile) == 3) {
            lastTile = true;
        } else if (hand.getConcealedTiles().count(declaredTile) > 0) {
            lastTile = false;
        }
        // fix selfDrawn
        if (kong) {
            final boolean selfDrawnDisallowed = hand.claimsOfType(Claim.Type.KONG).isEmpty();
            final boolean selfDrawnForced = hand.getHandTilesWithClaims().count(declaredTile) > 1;
            if (selfDrawnDisallowed && selfDrawnForced) {
                // kong causes an unresolvable conflict; disable kong
                kong = false;
            } else if (selfDrawnDisallowed) {
                selfDrawn = false;
            } else if (selfDrawnForced) {
                selfDrawn = true;
            }
        }
        // robbing the kong
        if (kong && !selfDrawn) {
            if (!lastTile) kong = false;
            lastDrawOrClaim = false;
        }
    }

    private WeightRandom<Claim.Type> getClaimTypeRandom(Set<Claim.Type> types) {
        final Claim.Type[] typeArray = types.toArray(new Claim.Type[0]);
        final int[] weights = new int[typeArray.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = switch (typeArray[i]) {
                case CHOW -> 32;
                case PUNG -> 16;
                case KONG -> 2;
                case KNITTED_CHOW -> 1;
            };
        }
        // in debug mode, the weights of all claim types are the same
        return debugMode ? new WeightRandom<>(typeArray) : new WeightRandom<>(typeArray, weights);
    }

    private Set<Tile> getRandomSuitTileSet() {
        final int suitIndex = simpleRandom.nextInt(3);
        return SUIT_TILE_SETS[suitIndex];
    }

    private int getSuitIndex(char suit) {
        return switch (suit) {
            case 'm' -> 0;
            case 'p' -> 1;
            case 's' -> 2;
            default -> throw new IllegalArgumentException("Invalid suit " + suit);
        };
    }

    private MCRHand getTmpHand() {
        return new MCRHand(new Tile[0], claims.toArray(new Claim[0]), tiles.toArray(new Tile[0]), declaredTile, true, true);
    }

    private MCRHand getHand() {
        System.out.println(claims);
        System.out.println(tiles);
        System.out.println(declaredTile);
        final MCRHand hand = new MCRHand(new Tile[0], claims.toArray(new Claim[0]), tiles.toArray(new Tile[0]), declaredTile,
                selfDrawn, lastTile, lastDrawOrClaim, kong, prevalentWind, seatWind);
        hand.sort();
        return hand;
    }

}
