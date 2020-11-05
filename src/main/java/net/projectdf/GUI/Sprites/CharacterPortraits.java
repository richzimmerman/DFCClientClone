package net.projectdf.GUI.Sprites;

import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Map;

public class CharacterPortraits {
    /**
     * Using a grid set up for this spritesheet since all sprites are the same size / evenly placed.
     */

    private static int width = 111;
    private static int height = 126;

    private static BufferedImage spriteSheet = (BufferedImage) StandardGUI.getImage(GUIConstantsConfig.CON_CHARS);

    // Accidentially wrote this as [y, x] instead of [x, y]... TODO...
    private static int[] TROLL_MALE = new int[] {0, 0};
    private static int[] TROLL_FEMALE = new int[] {0, 1};
    private static int[] RANGER_MALE = new int[] {0, 2};
    private static int[] RANGER_FEMALE = new int[] {0, 3};

    private static int[] OGRE_MALE = new int[] {1, 0};
    private static int[] OGRE_FEMALE = new int[] {1, 1};
    private static int[] MONK_MALE = new int[] {1, 2};
    private static int[] MONK_FEMALE = new int[] {1, 3};

    private static int[] GOBLIN_MALE = new int[] {2, 0};
    private static int[] GOBLIN_FEMALE = new int[] {2, 1};
    private static int[] CRAFTS_MALE = new int[] {2, 2};
    private static int[] CRAFTS_FEMALE = new int[] {2, 3};

    private static int[] KOBOLD_MALE = new int[] {3, 0};
    private static int[] KOBOLD_FEMALE = new int[] {3, 1};
    private static int[] NECRO_MALE = new int[] {3, 2};
    private static int[] NECRO_FEMALE = new int[] {3, 3};

    private static int[] ORC_MALE = new int[] {4, 0};
    private static int[] ORC_FEMALE = new int[] {4, 1};
    private static int[] SKEL_MALE = new int[] {4, 2};
    private static int[] SKEL_FEMALE = new int[] {4, 3};

    private static int[] GNOLL_MALE = new int[] {5, 0};
    private static int[] GNOLL_FEMALE = new int[] {5, 1};
    private static int[] ZOMBIE_MALE = new int[] {5, 2};
    private static int[] ZOMBIE_FEMALE = new int[] {5, 3};

    private static int[] LIZ_MALE = new int[] {6, 0};
    private static int[] LIZ_FEMALE = new int[] {6, 1};
    private static int[] WOLF_MALE = new int[] {6, 2};
    private static int[] WOLF_FEMALE = new int[] {6, 3};

    private static int[] PRIEST_MALE = new int[] {7, 0};
    private static int[] PRIEST_FEMALE = new int[] {7, 1};
    private static int[] VAMP_MALE = new int[] {7, 2};
    private static int[] VAMP_FEMALE = new int[] {7, 3};

    private static int[] PALLY_MALE = new int[] {8, 0};
    private static int[] PALLY_FEMALE = new int[] {8, 1};
    private static int[] DEMON_MALE = new int[] {8, 2};
    private static int[] DEMON_FEMALE = new int[] {8, 3};

    private static int[] ROGUE_MALE = new int[] {9, 0};
    private static int[] ROGUE_FEMALE = new int[] {9, 1};
    private static int[] IMP_MALE = new int[] {9, 2};
    private static int[] IMP_FEMALE = new int[] {9, 3};

    private static int[] WIZ_MALE = new int[] {10, 0};
    private static int[] WIZ_FEMALE = new int[] {10, 1};
    private static int[] ARCH_MALE = new int[] {10, 2};
    private static int[] ARCH_FEMALE = new int[] {10, 2};

    private static int[] BLANK = new int[] {10, 3};

    private static Image getSprite(int[] coords) {
        int xPos = coords[1] * width;
        int yPos = coords[0] * height;
        return spriteSheet.getSubimage(xPos, yPos, width, height);
    }

    private static Map<String, Portrait> spriteMap = new Hashtable<String, Portrait>() {{
        put("Imp", new Portrait(IMP_MALE, IMP_FEMALE));
        put("Werewolf", new Portrait(WOLF_MALE, WOLF_FEMALE));
        put("Demon", new Portrait(DEMON_MALE, DEMON_FEMALE));
        put("Craftsman", new Portrait(CRAFTS_MALE, CRAFTS_FEMALE));
        put("Coven", new Portrait(VAMP_MALE, VAMP_FEMALE));
        put("Gnoll", new Portrait(GNOLL_MALE, GNOLL_FEMALE));
        put("Goblin", new Portrait(GOBLIN_MALE, GOBLIN_FEMALE));
        put("Priest", new Portrait(PRIEST_MALE, PRIEST_FEMALE));
        put("Kobold", new Portrait(KOBOLD_MALE, KOBOLD_FEMALE));
        put("Lizardman", new Portrait(LIZ_MALE, LIZ_FEMALE));
        put("Necromancer", new Portrait(NECRO_MALE, NECRO_FEMALE));
        put("Monk", new Portrait(MONK_MALE, MONK_FEMALE));
        put("Ogre", new Portrait(OGRE_MALE, OGRE_FEMALE));
        put("Orc", new Portrait(ORC_MALE, ORC_FEMALE));
        put("Paladin", new Portrait(PALLY_MALE, PALLY_FEMALE));
        put("Sin", new Portrait(NECRO_MALE, NECRO_FEMALE));
        put("Ranger", new Portrait(RANGER_MALE, RANGER_FEMALE));
        put("Rogue", new Portrait(ROGUE_MALE, ROGUE_FEMALE));
        put("Troll", new Portrait(TROLL_MALE, TROLL_FEMALE));
        put("Vampire", new Portrait(VAMP_MALE, VAMP_FEMALE));
        put("Skeleton", new Portrait(SKEL_MALE, SKEL_FEMALE));
        put("Wizard", new Portrait(WIZ_MALE, WIZ_FEMALE));
        put("Xionakis", new Portrait(DEMON_MALE, DEMON_FEMALE));
        put("Zombie", new Portrait(ZOMBIE_MALE, ZOMBIE_FEMALE));
        put("Archon", new Portrait(ARCH_MALE, ARCH_FEMALE));
    }};

    public static Image getCharacterPortrait(String race, String gender) {
        int[] coords = spriteMap.get(race).get(gender);
        return getSprite(coords);
    }

    public static Image getBlankCharacterPortrait() {
        return getSprite(BLANK);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

}
