package net.projectdf.GUI.uiComponents;

import net.projectdf.GUI.StandardGUI;

import javax.swing.JComponent;
import java.awt.*;

public class BackgroundImagePanel extends JComponent {

    private Image img;

    public BackgroundImagePanel(String img) {
        this.img = StandardGUI.getImage(img);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int imgWidth = img.getWidth(this);
        int imgHeight = img.getHeight(this);

        for (int i = 0; i < getWidth(); i += imgWidth) {
            for (int j = 0; j < getHeight(); j += imgHeight) {
                g.drawImage(img, i, j, this);
            }
        }
        super.paintComponent(g);

    }

    public void updateBackgroundImage(String img) {
        this.img = StandardGUI.getImage(img);
        repaint();
    }

}
