package de.olivelo;

import javax.swing.*;
import java.awt.*;

/**
 * A panel full of spinning rectangles.
 * Every rectangle completes a whole rotation in 1 second.
 *
 * User: oliver
 * Date: 14.10.12
 * Time: 01:34
 */
public class SpinningRectangles extends JPanel {

    private static final Color[] RECTANGLE_COLORS = {Color.CYAN, new Color(0x3c3c3c), Color.MAGENTA};

    private final Rectangle[] rectangles;

    public SpinningRectangles(int max) {
        this.rectangles = new Rectangle[max];
        System.out.println(getPreferredSize());
    }

    @Override
    public void setMaximumSize(Dimension maximumSize) {
        super.setMaximumSize(maximumSize);
        recalculateRectangles();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        recalculateRectangles();
    }

    private void recalculateRectangles() {
        System.out.println("Recalculated rectangles");
        final double maxRectWidth = getPreferredSize().getWidth() / 4;
        final double maxRectHeight = getPreferredSize().getHeight() / 4;
        for (int i = 0; i < rectangles.length; i++) {
            final double zoomFactor = Math.pow((double) (i + 1) / rectangles.length, 3);
            System.out.println("zoom " + zoomFactor);
            final double angle = Math.PI * Math.pow(i, 3) / Math.pow(rectangles.length, 3);
            System.out.println("angle " + angle);
            final int xOffset = (int) ((1 + Math.cos(angle)) * maxRectWidth);
            final int yOffset = (int) ((1 + Math.sin(angle)) * maxRectHeight);
            final int width = (int) (maxRectWidth * zoomFactor);
            final int height = (int) (maxRectHeight * zoomFactor);
            System.out.println("rect values: " + xOffset + "/" + yOffset + " " + width + "x" + height);
            this.rectangles[i] = new Rectangle(xOffset, yOffset, width, height);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println(getPreferredSize());
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int currentColorIndex = 0;
        for (Rectangle rectangle : this.rectangles) {
            if (rectangle == null) continue;
            g2.setColor(RECTANGLE_COLORS[currentColorIndex]);
            g2.fill(rectangle);
            g2.setColor(Color.BLACK);
            g2.draw(rectangle);
            currentColorIndex = (currentColorIndex + 1) % RECTANGLE_COLORS.length;
        }
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Spinning Rectangles");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 700));
        frame.setLayout(new FlowLayout());
        SpinningRectangles panel = new SpinningRectangles(10);
        panel.setPreferredSize(frame.getPreferredSize());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
