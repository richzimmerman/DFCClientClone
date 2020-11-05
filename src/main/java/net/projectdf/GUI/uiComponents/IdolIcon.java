package net.projectdf.GUI.uiComponents;

import java.awt.*;
import javax.swing.*;
import net.projectdf.GUI.Sprites.ControlSprites;
import net.projectdf.GUI.Sprites.Sprite;

public class IdolIcon extends JPanel {

    private Image inChaos;
    private Image inGood;
    private Image inEvil;
    private boolean notPlaced = true;
    private Image img;
    private String name;

    public IdolIcon(String name, Sprite inChaos, Sprite inGood, Sprite inEvil) {
        setOpaque(false);
        this.name = name;
        this.inChaos = ControlSprites.getSprite(inChaos);
        this.inGood = ControlSprites.getSprite(inGood);
        this.inEvil = ControlSprites.getSprite(inEvil);
        img = ControlSprites.getSprite(inChaos);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!notPlaced) {
            g.drawImage(img, 0, 0, null);
        }
    }

    public void changeLocation(int realm) {
        notPlaced = false;
        switch(realm) {
            case 1:
                img = inEvil;
                break;
            case 2:
                img = inChaos;
                break;
            case 3:
                img = inGood;
                break;
            default:
                notPlaced = true;
                break;
        }
        repaint();
    }
}
