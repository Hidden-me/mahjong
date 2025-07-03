package net.hidme.mahjong.core.util;

import java.util.*;

/**
 * A pool of objects that can be randomly picked (they are not returned).
 */
public class PoolRandom<T> {

    public PoolRandom(Collection<T> pool) {
        this.pool = new LinkedList<>(pool);
    }

    /**
     * Get and remove an element from the pool randomly.
     */
    public T next() {
        if (pool.isEmpty())
            throw new NoSuchElementException("No more elements in the pool");
        final WeightRandom<Integer> randomIndices = new WeightRandom<>(toIndices());
        final int index = randomIndices.next();
        return pool.remove(index);
    }

    private final List<T> pool;

    private List<Integer> toIndices() {
        final List<Integer> indices = new ArrayList<>();
        for (int i = 0, bound = pool.size(); i < bound; i++) {
            indices.add(i);
        }
        return indices;
    }

}
