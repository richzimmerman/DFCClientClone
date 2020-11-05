package net.projectdf.GUI.uiComponents;

import java.util.logging.Logger;
import net.projectdf.GUI.Sprites.CharacterPortraits;

import javax.swing.*;
import java.awt.*;

public class CharacterPortraitComponent extends JPanel {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Image img;
    private Image blank;
    private String currentRace;

    public CharacterPortraitComponent() {
        setOpaque(false);
        blank = CharacterPortraits.getBlankCharacterPortrait();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img.getScaledInstance(getBounds().width, getBounds().height, Image.SCALE_SMOOTH),
                    0, 0, null);
        } else {
            g.drawImage(blank.getScaledInstance(getBounds().width, getBounds().height, Image.SCALE_SMOOTH),
                    0, 0, null);
        }
    }

    public void setCharacterPortrait(String race, String gender) {
        currentRace = race;
        img = CharacterPortraits.getCharacterPortrait(race, gender);
        if (img == null) {
            logger.warning("character portrait is null. race: " + race + ", gender: " + gender);
        }
        repaint();
    }

    public String getCurrentPortrait() {
        return currentRace;
    }
}
