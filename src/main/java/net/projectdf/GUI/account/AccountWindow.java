package net.projectdf.GUI.account;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.*;
import net.projectdf.App;
import net.projectdf.GUI.account.Panels.AccountMenu;
import net.projectdf.GUI.uiComponents.BackgroundImagePanel;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

import static net.projectdf.GUI.StandardGUI.getImage;

public class AccountWindow extends JFrame {

    private static AccountWindow instance;

    private String selectedCharacter;
    private JPanel currentPanel;

    public AccountWindow() {
        setTitle("ProjectDF");

        Image icon = getImage(GUIConstantsConfig.ICON);
        setIconImage(icon);

        BackgroundImagePanel background = new BackgroundImagePanel(GUIConstantsConfig.backgrounds.get(0));
        setContentPane(background);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(612, 414));
        setMaximumSize(new Dimension(612, 414));


        // Default to main menu
        mainMenu();

        pack();
    }

    public void showWindow() {
        setLocationRelativeTo(App.getCurrentWindow());
        setVisible(true);
    }

    public void hideWindow() {
        setVisible(false);
    }

    public static AccountWindow getInstance() {
        if (instance == null) {
            instance = new AccountWindow();
        }
        return instance;
    }

    private void createCharacer() {
        // TODO: Change the panel to char select screen and go from there.
    }

    private void credits() {
        // TODO: Change panel to credits screen
    }

    private void mainMenu() {
        // TODO: Change panel back to main menu (might need to re-log in to exit char creation)
        AccountMenu accountMenu = new AccountMenu();
        getContentPane().add(accountMenu);

    }

}
