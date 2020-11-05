package net.projectdf.Configuration;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.Account.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.awt.event.KeyEvent.*;

public class ClientConfiguration {

    private final static Color DEFAULT_CHAT_COLOR = Color.WHITE;

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static ClientConfiguration instance;

    private static String FONT_KEY = "font";
    private static String SIZE = "size";
    private static String CURRENT_FONT = "currentFont";
    private static String DEFAULT_FONT = "Fixedsys Excelsior 3.01";
    private static String DEFAULT_SIZE = "17";

    private static String CHAT_COLORS = "chatColors";
    private static String BACKGROUND_COLOR = "backgroundColor";
    private static String CUSTOM_BACKGROUND = "customBackground";
    private static String MACRO_KEY = "macros";
    private static String LOCAL_ECHO = "localEcho";
    private static String HIGLIGHT_STRINGS = "highlightStrings";
    private static String PERSIST_COMMANDS = "persistCommands";

    String CONFIG_FILE = "Client.cfg";

    private static JSONObject config = null;

    public static int[] MACRO_POSITIONS = {
            VK_Q, VK_W, VK_E, VK_R, VK_T, VK_Y, VK_U, VK_I, VK_O, VK_P,
            VK_A, VK_S, VK_D, VK_F, VK_G, VK_H, VK_J, VK_K, VK_L,
            VK_Z, VK_B, VK_N, VK_M, VK_F1, VK_F2, VK_F3, VK_F4, VK_F5, VK_F6, VK_F7, VK_F8,
            VK_F9, VK_F10, VK_F11, VK_F12
    };

    public static String[] CHAT_CHANNELS = {
            "Say", "Tell", "Plan", "Gsend", "Party", "Death"
    };

