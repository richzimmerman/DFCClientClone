package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.projectdf.Client.Client;
import net.projectdf.GUI.Sprites.Buttons;

public class ConnectionLost extends JFrame {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static ConnectionLost instance;

    public ConnectionLost() {
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
            doc.insertString(doc.getLength(), "Connection to the server has been lost.", center);
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
            dispatchEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING
            ));
            //System.exit(1);
        });

        buttons.add(quitButton, gbc);

        panel.add(label);
        panel.add(buttons);

        add(panel);

        pack();
        setLocationRelativeTo(Client.getInstance().getGui());
        setVisible(true);
    }

    public static ConnectionLost getInstance() {
        if (instance == null) {
            instance = new ConnectionLost();
        }
        return instance;
    }
}
