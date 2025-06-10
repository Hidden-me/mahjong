package net.hidme.mahjong.gui;

import javax.swing.*;

public class ScenePanel extends JPanel {

    public ScenePanel(MainFrame window) {
        this.window = window;
    }

    private final MainFrame window;

    /**
     * Make a button a switch to another scene.
     */
    protected void bindSceneButton(JButton button, String sceneName) {
        button.addActionListener(e -> window.switchScene(sceneName));
    }

}
