package net.hidme.mahjong.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class WeightRandom<T> {

    /**
     * @see WeightRandom#WeightRandom(Collection)
     */
    public WeightRandom(T[] elements) {
        this(new ArrayList<>(List.of(elements)));
    }

    /**
     * Create a random element generator which returns each element with the same probability.
     * @param elements possible elements
     */
    public WeightRandom(Collection<T> elements) {
        if (elements.isEmpty())
            throw new IllegalArgumentException("Empty element set");
        this.elements = new ArrayList<>(elements);
        this.weightPivots = new int[elements.size()];
        for (int i = 0, bound = elements.size(); i < bound; i++) {
            weightPivots[i] = i + 1;
        }
        random = new Random();
        weightSum = elements.size();
    }

    /**
     * @see WeightRandom#WeightRandom(Collection, int[])
     */
    public WeightRandom(T[] elements, int[] weights) {
        this(new ArrayList<>(List.of(elements)), weights);
    }

    /**
     * Create a random element generator which returns each element with a certain probability.
     * Probabilities of elements are proportional to their weights.
     * @param elements possible elements
     * @param weights weight of each element
     */
    public WeightRandom(Collection<T> elements, int[] weights) {
        this.elements = new ArrayList<>(elements);
        this.weightPivots = new int[weights.length];
        weightPivots[0] = weights[0];
        for (int i = 1; i < weights.length; i++) {
            weightPivots[i] = weightPivots[i - 1] + weights[i];
        }
        random = new Random();
        weightSum = weightPivots[weights.length - 1];
    }

    public T next() {
        final int rnd = random.nextInt(weightSum);
        int i = 0;
        while (weightPivots[i] <= rnd) {
            i++;
        }
        return elements.get(i);
    }

    private final List<T> elements;
    private final int[] weightPivots;
    private final Random random;
    private final int weightSum;

}
