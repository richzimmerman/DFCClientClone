package net.projectdf.GUI.uiComponents;

import java.awt.*;
import javax.swing.*;
import net.projectdf.GUI.Sprites.GargoyleSprites;

public class ActionWindowControl extends JPanel {

    public ActionWindowControl() {
        setOpaque(false);
        setLayout(new GridLayout(2, 1));
        setFocusable(false);
        setAlignmentX(Component.RIGHT_ALIGNMENT);

        Gargoyle gargoyle = new Gargoyle();
        ActionWindowScrollButtons scroll = new ActionWindowScrollButtons();

        add(gargoyle);
        add(scroll);

        int h = GargoyleSprites.GARGOYLE.getHeight() + GargoyleSprites.CONTROL.getHeight();
        int w = GargoyleSprites.GARGOYLE.getWidth(); // This is the wider of the 2 components
//
        setPreferredSize(new Dimension(w, h));
    }
}
