package net.projectdf.GUI.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class GargoyleSprites {

    public static Sprite GARGOYLE = new Sprite(0, 0, 115, 159);
    public static Sprite CONTROL = new Sprite(0, 161, 78, 140);
    public static Sprite LOGGER = new Sprite(80, 164, 33, 32);
    public static Sprite UP_PRESSED = new Sprite(80, 199, 33, 32);
    public static Sprite DOWN_PRESSED = new Sprite(80, 234, 33, 32);
    public static Sprite RETURN_PRESSED = new Sprite(80, 269, 33, 32);

    private static BufferedImage spriteSheet = (BufferedImage) StandardGUI.getImage(GUIConstantsConfig.gargoyles.get(2));

    public static Image getSprite(Sprite s) {
        return spriteSheet.getSubimage(s.getX(), s.getY(), s.getWidth(), s.getHeight());
    }

}
