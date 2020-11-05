package net.projectdf.GUI.uiComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.Sprites.Buttons;

import static java.awt.event.KeyEvent.VK_F1;
import static java.awt.event.KeyEvent.VK_F10;
import static java.awt.event.KeyEvent.VK_F11;
import static java.awt.event.KeyEvent.VK_F12;
import static java.awt.event.KeyEvent.VK_F2;
import static java.awt.event.KeyEvent.VK_F3;
import static java.awt.event.KeyEvent.VK_F4;
import static java.awt.event.KeyEvent.VK_F5;
import static java.awt.event.KeyEvent.VK_F6;
import static java.awt.event.KeyEvent.VK_F7;
import static java.awt.event.KeyEvent.VK_F8;
import static java.awt.event.KeyEvent.VK_F9;

public class MacroWindow extends JFrame {

    private static MacroWindow instance;

    private int pointerX;
    private int pointerY;

    private HashMap<String, JTextField> macroFields;

    public MacroWindow() {
        setUndecorated(true);
        BackgroundImagePanel backgroundImagePanel = new BackgroundImagePanel(GUIConstantsConfig.PARCHMENT);
        setContentPane(backgroundImagePanel);
        getRootPane().setBorder(new PerforatedBorder(PerforatedBorder.GREEN));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 200));
        setPreferredSize(new Dimension(400, 400));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Stores the input fields to iterate over later, rather than write 100s of extra lines of code
        macroFields = new HashMap<>();

        JPanel macros = new JPanel(new GridLayout(0, 2));
        macros.setBackground(new Color(0, 0, 0, 0));
        macros.setBorder(BorderFactory.createEmptyBorder());
        macros.setOpaque(false);

        for (int keyCode: ClientConfiguration.MACRO_POSITIONS) {
            String label;
            if (isFunctionKey(keyCode)) {
                label = KeyEvent.getKeyText(keyCode);
            } else {
                label = "CTRL + " + KeyEvent.getKeyText(keyCode);
            }
            JLabel l = new JLabel(label);
            l.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            l.setOpaque(false);

            macros.add(l);
            JTextField macroInput = new JTextField(15);
            macroInput.setFont(new Font(macroInput.getFont().getName(), Font.BOLD, 11));
            macroInput.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(2, 2,2, 2),
                    BorderFactory.createLoweredSoftBevelBorder()));
            macroInput.setBackground(new Color(157, 138, 108));

            Object setMacro = ClientConfiguration.getInstance().getMacro(keyCode);
            macroInput.setText((String) setMacro);

            l.setLabelFor(macroInput);
            macros.add(macroInput);

            macroFields.put(Integer.toString(keyCode), macroInput);
        }

        JScrollPane scroller = new JScrollPane(macros);
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 125)));
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.getVerticalScrollBar().setUnitIncrement(40);

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
            saveMacros();
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
            instance = null;
            dispatchEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING
            ));
        });

        buttons.add(saveButton, gbc);
        buttons.add(closeButton, gbc);

        setDraggable(scroller);
        setDraggable(buttons);

        panel.add(scroller);
        panel.add(buttons, BorderLayout.SOUTH);

        getContentPane().add(panel, BorderLayout.CENTER);
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

    private boolean isFunctionKey(int code) {
        Integer[] functionKeys = {VK_F1, VK_F2, VK_F3, VK_F4, VK_F5, VK_F6, VK_F7, VK_F8,
                VK_F9, VK_F10, VK_F11, VK_F12};
        Set<Integer> F_KEYS = new HashSet<>(Arrays.asList(functionKeys));
        return F_KEYS.contains(code);
    }

    private void saveMacros() {
        for (String key : macroFields.keySet()) {
            ClientConfiguration.getInstance().setMacro(key, macroFields.get(key).getText());
        }
        ClientConfiguration.getInstance().saveConfig();
    }

    public static MacroWindow getInstance() {
        if (instance == null) {
            instance = new MacroWindow();
        }
        return instance;
    }

}
