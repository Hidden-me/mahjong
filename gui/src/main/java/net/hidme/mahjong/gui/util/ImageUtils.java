package net.hidme.mahjong.gui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public interface ImageUtils {

    static Icon rotate90Clockwise(Icon icon) {
        return rotate90Clockwise(icon, 1);
    }

    static Icon rotate90CounterClockwise(Icon icon) {
        return rotate90Clockwise(icon, 3);
    }

    static Icon rotate90Clockwise(Icon icon, int times) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        // Convert to BufferedImage
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        // Create new rotated image with swapped dimensions
        BufferedImage rotated = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
        g2d = rotated.createGraphics();

        // Rotate times*90 degrees clockwise around the center
        AffineTransform at = new AffineTransform();
        switch (times % 4) {
            case 1 -> at.translate(h, 0);
            case 2 -> at.translate(w, h);
            case 3 -> at.translate(0, w);
        }
        at.rotate(Math.toRadians(times * 90));

        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(rotated);
    }

}
