package net.projectdf.GUI.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class Buttons {

    private static int width = 66;
    private static int height = 24;

    private static BufferedImage spriteSheet = (BufferedImage) StandardGUI.getImage(GUIConstantsConfig.BUTTONS);

    public static int[] CLOSE_GREYED = new int[] {0, 0};
    public static int[] SELECT_GREYED = new int[] {0, 1};
    public static int[] ACCEPT_GREYED = new int[] {0, 2};
    public static int[] REROLL_GREYED = new int[] {0, 3};
    public static int[] UPDATE_GREYED = new int[] {0, 4};
    public static int[] PLAY_GREYED = new int[] {0, 5};
    public static int[] RAISE_GREYED = new int[] {0, 6};
    public static int[] TRAIN_GREYED = new int[] {0, 7};
    public static int[] QUIT_GREYED = new int[] {0, 8};
    public static int[] CREATE_GREYED = new int[] {0, 9};
    public static int[] BUY_GREYED = new int[] {0, 10};
    public static int[] SELL_GREYED = new int[] {0, 11};
    public static int[] MALE_GREYED = new int[] {0, 12};
    public static int[] FEMALE_GREYED = new int[] {0, 13};
    public static int[] OK_GREYED = new int[] {0, 14};
    public static int[] CANCEL_GREYED = new int[] {0, 15};
    public static int[] GOOD_GREYED = new int[] {0, 16};
    public static int[] CHAOS_GREYED = new int[] {0, 17};
    public static int[] EVIL_GREYED = new int[] {0, 18};
    public static int[] WEAR_GREYED = new int[] {0, 26};
    public static int[] HOLD_GREYED = new int[] {0, 27};
    public static int[] REMOVE_GREYED = new int[] {0, 28};
    public static int[] RETURN_GREYED = new int[] {0, 29};
    public static int[] DROP_GREYED = new int[] {0, 30};
    public static int[] STORE_GREYED = new int[] {0, 31};
    public static int[] DEPOSIT_GREYED = new int[] {0, 32};
    public static int[] RETRIEVE_GREYED = new int[] {0, 33};
    public static int[] WITHDRAW_GREYED = new int[] {0, 34};

    public static int[] CLOSE_HOVER = new int[] {1, 0};
    public static int[] SELECT_HOVER = new int[] {1, 1};
    public static int[] ACCEPT_HOVER = new int[] {1, 2};
    public static int[] REROLL_HOVER = new int[] {1, 3};
    public static int[] UPDATE_HOVER = new int[] {1, 4};
    public static int[] PLAY_HOVER = new int[] {1, 5};
    public static int[] RAISE_HOVER = new int[] {1, 6};
    public static int[] TRAIN_HOVER = new int[] {1, 7};
    public static int[] QUIT_HOVER = new int[] {1, 8};
    public static int[] CREATE_HOVER = new int[] {1, 9};
    public static int[] BUY_HOVER = new int[] {1, 10};
    public static int[] SELL_HOVER = new int[] {1, 11};
    public static int[] MALE_HOVER = new int[] {1, 12};
    public static int[] FEMALE_HOVER = new int[] {1, 13};
    public static int[] OK_HOVER = new int[] {1, 14};
    public static int[] CANCEL_HOVER = new int[] {1, 15};
    public static int[] GOOD_HOVER = new int[] {1, 16};
    public static int[] CHAOS_HOVER = new int[] {1, 17};
    public static int[] EVIL_HOVER = new int[] {1, 18};
    public static int[] WEAR_HOVER = new int[] {1, 26};
    public static int[] HOLD_HOVER = new int[] {1, 27};
    public static int[] REMOVE_HOVER = new int[] {1, 28};
    public static int[] RETURN_HOVER = new int[] {1, 29};
    public static int[] DROP_HOVER = new int[] {1, 30};
    public static int[] STORE_HOVER = new int[] {1, 31};
    public static int[] DEPOSIT_HOVER = new int[] {1, 32};
    public static int[] RETRIEVE_HOVER = new int[] {1, 33};
    public static int[] WITHDRAW_HOVER = new int[] {1, 34};

    public static int[] CLOSE_GREEN = new int[] {2, 0};
    public static int[] SELECT_GREEN = new int[] {2, 1};
    public static int[] ACCEPT_GREEN = new int[] {2, 2};
    public static int[] REROLL_GREEN = new int[] {2, 3};
    public static int[] UPDATE_GREEN = new int[] {2, 4};
    public static int[] PLAY_GREEN = new int[] {2, 5};
    public static int[] RAISE_GREEN = new int[] {2, 6};
    public static int[] TRAIN_GREEN = new int[] {2, 7};
    public static int[] QUIT_GREEN = new int[] {2, 8};
    public static int[] CREATE_GREEN = new int[] {2, 9};
    public static int[] BUY_GREEN = new int[] {2, 10};
    public static int[] SELL_GREEN = new int[] {2, 11};
    public static int[] MALE_GREEN = new int[] {2, 12};
    public static int[] FEMALE_GREEN = new int[] {2, 13};
    public static int[] OK_GREEN = new int[] {2, 14};
    public static int[] CANCEL_GREEN = new int[] {2, 15};
    public static int[] GOOD_GREEN = new int[] {2, 16};
    public static int[] CHAOS_GREEN = new int[] {2, 17};
    public static int[] EVIL_GREEN = new int[] {2, 18};
    public static int[] WEAR_GREEN = new int[] {2, 26};
    public static int[] HOLD_GREEN = new int[] {2, 27};
    public static int[] REMOVE_GREEN = new int[] {2, 28};
    public static int[] RETURN_GREEN = new int[] {2, 29};
    public static int[] DROP_GREEN = new int[] {2, 30};
    public static int[] STORE_GREEN = new int[] {2, 31};
    public static int[] DEPOSIT_GREEN = new int[] {2, 32};
    public static int[] RETRIEVE_GREEN = new int[] {2, 33};
    public static int[] WITHDRAW_GREEN = new int[] {2, 34};

    public static int[] CLOSE_CLICKED = new int[] {3, 0};
    public static int[] SELECT_CLICKED = new int[] {3, 1};
    public static int[] ACCEPT_CLICKED = new int[] {3, 2};
    public static int[] REROLL_CLICKED = new int[] {3, 3};
    public static int[] UPDATE_CLICKED = new int[] {3, 4};
    public static int[] PLAY_CLICKED = new int[] {3, 5};
    public static int[] RAISE_CLICKED = new int[] {3, 6};
    public static int[] TRAIN_CLICKED = new int[] {3, 7};
    public static int[] QUIT_CLICKED = new int[] {3, 8};
    public static int[] CREATE_CLICKED = new int[] {3, 9};
    public static int[] BUY_CLICKED = new int[] {3, 10};
    public static int[] SELL_CLICKED = new int[] {3, 11};
    public static int[] MALE_CLICKED = new int[] {3, 12};
    public static int[] FEMALE_CLICKED = new int[] {3, 13};
    public static int[] OK_CLICKED = new int[] {3, 14};
    public static int[] CANCEL_CLICKED = new int[] {3, 15};
    public static int[] GOOD_CLICKED = new int[] {3, 16};
    public static int[] CHAOS_CLICKED = new int[] {3, 17};
    public static int[] EVIL_CLICKED = new int[] {3, 18};
    public static int[] WEAR_CLICKED = new int[] {3, 26};
    public static int[] HOLD_CLICKED = new int[] {3, 27};
    public static int[] REMOVE_CLICKED = new int[] {3, 28};
    public static int[] RETURN_CLICKED = new int[] {3, 29};
    public static int[] DROP_CLICKED = new int[] {3, 30};
    public static int[] STORE_CLICKED = new int[] {3, 31};
    public static int[] DEPOSIT_CLICKED = new int[] {3, 32};
    public static int[] RETRIEVE_CLICKED = new int[] {3, 33};
    public static int[] WITHDRAW_CLICKED = new int[] {3, 34};

    public static Image getSprite(int[] spriteCoords) {
        int xPos = spriteCoords[0] * width;
        int yPos = spriteCoords[1] * height;
        return spriteSheet.getSubimage(xPos, yPos, width, height);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
