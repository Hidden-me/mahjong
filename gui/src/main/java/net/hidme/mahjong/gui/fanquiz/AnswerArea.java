package net.hidme.mahjong.gui.fanquiz;

import net.hidme.mahjong.core.data.MCRFan;
import net.hidme.mahjong.core.data.MCRResult;
import net.hidme.mahjong.gui.text.MCRFanTranslator;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static net.hidme.mahjong.gui.text.Localization.*;

public class AnswerArea extends JTextArea {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public AnswerArea() {
        super(text(KEY_FAN_QUIZ_HINT));
        setEditable(false);
        setFont(DEFAULT_FONT);
        setBackground(Color.WHITE);
        setFocusable(false);
    }

    public void setResult(MCRResult result) {
        final StringBuilder sb = new StringBuilder();
        final MCRFanTranslator fanTranslator = new MCRFanTranslator();
        final Map<MCRFan, Integer> fanCombination = result.getFanCombination();
        for (Map.Entry<MCRFan, Integer> entry : fanCombination.entrySet()) {
            final String fanName = fanTranslator.translate(entry.getKey());
            final int fanScore = entry.getKey().score;
            final int multiplicity = entry.getValue();
            sb.append(fanName).append("\t")
                    .append(text(KEY_FAN_QUIZ_SINGLE_SCORE, fanScore))
                    .append(multiplicity > 1 ? " * " + multiplicity : "")
                    .append('\n');
        }
        sb.append(text(KEY_FAN_QUIZ_TOTAL_SCORE, result.getFanTotal()));
        setText(sb.toString());
    }

    public void reset() {
        setText(text(KEY_FAN_QUIZ_HINT));
    }

}
