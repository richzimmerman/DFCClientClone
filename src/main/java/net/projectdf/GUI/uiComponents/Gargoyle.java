package net.projectdf.GUI.uiComponents;

import java.awt.*;
import javax.swing.*;
import net.projectdf.GUI.Sprites.GargoyleSprites;

public class Gargoyle extends JPanel {

    private Image img;

    public Gargoyle() {
        setOpaque(false);
        setFocusable(false);
        img = GargoyleSprites.getSprite(GargoyleSprites.GARGOYLE);
        setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    public void updateGargoyle(int realm) {
        img = GargoyleSprites.getSprite(GargoyleSprites.GARGOYLE);
        repaint();
    }
}
