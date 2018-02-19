package components.buttons;

import javax.swing.*;
import java.awt.*;

public class TimeButtonPanel extends JPanel {
    TimeButtonPanel() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout(layout);
    }

    /**
     * Deselect all TimeButtons.
     */
    public void deselectAll() {
        for (Component c : getComponents()) {
            if (c instanceof TimeButton) {
                TimeButton b = (TimeButton) c;
                b.deselect();
            }
        }
    }

    /**
     * Press the first button if it hasn't already been pressed.
     */
    public void pressFirst() {
        TimeButton first = null;
        boolean allDeselected = true;
        for (Component c : getComponents()) {
            if (c instanceof TimeButton) {
                TimeButton b = (TimeButton) c;
                if (b.isPressed()) {
                    allDeselected = false;
                    break;
                }

                if (b.toString().equals("1D")) {
                    first = b;
                }
            }
        }
        if (allDeselected) {
            assert first != null;
            first.setPressed();
        }
    }

    /**
     * Press a specific TimeButton
     *
     * @param time The time interval the button is associated with.
     */
    public void pressButton(String time) {
        deselectAll();
        TimeButton button = null;
        for (Component c : getComponents()) {
            if (c instanceof TimeButton) {
                TimeButton b = (TimeButton) c;
                if (b.toString().equals(time)) {
                    button = b;
                }
            }
        }

        assert button != null;
        button.setPressed();
    }
}
