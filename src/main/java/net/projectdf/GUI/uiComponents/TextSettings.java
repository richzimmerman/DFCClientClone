package net.projectdf.GUI.uiComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.Sprites.Buttons;

public class TextSettings extends JFrame {

    private Color BACKGROUND = new Color(157, 138, 108);

    private static TextSettings instance;

    private int pointerX;
    private int pointerY;

    public TextSettings() {
        setUndecorated(true);
        BackgroundImagePanel backgroundImagePanel = new BackgroundImagePanel(GUIConstantsConfig.PARCHMENT);
        setContentPane(backgroundImagePanel);
        setLayout(new BorderLayout());
        getRootPane().setBorder(new PerforatedBorder(PerforatedBorder.GREEN));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(450, 175));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 30;

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        Font[] fonts = env.getAllFonts();

        String[] defaultFont = new String[] {ClientConfiguration.getInstance().getDefaultFont()};
        JComboBox<String> fontComboBox = new JComboBox<>(defaultFont);
        fontComboBox.setBackground(BACKGROUND);

        // Using this to check if fonts are monospaced otherwise the game can get ugly.
//        FontRenderContext frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
//                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        for (Font font : fonts) {
//            Rectangle2D iBounds = font.getStringBounds("i", frc);
//            Rectangle2D mBounds = font.getStringBounds("m", frc);
//            if (iBounds.getWidth() == mBounds.getWidth()) {
            fontComboBox.addItem(font.getName());
//            }
        }

        JComboBox<Integer> sizeComboBox = new JComboBox<>();
        sizeComboBox.setBackground(BACKGROUND);
        for (int i = 5; i < 40; i++) {
            sizeComboBox.addItem(i);
        }

        fontComboBox.setSelectedItem(ClientConfiguration.getInstance().getFont().getName());
        sizeComboBox.setSelectedItem(ClientConfiguration.getInstance().getFont().getSize());

        panel.add(fontComboBox, gbc);
        panel.add(sizeComboBox, gbc);

        JPanel exampleText = new JPanel();
        exampleText.setOpaque(false);
        JTextPane example = new JTextPane();
        example.setOpaque(false);
        example.setEditable(false);
        example.setFont(ClientConfiguration.getInstance().getFont());

        String data = ClientConfiguration.getInstance().getFont().getName();
        StyledDocument doc = example.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(center, 0.3f);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        try {
            doc.insertString(doc.getLength(), data, center);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        exampleText.add(example);

        fontComboBox.addActionListener(e -> {
            String fontSelection = (String) fontComboBox.getModel().getSelectedItem();
            int sizeSelection = (int) sizeComboBox.getModel().getSelectedItem();
            example.setFont(new Font(fontSelection, Font.PLAIN, sizeSelection));
        });
        sizeComboBox.addActionListener(e -> {
            String fontSelection = (String) fontComboBox.getModel().getSelectedItem();
            int sizeSelection = (int) sizeComboBox.getModel().getSelectedItem();
            example.setFont(new Font(fontSelection, Font.PLAIN, sizeSelection));
        });

        JPanel buttons = new JPanel(new GridBagLayout());
        gbc.ipady = 20;
        gbc.gridy = 2;

        buttons.setOpaque(false);

        ImageIcon saveHovered = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_HOVER));
        ImageIcon saveNeutral = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_GREEN));
        ImageIcon saveClicked = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_CLICKED));
        Button saveButton = new Button(saveNeutral);
        saveButton.setClickedIcon(saveClicked);
        saveButton.setHoveredIcon(saveHovered);
        saveButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        saveButton.addActionListener(e -> {
            int size = (int) sizeComboBox.getSelectedItem();
            Font f = new Font((String) fontComboBox.getSelectedItem(), Font.PLAIN, size);
            chooseFont(f);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        ImageIcon closeHovered = new ImageIcon(Buttons.getSprite(Buttons.CANCEL_HOVER));
        ImageIcon closeNeutral = new ImageIcon(Buttons.getSprite(Buttons.CANCEL_GREEN));
        ImageIcon closeClicked = new ImageIcon(Buttons.getSprite(Buttons.CANCEL_CLICKED));
        Button closeButton = new Button(closeNeutral);
        closeButton.setClickedIcon(closeClicked);
        closeButton.setHoveredIcon(closeHovered);
        closeButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        closeButton.addActionListener(e -> {
            dispatchEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING
            ));
        });

        buttons.add(saveButton, gbc);
        buttons.add(closeButton, gbc);

        gbc.gridy = 1;
        gbc.ipady = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        buttons.add(exampleText, gbc);

        setDraggable(buttons);
        setDraggable(panel);

        JPanel label = new JPanel();
        label.setOpaque(false);
        JLabel l = new JLabel("Select your font");
        label.add(l, BorderLayout.CENTER);

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        p.add(label, BorderLayout.NORTH);
        p.add(panel);
        p.add(buttons, BorderLayout.SOUTH);

        getContentPane().add(p);

        pack();
        setLocationRelativeTo(Client.getInstance().getGui());
    }

    private void setDraggable(JComponent p) {
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                pointerX = mouseEvent.getX();
                pointerY = mouseEvent.getY();
            }

            public void mouseDragged(MouseEvent mouseEvent) {
                instance.setLocation(instance.getLocation().x + mouseEvent.getX() - pointerX,
                        instance.getLocation().y + mouseEvent.getY() - pointerY);
            }
        });

        p.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                instance.setLocation(instance.getLocation().x + mouseEvent.getX() - pointerX,
                        instance.getLocation().y + mouseEvent.getY() - pointerY);
            }
        });
    }

    private void chooseFont(Font f) {
        // set font as config font and size
        ClientConfiguration.getInstance().setFont(f.getName(), f.getSize());
        ClientConfiguration.getInstance().saveConfig();

        // update text area fonts
        Client.getInstance().getGui().getActionWindow().getTextArea().changeFont(f);
        Client.getInstance().getGui().getActionWindow().getInputField().changFont(f);
        Client.getInstance().getGui().getChatWindow().getTextArea().changeFont(f);
    }

    public static TextSettings getInstance() {
        if (instance == null) {
            instance = new TextSettings();
        }
        return instance;
    }

}
