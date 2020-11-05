package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.projectdf.Client.Client;
import net.projectdf.GUI.Sprites.Buttons;

public class OutDatedClientAlert extends JFrame {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public OutDatedClientAlert() {
        setUndecorated(true);
        BackgroundImagePanel backgroundImagePanel = new BackgroundImagePanel(GUIConstantsConfig.PARCHMENT);
        setContentPane(backgroundImagePanel);
        getRootPane().setBorder(new PerforatedBorder(PerforatedBorder.GREEN));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 100));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JTextPane label = new JTextPane();
        label.setOpaque(false);
        label.setForeground(Color.BLACK);
        label.setEditable(false);

        StyledDocument doc = label.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        center.addAttribute(StyleConstants.CharacterConstants.Bold, true);
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(center, 0.3f);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        try {
            doc.insertString(doc.getLength(), "Your client is out-of-date", center);
        } catch (BadLocationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        JPanel buttons = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 30;

        buttons.setOpaque(false);

        ImageIcon quitHovered = new ImageIcon(Buttons.getSprite(Buttons.QUIT_HOVER));
        ImageIcon quitNeutral = new ImageIcon(Buttons.getSprite(Buttons.QUIT_GREEN));
        ImageIcon quitClicked = new ImageIcon(Buttons.getSprite(Buttons.QUIT_CLICKED));
        Button quitButton = new Button(quitNeutral);
        quitButton.setClickedIcon(quitClicked);
        quitButton.setHoveredIcon(quitHovered);
        quitButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        quitButton.addActionListener(e -> {
            logger.info("closing the client normally");
            Client.getInstance().disconnect();
            System.exit(0);
        });

        ImageIcon acceptHovered = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_HOVER));
        ImageIcon acceptNeutral = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_GREEN));
        ImageIcon acceptClicked = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_CLICKED));
        Button acceptButton = new Button(acceptNeutral);
        acceptButton.setClickedIcon(acceptClicked);
        acceptButton.setHoveredIcon(acceptHovered);
        acceptButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        acceptButton.addActionListener(e -> {
            dispatchEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING
            ));
        });

        buttons.add(acceptButton, gbc);
        buttons.add(quitButton, gbc);

        panel.add(label);
        panel.add(buttons);

        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setAlwaysOnTop(true);
    }

    public static boolean currentVersion() {
        try {
            URL url = new URL("http://projectdf.net:5252/version");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            String version = content.toString().replaceAll("\"", "");
            return version.equals(GUIConstantsConfig.version);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return true; // TODO: maybe return false with "not sure if out of date" ? feels like bad UX..
    }
}
