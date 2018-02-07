package components;

import stocks.StockWidget;
import stocks.graphs.GraphPoint;
import utils.HTTPUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StockHistoryPanel extends JPanel {

    private static final int PADDING_X = 10;

    private static String timeInterval = "1D";
    private static int numPoints = 0;

    public StockHistoryPanel() {
        setBackground(Color.PINK); //TODO remove
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                GraphPoint g = GraphPoint.findGraphPoint(e.getX());
                if (g != null) {
                    StockWidget.getLayerUI().paint(getGraphics(), g);
                }
            }
        });
    }

    public static void setTimeInterval(String time) {
        timeInterval = time;
        String json = HTTPUtils.getRequest("https://api.iextrading.com/1.0/stock/aapl/chart/" + timeInterval);
        System.out.println(json);
    }
}
