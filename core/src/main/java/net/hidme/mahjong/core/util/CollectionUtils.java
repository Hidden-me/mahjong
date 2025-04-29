package net.hidme.mahjong.core.util;

import java.util.*;

public interface CollectionUtils {

    static <T> Set<T> setOf(T... elements) {
        final Set<T> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    /**
     * Return a copy of list whose index-th element is removed.
     */
    static <T> List<T> minus(List<T> list, int index) {
        final List<T> newList = new ArrayList<>(list);
        newList.remove(index);
        return newList;
    }

}
