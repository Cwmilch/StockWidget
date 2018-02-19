package components;

import components.buttons.TimeButton;
import stocks.StockWidget;
import stocks.tickers.StockTicker;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchBar extends PlainDocument {

    private List<StockTicker> tickers;
    private JComboBox comboBox;
    private JTextComponent comboEditor;

    private boolean selecting = false;
    private boolean backspace = false;
    private boolean onSelection = false;
    private AttributeSet attributeSet = null;
    private String typed = "";

    private List<StockTicker> matches;

    public SearchBar(List<StockTicker> tickers, JComboBox container) {
        this.tickers = tickers;
        comboBox = container;
        comboBox.setPreferredSize(new Dimension(300, comboBox.getHeight()));
        comboEditor = (JTextComponent) comboBox.getEditor().getEditorComponent();

        comboEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                backspace = false;
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    backspace = true;
                    onSelection = comboEditor.getSelectionStart() != comboEditor.getSelectionEnd();
                    if (typed.length() > 0)
                        typed = typed.substring(0, typed.length() - 1);
                    try {
                        //highlight auto-completed section
                        comboEditor.setCaretPosition(comboBox.getSelectedItem().toString().length());
                        comboEditor.moveCaretPosition(typed.length());
                    } catch (IllegalArgumentException ex) {
                        comboEditor.setCaretPosition(typed.length());
                    }
                    super.keyPressed(e);
                } else if (keyCode == KeyEvent.VK_DELETE) {
                    e.consume(); //ignore the event
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    TimeButton.getPanel().pressButton("1D"); //Default time interval
                    StockTicker ticker = matches.get(comboBox.getSelectedIndex());
                    StockWidget.getInfoPanel().setTicker(ticker);
                    StockWidget.getStockPanel().setStock(ticker);

                    //Remove highlighting
                    comboEditor.setCaretPosition(ticker.toString().length());
                    comboEditor.moveCaretPosition(ticker.toString().length());

                    comboBox.setPopupVisible(false);
                } else {
                    char c = e.getKeyChar();
                    if (Character.isLetterOrDigit(c) || c == ' ') {
                        typed += Character.toString(e.getKeyChar());
                        super.keyPressed(e);
                    } else {
                        if (matches != null) {
                            for (StockTicker t : matches) {
                                //If character isn't a letter or number, make sure it's a valid stock character
                                if (t.toString().contains(Character.toString(e.getKeyChar()))) {
                                    typed += Character.toString(e.getKeyChar());
                                    super.keyPressed(e);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void insertString(int offset, String string, AttributeSet s) throws BadLocationException {
        attributeSet = attributeSet == null ? s : attributeSet;
        if (selecting)
            return;
        super.insertString(offset, string, s);
        matches = findMatches(typed);
        if (matches.size() > 0) {
            comboBox.setModel(new DefaultComboBoxModel<>((matches.toArray())));
            StockTicker t = matches.get(0);

            selecting = true;
            comboBox.getModel().setSelectedItem(t);
            selecting = false;

            //Clear last auto-complete, insert the new one
            super.remove(0, getLength());
            super.insertString(0, t.toString(), s);

            //Highlight text
            comboEditor.setCaretPosition(t.toString().toLowerCase().indexOf(typed.toLowerCase()) + typed.length());
            comboEditor.moveCaretPosition(getLength());

            comboBox.setPopupVisible(true);
        } else {
            comboBox.setPopupVisible(false);
            super.remove(0, getLength());
            super.insertString(0, typed, s);
        }
    }

    @Override
    public void remove(int offset, int length) throws BadLocationException {
        if (selecting)
            return;
        if (backspace) {
            if (offset > 0) {
                offset -= onSelection ? 1 : 0;
            }
            super.remove(offset, length + 1);
            insertString(0, typed, attributeSet);
        } else {
            super.remove(offset, length);
        }
    }

    /**
     * Search all stocks for a matching company name/ticker symbol
     *
     * @param s The string to search
     * @return all matches
     */
    private List<StockTicker> findMatches(String s) {
        if (s.equals("")) {
            return tickers;
        }
        String query = s.toUpperCase(); //force case matching
        List<StockTicker> tickerMatches = new ArrayList<>();
        List<StockTicker> companyMatches = new ArrayList<>();

        Thread t1 = new Thread(() -> {
            boolean match = false;
            for (StockTicker t : tickers) {
                if (t.getTicker().toUpperCase().startsWith(query)) {
                    tickerMatches.add(t);
                    match = true;
                } else {
                    if (match) {
                        break;
                    }
                }
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            for (StockTicker t : tickers) {
                if (!tickerMatches.contains(t)) { //Make sure the ticker hasn't already been added
                    if (t.getCompany().toUpperCase().startsWith(query)) {
                        companyMatches.add(t);
                    }
                }
            }
        });
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tickerMatches.addAll(companyMatches); //combine results
        return tickerMatches;
    }
}
