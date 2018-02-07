package components.buttons;

import components.StockHistoryPanel;
import stocks.StockWidget;
import stocks.graphs.GraphPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeButton extends JButton {

    private static int x_offset = 0;
    private static int y_offset = 0;
    private static int button_offset = 0;

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

    private void setPressed() {
        this.pressed = true;
    }

    private void setLoc(int x, int y) {
        this.setLocation(x, y);
    }

    public static void addButton(TimeButton t, int index) {
        if (timeButtons[index] == null) {
            if (index == 0) {
                int xArea = t.getWidth() * 8 + (t.getWidth() * 2) * 8;
                button_offset = Math.floorDiv(StockWidget.getWidth() - xArea, 5);
                panel.add(Box.createRigidArea(new Dimension(button_offset, 0)));
            }
            timeButtons[index] = t;
            panel.add(t);
            if (index != 7)
                panel.add(Box.createRigidArea(new Dimension(t.getWidth(), 0)));
        }
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

    public static void setOffsets() {
        if (x_offset == 0) {
            x_offset = StockWidget.getWidth() / 10;
        }

        if (y_offset == 0) {
            y_offset = (int) (StockWidget.getHeight() * 0.8);
        }
    }

    public static TimeButtonPanel getPanel() {
        return panel;
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
            if (!pressed) {
                StockHistoryPanel.setTimeInterval(e.getActionCommand());
                for (TimeButton t : timeButtons) {
                    t.pressed = false;
                    t.getModel().setPressed(false);
                }
                pressed = true;
                getModel().setPressed(true);
                GraphPoint.resetPoints();
            } else {
                TimeButton oneDay = timeButtons[0];
                if (oneDay != e.getSource()) {
                    StockHistoryPanel.setTimeInterval("1D");
                    pressed = false;
                    oneDay.setPressed();
                    oneDay.getModel().setPressed(true);
                    GraphPoint.resetPoints();
                }
            }
            repaint();
        }
    }
}
