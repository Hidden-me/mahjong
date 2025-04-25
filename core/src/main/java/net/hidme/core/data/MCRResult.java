package net.hidme.core.data;

import java.util.Map;

/**
 * The calculation result for MCR (Mahjong Chinese Rule).
 * It contains the Fan combination and the total Fan score.
 */
public class MCRResult implements Result {

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
        fans.put(fan, fans.getOrDefault(fan, 0) + 1);
    }

    // the multiplicity of each Fan
    private Map<MCRFan, Integer> fans;
}
