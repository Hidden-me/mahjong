package net.hidme.mahjong.gui;

import net.hidme.mahjong.gui.fancalc.FanCalcPanel;

import javax.swing.*;
import java.awt.*;

import static net.hidme.mahjong.gui.text.Localization.KEY_WINDOW_TITLE;
import static net.hidme.mahjong.gui.text.Localization.text;

public class MainFrame extends JFrame {

    public static final String SCENE_MENU = "menu";
    public static final String SCENE_FAN_QUIZ = "fan-quiz";
    public static final String SCENE_FAN_CALC = "fan-calc";

    public MainFrame() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        // title
        setTitle(text(KEY_WINDOW_TITLE));
        // scenes
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        final FanQuizPanel fanQuizPanel = new FanQuizPanel(this);
        addScene(fanQuizPanel, SCENE_FAN_QUIZ);
        final FanCalcPanel fanCalcPanel = new FanCalcPanel(this);
        addScene(fanCalcPanel, SCENE_FAN_CALC);
        final MenuPanel menuPanel = new MenuPanel(this);
        addScene(menuPanel, SCENE_MENU);
        // frame location and size
        setLocation(screenSize.width / 4, screenSize.height / 5);
        setSize(screenSize.width / 2, screenSize.height * 3 / 5);
        // close operation
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // show the frame
        switchScene(SCENE_MENU);
        setVisible(true);
    }

    public void switchScene(String sceneName) {
        cardLayout.show(getContentPane(), sceneName);
    }

    // for panel switch
    private final CardLayout cardLayout;

    private void addScene(Component scene, String sceneName) {
        getContentPane().add(scene, sceneName);
    }

}
