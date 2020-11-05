package net.projectdf.GUI.uiComponents;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.Client.Client;
import net.projectdf.Account.Player;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Font;

public class CharStatsTextField extends JTextPane {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // TODO: move each line into separate text fields
    private String dataString = "\nLvl %s %s\n" + // Level, Race
                "%s of\n" + // Title
                "%s\n" + // Guild
                "%s\n" + // XP to level
                "exp for Lvl %s"; // Next Level

    public CharStatsTextField() {
        super();
        setFont(new Font("Arial", Font.PLAIN, 11));
        setOpaque(false);
        setForeground(Color.BLACK);
        setEditable(false);
        setFocusable(false);
    }

    public void refresh() {
        Player p = Player.getInstance();
        String name = p.getName();
        String data = String.format(dataString, p.getLevel(),
                p.getRace(), p.getTitle(), p.getGuild(), p.getXpToLevel(),
                p.getNextLevel());
        try {
            StyledDocument doc = getStyledDocument();
            if (doc.getLength() > 0) {
                getStyledDocument().remove(0, getStyledDocument().getLength());
            }
        } catch (BadLocationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        addName(name);
        updateText(data);
    }

    private void addName(String name) {
        StyledDocument doc = getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        set.addAttribute(StyleConstants.CharacterConstants.Bold, true);
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(set, 11);
        try {
            doc.insertString(0, name, set);
        } catch (BadLocationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void updateText(String data) {
        // TODO: refresh static string. replace substrings?
        StyledDocument doc = getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(center, 0.3f);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        try {
            doc.insertString(doc.getLength(), data, center);
        } catch (BadLocationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void updateCharacterPortrait() {
        Player p = Player.getInstance();
        String currentRace = Client.getInstance().getGui().getUserDisplay().getControl()
                .getCharacter().getCurrentPortrait();
        if (p.getRace() != null) {
            if (!p.getRace().equals(currentRace)) {
                Client.getInstance().getGui().getUserDisplay().getControl()
                        .getCharacter().setCharacterPortrait(p.getRace(), p.getGender().toUpperCase());
                Client.getInstance().getGui().updateBackground(p.getRealm());
            }
        }
    }

}
