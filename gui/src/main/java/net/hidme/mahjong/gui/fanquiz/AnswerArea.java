package net.hidme.mahjong.gui.fanquiz;

import net.hidme.mahjong.core.data.MCRFan;
import net.hidme.mahjong.core.data.MCRResult;
import net.hidme.mahjong.gui.text.MCRFanTranslator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Map;

import static net.hidme.mahjong.gui.text.Localization.*;

public class AnswerArea extends JTextArea {

    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    private static final Color COLOR_CORRECT = new Color(0x00, 0xCC, 0x00);
    private static final Color COLOR_WRONG = new Color(0xFF, 0x66, 0x66);
    private static final Border BORDER_CORRECT, BORDER_WRONG, BORDER_NORMAL;

    static {
        BORDER_CORRECT = new CompoundBorder(new LineBorder(COLOR_CORRECT, 5), new EmptyBorder(5, 5, 5, 5));
        BORDER_WRONG = new CompoundBorder(new LineBorder(COLOR_WRONG, 5), new EmptyBorder(5, 5, 5, 5));
        BORDER_NORMAL = new EmptyBorder(5, 5, 5, 5);
    }

    public AnswerArea() {
        super();
        setEditable(false);
        setFont(DEFAULT_FONT);
        setBackground(Color.WHITE);
        setFocusable(false);
        reset();
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

    public void setStyleSuccess() {
        setBorder(BORDER_CORRECT);
    }

    public void setStyleFailure() {
        setBorder(BORDER_WRONG);
    }

    public void reset() {
        setText(text(KEY_FAN_QUIZ_HINT));
        setBorder(BORDER_NORMAL);
    }

}