    public ClientConfiguration() {
        instance = this;

        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            config = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            if (e.getMessage().contains("No such file") || e.getMessage().contains("The system cannot find the file")) {
                config = generateEmptyConfig();
                try {
                    FileWriter fileWriter = new FileWriter(CONFIG_FILE);
                    fileWriter.write(config.toJSONString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    public static ClientConfiguration getInstance() {
        if (instance == null) {
            instance = new ClientConfiguration();
        }
        return instance;
    }

    public Object get(String key) {
        return config.get(key);
    }

    public Object getMacro(int i) {
        String charName = Player.getInstance().getName();
        JSONObject allMacros = (JSONObject) config.get(MACRO_KEY);
        JSONObject macros = (JSONObject) allMacros.get(charName);
        if (macros == null) {
            macros = generateEmptyMacros();
            allMacros.put(charName, macros);
            saveConfig();
        }
        String key = Integer.toString(i);
        return macros.get(key);
    }

    public Font getFont() {
        JSONObject font = (JSONObject) config.get(FONT_KEY);
        String fontName = (String) font.get(CURRENT_FONT);
        int size = Integer.parseInt((String) font.get(SIZE));
        Font f = new Font(fontName, Font.PLAIN, size);
        return f;
    }

    public void setFont(String fontName, int size) {
        JSONObject font = (JSONObject) config.get(FONT_KEY);
        font.put(CURRENT_FONT, fontName);
        font.put(SIZE, Integer.toString(size));
    }

    public String getDefaultFont() {
        return DEFAULT_FONT;
    }

    private JSONObject generateEmptyConfig() {
        JSONObject cfg = new JSONObject();

        JSONObject macros = new JSONObject();
        cfg.put(MACRO_KEY, macros);

        JSONObject font = new JSONObject();
        font.put(CURRENT_FONT, DEFAULT_FONT);
        font.put(SIZE, DEFAULT_SIZE);
        cfg.put(FONT_KEY, font);

        cfg.put(LOCAL_ECHO, false);

        JSONArray emptyHighlightStrings = new JSONArray();
        cfg.put(HIGLIGHT_STRINGS, emptyHighlightStrings);

        JSONObject chatColors = generateEmptyChatColors();
        cfg.put(CHAT_COLORS, chatColors);

        JSONArray backgroundColor = new JSONArray();
        // Add 0 three times, to generate the default BLACK color
        for (int i = 0; i < 3; i++) {
            backgroundColor.add(0);
        }
        cfg.put(BACKGROUND_COLOR, backgroundColor);

        cfg.put(CUSTOM_BACKGROUND, false);

        cfg.put(PERSIST_COMMANDS, false);

        return cfg;
    }

    private JSONObject generateEmptyMacros() {
        JSONObject emptyMacros = new JSONObject();
        for (int i : MACRO_POSITIONS) {
            emptyMacros.put(Integer.toString(i), null);
        }
        return emptyMacros;
    }

    private JSONObject generateEmptyChatColors() {
        JSONObject emptyChatColors = new JSONObject();
        for (String channel : CHAT_CHANNELS) {
            JSONArray j = new JSONArray();
            int r = DEFAULT_CHAT_COLOR.getRed();
            j.add(r);
            int g = DEFAULT_CHAT_COLOR.getGreen();
            j.add(g);
            int b = DEFAULT_CHAT_COLOR.getBlue();
            j.add(b);
            emptyChatColors.put(channel.toLowerCase(), j);
        }
        return emptyChatColors;
    }

    public void setMacro(String position, String macro) {
        String charName = Player.getInstance().getName();
        JSONObject allMacros = (JSONObject) config.get(MACRO_KEY);
        JSONObject macros = (JSONObject) allMacros.get(charName);
        macros.put(position, macro);
    }

    public void saveConfig() {
        try {
            FileWriter fileWriter = new FileWriter(CONFIG_FILE);
            fileWriter.write(config.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
        }
    }

    public Color getColor(String channel) {
        JSONObject colors = (JSONObject) config.get(CHAT_COLORS);
        if (colors == null) {
            config.put(CHAT_COLORS, generateEmptyChatColors());
            colors = (JSONObject) config.get(CHAT_COLORS);
        }
        JSONArray colorSettings = (JSONArray) colors.get(channel.toLowerCase());
        if (colorSettings == null) {
            try {
                String white = "{\"white\": [255, 255, 255] }";
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(white);
                colorSettings = (JSONArray) json.get("white");
            } catch (ParseException e) {
                logger.warning(e.getMessage());
            }
        }
        Object[] values = colorSettings.toArray();
        int r, g, b;
        try {
            r = new Long((long) values[0]).intValue();
            g = new Long((long) values[1]).intValue();
            b = new Long((long) values[2]).intValue();
        } catch (ClassCastException e) {
            r = (int) values[0];
            g = (int) values[1];
            b = (int) values[2];
        }
        return new Color(r, g, b);
    }

    public void setColor(String channel, Color color) {
        JSONObject colors = (JSONObject) config.get(CHAT_COLORS);
        JSONArray j = new JSONArray();
        int r = color.getRed();
        j.add(r);
        int g = color.getGreen();
        j.add(g);
        int b = color.getBlue();
        j.add(b);
        colors.put(channel.toLowerCase(), j);
    }

    public Color getBackgroundColor() {
        JSONArray c = (JSONArray) config.get(BACKGROUND_COLOR);
        if (c != null) {
            Object[] values = c.toArray();
            int r, g, b;
            try {
                r = new Long((long) values[0]).intValue();
                g = new Long((long) values[1]).intValue();
                b = new Long((long) values[2]).intValue();
            } catch (ClassCastException e) {
                r = (int) values[0];
                g = (int) values[1];
                b = (int) values[2];
            }
            return new Color(r, g, b);
        } else {
            return Color.BLACK;
        }
    }

    public void setBackgroundColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        JSONArray c = new JSONArray();
        c.add(r);
        c.add(g);
        c.add(b);
        config.put(BACKGROUND_COLOR, c);
    }

    public boolean getCustomBackground() {
        Object b = config.get(CUSTOM_BACKGROUND);
        return b != null && (Boolean) b;
    }

    public void setCustomBackground(boolean b) {
        config.put(CUSTOM_BACKGROUND, b);
        saveConfig();
    }

    public boolean getLocalEcho() {
        return (Boolean) config.get(LOCAL_ECHO);
    }

    public void setLocalEcho(boolean b) {
        config.put(LOCAL_ECHO, b);
        saveConfig();
    }

    public boolean getPersistLastCommand() {
        Object b = config.get(PERSIST_COMMANDS);
        return b != null && (Boolean) b;
    }

    public void setPersistLastCommand(boolean b) {
        config.put(PERSIST_COMMANDS, b);
        saveConfig();
    }

    public List<String> getHighlightStrings() {
        List<String> h = new ArrayList<> ();
        JSONArray highlightStrings = (JSONArray) config.get(HIGLIGHT_STRINGS);
        if (highlightStrings != null) {
            for (int i = 0; i < highlightStrings.size(); i++) {
                h.add(highlightStrings.get(i).toString());
            }
        }
        return h;
    }

    public void setHighlightStrings(List<String> list) {
        JSONArray j = new JSONArray();
        for (String highlightString : list) {
            j.add(highlightString);
        }
        config.put(HIGLIGHT_STRINGS, j);
        saveConfig();
    }
}
