package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.Sprites.Buttons;

public class ChatColorSettings extends JFrame {

    private static ChatColorSettings instance;

    public ChatColorSettings() {
        setUndecorated(true);
        BackgroundImagePanel backgroundImagePanel = new BackgroundImagePanel(GUIConstantsConfig.PARCHMENT);
        setContentPane(backgroundImagePanel);
        getRootPane().setBorder(new PerforatedBorder(PerforatedBorder.GREEN));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 200));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JPanel colorPanel = new JPanel(new GridLayout(0, 2));
        colorPanel.setBorder(BorderFactory.createEmptyBorder());
        colorPanel.setOpaque(false);

        for (String channel : ClientConfiguration.CHAT_CHANNELS) {
            String label;
            switch (channel) {
                case "Tell":
                    label = "Send";
                    break;
                case "Gsend":
                    label = "Guild";
                    break;
                case "death":
                    label = "Deaths";
                    break;
                default:
                    label = channel;
            }
            JLabel l = new JLabel(label);
            l.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            l.setOpaque(false);

            colorPanel.add(l);

            JButton color = new JButton();
            color.setSize(new Dimension(15, 15));
            color.setFocusPainted(false);
            color.setFocusable(false);
            Color channelColor = ClientConfiguration.getInstance().getColor(channel);
            Color background = channelColor != null ? channelColor : Color.WHITE;
            color.setBackground(background);
            color.addActionListener(e -> {
                Color newColor = JColorChooser.showDialog(instance, "Choose text color for this channel",
                        this.getBackground());
                if (newColor != null) {
                    ClientConfiguration.getInstance().setColor(channel, newColor);
                    color.setBackground(newColor);
                }
            });

            colorPanel.add(color);
        }

        JPanel buttons = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 30;

        buttons.setOpaque(false);

        ImageIcon saveHovered = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_HOVER));
        ImageIcon saveNeutral = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_GREEN));
        ImageIcon saveClicked = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_CLICKED));
        Button saveButton = new Button(saveNeutral);
        saveButton.setClickedIcon(saveClicked);
        saveButton.setHoveredIcon(saveHovered);
        saveButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        saveButton.addActionListener(e -> {
            ClientConfiguration.getInstance().saveConfig();
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

        JPanel title = new JPanel();
        title.setOpaque(false);
        JLabel l = new JLabel("Select your chat channel colors");
        title.add(l, BorderLayout.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        panel.add(title, BorderLayout.NORTH);
        panel.add(colorPanel);
        panel.add(buttons, BorderLayout.SOUTH);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(Client.getInstance().getGui());
    }

    public static ChatColorSettings getInstance() {
        if (instance == null) {
            instance = new ChatColorSettings();
        }
        return instance;
    }
}
