package components;

import stocks.StockWidget;
import stocks.tickers.StockTicker;
import utils.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TickerInfoPanel extends JPanel {

    private BufferedImage image;
    private StockTicker ticker;

    public TickerInfoPanel(StockTicker t) {
        Thread imageThread = new Thread(new ImageLoader(t.getLogoURL(), this));
        imageThread.start();

        ticker = t;
        this.setPreferredSize(new Dimension(StockWidget.getWidth() / 3, StockWidget.getHeight() / 6));
        this.setBackground(Color.GREEN); //TODO remove
        this.setBounds(0, 0, StockWidget.getWidth() / 3, StockWidget.getHeight() / 6);
        this.setVisible(true);
    }

    private void setComponents() {

    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }
}
