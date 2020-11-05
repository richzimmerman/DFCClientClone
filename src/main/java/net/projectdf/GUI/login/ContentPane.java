package net.projectdf.GUI.login;

import java.awt.*;
import javax.swing.*;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class ContentPane extends JComponent {

    private Image img;

    public ContentPane() {
        img = StandardGUI.getImage(GUIConstantsConfig.LOGIN_BACKGROUND);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }
}
