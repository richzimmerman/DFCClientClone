package net.projectdf.GUI.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class ControlSprites {

    private static BufferedImage spriteSheet = (BufferedImage) StandardGUI.getImage(GUIConstantsConfig.CON_SPRITES);

    public static Sprite NORTH_AVAIL = new Sprite(1, 31, 28, 28);
    public static Sprite WEST_AVAIL = new Sprite(31, 31, 28, 28);
    public static Sprite SOUTH_AVAIL = new Sprite(61, 31, 28, 28);
    public static Sprite EAST_AVAIL = new Sprite(91, 31, 28, 28);

    public static Sprite NORTH_UNAVAIL = new Sprite(1, 1, 28, 28);
    public static Sprite WEST_UNAVAIL = new Sprite(31, 1, 28, 28);
    public static Sprite SOUTH_UNAVAIL = new Sprite(61, 1, 28, 28);
    public static Sprite EAST_UNAVAIL = new Sprite(91, 1, 28, 28);

    public static Sprite NORTH_CLICKED = new Sprite(1, 61, 28, 28);
    public static Sprite WEST_CLICKED = new Sprite(31, 61, 28, 28);
    public static Sprite SOUTH_CLICKED = new Sprite(61, 61, 28, 28);
    public static Sprite EAST_CLICKED = new Sprite(91, 61, 28, 28);

    public static Sprite NW_AVAIL = new Sprite(43, 108, 15, 15);
    public static Sprite NE_AVAIL = new Sprite(60, 108, 15, 15);
    public static Sprite SE_AVAIL = new Sprite(77, 108, 15, 15);
    public static Sprite SW_AVAIL = new Sprite(94, 108, 15, 15);

    public static Sprite NW_UNAVAIL = new Sprite(43, 91, 15, 15);
    public static Sprite NE_UNAVAIL = new Sprite(60, 91, 15, 15);
    public static Sprite SE_UNAVAIL = new Sprite(77, 91, 15, 15);
    public static Sprite SW_UNAVAIL = new Sprite(94, 91, 15, 15);

    public static Sprite NW_CLICKED = new Sprite(43, 125, 15, 15);
    public static Sprite NE_CLICKED = new Sprite(60, 125, 15, 15);
    public static Sprite SE_CLICKED = new Sprite(77, 125, 15, 15);
    public static Sprite SW_CLICKED = new Sprite(94, 125, 15, 15);

    public static Sprite UP_AVAIL = new Sprite(12, 204, 9, 9);
    public static Sprite OUT_AVAIL = new Sprite(12, 215, 9, 9);
    public static Sprite DOWN_AVAIL = new Sprite(12, 226, 9, 9);

    public static Sprite UP_UNAVAIL = new Sprite(1, 204, 9, 9);
    public static Sprite OUT_UNAVAIL = new Sprite(1, 215, 9, 9);
    public static Sprite DOWN_UNAVAIL = new Sprite(1, 226, 9, 9);

    public static Sprite UP_CLICKED = new Sprite(23, 204, 9, 9);
    public static Sprite OUT_CLICKED = new Sprite(23, 215, 9, 9);
    public static Sprite DOWN_CLICKED = new Sprite(23, 226, 9, 9);

    public static Sprite HEALTH_BAR = new Sprite(1, 91, 12, 111);
    public static Sprite FAT_BAR = new Sprite(15, 91, 12, 111);
    public static Sprite POWER_BAR = new Sprite(29, 91, 12, 111);

    public static Sprite TIMER_BAR = new Sprite(43, 157, 8, 45);

    public static Sprite MAP_POINTER = new Sprite(34, 261, 10, 10);

    // Power: Circle
    // Knowledge: Triangle
    // Strength: Square
    // Naming convention is "IDOL_<TYPE>_<HOME_LOCATION>_<CURRENT_LOCATION>" initials.
    public static Sprite IDOL_Pow_C_C = new Sprite(69, 254, 14, 14);
    public static Sprite IDOL_Pow_C_G = new Sprite(69, 238, 14, 14);
    public static Sprite IDOL_Pow_C_E = new Sprite(69, 270, 14, 14);
    public static Sprite IDOL_Pow_E_C = new Sprite(53, 254, 14, 14);
    public static Sprite IDOL_Pow_E_G = new Sprite(53, 238, 14, 14);
    public static Sprite IDOL_Pow_E_E = new Sprite(53, 270, 14, 14);
    public static Sprite IDOL_Pow_G_C = new Sprite(85, 254, 14, 14);
    public static Sprite IDOL_Pow_G_G = new Sprite(85, 238, 14, 14);
    public static Sprite IDOL_Pow_G_E = new Sprite(85, 270, 14, 14);

    public static Sprite IDOL_Know_C_C = new Sprite(69, 206, 14, 14);
    public static Sprite IDOL_Know_C_G = new Sprite(69, 190, 14, 14);
    public static Sprite IDOL_Know_C_E = new Sprite(69, 222, 14, 14);
    public static Sprite IDOL_Know_E_C = new Sprite(53, 206, 14, 14);
    public static Sprite IDOL_Know_E_G = new Sprite(53, 190, 14, 14);
    public static Sprite IDOL_Know_E_E = new Sprite(53, 222, 14, 14);
    public static Sprite IDOL_Know_G_C = new Sprite(85, 206, 14, 14);
    public static Sprite IDOL_Know_G_G = new Sprite(85, 190, 14, 14);
    public static Sprite IDOL_Know_G_E = new Sprite(85, 222, 14, 14);

    public static Sprite IDOL_Str_C_C = new Sprite(69, 158, 14, 14);
    public static Sprite IDOL_Str_C_G = new Sprite(69, 142, 14, 14);
    public static Sprite IDOL_Str_C_E = new Sprite(69, 174, 14, 14);
    public static Sprite IDOL_Str_E_C = new Sprite(53, 158, 14, 14);
    public static Sprite IDOL_Str_E_G = new Sprite(53, 142, 14, 14);
    public static Sprite IDOL_Str_E_E = new Sprite(53, 174, 14, 14);
    public static Sprite IDOL_Str_G_C = new Sprite(85, 158, 14, 14);
    public static Sprite IDOL_Str_G_G = new Sprite(85, 142, 14, 14);
    public static Sprite IDOL_Str_G_E = new Sprite(85, 174, 14, 14);

    public static Image getSprite(Sprite s) {
        return spriteSheet.getSubimage(s.getX(), s.getY(), s.getWidth(), s.getHeight());
    }

}
