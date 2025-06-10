package net.hidme.mahjong.gui.fancalc;

import net.hidme.mahjong.gui.MainFrame;
import net.hidme.mahjong.gui.ScenePanel;

import javax.swing.*;
import java.awt.*;

import static net.hidme.mahjong.gui.text.Localization.emojiFontName;

public class FanCalcPanel extends ScenePanel {

    private static final Font BUTTON_EMOJI_FONT = new Font(emojiFontName(), Font.PLAIN, 18);

    public FanCalcPanel(MainFrame window) {
        super(window);
        setLayout(new BorderLayout());
        // model
        hand = new ConcurrentHand();
        // hand preview
        handPreview = new HandPreviewPanel(this, hand);
        add(handPreview, BorderLayout.NORTH);
        // hand input panel
        final HandInputPanel inputPanel = new HandInputPanel(this, hand);
        add(inputPanel);
        // back button
        final JPanel bottomPanel = new JPanel();
        final JButton backButton = createBackButton();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private ConcurrentHand hand;
    private HandPreviewPanel handPreview;

    private JButton createBackButton() {
        final JButton button = new JButton("👈");
        button.setFont(BUTTON_EMOJI_FONT);
        bindSceneButton(button, MainFrame.SCENE_MENU);
        return button;
    }

    protected void onHandUpdate() {
        handPreview.onHandUpdate();
    }

}
