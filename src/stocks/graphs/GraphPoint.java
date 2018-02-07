package stocks.graphs;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphPoint extends JComponent implements Comparable {

    private static List<GraphPoint> graphPoints = new ArrayList<>();
    private static int currentIndex = 0;

    private String date;
    private double price, change, changePct;
    private int boundRange, x, y;

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
        for (int i = 0; i < graphPoints.size(); i++) {
            graphPoints.set(i, null);
        }
        currentIndex = 0;
    }

    public static void addGraphPoint(GraphPoint g) {
        if (currentIndex < graphPoints.size()) {
            graphPoints.set(currentIndex, g);
            Collections.sort(graphPoints);
            currentIndex++;
        } else {
            if (g == null || (currentIndex == 0 || g.compareTo(graphPoints.get(currentIndex - 1)) > 0)) {
                graphPoints.add(g);
            } else {
                graphPoints.add(g);
                Collections.sort(graphPoints);
            }
            currentIndex++;
        }
    }

    public static GraphPoint findGraphPoint(int xLoc) {
        if (graphPoints.size() <= 1) {
            if (graphPoints.size() == 0) {
                return null;
            } else {
                GraphPoint g = graphPoints.get(0);
                return g.isInXRange(xLoc) ? g : null;
            }
        }

        int min = 0;
        int max = currentIndex;
        while (max != min) {
            int mid = (max - min) / 2;
            GraphPoint g = graphPoints.get(mid);
            if (g.isInXRange(xLoc)) {
                return g;
            } else {
                int x = g.getX();
                if (xLoc < x) {
                    max = mid;
                } else {
                    min = mid;
                }
            }
        }
        return null;
    }

    private boolean isInXRange(int xLoc) {
        return xLoc <= (x + boundRange) && xLoc >= (x - boundRange);
    }

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
}
