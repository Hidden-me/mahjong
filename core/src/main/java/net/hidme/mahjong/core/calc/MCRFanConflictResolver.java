package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.MCRFan;
import net.hidme.mahjong.core.data.MCRResult;

import java.util.*;

import static net.hidme.mahjong.core.data.MCRFan.*;

/**
 * Remove conflicts among MCR Fans.
 */
public abstract class MCRFanConflictResolver {

    static void resolveConflict(MCRResult result) {
        for (MCRFan fan : MCRFan.values()) {
            if (result.containsFan(fan)) {
                for (MCRFan toRemove : CONFLICT_MAP.get(fan)) {
                    result.removeFan(toRemove);
                }
                // special
                if (fan == NINE_GATES)
                    result.reduceFan(PUNG_OF_TERMINALS_OR_HONORS);
            }
        }
    }

    private static final Map<MCRFan, Collection<MCRFan>> CONFLICT_MAP;

    static {
        CONFLICT_MAP = new HashMap<>();
        addConflict(BIG_FOUR_WINDS, ALL_PUNGS);
        addConflict(ALL_GREEN, HALF_FLUSH);
        addConflict(NINE_GATES, FULL_FLUSH, FULLY_CONCEALED_HAND, CONCEALED_HAND, NO_HONORS);
        addConflict(FOUR_KONGS, ALL_PUNGS, SINGLE_WAIT);
        addConflict(SEVEN_SHIFTED_PAIRS, FULL_FLUSH, SEVEN_PAIRS, FULLY_CONCEALED_HAND, CONCEALED_HAND, NO_HONORS);
        addConflict(THIRTEEN_ORPHANS, ALL_TERMINALS_AND_HONORS, ALL_TYPES, FULLY_CONCEALED_HAND, CONCEALED_HAND);
        addConflict(ALL_TERMINALS, ALL_TERMINALS_AND_HONORS, ALL_PUNGS, OUTSIDE_HAND, DOUBLE_PUNG, PUNG_OF_TERMINALS_OR_HONORS, NO_HONORS);
        addConflict(ALL_HONORS, ALL_TERMINALS_AND_HONORS, ALL_PUNGS, OUTSIDE_HAND, PUNG_OF_TERMINALS_OR_HONORS);
        addConflict(FOUR_CONCEALED_PUNGS, ALL_PUNGS, FULLY_CONCEALED_HAND, CONCEALED_HAND);
        addConflict(PURE_TERMINAL_CHOWS, FULL_FLUSH, ALL_CHOWS, PURE_DOUBLE_CHOW, TWO_TERMINAL_CHOWS, NO_HONORS);
        addConflict(QUADRUPLE_CHOW, TILE_HOG);
        addConflict(FOUR_PURE_SHIFTED_PUNGS, ALL_PUNGS);
        addConflict(ALL_TERMINALS_AND_HONORS, ALL_PUNGS, OUTSIDE_HAND, PUNG_OF_TERMINALS_OR_HONORS);
        addConflict(SEVEN_PAIRS, FULLY_CONCEALED_HAND, CONCEALED_HAND);
        addConflict(GREATER_HONORS_AND_KNITTED_TILES, LESSER_HONORS_AND_KNITTED_TILES, ALL_TYPES, FULLY_CONCEALED_HAND, CONCEALED_HAND);
        addConflict(ALL_EVEN_PUNGS, ALL_PUNGS, ALL_SIMPLES, NO_HONORS);
        addConflict(FULL_FLUSH, NO_HONORS);
        addConflict(UPPER_TILES, UPPER_FOUR, NO_HONORS);
        addConflict(MIDDLE_TILES, ALL_SIMPLES, NO_HONORS);
        addConflict(LOWER_TILES, LOWER_FOUR, NO_HONORS);
        addConflict(THREE_SUITED_TERMINAL_CHOWS, ALL_CHOWS, NO_HONORS);
        addConflict(ALL_FIVE, ALL_SIMPLES, NO_HONORS);
        addConflict(LESSER_HONORS_AND_KNITTED_TILES, ALL_TYPES, FULLY_CONCEALED_HAND, CONCEALED_HAND);
        addConflict(UPPER_FOUR, NO_HONORS);
        addConflict(LOWER_FOUR, NO_HONORS);
        addConflict(REVERSIBLE_TILES, ONE_VOIDED_SUIT);
        addConflict(LAST_TILE_DRAW, SELF_DRAWN);
        addConflict(OUT_WITH_REPLACEMENT_TILE, SELF_DRAWN);
        addConflict(ROBBING_THE_KONG, LAST_TILE);
        addConflict(MELDED_HAND, SINGLE_WAIT);
        addConflict(TWO_CONCEALED_KONGS, TWO_CONCEALED_PUNGS);
        addConflict(FULLY_CONCEALED_HAND, CONCEALED_HAND, SELF_DRAWN);
        addConflict(ALL_CHOWS, NO_HONORS);
        addConflict(ALL_SIMPLES, NO_HONORS);
    }

    private static void addConflict(MCRFan fan, MCRFan... conflicts) {
        CONFLICT_MAP.computeIfAbsent(fan, k -> new HashSet<>())
                .addAll(List.of(conflicts));
    }

}
