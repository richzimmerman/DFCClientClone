package net.projectdf.GUI.uiComponents;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import net.projectdf.GUI.Sprites.BorderSprites;

public class UserDisplay extends JPanel {

    private Map map;
    private Control control;

    public UserDisplay(Dimension dimension) {
        setLayout(new RelativeLayout(RelativeLayout.X_AXIS));
        setPreferredSize(dimension);
        setAlignmentY(Component.BOTTOM_ALIGNMENT);
        setBackground(Color.BLACK);
        setBorder(new PerforatedBorder(PerforatedBorder.GREY));
        setFocusable(false);

        control = new Control();
        control.setPreferredSize(new Dimension(control.getImg().getWidth(this), control.getImg().getHeight(this)));
//        control.setBorder(new PerforatedBorder());

        map = new Map();
        map.setPreferredSize(new Dimension(control.getImg().getWidth(this),
                control.getImg().getHeight(this)));
        map.setBorder(new PerforatedBorder(PerforatedBorder.GREY));

        add(control, 3f);
        add(map, 3f);

        setPreferredSize(new Dimension(control.getImg().getWidth(this), (control.getImg().getHeight(this)+ BorderSprites.B_HORIZONTAL.getHeight() * 2)));
    }

    public Map getMap() {
        return map;
    }

    public Control getControl() {
        return control;
    }

}
