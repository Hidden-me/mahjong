package net.hidme.mahjong.gui.text;

import java.util.HashMap;
import java.util.Map;

public class Localization {

    public static final String KEY_WINDOW_TITLE = "window.title";
    public static final String KEY_MAIN_MENU_TITLE = "main-menu.title";
    public static final String KEY_MAIN_MENU_BUTTON_FAN_QUIZ = "main-menu.button.fan-quiz";
    public static final String KEY_MAIN_MENU_BUTTON_FAN_CALC = "main-menu.button.fan-calc";

    public static final String KEY_FAN_CALC_TITLE_INPUT_MODE = "fan-calc.title.input-mode";
    public static final String KEY_FAN_CALC_TITLE_OPTIONS = "fan-calc.title.options";
    public static final String KEY_FAN_CALC_BUTTON_TILE = "fan-calc.button.tile";
    public static final String KEY_FAN_CALC_BUTTON_CHOW = "fan-calc.button.chow";
    public static final String KEY_FAN_CALC_BUTTON_PUNG = "fan-calc.button.pung";
    public static final String KEY_FAN_CALC_BUTTON_MELDED_KONG = "fan-calc.button.melded-kong";
    public static final String KEY_FAN_CALC_BUTTON_CONCEALED_KONG = "fan-calc.button.concealed-kong";
    public static final String KEY_FAN_CALC_BUTTON_CLEAR = "fan-calc.button.clear";
    public static final String KEY_FAN_CALC_OPTION_SELF_DRAWN = "fan-calc.option.self-drawn";
    public static final String KEY_FAN_CALC_OPTION_LAST_TILE = "fan-calc.option.last-tile";
    public static final String KEY_FAN_CALC_OPTION_LAST_DRAW_OR_CLAIM = "fan-calc.option.last-draw-or-claim";
    public static final String KEY_FAN_CALC_OPTION_KONG = "fan-calc.option.kong";
    public static final String KEY_FAN_CALC_OPTION_PREVALENT_WIND_EAST = "fan-calc.option.prevalent-wind.east";
    public static final String KEY_FAN_CALC_OPTION_PREVALENT_WIND_SOUTH = "fan-calc.option.prevalent-wind.south";
    public static final String KEY_FAN_CALC_OPTION_PREVALENT_WIND_WEST = "fan-calc.option.prevalent-wind.west";
    public static final String KEY_FAN_CALC_OPTION_PREVALENT_WIND_NORTH = "fan-calc.option.prevalent-wind.north";
    public static final String KEY_FAN_CALC_OPTION_SEAT_WIND_EAST = "fan-calc.option.seat-wind.east";
    public static final String KEY_FAN_CALC_OPTION_SEAT_WIND_SOUTH = "fan-calc.option.seat-wind.south";
    public static final String KEY_FAN_CALC_OPTION_SEAT_WIND_WEST = "fan-calc.option.seat-wind.west";
    public static final String KEY_FAN_CALC_OPTION_SEAT_WIND_NORTH = "fan-calc.option.seat-wind.north";
    public static final String KEY_FAN_CALC_HINT = "fan-calc.hint";

    public static final String KEY_FAN_QUIZ_DETAIL_SELF_DRAWN = "fan-quiz.detail.self-drawn";
    public static final String KEY_FAN_QUIZ_DETAIL_LAST_TILE = "fan-quiz.detail.last-tile";
    public static final String KEY_FAN_QUIZ_DETAIL_LAST_DRAW_OR_CLAIM = "fan-quiz.detail.last-draw-or-claim";
    public static final String KEY_FAN_QUIZ_DETAIL_KONG = "fan-quiz.detail.kong";
    public static final String KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_EAST = "fan-quiz.detail.prevalent-wind.east";
    public static final String KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_SOUTH = "fan-quiz.detail.prevalent-wind.south";
    public static final String KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_WEST = "fan-quiz.detail.prevalent-wind.west";
    public static final String KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_NORTH = "fan-quiz.detail.prevalent-wind.north";
    public static final String KEY_FAN_QUIZ_DETAIL_SEAT_WIND_EAST = "fan-quiz.detail.seat-wind.east";
    public static final String KEY_FAN_QUIZ_DETAIL_SEAT_WIND_SOUTH = "fan-quiz.detail.seat-wind.south";
    public static final String KEY_FAN_QUIZ_DETAIL_SEAT_WIND_WEST = "fan-quiz.detail.seat-wind.west";
    public static final String KEY_FAN_QUIZ_DETAIL_SEAT_WIND_NORTH = "fan-quiz.detail.seat-wind.north";
    public static final String KEY_FAN_QUIZ_BUTTON_CLEAR = "fan-quiz.button.clear";
    public static final String KEY_FAN_QUIZ_BUTTON_CHECK = "fan-quiz.button.check";
    public static final String KEY_FAN_QUIZ_BUTTON_NEXT = "fan-quiz.button.next";
    public static final String KEY_FAN_QUIZ_HINT = "fan-quiz.hint";

