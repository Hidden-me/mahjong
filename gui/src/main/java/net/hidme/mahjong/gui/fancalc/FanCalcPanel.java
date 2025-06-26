package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.core.calc.MCRCalculator;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.MCRResult;
import net.hidme.mahjong.gui.MainFrame;
import net.hidme.mahjong.gui.ScenePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static net.hidme.mahjong.gui.text.Localization.emojiFontName;

public class FanCalcPanel extends ScenePanel {

    private static final Font BUTTON_EMOJI_FONT = new Font(emojiFontName(), Font.PLAIN, 18);

    public FanCalcPanel(MainFrame window) {
        super(window);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 50, 20, 50));
        // model
        hand = new ConcurrentHand();
        // hand preview
        handPreview = new HandPreviewPanel(this, hand);
        handPreview.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(handPreview, BorderLayout.NORTH);
        // center panel
        final JPanel centerPanel = new JPanel(new BorderLayout());
        final HandInputPanel inputPanel = new HandInputPanel(this, hand);
        inputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        centerPanel.add(inputPanel);
        optionPanel = new OptionPanel(this);
        optionPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        centerPanel.add(optionPanel, BorderLayout.SOUTH);
        centerPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(centerPanel);
        // calculation result
        final JPanel resultPanel = new JPanel();
        calcResultArea = new CalcResultArea();
        resultPanel.add(calcResultArea);
        resultPanel.setBorder(new EmptyBorder(20, 20, 20, 0));
        add(resultPanel, BorderLayout.EAST);
        // back button
        final JButton backButton = createBackButton();
        add(backButton, BorderLayout.SOUTH);
    }

    private final ConcurrentHand hand;
    private final HandPreviewPanel handPreview;
    private final OptionPanel optionPanel;
    private final CalcResultArea calcResultArea;

    private JButton createBackButton() {
        final JButton button = new JButton("👈");
        button.setFont(BUTTON_EMOJI_FONT);
        bindSceneButton(button, MainFrame.SCENE_MENU);
        return button;
    }

    protected void onHandUpdate() {
        handPreview.onHandUpdate();
        // if the hand is complete, calculate Fan
        if (hand.size() == 14) {
            calculateResult();
        }
    }

    protected void onOptionUpdate() {
        if (hand.size() == 14) {
            calculateResult();
        }
    }

    protected void reset() {
        hand.clear();
        calcResultArea.reset();
        onHandUpdate();
    }

    private void calculateResult() {
        final MCRHand mcrHand = hand.getViewModel().toMCRHand(optionPanel.getOptions());
        if (mcrHand != null) {
            final MCRCalculator calculator = new MCRCalculator();
            final MCRResult result = (MCRResult) calculator.calculate(mcrHand);
            calcResultArea.setResult(result);
        }
    }

}
