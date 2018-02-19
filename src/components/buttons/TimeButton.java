package components.buttons;

import stocks.StockWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeButton extends JButton {

    private static int x_offset = 0;
    private static int y_offset = 0;

    private static TimeButton[] timeButtons = {null, null, null, null, null, null, null, null};
    private static TimeButtonPanel panel = new TimeButtonPanel();
    private boolean pressed;
    private static int radius;

    public TimeButton(String time, int x, int y) {
        pressed = time.equals("1D");
        setLoc(x, y);
        this.setText(time);
        radius = (int) (StockWidget.getHeight() * 0.08);
        setSize(radius, radius);

        addActionListener(new TimeButtonListener());
    }

    /**
     * Add a TimeButton to the TimeButtonPanel.
     */
    public static void addButton(TimeButton t, int index) {
        if (timeButtons[index] == null) {
            if (index == 0) {
                int xArea = t.getWidth() * 8 + (t.getWidth() * 2) * 8;
                int button_offset = Math.floorDiv(StockWidget.getWidth() - xArea, 5);
                panel.add(Box.createRigidArea(new Dimension(button_offset, 0)));
            }
            timeButtons[index] = t;
            panel.add(t);
            if (index != 7)
                panel.add(Box.createRigidArea(new Dimension(t.getWidth(), 0)));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setFocusPainted(false);
        if (pressed) {
            this.setBackground(new Color(0x00b894));
            this.getModel().setPressed(true);
        } else {
            this.setBackground(new Color(0xb2bec3));
        }
    }

    class TimeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!pressed) { //If the button isn't already pressed, press it
                StockWidget.getStockPanel().setTimeInterval(e.getActionCommand());
                for (TimeButton t : timeButtons) {
                    t.deselect();
                }
                setPressed();
            } else {
                TimeButton oneDay = timeButtons[0];
                if (oneDay != e.getSource()) { //If the button is already pressed, deselect all and press "1D"
                    StockWidget.getStockPanel().setTimeInterval("1D");
                    pressed = false;
                    oneDay.pressed = true;
                    oneDay.getModel().setPressed(true);
                }
            }
            repaint();
        }
    }

    /**
     * "Unpress" a button.
     */
    void deselect() {
        pressed = false;
        getModel().setPressed(false);
    }

    /**
     * Press a button.
     */
    void setPressed() {
        pressed = true;
        getModel().setPressed(true);
    }

    /**
     * Calibrate the offset between each button based on screen dimensions.
     */
    public static void setOffsets() {
        if (x_offset == 0) {
            x_offset = StockWidget.getWidth() / 10;
        }

        if (y_offset == 0) {
            y_offset = (int) (StockWidget.getHeight() * 0.8);
        }
    }

    private void setLoc(int x, int y) {
        this.setLocation(x, y);
    }

    public static TimeButtonPanel getPanel() {
        return panel;
    }

    public static int getRadius() {
        return radius;
    }

    public static int getOffsetX() {
        return x_offset;
    }

    public static int getOffsetY() {
        return y_offset;
    }

    boolean isPressed() {
        return pressed;
    }

    @Override
    public String toString() {
        return getText();
    }
}
