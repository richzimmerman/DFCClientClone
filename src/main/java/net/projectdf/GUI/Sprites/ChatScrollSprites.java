package net.projectdf.GUI.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class ChatScrollSprites {

    public static Sprite CONTROL = new Sprite(0, 0, 95, 93);
    public static Sprite LOGGER = new Sprite(97, 72, 34, 34);
    public static Sprite UP_PRESSED = new Sprite(97, 0, 34, 34);
    public static Sprite DOWN_PRESSED = new Sprite(97, 36, 34, 34);

    private static BufferedImage spriteSheet = (BufferedImage) StandardGUI.getImage(GUIConstantsConfig.CHAT_SCROLL);

    public static Image getSprite(Sprite s) {
        return spriteSheet.getSubimage(s.getX(), s.getY(), s.getWidth(), s.getHeight());
    }
}
