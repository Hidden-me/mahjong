package net.hidme.mahjong.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static net.hidme.mahjong.gui.text.Localization.*;

public class MenuPanel extends ScenePanel {

    private static final Font TITLE_FONT = new Font(textFontName(), Font.BOLD | Font.ITALIC, 48);
    private static final Font BUTTON_FONT = new Font(textFontName(), Font.PLAIN, 18);

    public MenuPanel(MainFrame window) {
        super(window);
        setLayout(new BorderLayout());
        // title
        final JPanel titlePanel = new JPanel();
        final JLabel titleLabel = new JLabel(text(KEY_MAIN_MENU_TITLE));
        titlePanel.add(titleLabel);
        titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        titleLabel.setFont(TITLE_FONT);
        add(titlePanel, BorderLayout.NORTH);
        // buttons
        final JButton fanQuizButton = createSceneButton(MainFrame.SCENE_FAN_QUIZ, text(KEY_MAIN_MENU_BUTTON_FAN_QUIZ));
        final JButton fanCalcButton = createSceneButton(MainFrame.SCENE_FAN_CALC, text(KEY_MAIN_MENU_BUTTON_FAN_CALC));
        final JPanel buttonPanel = createButtonPanel(fanQuizButton, fanCalcButton);
        add(buttonPanel, BorderLayout.CENTER);
        // borders
        add(new JPanel(), BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
    }

    private JButton createSceneButton(String sceneName, String text) {
        final JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        bindSceneButton(button, sceneName);
        return button;
    }

    private JPanel createButtonPanel(JButton fanQuizButton, JButton fanCalcButton) {
        final JPanel buttonPanel = new JPanel();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(1);
        gridLayout.setColumns(2);
        buttonPanel.setLayout(gridLayout);
        buttonPanel.add(fanQuizButton);
        buttonPanel.add(fanCalcButton);
        buttonPanel.setBorder(new EmptyBorder(0, 50, 50, 50));
        return buttonPanel;
    }

}
