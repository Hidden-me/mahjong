package net.hidme.mahjong.core.calc;

import com.google.common.collect.*;
import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.Tile;

import java.util.*;

public class MCRStructureAnalyzer {

    public List<HandStructure> getPossibleStructures(MCRHand hand) {
        final List<HandStructure> structures = new LinkedList<>();
        addNormalStructure(hand, structures);
        addPairStructure(hand, structures);
        addOrphanStructure(hand, structures);
        addHonorKnittedStructure(hand, structures);
        if (!hand.selfDrawn)
            setSourceOfDeclaredTile(structures, hand.declaredTile);
        return structures;
    }

    // assign claimedFrom of the declared claim if not self-drawn
    private void setSourceOfDeclaredTile(List<HandStructure> structures, Tile declaredTile) {
        final List<HandStructure> oldStructures = new ArrayList<>(structures);
        for (HandStructure structure : oldStructures) {
            if (structure instanceof NormalHandStructure normalStruct) {
                boolean added = false;
                for (int i = 0, bound = normalStruct.claims.length; i < bound; i++) {
                    final Claim claim = normalStruct.claims[i];
                    if (claim.claimedFrom() == 0 && claim.getTileSet().contains(declaredTile)) {
                        // Notes:
                        // Such a claim must be in hand.
                        // A concealed kong must not contain the declared tile,
                        // since it must use up all 4 tiles.
                        final NormalHandStructure newStruct = new NormalHandStructure(normalStruct);
                        newStruct.claims[i] = new Claim(newStruct.claims[i].type(),
                                newStruct.claims[i].start(),
                                newStruct.claims[i].claimedIndex(),
                                1);
                        structures.add(newStruct);
                        added = true;
                    }
                }
                if (added) structures.remove(structure);
            }
        }
    }

    private void addNormalStructure(MCRHand hand, List<HandStructure> structures) {
        final List<NormalHandStructure> normalStructs = new ArrayList<>();
        // check possible knitted straights
        addNormalStructureWithKnittedStraight(hand.getHandTiles(), normalStructs);
        // ignore knitted straight
        addNormalStructureWithoutKnittedStraight(hand.getHandTiles(), normalStructs);
        // add fixed claims
        normalStructs.replaceAll(s -> new NormalHandStructure(
                new NormalHandStructure(hand.claims, null),
                s
        ));
        structures.addAll(normalStructs);
    }

    private void addNormalStructureWithKnittedStraight(SortedMultiset<Tile> tiles, List<NormalHandStructure> normalStructs) {
        final SortedMultiset<Tile> newTilesMultiset = TreeMultiset.create(tiles);
        final List<NormalHandStructure> knittedStraights = getKnittedStraights(newTilesMultiset);
        for (NormalHandStructure knittedStraight : knittedStraights) {
            // remove the knitted straight
            final SortedMultiset<Tile> remainingTiles = removeTiles(newTilesMultiset, knittedStraight);
            // get possible structures of the rest of tiles
            final List<NormalHandStructure> structsWOKnittedStraight = new ArrayList<>();
            addNormalStructureWithoutKnittedStraight(remainingTiles, structsWOKnittedStraight);
            // combine knitted straight with those structures
            for (NormalHandStructure struct : structsWOKnittedStraight) {
                normalStructs.add(new NormalHandStructure(knittedStraight, struct));
            }
        }
    }

    // remove tiles in structure from tiles; the removal is not in-place
    private SortedMultiset<Tile> removeTiles(SortedMultiset<Tile> tiles, NormalHandStructure structure) {
        final SortedMultiset<Tile> ret = TreeMultiset.create(tiles);
        for (Claim claim : structure.claims) {
            removeClaim(ret, claim);
        }
        return ret;
    }

    private void removeClaim(SortedMultiset<Tile> tiles, Claim claim) {
        for (Tile tile : claim.getTiles()) {
            tiles.remove(tile);
        }
    }

    private List<NormalHandStructure> getKnittedStraights(SortedMultiset<Tile> tiles) {
        final List<NormalHandStructure> structures = new ArrayList<>();
        getKnittedStraightsRecursive(tiles, 1, new LinkedHashSet<>(), structures);
        return structures;
    }

