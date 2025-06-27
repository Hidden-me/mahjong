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
        fanRandom = new WeightRandom<>(fans, fanWeights);
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

    private static final MCRFan[] fans;
    private static final int[] fanWeights;

    static {
        fans = MCRFan.values();
        fanWeights = new int[fans.length];
        for (int i = 0; i < fans.length; i++) {
            switch (fans[i].score) {
                case 88, 64 -> fanWeights[i] = 1;
                case 48 -> fanWeights[i] = 2;
                case 32 -> fanWeights[i] = 3;
                case 24 -> fanWeights[i] = 8;
                case 16, 12 -> fanWeights[i] = 16;
                case 8 -> fanWeights[i] = 32;
                case 6, 5 -> fanWeights[i] = 48;
                case 4, 2, 1 -> fanWeights[i] = 64;
            }
        }
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
            case NINE_GATES -> generateBigFourWinds();
            case FOUR_KONGS -> generateBigFourWinds();
            case SEVEN_SHIFTED_PAIRS -> generateBigFourWinds();
            case THIRTEEN_ORPHANS -> generateBigFourWinds();
            case ALL_TERMINALS -> generateBigFourWinds();
            case LITTLE_FOUR_WINDS -> generateBigFourWinds();
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
            final WeightRandom<Claim.Type> claimTypeRandom = new WeightRandom<>(possibleTypes.toArray(new Claim.Type[0]));
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
        appendClaimOrTiles(type, start, false);
    }

    private void appendClaimOrTilesConcealed(Claim.Type type, Tile start) {
        // add the triple as a concealed claim (kong only) or 3 tiles
        appendClaimOrTiles(type, start, true);
    }

    private void appendClaimOrTiles(Claim.Type type, Tile start, boolean mustBeConcealed) {
        // add a claim or 3 tiles
        final Claim tmpClaim = new Claim(type, start, 0, 0);
        // kong must be a claim
        if (type == KONG) {
            appendClaim(type, start, mustBeConcealed);
            return;
        }
        // knitted chow must not be a claim
        // claims other than kongs must not be concealed
        if (type != KNITTED_CHOW && !mustBeConcealed && simpleRandom.nextBoolean()) {
            // claim
            appendClaim(type, start, mustBeConcealed);
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
            declaredTile = tile;
        } else {
            // non-declared tile
            tiles.add(tile);
        }
        usedTiles.add(tile);
        effectiveTileCount++;
    }

    private void fixHand() {
        final MCRHand hand = getTmpHand();
        // make sure that options do not conflict with the hand of tiles
        if (Claim.getTiles(claims).count(declaredTile) == 3) {
            lastTile = true;
        } else if (hand.getConcealedTiles().count(declaredTile) > 0) {
            lastTile = false;
        }
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
