package net.hidme.mahjong.core.calc;

import net.hidme.mahjong.core.data.MCRResult;

/**
 * Remove conflicts among MCR Fans.
 */
public interface MCRFanConflictResolver {

    static MCRResult resolveConflict(MCRResult result) {
        // TODO: impl
        //   two concealed kongs -> two concealed pungs
        //   fully concealed hand -> concealed hand, self-drawn
        return result;
    }

}
