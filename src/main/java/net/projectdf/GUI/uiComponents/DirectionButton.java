package net.projectdf.GUI.uiComponents;

import java.awt.*;
import javax.swing.*;
import net.projectdf.GUI.Sprites.ControlSprites;
import net.projectdf.GUI.Sprites.Sprite;

public class DirectionButton extends JPanel {

    private Image available;
    private Image unavailable;
    private Image click;
    private Image img;
    private boolean directionAvailable = false;
    private String name;

    public DirectionButton(String name, Sprite available, Sprite unavailable, Sprite click) {
        setOpaque(false);
        this.name = name;
        this.available = ControlSprites.getSprite(available);
        this.unavailable = ControlSprites.getSprite(unavailable);
        this.click = ControlSprites.getSprite(click);
        img = ControlSprites.getSprite(unavailable);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    public void isAvailable(boolean isAvailable) {
        synchronized (this) {
            directionAvailable = isAvailable;
            if (isAvailable) {
                img = available;
            } else {
                img = unavailable;
            }
            repaint();
        }
    }

    public void click() {
        img = click;
        repaint();
    }

    public void clickUp() {
        if (directionAvailable) {
            img = available;
        } else {
            img = unavailable;
        }
        repaint();
    }
}
