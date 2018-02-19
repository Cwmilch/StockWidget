package stocks;

import components.SearchBar;
import components.StockHistoryPanel;
import components.TickerInfoPanel;
import components.TimeEntryBar;
import components.buttons.TimeButton;
import stocks.graphs.GraphOverlay;
import stocks.tickers.StockTicker;
import stocks.tickers.TickerGen;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class StockWidget {

    private static int width = 0;
    private static int height = 0;

    private static JFrame widget;
    private static GraphOverlay layerUI;
    private static StockHistoryPanel panel;
    private static TickerInfoPanel infoPanel;

    public static void main(String[] args) {
        TickerGen.loadTickers();
        StockTicker.sortTickers();

        initGUI();
    }

    private static void initGUI() {
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double screenH = screen.getHeight();

        widget = new JFrame("Stock Info");
        layerUI = new GraphOverlay();

        StockWidget.width = 1278;
        StockWidget.height = (int) (screenH / 2);
        widget.setSize(width, height);
        widget.setLayout(new BorderLayout());
        widget.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        widget.setResizable(false);
        widget.setBackground(new Color(0x636E72));
        panel = new StockHistoryPanel();
        JLayer<JComponent> overlay = new JLayer<>(panel, layerUI);
        widget.add(overlay, BorderLayout.CENTER);


        addButtons();
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        infoPanel = new TickerInfoPanel(StockTicker.getTickers().get(0));
        topPanel.add(infoPanel);
        topPanel.add(Box.createRigidArea(new Dimension(300, 10)));
        addSearchBar(topPanel);
        Dimension size = new Dimension(width / 3, height / 6);
        topPanel.setPreferredSize(size);
        topPanel.setBounds(0, 0, size.width, size.height);
        widget.add(topPanel, BorderLayout.NORTH);
        widget.setVisible(true);
        panel.setTimeInterval("1D");
    }

    /**
     * Add the TimeButton for each time interval to the panel.
     */
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

        TimeButton.getPanel().add(new TimeEntryBar());
        widget.add(TimeButton.getPanel(), BorderLayout.SOUTH);
    }

    /**
     * Initialize the search bar.
     *
     * @param panel the JPanel to add it to
     */
    private static void addSearchBar(JPanel panel) {
        JComboBox box = new JComboBox();
        box.setEditable(true);
        ((JTextComponent) box.getEditor().getEditorComponent()).setDocument(new SearchBar(StockTicker.getTickers(), box));
        panel.add(box);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static GraphOverlay getOverlay() {
        return layerUI;
    }

    public static StockHistoryPanel getStockPanel() {
        return panel;
    }

    public static TickerInfoPanel getInfoPanel() {
        return infoPanel;
    }
}
