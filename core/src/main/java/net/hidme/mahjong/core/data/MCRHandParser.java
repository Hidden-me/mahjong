package net.hidme.mahjong.core.data;

import java.text.ParseException;
import java.util.*;

/**
 * A parser for a hand of tiles in MCR.
 * The format is {@code flowers;claims;tiles;wind,wind,self-drawn,last-tile,last-draw-or-claim,kong},
 * where tiles are in a row without separation,
 * the first wind is the prevalent wind,
 * the second wind is the seat wind,
 * and the rest are binary values (i.e. '0' or '1').
 * Claims should be separated by ",".
 * The declared tile is the rightmost tile.
 * <p>
 *     Each tile should be among one of the following:
 *     <ul>
 *         <li>Number tiles: 1s-9s/1m-9m/1p-9p</li>
 *         <li>Wind tiles: E/S/W/N</li>
 *         <li>Dragon tiles: P/F/C</li>
 *         <li>Flower tiles: 1f-8f</li>
 *     </ul>
 *     For number and flower tiles, tiles of the same suit can be written in a row without repetition of the suit.
 *     For example, "123s" stands for bamboo tiles with index 1 to 3.
 * </p>
 * <p>
 *     A claim consists of tiles and a number.
 *     <ul>
 *         <li>
 *             For chows, the number stands for the index of the claimed tile.
 *             For example, "123s0" is a chow where "1s" is from the player on the left.
 *         </li>
 *         <li>
 *             For pungs/kongs, from which player the claimed tile comes.
 *             We use 0 for the current player, 1 for the player on the right,
 *             2 for the player on the opposite, and 3 for the player on the left.
 *             For example, "999s2" is a pung where the claimed tile is from the player on the opposite.
 *             Another example is "9999m0", a concealed kong.
 *         </li>
 *     </ul>
 * </p>
 * <p>
 *     A case study: "12f;9999m0;11m234567891s;E,E,1,0,0,1" is worth 35 Fan:
 *     <ul>
 *         <li>pure straight (16)
 *         <li>out with replacement tile (8)
 *         <li>fully concealed hand (4)
 *         <li>concealed kong (2)
 *         <li>pung of terminals or honors (1)
 *         <li>one voided suit (1)
 *         <li>no honors (1)
 *         <li>flowers (1) *2
 *     </ul>
 * </p>
 */
public class MCRHandParser {

    public MCRHand parse(String string) throws ParseException {
        try {
            final String[] segments = string.split(";");
            final Tile[] flowers = parseTiles(segments[0]).toArray(new Tile[0]);
            final Claim[] claims = parseClaims(segments[1]).toArray(new Claim[0]);
            final List<Tile> tileList = parseTiles(segments[2]);
            final Tile[] tiles = tileList.subList(0, tileList.size() - 1).toArray(new Tile[0]);
            final Tile declaredTile = tileList.getLast();
            // besides tiles
            final String[] options = segments[3].split(",");
            final Wind prevalentWind = Wind.parse(options[0]);
            final Wind seatWind = Wind.parse(options[1]);
            final boolean selfDrawn = parseBinary(options[2]);
            final boolean lastTile = parseBinary(options[3]);
            final boolean lastDrawOrClaim = parseBinary(options[4]);
            final boolean kong = parseBinary(options[5]);
            return new MCRHand(flowers, claims, tiles, declaredTile,
                    selfDrawn, lastTile, lastDrawOrClaim, kong, prevalentWind, seatWind);
        } catch (ParseException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ParseException(e.getMessage(), -1);
        }
    }

    private List<Claim> parseClaims(String string) throws ParseException {
        final List<Claim> claims = new ArrayList<>();
        if (string.isEmpty()) return claims;
        final String[] segments = string.split(",");
        for (String segment : segments) {
            claims.add(parseClaim(segment));
        }
        return claims;
    }

    private Claim parseClaim(String string) throws ParseException {
        // the last char is the index
        int index = string.charAt(string.length() - 1) - '0';
        // the rest are tiles
        final List<Tile> tiles = parseTiles(string.substring(0, string.length() - 1));
        // derive the claim type
        final Claim.Type type = Claim.getType(tiles);
        final SortedSet<Tile> sortedTiles = new TreeSet<>(tiles);
        final Tile startTile = sortedTiles.first();
        final int claimedIndex = type == Claim.Type.CHOW ? index : 0;
        final int claimedFrom = type == Claim.Type.CHOW ? 3 : index;
        if (claimedIndex < 0 || claimedIndex > 2 || claimedFrom < 0 || claimedFrom > 3)
            throw new ParseException("invalid claim", -1);
        return new Claim(type, startTile, claimedIndex, claimedFrom);
    }

    private List<Tile> parseTiles(String string) throws ParseException {
        final List<Tile> tiles = new ArrayList<>();
        int index = 0;
        while(index < string.length()) {
            index = parseFirstTileGroup(string, index, tiles);
        }
        return tiles;
    }

    // like 123456p or E
    // return the end location of tile group + 1
    private int parseFirstTileGroup(String string, int fromIndex, List<Tile> tiles) throws ParseException {
        // case: the first tile is an honor tile
        char[] honors = {'E', 'S', 'W', 'N', 'P', 'F', 'C'};
        for (char honor : honors) {
            if (string.charAt(fromIndex) == honor) {
                tiles.add(Tile.parseHonor(honor));
                return fromIndex + 1;
            }
        }
        // case: the first tile is a number/flower tile
        char[] suits = {'m', 'p', 's', 'f'};
        int index = -1;
        char suit = 0;
        for (char tmpSuit : suits) {
            int tmpIndex = string.indexOf(tmpSuit, fromIndex);
            if (tmpIndex != -1 && (index == -1 || index > tmpIndex)) {
                index = tmpIndex;
                suit = tmpSuit;
            }
        }
        if (index != -1) {
            for (int i = fromIndex; i < index; i++) {
                int number = string.charAt(i) - '0';
                tiles.add(Tile.getInstance(number, suit));
            }
            return index + 1;
        }
        throw new ParseException("No tile group found in" + string, fromIndex);
    }

    private boolean parseBinary(String string) {
        return !"0".equals(string);
    }

}
