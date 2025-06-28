package net.hidme.mahjong.gui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class CounterButton extends JButton {

    public CounterButton(String text, Integer minCount, Integer maxCount) {
        super(text);
        this.minCount = minCount;
        this.maxCount = maxCount;
        final JButton buttonRef = this;
        addMouseListener(new MouseAdapter() {
            // upon click is triggered with proximity
            private void onClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // increase the count upon a left click
                    increase();
                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // decrease the count upon a right click
                    decrease();
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // visual effect for the right mouse button
                    getModel().setArmed(true);
                    getModel().setPressed(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // visual effect for the right mouse button
                    getModel().setPressed(false);
                    getModel().setArmed(false);
                }
                if (buttonRef.contains(e.getPoint())) onClicked(e);
            }
        });
    }

    /**
     * Set the method to invoke upon counter increase.
     * The method will receive the previous counter value as its argument.
     */
    public void setIncreaseAction(Consumer<Integer> method) {
        increaseAction = method;
    }

    /**
     * Set the method to invoke upon counter decrease.
     * The method will receive the previous counter value as its argument.
     */
    public void setDecreaseAction(Consumer<Integer> method) {
        decreaseAction = method;
    }

    public int getCount() {
        return count;
    }

    public void reset() {
        setCount(0);
    }

    public void setCount(int count) {
        this.count = count;
        repaint();
    }

    private final Integer minCount, maxCount;
    private int count = 0;
    private Consumer<Integer> increaseAction = null, decreaseAction = null;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the counter in the top-right corner
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));
        String text = String.valueOf(count);
        FontMetrics fm = g2.getFontMetrics();
        int x = getWidth() - fm.stringWidth(text) - 5;
        int y = fm.getAscent() + 2;
        g2.drawString(text, x, y);
    }

    private void increase() {
        final int oldCount = count;
        updateCount(count + 1);
        if (increaseAction != null) increaseAction.accept(oldCount);
    }

    private void decrease() {
        final int oldCount = count;
        updateCount(count - 1);
        if (decreaseAction != null) decreaseAction.accept(oldCount);
    }

    private void updateCount(int newCount) {
        if (minCount != null && newCount < minCount) return;
        if (maxCount != null && newCount > maxCount) return;
        count = newCount;
    }

}
