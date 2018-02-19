package components;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import components.buttons.TimeButton;
import stocks.StockWidget;
import stocks.graphs.GraphPoint;
import stocks.tickers.StockTicker;
import utils.HTTPUtils;
import utils.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class StockHistoryPanel extends JPanel {

    private int xOffset = 0;
    private boolean set = false;
    private StockTicker stock;

    public StockHistoryPanel() {
        stock = StockTicker.getTickers().get(0);

        this.setBorder(BorderFactory.createEmptyBorder());
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (set) { //Make sure panel isn't updating before drawing on it
                    int x = e.getX();
                    GraphPoint lastAdded = GraphPoint.getLast();
                    assert lastAdded != null;
                    if (x <= lastAdded.getX() + xOffset && x >= xOffset) {
                        GraphPoint g = GraphPoint.findGraphPoint(x);
                        if (g != null) {
                            StockWidget.getOverlay().paint(getGraphics(), g);
                        }
                    }
                }
            }
        });
    }

    public void setTimeInterval(String time) {
        if (time == null) {
            JOptionPane.showMessageDialog(null, "Unable to find data for " +
                    stock.getTicker() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            setStock(StockTicker.getTickers().get(0));
            setTimeInterval("1D");
        }
        set = false;
        StockWidget.getOverlay().clearImage();
        GraphPoint.resetPoints();
        boolean day = time.equals("1D") || time.contains("/");

        //If user entered custom date, format it for the URL request
        if (time.contains("/")) {
            String[] components = time.split("/");
            time = "date/" + components[2] + components[0] + components[1];
        }

        String value = day ? "average" : "close";

        JsonElement json = HTTPUtils.getRequest("https://api.iextrading.com/1.0/stock/" +
                stock.getTicker() + "/chart/" + time);

        JsonArray array = json.getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext()) {
            JsonObject obj = iterator.next().getAsJsonObject();
            if (obj.get(value).getAsInt() <= 0) {
                iterator.remove();
            }
        }

        int size = array.size();

        //If no data was returned, try the next largest time interval
        if (size <= 1) {
            final String s = time;
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Unable to find data for " + s + ".", "Error", JOptionPane.ERROR_MESSAGE));
            StockWidget.getOverlay().clearImage();
            GraphPoint.resetPoints();
            TimeButton.getPanel().pressFirst();
            repaint();
            revalidate();
            setTimeInterval(day && !time.equals("1D") ? "1D" : nextInterval(time));
            return;
        }

        //Set distance between GraphPoints
        int pointArea = size < 630 ? (StockWidget.getWidth() / size) : 0;
        int pointRadius = pointArea >= 2 ? pointArea / 2 : pointArea;

        //Determine max and min prices to scale y-value of points
        double min = array.get(0).getAsJsonObject().get(value).getAsDouble();
        double max = min;

        for (int i = 0; i < size; i++) {
            JsonElement element = array.get(i);
            double d = element.getAsJsonObject().get(value).getAsDouble();
            min = d < min ? d : min;
            max = d > max ? d : max;
        }

        double last = array.get(0).getAsJsonObject().get(value).getAsInt();

        for (int i = 0; i < size; i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            String date = object.get(day ? "label" : "date").getAsString();
            double price = object.get(value).getAsDouble();
            double change;
            double changePct;

            //Data for price changes is given for time intervals greater than 1D, otherwise it has to be manually calculated
            if (!day) {
                change = object.get("change").getAsDouble();
                changePct = object.get("changePercent").getAsDouble();
            } else {
                change = price - last;
                changePct = (price / last) * (change > 0 ? 1 : -1);
                last = price;
            }

            int x = (pointRadius != 0 ? (pointRadius * 2) * i : i); //Space points by calculated radius
            int y = (int) MathUtils.getScaledY(price, max, min, getY(), getHeight());
            GraphPoint g = new GraphPoint(date, price, change, changePct,
                    pointRadius, x, y); //Create a new GraphPoint for the given data, add it to the list
            GraphPoint.addGraphPoint(g);
        }

        xOffset = size > 1000 ? 0 : (StockWidget.getWidth() - GraphPoint.getLast().getX()) / 2;
        repaint();
        revalidate();
        StockWidget.getInfoPanel().passPointInfo(GraphPoint.getGraphPoint(0));
        set = true;
    }

    void setStock(StockTicker stockTicker) {
        stock = stockTicker;
        setTimeInterval("1D");
    }

    @Override
    public void paintComponent(Graphics g) {
        GraphPoint.paintLines(g);
    }

    public int getOffset() {
        return xOffset;
    }

    /**
     * Return the next largest time interval.
     *
     * @param time the current time interval
     */
    private String nextInterval(String time) {
        String s = null;
        switch (time) {
            case "1D":
                s = "1M";
                break;
            case "1M":
                s = "3M";
                break;
            case "3M":
                s = "6M";
                break;
            case "6M":
                s = "1Y";
                break;
            case "1Y":
                s = "2Y";
                break;
            case "2Y":
                s = "5Y";
                break;
            case "5Y":
                s = "YTD";
                break;
        }
        if (s != null) {
            TimeButton.getPanel().pressButton(s);
        }

        return s;
    }
}
