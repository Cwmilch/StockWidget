package components;

import components.buttons.TimeButton;
import components.buttons.TimeButtonPanel;
import stocks.StockWidget;
import stocks.graphs.GraphOverlay;
import stocks.graphs.GraphPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TimeEntryBar extends JTextField {

    private static final String DEFAULT; //The String to display when no user input is being entered

    private String date = DEFAULT;
    private int pos;

    static {
        DEFAULT = "mm/dd/yyyy";
    }

    public TimeEntryBar() {
        setColumns(11);
        setText(date);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(DEFAULT)) {
                    pos = 0;
                    setText("  /  /    "); //Clear the default text if nothing has been previously entered
                    setCaretPosition(0);
                } else {
                    setCaretPosition(pos);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                char key = e.getKeyChar();
                if (Character.isDigit(key)) {
                    if (date.equals("mm/dd/yyyy")) {
                        date = key + " /  /    ";
                        setText(date);
                        setCaretPosition(pos + 1);
                        pos++;
                    } else {
                        char[] dateChars = date.toCharArray();
                        dateChars[pos] = key; //Set individual character
                        date = arrayString(dateChars);
                        setText(date);
                        if (pos < 10) {
                            setCaretPosition(pos + 1);//Increment caret position if there's still room
                            pos++;
                            if (pos == 10) {
                                //Set time to user entered date
                                TimeButton.getPanel().deselectAll();
                                GraphOverlay.clearImage();
                                GraphPoint.resetPoints();
                                StockWidget.getStockPanel().setTimeInterval(date);
                                transferFocusBackward();
                            } else if (dateChars[pos] == '/') {
                                //Skip past slashes
                                pos++;
                                setCaretPosition(pos);
                            }
                        } else if (pos == 10) { //Ignore event if character limit has been reached
                            setCaretPosition(10);
                            e.setKeyChar(KeyEvent.CHAR_UNDEFINED);
                        }
                    }
                } else if (key == KeyEvent.VK_BACK_SPACE) {
                    if (pos > 0) {
                        //Delete character
                        pos--;
                        char[] dateChars = date.toCharArray();
                        if (dateChars[pos] == '/') {
                            //skip slashes
                            pos--;
                            setCaretPosition(pos);
                        }
                        dateChars[pos] = ' '; //Clear character
                        date = arrayString(dateChars);
                        setText(date);
                        setCaretPosition(pos);
                    }
                } else if (key == KeyEvent.VK_LEFT) {
                    //Move caret to the left, skip slashes
                    if (pos > 0) {
                        pos--;
                        char[] dateChars = date.toCharArray();
                        if (dateChars[pos] == '/') {
                            pos--;
                            setCaretPosition(pos);
                        }
                    }
                } else if (key == KeyEvent.VK_RIGHT) {
                    //Move caret to the right, skip slashes
                    if (pos < 10) {
                        pos++;
                        char[] dateChars = date.toCharArray();
                        if (dateChars[pos] == '/') {
                            pos++;
                            setCaretPosition(pos);
                        }
                    }
                } else {
                    setText(date);
                    setCaretPosition(pos);
                }
            }
        });

        TimeButtonPanel p = TimeButton.getPanel();
        p.add(Box.createRigidArea(new Dimension(200, 10)));
    }

    /**
     * Construct a String from an array of characters.
     *
     * @param array The char array to use
     * @return The char array as a String
     */
    private String arrayString(char[] array) {
        StringBuilder sb = new StringBuilder();
        for (char c : array) {
            sb.append(c);
        }
        return sb.toString();
    }
}
