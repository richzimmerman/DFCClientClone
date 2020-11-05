package net.projectdf.GUI.uiComponents;

import javax.swing.*;

public class TransparentButton extends JButton {
    
    public TransparentButton() {
        super();
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setFocusable(false);
    }
}
