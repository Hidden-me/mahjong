package net.hidme.mahjong.core.util;

import java.util.*;

public interface CollectionUtils {

    @SafeVarargs
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

    /**
     * Return the intersection of two sets.
     */
    static <T> Set<T> intersect(Set<T> set1, Set<T> set2) {
        final Set<T> newSet = new HashSet<>(set1);
        newSet.retainAll(set2);
        return newSet;
    }

}
