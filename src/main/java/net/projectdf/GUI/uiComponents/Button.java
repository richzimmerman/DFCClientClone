package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class Button extends JButton {

    private ImageIcon hoveredIcon;
    private ImageIcon clickedIcon;

    public Button(ImageIcon neutralIcon) {
        super();
        setIcon(neutralIcon);
        setBorderPainted(false);
        setBackground(new Color(0, 0, 0, 0));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // Do nothing
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                setIcon(clickedIcon);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                setIcon(neutralIcon);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                setIcon(hoveredIcon);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                setIcon(neutralIcon);
            }
        });
    }

    public void setHoveredIcon(ImageIcon i) {
        hoveredIcon = i;
    }

    public void setClickedIcon(ImageIcon i) {
        clickedIcon = i;
    }

}
