package net.projectdf.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.projectdf.App;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.uiComponents.ActionWindow;
import net.projectdf.GUI.uiComponents.BackgroundImagePanel;
import net.projectdf.GUI.uiComponents.BackgroundSettings;
import net.projectdf.GUI.uiComponents.ChatColorSettings;
import net.projectdf.GUI.uiComponents.ChatWindow;
import net.projectdf.GUI.uiComponents.ExitConfirmation;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;
import net.projectdf.GUI.uiComponents.HighlightStringsSettings;
import net.projectdf.GUI.uiComponents.MacroWindow;
import net.projectdf.GUI.uiComponents.TextSettings;
import net.projectdf.GUI.uiComponents.UserDisplay;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class StandardGUI extends JFrame {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private ChatWindow chatWindow;
    private ActionWindow actionWindow;
    private UserDisplay userDisplay;
    private BackgroundImagePanel background;
    private JSplitPane splitPane;
    private static StandardGUI instance;

    public StandardGUI() {
        setTitle("Olmran");
        Image icon = getImage(GUIConstantsConfig.ICON);
        setIconImage(icon);
        StandardGUI.instance = this;
        createWindow();

        this.pack();
    }

    public void showWindow() {
        this.setVisible(true);
        this.setFocusable(false);
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                getActionWindow().getInputField().requestFocusInWindow();
            }
        });
    }

    public void hideWindow() {
        this.setVisible(false);
    }

    private void createWindow() {
        // Window
        // Close operation
        background = new BackgroundImagePanel(GUIConstantsConfig.backgrounds.get(0));
        this.setContentPane(background);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                new ExitConfirmation();
            }
        });
        this.setLayout(new BorderLayout());
        // Default window size
        this.setMinimumSize(new Dimension(800,600));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        setMenuBar();

        chatWindow = new ChatWindow(new BorderLayout(),
                new Dimension(this.getWidth(), (int)(this.getHeight() * 0.25f)));
        actionWindow = new ActionWindow(new Dimension(this.getWidth(), (int)(this.getHeight() * 0.40f)));
        userDisplay = new UserDisplay(new Dimension(this.getWidth(), (int)(this.getHeight() * 0.35f)));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatWindow, actionWindow);

        splitPane.setOpaque(false);
        splitPane.setDividerSize(2);
        splitPane.setDividerLocation(250);
        splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        splitPane.setPreferredSize(new Dimension(this.getWidth(), (int)(this.getHeight() * 0.75f)));

        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        this.getContentPane().add(userDisplay, BorderLayout.SOUTH);
    }

    public void updateBackground(int realm) {
        String background = GUIConstantsConfig.backgrounds.get(realm);
        if (background != null) {
            this.background.updateBackgroundImage(background);
        }
    }

    private void setMenuBar() {
        // Menu Bar
        JMenuBar menu = new JMenuBar();
        menu.setFocusable(false);

        JMenu file = new JMenu("File");
        JMenuItem close = new JMenuItem("Close");
        close.setMnemonic(KeyEvent.VK_C);
        close.setToolTipText("Close the application.");
        close.addActionListener(e -> {
            Client.getInstance()
                .getGui().dispatchEvent(new WindowEvent(
                        Client.getInstance().getGui(), WindowEvent.WINDOW_CLOSING
                ));
        });

        file.add(close);

        JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_O);

        JMenuItem macros = new JMenuItem("Macros");
        macros.setMnemonic(KeyEvent.VK_M);
        macros.setToolTipText("Create and edit macros.");
        macros.addActionListener(e -> {
            MacroWindow m = MacroWindow.getInstance();
            m.setVisible(true);
        });
        options.add(macros);

        JMenuItem fonts = new JMenuItem("Font Settings");
        fonts.setMnemonic(KeyEvent.VK_F);
        fonts.setToolTipText("Change the game font");
        fonts.addActionListener(e -> {
            TextSettings t = TextSettings.getInstance();
            t.setVisible(true);
        });
        options.add(fonts);

        JMenuItem highlightStrings = new JMenuItem("Highlight Strings");
        highlightStrings.setMnemonic(KeyEvent.VK_H);
        highlightStrings.setToolTipText("Set strings to highlight");
        highlightStrings.addActionListener(e -> {
            HighlightStringsSettings h = HighlightStringsSettings.getInstance();
            h.setVisible(true);
        });
        options.add(highlightStrings);

        JMenuItem chatColors = new JMenuItem("Chat Settings");
        chatColors.setToolTipText("Select your different chat channel colors");
        chatColors.addActionListener(e -> {
            ChatColorSettings col = ChatColorSettings.getInstance();
            col.setVisible(true);
        });
        options.add(chatColors);

        JMenuItem backgroundColor = new JMenuItem("Background Settings");
        backgroundColor.setToolTipText("Enable and change background color");
        backgroundColor.addActionListener(e -> {
            BackgroundSettings b = BackgroundSettings.getInstance();
            b.setVisible(true);
        });
        options.add(backgroundColor);

        boolean echo = ClientConfiguration.getInstance().getLocalEcho();
        JCheckBoxMenuItem localEcho = new JCheckBoxMenuItem("Local Echo");
        localEcho.setState(echo);
        localEcho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton a = (AbstractButton) actionEvent.getSource();
                boolean selected = a.getModel().isSelected();

                ClientConfiguration.getInstance().setLocalEcho(selected);
                localEcho.setState(selected);
            }
        });
        options.add(localEcho);

        boolean lastCmd = ClientConfiguration.getInstance().getPersistLastCommand();
        JCheckBoxMenuItem keepLastCmd = new JCheckBoxMenuItem("Persist Last Command");
        keepLastCmd.setState(lastCmd);
        keepLastCmd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton a = (AbstractButton) actionEvent.getSource();
                boolean selected = a.getModel().isSelected();

                ClientConfiguration.getInstance().setPersistLastCommand(selected);
                keepLastCmd.setState(selected);
            }
        });
        options.add(keepLastCmd);

        menu.add(file);
        menu.add(options);

        this.setJMenuBar(menu);
    }

    public ChatWindow getChatWindow() {
        return chatWindow;
    }

    public ActionWindow getActionWindow() {
        return actionWindow;
    }

    public UserDisplay getUserDisplay() {
        return userDisplay;
    }

    public BackgroundImagePanel getBackgroundImage() {
        return background;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public static BufferedImage getImage(final String filePath) {
        final URL url = App.class.getResource(filePath);
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public static StandardGUI getInstance() {
        // StandardGUI is instantiated before anything else so this is safe.
        return instance;
    }

}
