package net.hidme.mahjong.core.data;

import java.util.Map;
import java.util.TreeMap;

/**
 * The calculation result for MCR (Mahjong Chinese Rule).
 * It contains the Fan combination and the total Fan score.
 */
public class MCRResult implements Result {

    public MCRResult() {
        fans = new TreeMap<>();
    }

    /**
     * Get the Fan combination.
     * @return a map whose values are the multiplicity of each Fan
     */
    public Map<MCRFan, Integer> getFanCombination() {
        return fans;
    }

    public int getFanTotal() {
        int total = 0;
        for (Map.Entry<MCRFan, Integer> fan : fans.entrySet()) {
            total += fan.getKey().score * fan.getValue();
        }
        return total;
    }

    public void addFan(MCRFan fan) {
        addFan(fan, 1);
    }

    public void addFan(MCRFan fan, int multiplicity) {
        if (multiplicity < 1) return;
        fans.put(fan, fans.getOrDefault(fan, 0) + multiplicity);
    }

    public void reduceFan(MCRFan fan) {
        fans.computeIfPresent(fan, (f, m) -> m <= 1 ? null : m - 1);
    }

    public void removeFan(MCRFan fan) {
        fans.remove(fan);
    }

    public boolean containsFan(MCRFan fan) {
        return fans.containsKey(fan);
    }

    public boolean isEmpty() {
        return fans.isEmpty();
    }

    // the multiplicity of each Fan
    private Map<MCRFan, Integer> fans;
}
