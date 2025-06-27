package net.hidme.mahjong.core.quiz;

import java.util.Random;

public class WeightRandom<T> {

    /**
     * Create a random element generator which returns each element with the same probability.
     * @param elements possible elements
     */
    public WeightRandom(T[] elements) {
        if (elements.length == 0)
            throw new IllegalArgumentException("Empty element set");
        this.elements = elements;
        this.weightPivots = new int[elements.length];
        for (int i = 0; i < elements.length; i++) {
            weightPivots[i] = i + 1;
        }
        random = new Random();
        weightSum = elements.length;
    }

    /**
     * Create a random element generator which returns each element with a certain probability.
     * Probabilities of elements are proportional to their weights.
     * @param elements possible elements
     * @param weights weight of each element
     */
    public WeightRandom(T[] elements, int[] weights) {
        this.elements = elements;
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
        return elements[i];
    }

    private final T[] elements;
    private final int[] weightPivots;
    private final Random random;
    private final int weightSum;

}
