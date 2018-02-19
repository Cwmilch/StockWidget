package stocks.graphs;

import components.StockHistoryPanel;
import stocks.StockWidget;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPoint extends JComponent implements Comparable {

    private static List<GraphPoint> graphPoints;
    private static int currentIndex;
    private static Color graphColor;

    private String date;
    private double price, change, changePct;
    private int boundRange, x, y;
    private static double totalChange;

    static {
        graphPoints = new ArrayList<>();
        currentIndex = 0;
        graphColor = Color.GREEN;

        totalChange = 0d;
    }

    public GraphPoint(String date, double closePrice, double change, double changePct, int boundRange, int x, int y) {
        this.date = date;
        this.price = closePrice;
        this.change = change;
        this.changePct = changePct;
        this.boundRange = boundRange;
        this.x = x;
        this.y = y;
    }

    public static void resetPoints() {
        graphPoints = new ArrayList<>();
        currentIndex = 0;
    }

    public static void addGraphPoint(GraphPoint g) {
        if (currentIndex < graphPoints.size()) {
            graphPoints.set(currentIndex, g);
        } else {
            graphPoints.add(g);
        }

        currentIndex++;
        StockWidget.getStockPanel().add(g);
    }

    /**
     * Binary Search to find GraphPoint by x location.
     *
     * @return the GraphPoint if found, otherwise null
     */
    private static int findGraphPoint(int max, int min, int xLoc) {
        int offset = StockHistoryPanel.getOffset();
        if (graphPoints.size() <= 1) {
            if (graphPoints.size() == 0) {
                return -1;
            } else {
                GraphPoint g = graphPoints.get(0);
                return g.isInXRange(xLoc) ? 0 : -1;
            }
        }

        min = min == -1 ? 0 : min; //Set min to zero if it's the first iteration
        max = max == -1 ? currentIndex : max; //Set max to the current index if it's the first iteration

        if (max == min) {
            return -1;
        }

        int mid = (max + min) / 2;
        GraphPoint g = graphPoints.get(mid);
        if (g.isInXRange(xLoc)) {
            return mid;
        }
        int x = g.getX();

        if (xLoc - offset < x)
            return findGraphPoint(mid, min, xLoc);

        if (xLoc - offset > x)
            return findGraphPoint(max, mid + 1, xLoc);

        return -1;
    }

    /**
     * Find the GraphPoint at a given location along the x-axis.
     *
     * @param xLoc the location to try to find a GraphPoint at
     * @return the GraphPoint if found, otherwise null
     */
    public static GraphPoint findGraphPoint(int xLoc) {
        int index = findGraphPoint(-1, -1, xLoc);
        return index != -1 ? graphPoints.get(index) : null;
    }

    /**
     * Draw the lines connecting all GraphPoints together.
     */
    public static void paintLines(Graphics g) {
        StockWidget.getOverlay().resetTick();
        if (currentIndex != 0) {
            int offset = StockHistoryPanel.getOffset();
            Graphics2D graphics = (Graphics2D) g;
            totalChange = graphPoints.get(currentIndex - 1).price - graphPoints.get(0).price;
            graphColor = totalChange > 0 ? Color.GREEN : Color.RED;

            for (int i = 0; i < currentIndex - 1; i++) {
                graphics.setColor(graphColor);
                GraphPoint from = graphPoints.get(i);
                GraphPoint to = graphPoints.get(i + 1);
                graphics.drawLine(from.getX() + offset, from.getY(), to.getX() + offset, to.getY());
            }
        }
    }

    /**
     * Draw the lines connecting this point to the rest of the graph.
     */
    void repaintLines(Graphics2D g) {
        g.setColor(graphColor);
        int offset = StockHistoryPanel.getOffset();
        int index = findGraphPoint(-1, -1, getX() + offset);

        if (index == currentIndex - 1) {
            GraphPoint to = graphPoints.get(index - 1);
            g.drawLine(this.x + offset, this.y, to.x + offset, to.y);
        } else if (index == 0) {
            GraphPoint to = graphPoints.get(1);
            g.drawLine(this.x + offset, this.y, to.x + offset, to.y);
        } else {
            GraphPoint behind = graphPoints.get(index - 1);

            g.drawLine(behind.x + offset, behind.y, this.x + offset, this.y);

            GraphPoint ahead = graphPoints.get(index + 1);
            g.drawLine(this.x + offset, this.y, ahead.x + offset, ahead.y);
        }
    }

    /**
     * Return whether or not a given location along the x-axis is within the radius of the GraphPoint.
     */
    private boolean isInXRange(int xLoc) {
        int offset = StockHistoryPanel.getOffset();
        return (xLoc - offset) <= (x + boundRange) && (xLoc - offset) >= (x - boundRange);
    }

    /**
     * Return the last GraphPoint added to an array, null if none have been added.
     */
    public static GraphPoint getLast() {
        return currentIndex > 0 ? graphPoints.get(graphPoints.size() - 1) : null;
    }

    /**
     * Get a GraphPoint from the graphPoints ArrayList.
     *
     * @param index the index of the GraphPoint to get
     * @return the GraphPoint if found, otherwise null
     */
    public static GraphPoint getGraphPoint(int index) {
        return graphPoints.size() > 0 ? graphPoints.get(index) : null;
    }

    //Used to compare GraphPoints by x-axis location
    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        } else {
            return toString().compareTo(o.toString());
        }
    }

    @Override
    public String toString() {
        return x + "";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getTotalChange() {
        return totalChange;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public double getChangePct() {
        return changePct;
    }

    Color getGraphColor() {
        return graphColor;
    }
}
