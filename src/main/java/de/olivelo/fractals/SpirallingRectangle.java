package de.olivelo.fractals;

import java.awt.*;

/**
 * A spinning, moving rectangle that spirals out from a center.
 * The moving and spinning has to be controlled outside of this class
 * with the method {@link #stepForward(double)}.
 *
 * @author Oliver Lorenz
 */
public class SpirallingRectangle {

    public static final Color[] FILL_COLORS =
            {new Color(0xaa55aa), new Color(0x3c3c3c), new Color(0x993366), Color.MAGENTA};

    private final Point center;
    public final int fillColorIndex;

    private double distanceToCenter;
    private double angleToCenter;
    private double shapeRotation;

    private double rotationSpeed = -1;
    private double speed = 1;

    private Point position;
    private Rectangle shape;

    public SpirallingRectangle(double distanceToCenter, double angleToCenter, Point center, int fillIndex) {
        this.center = center;
        fillColorIndex = fillIndex % FILL_COLORS.length;

        this.angleToCenter = angleToCenter;
        this.distanceToCenter = distanceToCenter;

        stepForward(0);
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }

    /**
     * Advances this spiralling rectangle on its spiral.
     * The given percentage is the percentage of one full circle around the center.
     *
     * @param percentage the percentage of a full circle to advance
     */
    public void stepForward(double percentage) {
        shapeRotation += rotationSpeed * percentage * 2 * Math.PI;
        angleToCenter += speed * percentage * 2 * Math.PI;
        distanceToCenter *= (1 + percentage);

        int x = center.x + (int) (Math.cos(angleToCenter) * distanceToCenter);
        int y = center.y + (int) (Math.sin(angleToCenter) * distanceToCenter);
        int width = (int) (distanceToCenter / 10);
        int height = (int) (distanceToCenter / 10);
        position = new Point(x, y);
        shape = new Rectangle(-width/2, -height/2, width, height);
    }

    public void paint(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create(
                position.x - shape.width / 2, position.y - shape.height / 2,
                2 * shape.width, 2 * shape.height);
        g2.translate(shape.width, shape.height);
        g2.rotate(shapeRotation);
        g2.setColor(FILL_COLORS[fillColorIndex]);
        g2.fill(shape);
        g2.setColor(Color.BLACK);
        g2.draw(shape);
    }

}
