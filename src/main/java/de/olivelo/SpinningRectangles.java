package de.olivelo;

import sun.awt.WindowClosingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

/**
 * A panel full of spinning rectangles.
 * Every rectangle completes a whole rotation in 4 seconds.
 *
 * User: oliver
 * Date: 14.10.12
 * Time: 01:34
 */
public class SpinningRectangles extends JPanel {

    private static final Color[] RECTANGLE_COLORS = {Color.CYAN, new Color(0x3c3c3c), Color.MAGENTA};
    private static final long FRAMERATE = 30;

    private final Rectangle[] rectangles;
    private boolean running = true;
    private double rotation = 0;

    public SpinningRectangles(int max) {
        this.rectangles = new Rectangle[max];
        new Thread(new Tick()).start();
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

    public void addClosingListener(Window window) {
        window.addWindowListener(new ClosingListener());
    }

    private void recalculateRectangles() {
        System.out.println("Recalculated rectangles");
        final double maxRectSize = Math.min(
                getPreferredSize().getWidth(), getPreferredSize().getHeight())
                / 4;
        for (int i = 0; i < rectangles.length; i++) {
            final double zoomFactor = Math.pow((double) (i + 1) / rectangles.length, 3);
            System.out.println("zoom " + zoomFactor);
            final double angle = Math.PI * Math.pow(i, 3) / Math.pow(rectangles.length, 3);
            System.out.println("angle " + angle);
            final int xOffset = (int) ((1 + Math.cos(angle)) * maxRectSize);
            final int yOffset = (int) ((1 + Math.sin(angle)) * maxRectSize);
            final int width = (int) (maxRectSize * zoomFactor);
            final int height = (int) (maxRectSize * zoomFactor);
            System.out.println("rect values: " + xOffset + "/" + yOffset + " " + width + "x" + height);
            this.rectangles[i] = new Rectangle(xOffset, yOffset, width, height);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D tmp = (Graphics2D) g;
        int currentColorIndex = 0;
        //rotation = Math.PI;  // TODO remove after testing
        for (Rectangle rectangle : this.rectangles) {
            if (rectangle == null) continue;
            final Graphics2D g2 = (Graphics2D) tmp.create(
                    rectangle.x, rectangle.y,
                    rectangle.width + 1, rectangle.height + 1);
            final Rectangle drawnRectangle = new Rectangle(rectangle);
            final int newX;
            final int newY;
            final double relRotation = rotation % (2 * Math.PI);
            System.out.println(relRotation);
            if (relRotation > Math.PI / 2 && relRotation <= 3 * Math.PI / 2) {
                newX = (int) (rectangle.width * Math.sin((relRotation - Math.PI / 4) * 2) / 2);
                newY = (int) (rectangle.height * Math.sin((relRotation - Math.PI / 4) * 2) / 2);
            } else {
                newX = (int) Math.abs(rectangle.width * Math.sin(rotation) / 2);
                newY = (int) Math.abs(rectangle.height * Math.sin(rotation) / 2);
            }
            System.out.println(newX + ", " + newY);
            drawnRectangle.setLocation(0, 0);
            g2.rotate(rotation, Math.abs(newX), Math.abs(newY));
            g2.setColor(RECTANGLE_COLORS[currentColorIndex]);
            g2.fill(drawnRectangle);
            g2.setColor(Color.BLACK);
            g2.draw(drawnRectangle);
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
        panel.addClosingListener(frame);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private class Tick implements Runnable {

        private long lastTick = System.nanoTime();

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    continue;
                }
                if (System.nanoTime() - lastTick > 1000000000 / FRAMERATE) {
                    lastTick = System.nanoTime();
                    rotation += Math.PI / 2 / FRAMERATE;
                    System.out.println(rotation);
                    repaint();
                }
            }
        }
    }

    private class ClosingListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            running = false;
        }
    }
}
