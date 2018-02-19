package stocks.tickers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockTicker implements Comparable {

    private static List<StockTicker> tickers;

    private String ticker;
    private String company;
    private URL logoURL;

    static {
        tickers = new ArrayList<>();
    }


    StockTicker(String ticker, String company) {
        this.ticker = ticker;
        this.company = company;
        try {
            logoURL = new URL("https://storage.googleapis.com/iex/api/logos/" + ticker + ".png");
        } catch (MalformedURLException e) {
            logoURL = null;
        }
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

    public static List<StockTicker> getTickers() {
        return tickers;
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
