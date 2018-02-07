package stocks.tickers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockTicker implements Comparable {

    private static List<StockTicker> tickers = new ArrayList<>();

    private String ticker;
    private String company;
    private URL logoURL;


    public StockTicker(String ticker, String company, URL logoURL) {
        this.ticker = ticker;
        this.company = company;
        this.logoURL = logoURL;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompany() {
        return company;
    }

    public URL getLogoURL() {
        return logoURL;
    }

    public List<StockTicker> getTickers() {
        return tickers;
    }

    public BufferedImage getLogo() {
        try {
            return ImageIO.read(logoURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static void addTicker(StockTicker ticker) {
        tickers.add(ticker);
    }

    public static void sortTickers() {
        Collections.sort(tickers);
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return ticker + ": " + company;
    }
}
