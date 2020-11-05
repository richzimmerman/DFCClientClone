package net.projectdf.GUI.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class BorderSprites {

    private static BufferedImage spriteSheet = (BufferedImage) StandardGUI.getImage(GUIConstantsConfig.CON_SPRITES);

    public static Sprite GR_TOP_LEFT = new Sprite(61, 286, 4, 4);
    public static Sprite GR_TOP_RIGHT = new Sprite(66, 286, 4, 4);
    public static Sprite GR_BOTTOM_LEFT = new Sprite(71, 286, 4, 4);
    public static Sprite GR_BOTTOM_RIGHT = new Sprite(76, 286, 4, 4);

    public static Sprite GR_HORIZONTAL = new Sprite(61, 296, 16, 4);
    public static Sprite GR_VERTICAL = new Sprite(81, 286, 4, 16);

    public static Sprite B_TOP_LEFT = new Sprite(31, 286, 4, 4);
    public static Sprite B_TOP_RIGHT = new Sprite(36, 286, 4, 4);
    public static Sprite B_BOTTOM_LEFT = new Sprite(41, 286, 4, 4);
    public static Sprite B_BOTTOM_RIGHT = new Sprite(46, 286, 4, 4);

    public static Sprite B_HORIZONTAL = new Sprite(31, 296, 16, 4);
    public static Sprite B_VERTICAL = new Sprite(51, 286, 4, 16);

    public static Sprite G_TOP_LEFT = new Sprite(1, 286, 4, 4);
    public static Sprite G_TOP_RIGHT = new Sprite(6, 286, 4, 4);
    public static Sprite G_BOTTOM_LEFT = new Sprite(11, 286, 4, 4);
    public static Sprite G_BOTTOM_RIGHT = new Sprite(16, 286, 4, 4);

    public static Sprite G_HORIZONTAL = new Sprite(1, 296, 16, 4);
    public static Sprite G_VERTICAL = new Sprite(21, 286, 4, 16);

    public static Image getSprite(Sprite s) {
        return spriteSheet.getSubimage(s.getX(), s.getY(), s.getWidth(), s.getHeight());
    }
}
