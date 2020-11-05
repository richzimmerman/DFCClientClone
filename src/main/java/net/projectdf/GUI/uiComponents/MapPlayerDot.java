package net.projectdf.GUI.uiComponents;

import java.awt.*;
import javax.swing.*;
import net.projectdf.GUI.Sprites.ControlSprites;
import net.projectdf.GUI.Sprites.Sprite;

public class MapPlayerDot extends JPanel {

    private Image img;
    private boolean inMap = false;

    public MapPlayerDot(Sprite sprite) {
        setOpaque(false);
        img = ControlSprites.getSprite(sprite);
        setPreferredSize(new Dimension(ControlSprites.MAP_POINTER.getWidth(), ControlSprites.MAP_POINTER.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inMap) {
            g.drawImage(img, 0, 0, null);
        }
    }

    public void setInMap(boolean inMap) {
        this.inMap = inMap;
    }
}
