package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import net.projectdf.GUI.Sprites.ControlSprites;
import net.projectdf.GUI.Sprites.Sprite;

public class StatusBar extends JPanel {

    private Image img;
    private Image baseImage;

    private int y = 0;

    public StatusBar(Sprite sprite) {
        setOpaque(false);
        img = ControlSprites.getSprite(sprite);
        baseImage = ControlSprites.getSprite(sprite);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, y, null);
    }

    public void updateStatusBar(int max, int current) {
        if (current > max) {
            max = current;
        }
        float percent = ((float) current / (float) max);
        int imgHeight = (int)(baseImage.getHeight(null) * percent);
        // An exception is thrown if the image height is 0 or negative so keep this at a minimum of 1; Hopefully
        imgHeight = imgHeight <= 1 ? 1 : imgHeight;
        int imgY = baseImage.getHeight(null) - imgHeight;
        y = baseImage.getHeight(null) - (int)(baseImage.getHeight(null) * percent);
        img = ((BufferedImage) baseImage).getSubimage(0, imgY, ((BufferedImage) baseImage).getWidth(), imgHeight);
        repaint();
    }

}
