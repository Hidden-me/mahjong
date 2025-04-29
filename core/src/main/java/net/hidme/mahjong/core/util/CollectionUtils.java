package net.hidme.mahjong.core.util;

import java.util.Collections;
import java.util.HashSet;

public interface CollectionUtils {

    static <T> HashSet<T> setOf(T... elements) {
        HashSet<T> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }

}
