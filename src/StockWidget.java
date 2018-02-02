import javax.swing.*;
import java.awt.*;

public class StockWidget {

    public static void main(String[] args) {
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double screenW = screen.getWidth();
        double screenH = screen.getHeight();
        JFrame widget = new JFrame("Stock Info");
        widget.setSize((int) (screenW / 1.5), (int) (screenH / 2));
        widget.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        widget.setVisible(true);
        widget.setResizable(false);
    }
}
