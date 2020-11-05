package net.projectdf.GUI.account.Panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import net.projectdf.Account.Account;
import net.projectdf.Account.Character;
import net.projectdf.Client.Client;
import net.projectdf.GUI.Sprites.MenuButtons;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.uiComponents.Button;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

public class AccountMenu extends JLayeredPane {

    private static AccountMenu instance;
    private static Image background;
    private static Character selected;

    public AccountMenu() {
        List<Character> characters = Account.getInstance().getAccountCharacters();

        background = StandardGUI.getImage(GUIConstantsConfig.MENU_BACKGROUND);
        setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null)));

        Image neutralPlay = MenuButtons.getSprite(MenuButtons.PLAY_GREEN, MenuButtons.PLAY_WIDTH, MenuButtons.PLAY_HEIGHT);
        Image hoverPlay = MenuButtons.getSprite(MenuButtons.PLAY_GLOW, MenuButtons.PLAY_WIDTH, MenuButtons.PLAY_HEIGHT);
        Image clickPlay = MenuButtons.getSprite(MenuButtons.PLAY_RED, MenuButtons.PLAY_WIDTH, MenuButtons.PLAY_HEIGHT);
        Button play = new Button(new ImageIcon(neutralPlay));
        play.setOpaque(false);
        play.setHoveredIcon(new ImageIcon(hoverPlay));
        play.setClickedIcon(new ImageIcon(clickPlay));
        play.setBounds(246, 205, MenuButtons.PLAY_WIDTH, MenuButtons.PLAY_HEIGHT);
        add(play);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Client.getInstance().write(selected.getName());
            }
        });

        Image neutralCreate = MenuButtons.getSprite(MenuButtons.CREATE_GREEN, MenuButtons.CREATE_WIDTH, MenuButtons.CREATE_HEIGHT);
        Image hoverCreate = MenuButtons.getSprite(MenuButtons.CREATE_GLOW, MenuButtons.CREATE_WIDTH, MenuButtons.CREATE_HEIGHT);
        Image clickCreate = MenuButtons.getSprite(MenuButtons.CREATE_RED, MenuButtons.CREATE_WIDTH, MenuButtons.CREATE_HEIGHT);
        Button create = new Button(new ImageIcon(neutralCreate));
        create.setOpaque(false);
        create.setHoveredIcon(new ImageIcon(hoverCreate));
        create.setClickedIcon(new ImageIcon(clickCreate));
        create.setBounds(222, 266, MenuButtons.CREATE_WIDTH, MenuButtons.CREATE_HEIGHT);
        add(create);

        Image neutralCredits = MenuButtons.getSprite(MenuButtons.CREDITS_GREEN, MenuButtons.CREDITS_WIDTH, MenuButtons.CREDITS_HEIGHT);
        Image hoverCredits = MenuButtons.getSprite(MenuButtons.CREDITS_GLOW, MenuButtons.CREDITS_WIDTH, MenuButtons.CREDITS_HEIGHT);
        Image clickCredits = MenuButtons.getSprite(MenuButtons.CREDITS_RED, MenuButtons.CREDITS_WIDTH, MenuButtons.CREDITS_HEIGHT);
        Button credits = new Button(new ImageIcon(neutralCredits));
        credits.setHoveredIcon(new ImageIcon(hoverCredits));
        credits.setClickedIcon(new ImageIcon(clickCredits));
        credits.setBounds(222, 310, MenuButtons.CREDITS_WIDTH, MenuButtons.CREDITS_HEIGHT);
        add(credits);

        Image neutralQuit = MenuButtons.getSprite(MenuButtons.QUIT_GREEN, MenuButtons.QUIT_WIDTH, MenuButtons.QUIT_HEIGHT);
        Image hoverQuit = MenuButtons.getSprite(MenuButtons.QUIT_GLOW, MenuButtons.QUIT_WIDTH, MenuButtons.QUIT_HEIGHT);
        Image clickQuit = MenuButtons.getSprite(MenuButtons.QUIT_RED, MenuButtons.QUIT_WIDTH, MenuButtons.QUIT_HEIGHT);
        Button quit = new Button(new ImageIcon(neutralQuit));
        quit.setHoveredIcon(new ImageIcon(hoverQuit));
        quit.setClickedIcon(new ImageIcon(clickQuit));
        quit.setBounds(222, 355, MenuButtons.QUIT_WIDTH, MenuButtons.QUIT_HEIGHT);
        add(quit);

        JPanel characterList = new JPanel();
        characterList.setLayout(new BoxLayout(characterList, BoxLayout.Y_AXIS));
        characterList.setOpaque(false);
        characterList.setPreferredSize(new Dimension(220, 300));
        characterList.setBounds(25, 180, 220, 300);

        for (Character c : characters) {
            CharacterItem characterItem = new CharacterItem(c);
            characterItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    selected = c;
                }
            });
            characterList.add(characterItem);
        }

        add(characterList);
    }

    public static AccountMenu getInstance() {
        if (instance == null) {
            instance = new AccountMenu();
        }
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this);
    }
}