    public static final String KEY_FAN_CALC_SINGLE_SCORE = "fan-calc.single-score";
    public static final String KEY_FAN_CALC_TOTAL_SCORE = "fan-calc.total-score";

    public static String text(String key) {
        return TEXT_MAP.get(key);
    }

    public static String text(String key, Object... args) {
        return String.format(TEXT_TEMPLATE_MAP.get(key), args);
    }

    public static String textFontName() {
        return TEXT_FONT_NAME;
    }

    public static String emojiFontName() {
        return EMOJI_FONT_NAME;
    }

    private static final Map<String, String> TEXT_MAP, TEXT_TEMPLATE_MAP;
    private static final String TEXT_FONT_NAME, EMOJI_FONT_NAME;

    static {
        // default text
        TEXT_MAP = new HashMap<>();
        TEXT_MAP.put(KEY_WINDOW_TITLE, "国标麻将");
        TEXT_MAP.put(KEY_MAIN_MENU_TITLE, "欢迎");
        TEXT_MAP.put(KEY_MAIN_MENU_BUTTON_FAN_QUIZ, "算番测试");
        TEXT_MAP.put(KEY_MAIN_MENU_BUTTON_FAN_CALC, "算番器");
        TEXT_MAP.put(KEY_FAN_CALC_TITLE_INPUT_MODE, "输入模式：");
        TEXT_MAP.put(KEY_FAN_CALC_TITLE_OPTIONS, "其他选项：");
        TEXT_MAP.put(KEY_FAN_CALC_BUTTON_TILE, "单张");
        TEXT_MAP.put(KEY_FAN_CALC_BUTTON_CHOW, "吃");
        TEXT_MAP.put(KEY_FAN_CALC_BUTTON_PUNG, "碰");
        TEXT_MAP.put(KEY_FAN_CALC_BUTTON_MELDED_KONG, "明杠");
        TEXT_MAP.put(KEY_FAN_CALC_BUTTON_CONCEALED_KONG, "暗杠");
        TEXT_MAP.put(KEY_FAN_CALC_BUTTON_CLEAR, "清除");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_SELF_DRAWN, "自摸");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_LAST_TILE, "绝张");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_LAST_DRAW_OR_CLAIM, "海底");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_KONG, "杠上");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_PREVALENT_WIND_EAST, "东风圈");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_PREVALENT_WIND_SOUTH, "南风圈");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_PREVALENT_WIND_WEST, "西风圈");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_PREVALENT_WIND_NORTH, "北风圈");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_SEAT_WIND_EAST, "东风位");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_SEAT_WIND_SOUTH, "南风位");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_SEAT_WIND_WEST, "西风位");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_SEAT_WIND_NORTH, "北风位");
        TEXT_MAP.put(KEY_FAN_CALC_HINT, """
            欢迎使用算番器！
            - 要输入牌张，先选择输入模式，
              然后点击下方牌张以输入。
            - 以“吃”模式为例，会输入一个顺子副露，
              该顺子以你点击的牌张为起始（例如点击
              一万会输入一万-三万的顺子副露）。
            - 输入牌张后，可点击上方显示的牌张移除。
              最下方有若干选项，会影响算番结果，
              所以请仔细设置。""");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_SELF_DRAWN, "自摸");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_LAST_TILE, "绝张");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_LAST_DRAW_OR_CLAIM, "海底");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_KONG, "杠上");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_EAST, "东风圈");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_SOUTH, "南风圈");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_WEST, "西风圈");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_PREVALENT_WIND_NORTH, "北风圈");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_EAST, "东风位");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_SOUTH, "南风位");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_WEST, "西风位");
        TEXT_MAP.put(KEY_FAN_QUIZ_DETAIL_SEAT_WIND_NORTH, "北风位");
        TEXT_MAP.put(KEY_FAN_QUIZ_BUTTON_CLEAR, "清除");
        TEXT_MAP.put(KEY_FAN_QUIZ_BUTTON_CHECK, "检查");
        TEXT_MAP.put(KEY_FAN_QUIZ_BUTTON_NEXT, "下一个！");
        TEXT_MAP.put(KEY_FAN_QUIZ_HINT, "在左侧点击番数按钮进行累加，或直接在输入栏输入结果，然后点击下方检查按钮");
        // default text template
        TEXT_TEMPLATE_MAP = new HashMap<>();
        TEXT_TEMPLATE_MAP.put(KEY_FAN_CALC_SINGLE_SCORE, "%d番");
        TEXT_TEMPLATE_MAP.put(KEY_FAN_CALC_TOTAL_SCORE, "总计%d番");
        // default font
        TEXT_FONT_NAME = "微软雅黑";
        EMOJI_FONT_NAME = "Segoe UI Emoji";
    }

}
