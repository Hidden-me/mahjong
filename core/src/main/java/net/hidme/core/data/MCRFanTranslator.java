package net.hidme.core.data;

/**
 * Translate MCRFan to its name.
 */
public class MCRFanTranslator {
    public String translate(MCRFan fan) {
        return switch (fan) {
            case BIG_FOUR_WINDS -> "大四喜";
            case BIG_THREE_DRAGONS -> "大三元";
            case ALL_GREEN -> "绿一色";
            case NINE_GATES -> "九莲宝灯";
            case FOUR_KONGS -> "四杠";
            case SEVEN_SHIFTED_PAIRS -> "连七对";
            case THIRTEEN_ORPHANS -> "十三幺";
            case ALL_TERMINALS -> "清幺九";
            case LITTLE_FOUR_WINDS -> "小四喜";
            case LITTLE_THREE_DRAGONS -> "小三元";
            case ALL_HONORS -> "字一色";
            case FOUR_CONCEALED_PUNGS -> "四暗刻";
            case PURE_TERMINAL_CHOWS -> "一色双龙会";
            case QUADRUPLE_CHOW -> "一色四同顺";
            case FOUR_PURE_SHIFTED_PUNGS -> "一色四节高";
            case FOUR_PURE_SHIFTED_CHOWS -> "一色四步高";
            case THREE_KONGS -> "三杠";
            case ALL_TERMINALS_AND_HONORS -> "混幺九";
            case SEVEN_PAIRS -> "七对";
            case GREATER_HONORS_AND_KNITTED_TILES -> "七星不靠";
            case ALL_EVEN_PUNGS -> "全双刻";
            case FULL_FLUSH -> "清一色";
            case PURE_TRIPLE_CHOW -> "一色三同顺";
            case PURE_SHIFTED_PUNGS -> "一色三节高";
            case UPPER_TILES -> "全大";
            case MIDDLE_TILES -> "全中";
            case LOWER_TILES -> "全小";
            case PURE_STRAIGHT -> "清龙";
            case THREE_SUITED_TERMINAL_CHOWS -> "三色双龙会";
            case PURE_SHIFTED_CHOWS -> "一色三步高";
            case ALL_FIVE -> "全带五";
            case TRIPLE_PUNG -> "三同刻";
            case THREE_CONCEALED_PUNGS -> "三暗刻";
            case LESSER_HONORS_AND_KNITTED_TILES -> "全不靠";
            case KNITTED_STRAIGHT -> "组合龙";
            case UPPER_FOUR -> "大于五";
            case LOWER_FOUR -> "小于五";
            case BIG_THREE_WINDS -> "三风刻";
            case MIXED_STRAIGHT -> "花龙";
            case REVERSIBLE_TILES -> "推不倒";
            case MIXED_TRIPLE_CHOW -> "三色三同顺";
            case MIXED_SHIFTED_PUNGS -> "三色三节高";
            case CHICKEN_HAND -> "无番和";
            case LAST_TILE_DRAW -> "妙手回春";
            case LAST_TILE_CLAIM -> "海底捞月";
            case OUT_WITH_REPLACEMENT_TILE -> "杠上开花";
            case ROBBING_THE_KONG -> "抢杠和";
            case ALL_PUNGS -> "碰碰和";
            case HALF_FLUSH -> "混一色";
            case MIXED_SHIFTED_CHOWS -> "三色三步高";
            case ALL_TYPES -> "五门齐";
            case MELDED_HAND -> "全求人";
            case TWO_CONCEALED_KONGS -> "双暗杠";
            case TWO_DRAGON_PUNGS -> "双箭刻";
            case CONCEALED_AND_MELDED_KONGS -> "明暗杠";
            case OUTSIDE_HAND -> "全带幺";
            case FULLY_CONCEALED_HAND -> "不求人";
            case TWO_MELDED_KONGS -> "双明杠";
            case LAST_TILE -> "和绝张";
            case DRAGON_PUNG -> "箭刻";
            case PREVELANT_WIND -> "门风刻";
            case SEAT_WIND -> "圈风刻";
            case CONCEALED_HAND -> "门前清";
            case ALL_CHOWS -> "平和";
            case TILE_HOG -> "四归一";
            case DOUBLE_PUNG -> "双同刻";
            case TWO_CONCEALED_PUNGS -> "双暗刻";
            case CONCEALED_KONG -> "暗杠";
            case ALL_SIMPLES -> "断幺";
            case PURE_DOUBLE_CHOW -> "一般高";
            case MIXED_DOUBLE_CHOW -> "喜相逢";
            case SHORT_STRAIGHT -> "连六";
            case TWO_TERMINAL_CHOWS -> "老少副";
            case PUNG_OF_TERMINALS_OR_HONORS -> "幺九刻";
            case MELDED_KONG -> "明杠";
            case ONE_VOIDED_SUIT -> "缺一门";
            case NO_HONORS -> "无字";
            case EDGE_WAIT -> "边张";
            case CLOSED_WAIT -> "坎张";
            case SINGLE_WAIT -> "单钓将";
            case SELF_DRAWN -> "自摸";
        };
    }
}
