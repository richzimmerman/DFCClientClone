package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.Sprites.Buttons;

public class HighlightStringsSettings extends JFrame {

    private int MIN_FIELDS = 14;
    private Color BACKGROUND = new Color(157, 138, 108);

    private static HighlightStringsSettings instance;

    private List<String> highlightStrings;
    private List<JTextField> hlsFields;

    public HighlightStringsSettings() {
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

        JPanel label = new JPanel();
        label.setOpaque(false);
        JLabel l = new JLabel("Define strings of text to be highlighted");
        label.add(l, BorderLayout.CENTER);

        highlightStrings = ClientConfiguration.getInstance().getHighlightStrings();
        hlsFields = new ArrayList<>();

        JPanel hlsPanel = new JPanel();
        hlsPanel.setOpaque(false);
        hlsPanel.setLayout(new BoxLayout(hlsPanel, BoxLayout.Y_AXIS));

        if (highlightStrings.size() > 0) {
            int fields = MIN_FIELDS - highlightStrings.size();
            if (fields <= 0) {
                for (String hls : highlightStrings) {
                    JTextField hlsField = makeField(hls);
                    hlsPanel.add(hlsField);
                    hlsFields.add(hlsField);
                }
            } else {
                for (int i = 0; i <= MIN_FIELDS; i++) {
                    if (i < highlightStrings.size()) {
                        JTextField hlsField = makeField(highlightStrings.get(i));
                        hlsPanel.add(hlsField);
                        hlsFields.add(hlsField);
                    } else {
                        JTextField hlsField = makeField(null);
                        hlsPanel.add(hlsField);
                        hlsFields.add(hlsField);
                    }
                }
            }
        } else {
            for (int i = 0; i <= MIN_FIELDS; i++) {
                JTextField hlsField = makeField(null);
                hlsPanel.add(hlsField);
                hlsFields.add(hlsField);
            }
        }

        JScrollPane scroller = new JScrollPane(hlsPanel);
        scroller.setOpaque(false);
        scroller.getViewport().setOpaque(false);
        scroller.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0, 0, 0, 125)));
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.getVerticalScrollBar().setUnitIncrement(40);

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setPreferredSize(new Dimension(getWidth(),Buttons.getHeight() + 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 30;

        buttons.setOpaque(false);

        ImageIcon createHovered = new ImageIcon(Buttons.getSprite(Buttons.CREATE_HOVER));
        ImageIcon createNeutral = new ImageIcon(Buttons.getSprite(Buttons.CREATE_GREEN));
        ImageIcon createClicked = new ImageIcon(Buttons.getSprite(Buttons.CREATE_CLICKED));
        Button createButton = new Button(createNeutral);
        createButton.setClickedIcon(createClicked);
        createButton.setHoveredIcon(createHovered);
        createButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        createButton.addActionListener(e -> {
            JTextField hlsField = makeField(null);
            hlsFields.add(hlsField);
            hlsPanel.add(hlsField);
            pack();
        });

        ImageIcon saveHovered = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_HOVER));
        ImageIcon saveNeutral = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_GREEN));
        ImageIcon saveClicked = new ImageIcon(Buttons.getSprite(Buttons.ACCEPT_CLICKED));
        Button saveButton = new Button(saveNeutral);
        saveButton.setClickedIcon(saveClicked);
        saveButton.setHoveredIcon(saveHovered);
        saveButton.setPreferredSize(new Dimension(Buttons.getWidth(), Buttons.getHeight()));

        saveButton.addActionListener(e -> {
            saveHighlightStrings();
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

        buttons.add(createButton, gbc);
        buttons.add(saveButton, gbc);
        buttons.add(closeButton, gbc);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scroller);
        panel.add(buttons, BorderLayout.SOUTH);

        getContentPane().add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(Client.getInstance().getGui());
    }

    public static HighlightStringsSettings getInstance() {
        if (instance == null) {
            instance = new HighlightStringsSettings();
        }
        return instance;
    }

    private void saveHighlightStrings() {
        List<String> strings = new ArrayList<>();
        for (JTextField hls : hlsFields) {
            if (hls.getText().equals("")) {
                continue;
            }
            strings.add(hls.getText());
        }
        ClientConfiguration.getInstance().setHighlightStrings(strings);
    }

    private JTextField makeField(String text) {
        JTextField hlsField = new JTextField(15);
        hlsField.setOpaque(false);
        hlsField.setFont(new Font(hlsField.getFont().getName(), Font.BOLD, 11));
        hlsField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createLoweredSoftBevelBorder()));
        hlsField.setBackground(BACKGROUND);
        hlsField.setText(text);
        return hlsField;
    }

}
