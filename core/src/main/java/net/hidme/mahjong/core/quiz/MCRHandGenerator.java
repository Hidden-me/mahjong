package net.hidme.mahjong.core.quiz;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.*;

import java.util.*;

import static net.hidme.mahjong.core.data.Claim.Type.*;
import static net.hidme.mahjong.core.data.MCRFan.*;
import static net.hidme.mahjong.core.data.Tile.*;

public class MCRHandGenerator {

    public MCRHandGenerator() {
        fanRandom = new WeightRandom<>(FANS, FAN_WEIGHTS);
        simpleRandom = new Random();
        windRandom = new WeightRandom<>(Wind.values());
        usedTiles = TreeMultiset.create();
        effectiveTileCount = 0;
        claims = new ArrayList<>();
        tiles = new ArrayList<>();
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
    private static final Tile[] ORPHANS, WINDS;
    private static final Set<Tile> TERMINAL_SET, HONOR_SET, ORPHAN_SET;

    static {
        FANS = MCRFan.values();
        FAN_WEIGHTS = new int[FANS.length];
        for (int i = 0; i < FANS.length; i++) {
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
        ORPHANS = new Tile[]{M1, M9, P1, P9, S1, S9, E, S, W, N, C, F, P};
        WINDS = new Tile[]{E, S, W, N};
        TERMINAL_SET = Set.of(M1, M9, P1, P9, S1, S9);
        HONOR_SET = Set.of(E, S, W, N, C, F, P);
        ORPHAN_SET = Set.of(M1, M9, P1, P9, S1, S9, E, S, W, N, C, F, P);
    }

    private final WeightRandom<MCRFan> fanRandom;
    private final Random simpleRandom;
    private final WeightRandom<Wind> windRandom;
    private final SortedMultiset<Tile> usedTiles;
    private int effectiveTileCount;
    private final List<Claim> claims;
    private final List<Tile> tiles;
    private Tile declaredTile;
    private boolean selfDrawn, lastTile, lastDrawOrClaim, kong;
    private Wind prevalentWind, seatWind;

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
            case LITTLE_THREE_DRAGONS -> generateBigFourWinds();
            case ALL_HONORS -> generateBigFourWinds();
            case FOUR_CONCEALED_PUNGS -> generateBigFourWinds();
            case PURE_TERMINAL_CHOWS -> generateBigFourWinds();
            case QUADRUPLE_CHOW -> generateBigFourWinds();
            case FOUR_PURE_SHIFTED_PUNGS -> generateBigFourWinds();
            case FOUR_PURE_SHIFTED_CHOWS -> generateBigFourWinds();
            case THREE_KONGS -> generateBigFourWinds();
            case ALL_TERMINALS_AND_HONORS -> generateBigFourWinds();
            case SEVEN_PAIRS -> generateBigFourWinds();
            case GREATER_HONORS_AND_KNITTED_TILES -> generateBigFourWinds();
            case ALL_EVEN_PUNGS -> generateBigFourWinds();
            case FULL_FLUSH -> generateBigFourWinds();
            case PURE_TRIPLE_CHOW -> generateBigFourWinds();
            case PURE_SHIFTED_PUNGS -> generateBigFourWinds();
            case UPPER_TILES -> generateBigFourWinds();
            case MIDDLE_TILES -> generateBigFourWinds();
            case LOWER_TILES -> generateBigFourWinds();
            case PURE_STRAIGHT -> generateBigFourWinds();
            case THREE_SUITED_TERMINAL_CHOWS -> generateBigFourWinds();
            case PURE_SHIFTED_CHOWS -> generateBigFourWinds();
            case ALL_FIVE -> generateBigFourWinds();
            case TRIPLE_PUNG -> generateBigFourWinds();
            case THREE_CONCEALED_PUNGS -> generateBigFourWinds();
            case LESSER_HONORS_AND_KNITTED_TILES -> generateBigFourWinds();
            case KNITTED_STRAIGHT -> generateBigFourWinds();
            case UPPER_FOUR -> generateBigFourWinds();
            case LOWER_FOUR -> generateBigFourWinds();
            case BIG_THREE_WINDS -> generateBigFourWinds();
            case MIXED_STRAIGHT -> generateBigFourWinds();
            case REVERSIBLE_TILES -> generateBigFourWinds();
            case MIXED_TRIPLE_CHOW -> generateBigFourWinds();
            case MIXED_SHIFTED_PUNGS -> generateBigFourWinds();
            case CHICKEN_HAND -> generateBigFourWinds();
            case LAST_TILE_DRAW -> generateBigFourWinds();
            case LAST_TILE_CLAIM -> generateBigFourWinds();
            case OUT_WITH_REPLACEMENT_TILE -> generateBigFourWinds();
            case ROBBING_THE_KONG -> generateBigFourWinds();
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
            default -> throw new IllegalStateException("Unexpected value: " + baseFan);
        }
    }

    private void generateOnSmallBaseFan() {
        // TODO: impl
        generateOnNormalBaseFan(BIG_FOUR_WINDS);
    }

    private void generateBigFourWinds() {
        appendClaimOrTiles(PUNG, E);
        appendClaimOrTiles(PUNG, S);
        appendClaimOrTiles(PUNG, W);
        appendClaimOrTiles(PUNG, N);
        generatePair();
    }

