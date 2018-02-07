package utils;

import components.TickerInfoPanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageLoader implements Runnable {

    private URL url;
    private TickerInfoPanel panel;

    public ImageLoader(URL url, TickerInfoPanel panel) {
        this.url = url;
        this.panel = panel;
    }

    @Override
    public void run() {
        try {
            BufferedImage image = ImageIO.read(url);
            panel.setImage(image);
        } catch (IOException e) {
            panel.setImage(null);
        }
    }
}
