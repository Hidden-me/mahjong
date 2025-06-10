package net.hidme.mahjong.gui;

import javax.swing.*;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final MainFrame frame = new MainFrame();
    }

}