    private void generateBigThreeDragons() {
        appendClaimOrTiles(PUNG, C);
        appendClaimOrTiles(PUNG, F);
        appendClaimOrTiles(PUNG, P);
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
        final WeightRandom<Character> suitRandom = new WeightRandom<>(new Character[]{'m', 'p', 's'});
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
        final WeightRandom<Character> suitRandom = new WeightRandom<>(new Character[]{'m', 'p', 's'});
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
            else appendClaimOrTiles(PUNG, WINDS[i]);
        }
        generateClaim();
    }

    private void generateClaim() {
        // generate a claim or 3 tiles
        generateClaim(ALL_TILE_SET);
    }

    private void generatePair() {
        // generate a pair
        generatePair(ALL_TILE_SET);
    }

    private void generateClaim(Set<Tile> range) {
        // generate a claim or 3 tiles
        Set<Tile> newRange;
        Set<Claim.Type> possibleTypes = new HashSet<>(List.of(CHOW, PUNG, KONG));
        Claim.Type claimType;
        // try until a possible claim type is found
        do {
            final WeightRandom<Claim.Type> claimTypeRandom = getClaimTypeRandom(possibleTypes);
            claimType = claimTypeRandom.next();
            newRange = switch (claimType) {
                // how many tiles are required for each distinct tile in the claim
                case PUNG -> getUsableTiles(range, 3);
                case KONG -> getUsableTiles(range, 4);
                case CHOW -> getUsableTilesForChow(range, 1, 2);
                case KNITTED_CHOW -> getUsableTilesForChow(range, 3, 6);
            };
            if (newRange.isEmpty()) possibleTypes.remove(claimType);
        } while (newRange.isEmpty());
        if (possibleTypes.isEmpty()) {
            throw new IllegalStateException("No possible claim type; current hand: " + getTmpHand());
        }
        // append the claim/tiles
        appendClaimOrTiles(claimType, getRandomTile(newRange));
    }

    private void generateClaim(Claim.Type claimType) {
        // generate a claim or 3 tiles
        final Set<Tile> newRange = switch (claimType) {
            // how many tiles are required for each distinct tile in the claim
            case PUNG -> getUsableTiles(ALL_TILE_SET, 3);
            case KONG -> getUsableTiles(ALL_TILE_SET, 4);
            case CHOW -> getUsableTilesForChow(ALL_TILE_SET, 1, 2);
            case KNITTED_CHOW -> getUsableTilesForChow(ALL_TILE_SET, 3, 6);
        };
        if (newRange.isEmpty()) {
            throw new IllegalStateException("No possible claim of type " + claimType + "; current hand: " + getTmpHand());
        }
        // append the claim/tiles
        appendClaimOrTiles(claimType, getRandomTile(newRange));
    }

    private void generatePair(Set<Tile> range) {
        // generate a pair
        appendPair(getRandomTile(getUsableTiles(range, 2)));
    }

    private Tile getRandomTile(Set<Tile> range) {
        final WeightRandom<Tile> tileRandom = new WeightRandom<>(range.toArray(new Tile[0]));
        return tileRandom.next();
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

    private void appendClaimOrTiles(Claim.Type type, Tile start) {
        // when this is the last claim to add and the declared tile is null
        // the claim has to be added as 3 tiles
        appendClaimOrTiles(type, start, effectiveTileCount == 11 && declaredTile == null);
    }

    private void appendClaimOrTilesConcealed(Claim.Type type, Tile start) {
        // add the triple as a concealed claim (kong only) or 3 concealed tiles
        final Tile declaredTileBefore = declaredTile;
        appendClaimOrTiles(type, start, true);
        // if the claim includes the declared tile, the declared tile should be concealed
        if (declaredTileBefore == null && declaredTile != null) selfDrawn = true;
    }

    private void appendClaimOrTiles(Claim.Type type, Tile start, boolean mustBeTiles) {
        // add a claim or 3 tiles
        final Claim tmpClaim = new Claim(type, start, 0, 0);
        // kong must be a claim
        if (type == KONG) {
            appendClaim(type, start, mustBeTiles);
            return;
        }
        // knitted chow must not be a claim
        // claims other than kongs must not be concealed
        if (type != KNITTED_CHOW && !mustBeTiles && simpleRandom.nextBoolean()) {
            // claim
            appendClaim(type, start, mustBeTiles);
        } else {
            // non-claim tiles
            for (Tile tile : tmpClaim.getTiles()) {
                appendTile(tile);
            }
        }
    }

    private void appendPair(Tile tile) {
        appendTile(tile);
        appendTile(tile);
    }

    private void appendClaim(Claim.Type type, Tile start, boolean mustBeConcealed) {
        switch (type) {
            case CHOW -> {
                claims.add(new Claim(type, start, simpleRandom.nextInt(3), 3));
                usedTiles.add(start);
                usedTiles.add(start.shift(1));
                usedTiles.add(start.shift(2));
            }
            case PUNG -> {
                claims.add(new Claim(type, start, 0, simpleRandom.nextInt(1, 4)));
                usedTiles.add(start, 3);
            }
            case KONG -> {
                // mustBeConcealed only works for KONG
                claims.add(new Claim(type, start, 0, mustBeConcealed ? 0 : simpleRandom.nextInt(0, 4)));
                usedTiles.add(start, 4);
            }
        }
        effectiveTileCount += 3;
    }

    private void appendTile(Tile tile) {
        if (declaredTile == null && (effectiveTileCount == 13 || simpleRandom.nextInt(4) == 0)) {
            // declared tile
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
        return new WeightRandom<>(typeArray, weights);
    }

    private MCRHand getTmpHand() {
        return new MCRHand(new Tile[0], claims.toArray(new Claim[0]), tiles.toArray(new Tile[0]), declaredTile, true, true);
    }

    private MCRHand getHand() {
        System.out.println(claims);
        System.out.println(tiles);
        System.out.println(declaredTile);
        return new MCRHand(new Tile[0], claims.toArray(new Claim[0]), tiles.toArray(new Tile[0]), declaredTile,
                selfDrawn, lastTile, lastDrawOrClaim, kong, prevalentWind, seatWind);
    }

}