    // start: 3 for 369, 1 for 147, 2 for 258
    // suits: the suits to which a knitted chow has been assigned
    // structures: the possible structure of the removed knitted straight
    private void getKnittedStraightsRecursive(SortedMultiset<Tile> tiles, int start, LinkedHashSet<Character> suits, List<NormalHandStructure> structures) {
        if (start == 4) {
            final Claim[] claims = new Claim[3];
            int i = 0;
            for (char suit : suits) {
                claims[i] = new Claim(Claim.Type.KNITTED_CHOW, Tile.getInstance(i + 1, suit), 0, 0);
                i++;
            }
            structures.add(new NormalHandStructure(claims, null));
            return;
        }
        for (char suit : new char[]{'m', 'p', 's'}) {
            if (!suits.contains(suit) && containsKnittedChowOfSuit(tiles, start, suit)) {
                suits.add(suit);
                getKnittedStraightsRecursive(tiles, start + 1, suits, structures);
                suits.remove(suit);
            }
        }
    }

    private boolean containsKnittedChowOfSuit(SortedMultiset<Tile> tiles, int start, char suit) {
        return tiles.contains(Tile.getInstance(start, suit))
                && tiles.contains(Tile.getInstance(start + 3, suit))
                && tiles.contains(Tile.getInstance(start + 6, suit));
    }

    private void addNormalStructureWithoutKnittedStraight(SortedMultiset<Tile> tiles, List<NormalHandStructure> structures) {
        final Map<Character, SortedMultiset<Tile>> suitsTiles = new HashMap<>();
        for (char suit : new char[]{'m', 'p', 's', 'w', 'd'}) {
            suitsTiles.put(suit, Tile.getTilesOfSuit(tiles, suit));
        }
        // there should be only one pair
        char suitWithPair = getSuitWithPair(suitsTiles);
        if (suitWithPair == 0) return;
        // search for chows/pungs in hand tiles
        // claims are fixed
        List<NormalHandStructure> previousStructs = List.of(new NormalHandStructure());
        for (char suit : new char[]{'w', 'd', 'm', 'p', 's'}) {
            // get possible structures of a suit
            final List<NormalHandStructure> structs = getSuitStructure(suit, suitsTiles.get(suit), suitWithPair == suit);
            if (structs.isEmpty()) return;  // shortcut: no possible structure
            // combine possible structures of the suit with previous structures (Cartesian product)
            final List<NormalHandStructure> combinedStructs = new ArrayList<>();
            for (NormalHandStructure previousStruct : previousStructs) {
                for (NormalHandStructure struct : structs) {
                    combinedStructs.add(new NormalHandStructure(previousStruct, struct));
                }
            }
            previousStructs = combinedStructs;
        }
        structures.addAll(previousStructs);
    }

    // in which suit is the only pair
    // 0 for "invalid"
    private char getSuitWithPair(Map<Character, SortedMultiset<Tile>> suitsTiles) {
        char suitWithPair = 0;
        for (Map.Entry<Character, SortedMultiset<Tile>> entry : suitsTiles.entrySet()) {
            final int sizeMod3 = entry.getValue().size() % 3;
            if (sizeMod3 == 2) {
                // a pair is included in the suit
                if (suitWithPair == 0) suitWithPair = entry.getKey();
                else return 0;  // there should be no more than 1 pair
            } else if (sizeMod3 != 0) {
                // not a normal structure within the suit
                return 0;
            }
        }
        return suitWithPair;
    }

    // tiles should all be within the same suit
    private List<NormalHandStructure> getSuitStructure(char suit, SortedMultiset<Tile> tiles, boolean allowsPair) {
        // honor pungs/pair are fixed
        if (suit == 'w' || suit == 'd') {
            final NormalHandStructure structure = getHonorSuitStructure(tiles, allowsPair);
            if (structure != null) return List.of(structure);
            return List.of();
        }
        // search for number chows/pungs/pair
        return getNumberSuitStructure(tiles, allowsPair);
    }

    private List<NormalHandStructure> getNumberSuitStructure(SortedMultiset<Tile> tiles, boolean allowsPair) {
        final List<NormalHandStructure> structures = new ArrayList<>();
        getNumberSuitStructureRecursive(tiles, allowsPair, new ArrayList<>(), null, structures);
        return structures;
    }

