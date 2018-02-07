package stocks;

import components.StockHistoryPanel;
import components.TickerInfoPanel;
import components.buttons.TimeButton;
import stocks.graphs.GraphOverlay;
import stocks.graphs.GraphPoint;
import stocks.tickers.StockTicker;
import stocks.tickers.TickerGen;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class StockWidget {

    private static int width = 0;
    private static int height = 0;
    private static JFrame widget;
    private static LayerUI<JComponent> layerUI;

    public static void main(String[] args) {
        initGUI();
        addButtons();

        TickerGen.loadTickers();
        StockTicker.sortTickers();
    }

    private static void initGUI() {
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double screenW = screen.getWidth();
        double screenH = screen.getHeight();

        widget = new JFrame("Stock Info");
        layerUI = new GraphOverlay();

        StockWidget.width = (int) (screenW / 1.5);
        StockWidget.height = (int) (screenH / 2);
        widget.setSize(width, height);
        widget.setLayout(new BorderLayout());
        widget.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        widget.setResizable(false);
        widget.setBackground(new Color(0x636E72));
        StockHistoryPanel p = new StockHistoryPanel();
        GraphPoint g = new GraphPoint("", 1d, 1d, 1d, 25, 1, 1);
        GraphPoint.addGraphPoint(g);
        p.add(g);
        JLayer<JComponent> overlay = new JLayer<>(p, layerUI);
        widget.add(overlay, BorderLayout.CENTER);

        addButtons();
        try {
            widget.add(new TickerInfoPanel(
                            new StockTicker("AAPL", "Apple", new URL("http://google.com"))),
                    BorderLayout.NORTH);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        widget.setVisible(true);
    }

    private static void addButtons() {
        TimeButton.setOffsets();

        String[] times = {"1D", "1M", "3M", "6M", "1Y", "2Y", "5Y", "YTD"};
        int x = TimeButton.getOffsetX();
        int y = TimeButton.getOffsetY();
        int radius = TimeButton.getRadius();

        for (int i = 0; i < times.length; i++) {
            int xCoord = x + ((int) (radius * 1.5) * i);
            TimeButton.addButton(new TimeButton(times[i], xCoord, y), i);
        }

        widget.add(TimeButton.getPanel(), BorderLayout.SOUTH);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static LayerUI<JComponent> getLayerUI() {
        return layerUI;
    }
}
