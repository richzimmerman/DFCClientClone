package net.projectdf.GUI.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class MenuButtons {

    private static BufferedImage spriteSheet = StandardGUI.getImage(GUIConstantsConfig.MENU_BUTTONS);

    public static int PLAY_WIDTH = 130;
    public static int PLAY_HEIGHT = 49;

    public static int CREATE_WIDTH = 171;
    public static int CREATE_HEIGHT = 40;

    public static int CREDITS_WIDTH = 171;
    public static int CREDITS_HEIGHT = 40;

    public static int QUIT_WIDTH = 171;
    public static int QUIT_HEIGHT = 45;

    public static int[] PLAY_GRAY = {0, 0};
    public static int[] PLAY_RED = {132, 0};
    public static int[] PLAY_GREEN = {264, 0};
    public static int[] PLAY_GLOW = {396, 0};

    public static int[] CREATE_GRAY = {0, 51};
    public static int[] CREATE_RED = {0, 93};
    public static int[] CREATE_GREEN = {0, 135};
    public static int[] CREATE_GLOW = {0, 177};

    public static int[] CREDITS_GRAY = {173, 51};
    public static int[] CREDITS_RED = {173, 93};
    public static int[] CREDITS_GREEN = {173, 135};
    public static int[] CREDITS_GLOW = {173, 177};

    public static int[] QUIT_GRAY = {0, 264};
    public static int[] QUIT_RED = {173, 264};
    public static int[] QUIT_GREEN = {346, 264};
    public static int[] QUIT_GLOW = {0, 311};

    public static Image getSprite(int[] spriteCoords, int width, int height) {
        int xPos = spriteCoords[0];
        int yPos = spriteCoords[1];
        return spriteSheet.getSubimage(xPos, yPos, width, height);
    }

}
