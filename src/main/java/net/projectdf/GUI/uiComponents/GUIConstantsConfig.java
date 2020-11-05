package net.projectdf.GUI.uiComponents;

import java.util.Hashtable;
import java.util.Map;

public class GUIConstantsConfig {

    public static String version = "v1.0.0.1";

    // Art file paths
    private static String GOOD_BACKGROUND = "/art/tile_G.png";
    private static String GOOD_GARGOYLE = "/art/gar_G.png";
    private static String CHAOS_BACKGROUND = "/art/tile_C.png";
    private static String CHAOS_GARGOYLE = "/art/gar_C.png";
    private static String EVIL_BACKGROUND = "/art/tile_E.png";
    private static String EVIL_GARGOYLE = "/art/gar_E.png";
    public static String CHAT_SCROLL = "/art/chat.png";
    public static String MENU_BUTTONS = "/art/menubuts.BMP";
    public static String BUTTONS = "/art/butts.png";
    public static String PARCHMENT = "/art/PARCH.png";
    public static String CONTROL = "/art/control.png";
    public static String ICON = "/art/DARK_106.png";
    public static String CON_CHARS = "/art/con_char.png";
    public static String CON_SPRITES = "/art/gls_sprt.png";
    public static String MAP_PARCHMENT = "/art/Scroll.png";
//    public static String MAP_PARCHMENT = "/art/ParchmentLayer.png";

    public static String LOGIN_BACKGROUND = "/art/login.png";
    public static String MENU_BACKGROUND = "/art/menu.png";


    public static Map<Integer, String> backgrounds = new Hashtable<Integer, String>() {{
        put(0, EVIL_BACKGROUND);
        put(1, EVIL_BACKGROUND);
        put(2, CHAOS_BACKGROUND);
        put(3, GOOD_BACKGROUND);
    }};

    public static Map<Integer, String> gargoyles = new Hashtable<Integer, String>() {{
        put(0, EVIL_GARGOYLE);
        put(1, EVIL_GARGOYLE);
        put(2, CHAOS_GARGOYLE);
        put(3, GOOD_GARGOYLE);
    }};

    // Font file paths
    public static String DEFAULT_FONT = "/fonts/FSEX300.ttf";
    public static String UBUNTU_FONT = "/fonts/UbuntuMono-R.ttf";

}
