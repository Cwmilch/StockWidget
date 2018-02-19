package utils;

public class MathUtils {
    private static final int PADDING_Y = 10;

    /**
     * Scale a GraphPoint's y-axis location between a given min and max value
     *
     * @param price  the price stored with a GraphPoint
     * @param min    the min price to scale the GraphPoint between
     * @param max    the max price to scale the GraphPoint between
     * @param y      the y value to scale
     * @param height the height of the panel the y-value is being scaled on
     * @return the scaled value
     */
    public static double getScaledY(double price, double min, double max, int y, int height) {
        int maxY = y + height - PADDING_Y;
        int minY = y + PADDING_Y;

        return (((maxY - minY) * (price - min)) / (max - min)) + minY;
    }
}
