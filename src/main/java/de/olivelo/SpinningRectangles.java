package de.olivelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * A panel full of spinning rectangles.
 * Every rectangle completes a whole rotation in 4 seconds.
 *
 * User: oliver
 * Date: 14.10.12
 * Time: 01:34
 */
public class SpinningRectangles extends JPanel {

    private static final long FRAMERATE = 60;

    private final int max;
    private final java.util.List<SpirallingRectangle> rectangles;
    private boolean initialized = false;
    private boolean running = true;

    private long framerateLastDisplayed = 0;
    private long totalFrames = 0;
    private long countedFrames = 0;
    private long currentFramerate = 0;

    public SpinningRectangles(int max) {
        this.max = max;
        this.rectangles = new ArrayList<SpirallingRectangle>(max);
        new Thread(new Tick()).start();
    }

    public void addWindowListener(Window window) {
        WindowListener listener = new WindowListener();
        window.addWindowListener(listener);
    }

    private void initializeRectangles() {
        System.out.println("Initializing rectangles...");
        while (rectangles.size() < max) {
            generateNextRectangle();
            for (SpirallingRectangle rectangle : rectangles) {
                rectangle.stepForward(0.125);
            }
        }
    }

    private void generateNextRectangle() {
        final int fillColorIndex;
        if (rectangles.size() > 0) {
            fillColorIndex = rectangles.get(rectangles.size() - 1).fillColorIndex + 1;
        } else {
            fillColorIndex = 0;
        }
        rectangles.add(new SpirallingRectangle(13, 0, new Point(getWidth()/2, getHeight()/2), fillColorIndex));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // initialize spinning rectangles, but only once
        if (!initialized) {
            initializeRectangles();
            initialized = true;
        }

        if (totalFrames % (FRAMERATE / 2) == 0) {
            rectangles.remove(0);
            generateNextRectangle();
        }

        super.paintComponent(g);
        for (SpirallingRectangle rectangle : rectangles) {
            rectangle.stepForward(1.0 / 4 / FRAMERATE);
            rectangle.paint(g);
        }

        // show framerate
        g.drawString("FPS: " + currentFramerate, 10, 20);
        countedFrames++;
        totalFrames++;
        if (System.currentTimeMillis() - framerateLastDisplayed > 1000) {
            currentFramerate = countedFrames;
            countedFrames = 0;
            framerateLastDisplayed = System.currentTimeMillis();
        }
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Spinning Rectangles");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 700));
        frame.setLayout(new FlowLayout());

        final SpinningRectangles panel = new SpinningRectangles(50);
        panel.addWindowListener(frame);

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private class Tick implements Runnable {

        private long lastTick = System.nanoTime();

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    continue;
                }
                if (System.nanoTime() - lastTick > 1000000000 / FRAMERATE) {
                    lastTick = System.nanoTime();
                    repaint();
                }
            }
        }
    }

    private class WindowListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            running = false;
        }

    }

}
