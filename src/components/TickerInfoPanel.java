package components;

import stocks.StockWidget;
import stocks.graphs.GraphPoint;
import stocks.tickers.ImageLoader;
import stocks.tickers.StockTicker;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TickerInfoPanel extends JPanel {

    private static final Color BACKGROUND;

    private BufferedImage image;
    private StockTicker ticker;

    private List<String> info = new ArrayList<>();

    static {
        BACKGROUND = new Color(0xEEEEEE, false); //Default background color
    }

    public TickerInfoPanel(StockTicker t) {
        Thread imageThread = new Thread(new ImageLoader(t.getLogoURL(), this));
        imageThread.start(); //Download image for the default stock

        ticker = t;

        //Set panel size based on screen dimensions
        this.setPreferredSize(new Dimension(600, StockWidget.getHeight() / 6));
        this.setBounds(0, 0, StockWidget.getWidth() / 3, StockWidget.getHeight() / 6);
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        //Clear previous stock info
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawString(ticker.getTicker(), 90, 10);
        g.drawString(ticker.getCompany(), 90, 25);

        if (info.size() > 0) {
            boolean posChange = Double.parseDouble(info.get(0)) > 0;
            g.setColor(posChange ? Color.GREEN.darker() : Color.RED);
            g.drawString("Net Change Over Time: " + (posChange ? "+" : "") + format(info.get(0)) + "%", 90, 40);

            String datePrefix = info.get(1).contains("-") ? "Date: " : "Time: ";
            g.drawString(datePrefix + info.get(1), 90, 55);

            g.drawString("Price: " + format(info.get(2)), 90, 70);
            g.drawString("Change: " + format(info.get(3)) + " (" + format(info.get(4)) + "%)", 200, 70);
        }

        if (image != null) {
            g.drawImage(image, 0, 0, 83, 83, null);
        } else {
            g.setColor(BACKGROUND);
            g.fillRect(0, 0, 83, 83);
        }
    }

    /**
     * Set the GraphPoint info to display
     */
    public void passPointInfo(GraphPoint g) {
        info = new ArrayList<>(); //Clear current ArrayList
        if (g != null) {
            info.add(Double.toString(GraphPoint.getTotalChange()));
            info.add(g.getDate());
            info.add(Double.toString(g.getPrice()));
            info.add(Double.toString(g.getChange()));
            info.add(Double.toString(g.getChangePct()));
        }
        repaint();
    }

    private String format(String input) {
        DecimalFormat f = new DecimalFormat("#.###");
        return f.format(Double.parseDouble(input));
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Set the stock name/picture to display
     *
     * @param t The StockTicker containing the info
     */
    void setTicker(StockTicker t) {
        ticker = t;

        Thread imageThread = new Thread(new ImageLoader(ticker.getLogoURL(), this));
        imageThread.start();
        repaint();
    }
}
