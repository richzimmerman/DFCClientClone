package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.Sprites.Buttons;

public class BackgroundSettings extends JFrame {

    private static BackgroundSettings instance;

    public BackgroundSettings() {
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

        JPanel title = new JPanel();
        title.setOpaque(false);
        JLabel t = new JLabel("Enable and Select your background color");
        title.add(t, BorderLayout.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        boolean enabled = ClientConfiguration.getInstance().getCustomBackground();

        JPanel colorPanel = new JPanel(new GridLayout(0, 2));
        colorPanel.setBorder(BorderFactory.createEmptyBorder());
        colorPanel.setOpaque(false);

        JLabel l = new JLabel("Background Color");
        l.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        l.setOpaque(false);

        colorPanel.add(l);

        JButton color = new JButton();
        color.setSize(new Dimension(15, 15));
        color.setFocusPainted(false);
        color.setFocusable(false);
        color.setEnabled(enabled);
        Color backgroundColor = ClientConfiguration.getInstance().getBackgroundColor();
        color.setBackground(backgroundColor);
        color.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(instance, "Choose background color",
                    this.getBackground());
            if (newColor != null) {
                color.setBackground(newColor);
            }
        });

        colorPanel.add(color);

        JCheckBoxMenuItem plainColorBackground = new JCheckBoxMenuItem("Plain Color Background");
        plainColorBackground.setState(enabled);
        plainColorBackground.setOpaque(false);
        plainColorBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton a = (AbstractButton) actionEvent.getSource();
                boolean selected = a.getModel().isSelected();
                plainColorBackground.setState(selected);
                color.setEnabled(selected);
            }
        });

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
            // Update the UI
            boolean selected = plainColorBackground.getState();
            Color newColor = color.getBackground();
            // Update Config
            ClientConfiguration.getInstance().setCustomBackground(selected);
            ClientConfiguration.getInstance().setBackgroundColor(newColor);
            // Set and show the colors
            // Setting the background to a transparent color first since disabling and re-enabling plain background
            //   with the same color seems to act weird...
            Client.getInstance().getGui().getActionWindow().setBackground(new Color(0, 0, 0, 0));
            Client.getInstance().getGui().getChatWindow().setBackground(new Color(0, 0, 0, 0));

            Client.getInstance().getGui().getActionWindow().setBackground(newColor);
            Client.getInstance().getGui().getChatWindow().setBackground(newColor);
            Client.getInstance().getGui().getChatWindow().setOpaque(selected);
            Client.getInstance().getGui().getActionWindow().setOpaque(selected);
            // Repaint the background if disabling plain background
            if (!selected) {
                Client.getInstance().getGui().getBackgroundImage().repaint();
            }
            // Save and close
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
            color.setBackground(ClientConfiguration.getInstance().getBackgroundColor());
            dispatchEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING
            ));
        });

        buttons.add(saveButton, gbc);
        buttons.add(closeButton, gbc);

        panel.add(title, BorderLayout.NORTH);
        panel.add(plainColorBackground);
        panel.add(colorPanel);
        panel.add(buttons, BorderLayout.SOUTH);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(Client.getInstance().getGui());
    }

    public static BackgroundSettings getInstance() {
        if (instance == null) {
            instance = new BackgroundSettings();
        }
        return instance;
    }
}
