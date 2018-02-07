package stocks.graphs;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

public class GraphOverlay extends LayerUI<JComponent> {

    @Override
    public void paint(Graphics g, JComponent j) {
        if (j instanceof GraphPoint) {
            //TODO paint point info
        }
        super.paint(g, j);
    }
}
