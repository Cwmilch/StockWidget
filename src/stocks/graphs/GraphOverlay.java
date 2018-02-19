package stocks.graphs;

import components.StockHistoryPanel;
import stocks.StockWidget;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphOverlay extends LayerUI<JComponent> {

    private BufferedImage image;
    private Graphics2D imgGraphics;
    private GraphPoint lastPoint = null;

    //Whether or not the GraphOverlay has been painted since the StockHistoryPanel's time interval changed
    private static boolean firstTick;

    static {
        firstTick = true;
    }

    @Override
    public void paint(Graphics g, JComponent j) {
        super.paint(g, j);
        StockHistoryPanel panel = StockWidget.getStockPanel();
        if (image == null) {
            image = new BufferedImage(StockWidget.getStockPanel().getWidth(),
                    panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            imgGraphics = image.createGraphics();
        }
        if (j instanceof GraphPoint) {
            GraphPoint point = (GraphPoint) j;
            if (firstTick) {
                SwingUtilities.invokeLater(() -> {
                    clearLine(imgGraphics);
                    firstTick = false;
                });
            } else {
                clearLine(imgGraphics);
            }
            lastPoint = point;

            drawLine(point.getX());
            SwingUtilities.invokeLater(() -> StockWidget.getInfoPanel().passPointInfo(point));
            g.drawImage(image, 0, 0, null);
        }
    }

    /**
     * Reset the BufferedImage, called when the time interval is changed on the StockHistoryPanel.
     */
    public void clearImage() {
        if (image != null) {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    image.setRGB(i, j, 0);
                }
            }
        }
    }

    /**
     * Clear the last drawn vertical line.
     */
    private void clearLine(Graphics2D g) {
        if (lastPoint != null) {
            GraphPoint point = lastPoint;
            int x = point.getX() + StockHistoryPanel.getOffset();
            if (x > image.getWidth()) {
                return;
            }
            int y = point.getY();
            int rgb = new Color(0xEEEEEE, false).getRGB(); //Default background color
            for (int i = 0; i < image.getHeight(); i++) {
                if (i == y) {//Make sure it isn't clearing a point
                    image.setRGB(x, y, new Color(point.getGraphColor().getRGB(), false).getRGB());
                } else {
                    image.setRGB(x, i, rgb);
                }
            }

            point.repaintLines(g);//Repaint lines that connected the previous GraphPoint point to the rest of the graph
        }
    }

    /**
     * Draw a vertical line down the screen to indicate the selected GraphPoint.
     *
     * @param x x-position of the line
     */
    private void drawLine(int x) {
        int offsetX = x + StockHistoryPanel.getOffset();
        for (int i = 0; i < image.getHeight(); i++) {
            image.setRGB(offsetX, i, Color.RED.getRGB());
        }
    }

    void resetTick() {
        firstTick = true;
    }
}
