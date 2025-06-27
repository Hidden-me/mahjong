package net.hidme.mahjong.gui.fanquiz;

import net.hidme.mahjong.core.calc.MCRCalculator;
import net.hidme.mahjong.core.data.MCRHand;
import net.hidme.mahjong.core.data.MCRResult;
import net.hidme.mahjong.core.quiz.MCRHandGenerator;
import net.hidme.mahjong.gui.MainFrame;
import net.hidme.mahjong.gui.ScenePanel;
import net.hidme.mahjong.gui.common.AccumulatorButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static net.hidme.mahjong.gui.text.Localization.*;
import static net.hidme.mahjong.gui.util.GridBagLayoutUtils.makeConstraint;

public class FanQuizPanel extends ScenePanel {

    private static final Font BUTTON_EMOJI_FONT = new Font(emojiFontName(), Font.PLAIN, 18);
    private static final Font DEFAULT_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public FanQuizPanel(MainFrame window) {
        super(window);
        // quiz generator and checker
        generator = new MCRHandGenerator();
        calculator = new MCRCalculator();
        // GUI
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 50, 20, 50));
        accumulatorButtons = new LinkedList<>();
        accumulatorSum = new JTextField();
        answerArea = new AnswerArea();
        checkOrNextButton = createCheckOrNextButton();
        // hand view and details
        final JPanel northPanel = new JPanel(new BorderLayout());
        handViewPanel = new HandViewPanel();
        northPanel.add(handViewPanel);
        detailPanel = new DetailPanel();
        detailPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        northPanel.add(detailPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);
        // center panel
        final JPanel centerPanel = createCenterPanel();
        centerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(centerPanel);
        // back button
        final JButton backButton = createBackButton();
        add(backButton, BorderLayout.SOUTH);
        // initial hand
        generateHand();
    }

    private final MCRHandGenerator generator;
    private final MCRCalculator calculator;
    private final HandViewPanel handViewPanel;
    private final DetailPanel detailPanel;
    private final List<AccumulatorButton> accumulatorButtons;
    private final JTextField accumulatorSum;
    private final AnswerArea answerArea;
    private final JButton checkOrNextButton;

    private MCRHand hand;
    private boolean isCheckButton;

    /**
     * Generate a random MCR hand.
     */
    private void generateHand() {
        hand = generator.generate();
        // update GUI
        handViewPanel.updateHand(hand);
        detailPanel.updateDetails(hand);
    }

    private JPanel createCenterPanel() {
        final JPanel centerPanel = new JPanel(new GridBagLayout());
        // input view and buttons
        final JPanel inputPanel = new JPanel(new GridBagLayout());
        addInputButton(88, inputPanel, 0, 1, 1, 1);
        addInputButton(64, inputPanel, 1, 1, 1, 1);
        addInputButton(48, inputPanel, 2, 1, 1, 1);
        addInputButton(32, inputPanel, 3, 1, 1, 1);
        addInputButton(24, inputPanel, 0, 2, 1, 1);
        addInputButton(16, inputPanel, 1, 2, 1, 1);
        addInputButton(12, inputPanel, 2, 2, 1, 1);
        addInputButton(8, inputPanel, 3, 2, 1, 1);
        addInputButton(6, inputPanel, 0, 3, 2, 1);
        addInputButton(5, inputPanel, 2, 3, 1, 1);
        addInputButton(4, inputPanel, 3, 3, 1, 1);
        addInputButton(2, inputPanel, 0, 4, 2, 1);
        addInputButton(1, inputPanel, 2, 4, 2, 1);
        inputPanel.add(createResetInputButton(), makeConstraint(4, 1, 1, 4));
        centerPanel.add(inputPanel, makeConstraint(0, 0, 1, 1, 0, 0));
        // player answer (accumulator result)
        accumulatorSum.setFont(DEFAULT_FONT);
        accumulatorSum.setHorizontalAlignment(SwingConstants.RIGHT);
        inputPanel.add(accumulatorSum, makeConstraint(0, 0, 5, 1));
        // fan result
        final JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBorder(new EmptyBorder(0, 20, 0, 0));
        answerArea.setFont(DEFAULT_FONT);
        answerPanel.add(answerArea);
        centerPanel.add(answerPanel, makeConstraint(1, 0, 1, 1));
        // check/next button
        final JPanel checkPanel = new JPanel(new BorderLayout());
        checkPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        checkOrNextButton.setBorder(new EmptyBorder(10, 0, 10, 0));
        checkPanel.add(checkOrNextButton);
        centerPanel.add(checkPanel, makeConstraint(0, 1, 2, 1, 1, 0));
        return centerPanel;
    }

    private void addInputButton(int input, JPanel panel, int x, int y, int width, int height) {
        final AccumulatorButton button = new AccumulatorButton(input, 0, null);
        button.setFont(DEFAULT_FONT);
        button.setFocusable(false);
        button.setIncreaseAction(i -> onAccumulatorUpdate());
        button.setDecreaseAction(i -> onAccumulatorUpdate());
        panel.add(button, makeConstraint(x, y, width, height));
        accumulatorButtons.add(button);
    }

    private JButton createResetInputButton() {
        final JButton button = new JButton(text(KEY_FAN_QUIZ_BUTTON_CLEAR));
        button.setFont(DEFAULT_FONT);
        button.setFocusable(false);
        button.addActionListener(e -> resetInput());
        return button;
    }

    private JButton createCheckOrNextButton() {
        final JButton button = new JButton(text(KEY_FAN_QUIZ_BUTTON_CHECK));
        button.setFont(DEFAULT_FONT);
        button.addActionListener(e -> onCheckOrNextButtonClicked());
        isCheckButton = true;
        return button;
    }

    private JButton createBackButton() {
        final JButton button = new JButton("👈");
        button.setFont(BUTTON_EMOJI_FONT);
        bindSceneButton(button, MainFrame.SCENE_MENU);
        return button;
    }

    private void onAccumulatorUpdate() {
        // update the accumulator sum
        accumulatorSum.setText(String.valueOf(getAccumulatorSum()));
    }

    private int getAccumulatorSum() {
        int sum = 0;
        for (AccumulatorButton button : accumulatorButtons) {
            sum += button.getTotalValue();
        }
        return sum;
    }

    private void resetInput() {
        for (AccumulatorButton button : accumulatorButtons) {
            button.reset();
        }
        accumulatorSum.setText("");
    }

    private void onCheckOrNextButtonClicked() {
        if (isCheckButton) onCheckButtonClicked();
        else onNextButtonClicked();
    }

    private void onCheckButtonClicked() {
        // show the answer
        final MCRResult result = (MCRResult) calculator.calculate(hand);
        answerArea.setResult(result);
        final int playerAnswer;
        try {
            // check the player's answer
            playerAnswer = Integer.parseInt(accumulatorSum.getText());
            final int answer = result.getFanTotal();
            if (playerAnswer == answer) onCheckSuccess();
            else onCheckFailure();
        } catch (Throwable e) {
            // illegal input
            onCheckFailure();
        }
        // check -> next
        checkOrNextButton.setText(text(KEY_FAN_QUIZ_BUTTON_NEXT));
        isCheckButton = false;
    }

    private void onCheckSuccess() {
        answerArea.setStyleSuccess();
    }

    private void onCheckFailure() {
        answerArea.setStyleFailure();
    }

    private void onNextButtonClicked() {
        // reset
        resetInput();
        answerArea.reset();
        // next case
        generateHand();
        // next -> check
        checkOrNextButton.setText(text(KEY_FAN_QUIZ_BUTTON_CHECK));
        isCheckButton = true;
    }

}
