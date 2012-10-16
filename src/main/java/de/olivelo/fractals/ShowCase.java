package de.olivelo.fractals;

import javax.swing.*;
import java.awt.*;

/**
 * A showcase that shows off what the {@link SpinningRectangles} can do.
 *
 * @author Oliver Lorenz
 */
public class ShowCase {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Spinning Rectangles");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 400));
        frame.setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();

        final SpinningRectangles panel1 = new SpinningRectangles(50);
        panel1.setSpeed(2.5);
        panel1.addWindowListener(frame);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.25;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        frame.add(panel1, constraints);

        final SpinningRectangles panel2 = new SpinningRectangles(50);
        panel2.setSpeed(0.5);
        panel2.addWindowListener(frame);
        constraints.gridx = 1;
        constraints.gridy = 0;
        frame.add(panel2, constraints);

        final SpinningRectangles panel3 = new SpinningRectangles(50);
        panel3.setSpeed(1);
        panel3.addWindowListener(frame);
        constraints.gridx = 0;
        constraints.gridy = 1;
        frame.add(panel3, constraints);

        final SpinningRectangles panel4 = new SpinningRectangles(50);
        panel4.setSpeed(1.5);
        panel4.addWindowListener(frame);
        constraints.gridx = 1;
        constraints.gridy = 1;
        frame.add(panel4, constraints);

        final SpinningRectangles bigPanel = new SpinningRectangles(50);
        bigPanel.setSpeed(-1);
        bigPanel.addWindowListener(frame);
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        frame.add(bigPanel, constraints);

        frame.pack();
        frame.setVisible(true);
    }

}
