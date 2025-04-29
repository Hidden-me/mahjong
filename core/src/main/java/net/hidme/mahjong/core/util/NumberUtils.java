package net.hidme.mahjong.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface NumberUtils {

    static boolean isUnorderedArithSeq(List<Integer> seq, int diff) {
        final List<Integer> newSeq = new ArrayList<>(seq);
        Collections.sort(newSeq);
        return isArithSeq(newSeq, diff);
    }

    static boolean isArithSeq(List<Integer> seq, int diff) {
        for (int i = 0, bound = seq.size() - 1; i < bound; i++) {
            if (seq.get(i + 1) != seq.get(i) + diff) return false;
        }
        return true;
    }

}
