package net.hidme.mahjong.core.calc;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.hidme.mahjong.core.data.Claim;
import net.hidme.mahjong.core.data.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.hidme.mahjong.core.data.Claim.Type.*;

public class NormalHandStructure implements HandStructure {
    Claim[] claims;
    Tile pair;

    public NormalHandStructure() {
        this(new Claim[0], null);
    }

    public NormalHandStructure(Claim[] claims, Tile pair) {
        this.claims = claims;
        this.pair = pair;
    }

    /**
     * Merge two incomplete structures into one.
     * There should be no more than 1 pair.
     */
    public NormalHandStructure(NormalHandStructure s1, NormalHandStructure s2) {
        final List<Claim> claimList = new ArrayList<>(List.of(s1.claims));
        claimList.addAll(List.of(s2.claims));
        claims = claimList.toArray(new Claim[0]);
        if (s1.pair == null && s2.pair == null) pair = null;
        else if (s1.pair == null) pair = s2.pair;
        else if (s2.pair == null) pair = s1.pair;
        else throw new IllegalArgumentException("There should be no more than 1 pair");
    }

    public boolean chowsOnly() {
        return Stream.of(claims).allMatch(c -> c.type() == CHOW);
    }

    public boolean pungsOnly() {
        return Stream.of(claims).allMatch(c -> c.type() == PUNG || c.type() == KONG);
    }

    public SortedMultiset<Integer> getClaimStartNumbers() {
        final SortedMultiset<Integer> ret = TreeMultiset.create();
        for (Claim claim : claims) {
            ret.add(claim.start().number);
        }
        return ret;
    }

}
