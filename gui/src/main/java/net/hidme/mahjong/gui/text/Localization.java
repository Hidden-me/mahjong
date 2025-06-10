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
    public static final String KEY_FAN_CALC_OPTION_PREVALENT_WIND = "fan-calc.option.prevalent-wind";
    public static final String KEY_FAN_CALC_OPTION_SEAT_WIND = "fan-calc.option.seat-wind";
    public static final String KEY_FAN_CALC_OPTION_WIND_EAST = "fan-calc.option.wind.east";
    public static final String KEY_FAN_CALC_OPTION_WIND_SOUTH = "fan-calc.option.wind.south";
    public static final String KEY_FAN_CALC_OPTION_WIND_WEST = "fan-calc.option.wind.west";
    public static final String KEY_FAN_CALC_OPTION_WIND_NORTH = "fan-calc.option.wind.north";

    public static String text(String key) {
        return TEXT_MAP.get(key);
    }

    public static String textFontName() {
        return TEXT_FONT_NAME;
    }

    public static String emojiFontName() {
        return EMOJI_FONT_NAME;
    }

    private static final Map<String, String> TEXT_MAP;
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
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_KONG, "杠");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_PREVALENT_WIND, "圈风");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_SEAT_WIND, "门风");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_WIND_EAST, "东");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_WIND_SOUTH, "南");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_WIND_WEST, "西");
        TEXT_MAP.put(KEY_FAN_CALC_OPTION_WIND_NORTH, "北");
        // default font
        TEXT_FONT_NAME = "微软雅黑";
        EMOJI_FONT_NAME = "Segoe UI Emoji";
    }

}