    private void getNumberSuitStructureRecursive(SortedMultiset<Tile> tiles, boolean allowsPair, List<Claim> claims, Tile pair, List<NormalHandStructure> structures) {
        if (tiles.isEmpty()) {
            structures.add(new NormalHandStructure(claims.toArray(new Claim[0]), pair));
            return;
        }
        final Multiset.Entry<Tile> firstEntry = tiles.firstEntry();
        assert firstEntry != null;
        final Tile firstTile = firstEntry.getElement();
        final int firstCount = firstEntry.getCount();
        if (firstCount >= 3) {
            // try pung
            claims.add(new Claim(Claim.Type.PUNG, firstTile, 0, 0));
            tiles.remove(firstTile, 3);
            getNumberSuitStructureRecursive(tiles, allowsPair, claims, pair, structures);
            claims.removeLast();
            tiles.add(firstTile, 3);
        }
        if (firstCount >= 2 && allowsPair) {
            // try pair
            tiles.remove(firstTile, 2);
            getNumberSuitStructureRecursive(tiles, false, claims, firstTile, structures);
            tiles.add(firstTile, 2);
        }
        // the first tiles are all used in chows
        if (firstTile.number > 7)
            // chows must begin at 1-7
            return;
        int chowCount = firstCount;
        chowCount = Math.min(chowCount, tiles.count(firstTile.next()));
        chowCount = Math.min(chowCount, tiles.count(firstTile.shift(2)));
        if (chowCount < firstCount) return;
        // all the first tiles can form chows
        for (int i = 0; i < chowCount; i++) {
            claims.add(new Claim(Claim.Type.CHOW, firstTile, 0, 0));
        }
        tiles.remove(firstTile, chowCount);
        tiles.remove(firstTile.next(), chowCount);
        tiles.remove(firstTile.shift(2), chowCount);
        getNumberSuitStructureRecursive(tiles, allowsPair, claims, pair, structures);
        for (int i = 0; i < chowCount; i++) {
            claims.removeLast();
        }
        tiles.add(firstTile, chowCount);
        tiles.add(firstTile.next(), chowCount);
        tiles.add(firstTile.shift(2), chowCount);
    }

    private NormalHandStructure getHonorSuitStructure(SortedMultiset<Tile> tiles, boolean allowsPair) {
        final List<Claim> claims = new ArrayList<>();
        Tile pair = null;
        for (Multiset.Entry<Tile> entry : tiles.entrySet()) {
            final int count = entry.getCount();
            if (count == 3) {
                claims.add(new Claim(Claim.Type.PUNG, entry.getElement(), 0, 0));
            } else if (count == 2 && allowsPair && pair == null) {
                pair = entry.getElement();
            } else return null;
        }
        return new NormalHandStructure(claims.toArray(new Claim[0]), pair);
    }

    private void addPairStructure(MCRHand hand, List<HandStructure> structures) {
        if (hand.hasClaim()) return;
        final Multiset<Tile> tiles = hand.getHandTiles();
        if (tiles.entrySet().stream().allMatch(e -> e.getCount() % 2 == 0)) {
            final List<Tile> pairs = new ArrayList<>();
            for (Multiset.Entry<Tile> entry : tiles.entrySet()) {
                if (entry.getCount() >= 2) pairs.add(entry.getElement());
                if (entry.getCount() == 4) pairs.add(entry.getElement());
            }
            structures.add(new PairHandStructure(pairs.toArray(new Tile[0])));
        }
    }

    private void addOrphanStructure(MCRHand hand, List<HandStructure> structures) {
        if (hand.hasClaim()) return;
        final Multiset<Tile> tiles = hand.getHandTiles();
        if (tiles.entrySet().size() != 13) return;
        Tile doubleTile = null;
        for (Multiset.Entry<Tile> entry : tiles.entrySet()) {
            if (!entry.getElement().isOrphan()) return;
            if (entry.getCount() == 2)
                doubleTile = entry.getElement();
        }
        structures.add(new OrphanHandStructure(doubleTile));
    }

    private void addHonorKnittedStructure(MCRHand hand, List<HandStructure> structures) {
        if (hand.hasClaim()) return;
        final Multiset<Tile> tiles = hand.getHandTiles();
        if (tiles.entrySet().size() != 14) return;
        final List<Tile> honors = new ArrayList<>(), knittedTiles = new ArrayList<>();
        // 369<->suit1, 147<->suit2, 258<->suit3
        BiMap<Integer, Character> knittedSuits = HashBiMap.create();
        for (Tile tile : tiles.elementSet()) {
            if (tile.isHonor()) honors.add(tile);
            else if (tile.isNumber()) {
                final char suit = tile.suit;
                final int start = tile.number % 3;
                final Character assignedSuit = knittedSuits.get(start);
                if (assignedSuit == null && knittedSuits.inverse().get(suit) == null) {
                    // new knitted-chow-suit mapping
                    knittedSuits.put(start, suit);
                } else if (assignedSuit == null || assignedSuit != suit) {
                    // the suit and knitted chow does not match
                    return;
                }
            }
        }
        structures.add(new HonorKnittedHandStructure(honors.toArray(new Tile[0]), knittedTiles.toArray(new Tile[0])));
    }

}
