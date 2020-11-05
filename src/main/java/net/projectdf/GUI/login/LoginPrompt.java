package net.projectdf.GUI.login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import javax.swing.*;
import net.projectdf.GUI.Sprites.Buttons;
import net.projectdf.GUI.uiComponents.BackgroundImagePanel;
import net.projectdf.GUI.uiComponents.Button;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;
import net.projectdf.GUI.uiComponents.PerforatedBorder;

public class LoginPrompt extends JFrame {

    private static LoginPrompt instance;

    private Button loginButton;

    public LoginPrompt() {
        setUndecorated(true);
        BackgroundImagePanel backgroundImagePanel = new BackgroundImagePanel(GUIConstantsConfig.PARCHMENT);
        setContentPane(backgroundImagePanel);
        getRootPane().setBorder(new PerforatedBorder(PerforatedBorder.GREEN));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 100));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        panel.setLayout(new GridLayout(0, 2));
        panel.setOpaque(false);

        JLabel accountLabel = new JLabel("Account name:");
        accountLabel.setOpaque(false);

        JTextField accountName = new JTextField();
        accountName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2,2, 2),
                BorderFactory.createLoweredSoftBevelBorder()));
        accountName.setBackground(new Color(157, 138, 108));

        accountLabel.setLabelFor(accountName);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setOpaque(false);

        JPasswordField password = new JPasswordField();
        password.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2,2, 2),
                BorderFactory.createLoweredSoftBevelBorder()));
        password.setBackground(new Color(157, 138, 108));

        passwordLabel.setLabelFor(password);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JPanel buttons = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 30;

        buttons.setOpaque(false);

        ImageIcon saveHovered = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_HOVER));
        ImageIcon saveNeutral = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_GREEN));
        ImageIcon saveClicked = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_CLICKED));
        loginButton = new Button(saveNeutral);
        loginButton.setClickedIcon(saveClicked);
        loginButton.setHoveredIcon(saveHovered);
        loginButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        loginButton.addActionListener(e -> {
            // TODO: Dispatch Client instantiation
            String account = accountName.getText();
            String pw = new String(password.getPassword());
            if (account.equals("") || pw.equals("")) {
                JOptionPane.showMessageDialog(LoginWindow.getInstance(), "Please enter a valid account name and password.");
                return;
            }
            Login.setAccountName(account);
            Login.setPassword(pw);

            synchronized (LoginWindow.getInstance()) {
                LoginWindow.getInstance().notify();
            }
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
            System.exit(0);
        });

        buttons.add(loginButton, gbc);
        buttons.add(closeButton, gbc);

        buttonPanel.add(buttons);

        accountName.addKeyListener(enter());
        password.addKeyListener(enter());

        panel.add(accountLabel);
        panel.add(accountName);
        panel.add(passwordLabel);
        panel.add(password);

        getContentPane().add(panel);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    public static LoginPrompt getInstance() {
        if (instance == null) {
            instance = new LoginPrompt();
        }
        return instance;
    }

    public void showWindow() {
        setLocationRelativeTo(LoginWindow.getInstance());
        setVisible(true);
    }

    private KeyListener enter() {
        return  new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                // Don't worry about it
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    ActionListener a = Arrays.stream(loginButton.getActionListeners()).findFirst().orElse(null);
                    if (a != null) {
                        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                // Don't worry about it
            }
        };
    }

}
