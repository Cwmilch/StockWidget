package stocks.tickers;

public class TickerGen {

    private static boolean tickersSet = false;

    public static void loadTickers() {
        if (!tickersSet) {
            Thread nasdaq = new Thread(new TickerScanner("/nasdaqlisted.txt"));
            nasdaq.start();
            Thread other = new Thread(new TickerScanner("/otherlisted.txt"));
            other.start();
            try {
                nasdaq.join();
                other.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tickersSet = true;
        }
    }
}
