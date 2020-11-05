package net.projectdf.GUI.account.Panels;

import java.awt.*;
import javax.swing.*;
import net.projectdf.Account.Character;
import net.projectdf.GUI.uiComponents.PerforatedBorder;

public class CharacterItem extends JButton {

    private Character character;

    public CharacterItem(Character c) {
        this.character = c;

        setBorder(new PerforatedBorder(PerforatedBorder.GREY));
        setBackground(new Color(0,0,0,0));
        setPreferredSize(new Dimension(300, 25));

        String info = String.format("%s, Lvl %d %s", character.getName(), character.getLevel(),
                character.getCharacterClass());
        JLabel label = new JLabel(info);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        add(label);

        // TODO: add on click even to select as current character.
    }
}
