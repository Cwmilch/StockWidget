package stocks.tickers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Scanner;

public class TickerScanner implements Runnable {

    private String text;
    private boolean nasdaq;

    TickerScanner(String file) {
        nasdaq = file.contains("nasdaq");
        InputStream is = getClass().getResourceAsStream(file);
        Scanner s = new Scanner(is).useDelimiter("\\A");
        text = s.next();
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new StringReader(text));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                int split = line.indexOf("|");
                String ticker = nasdaq ? line.substring(0, split)
                        : line.substring(line.lastIndexOf("|") + 1);

                String beginCompany = line.substring(split + 1);
                String company = beginCompany.substring(0, beginCompany.indexOf("|"));

                StockTicker.addTicker(new StockTicker(ticker, company));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
