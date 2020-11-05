package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.GUI.Sprites.ChatScrollSprites;
import net.projectdf.Logging.ChatLogger;

public class ChatScrollControl extends JLayeredPane {

    private Image img;

    public ChatScrollControl() {
        setOpaque(false);
        setFocusable(false);
        img = ChatScrollSprites.getSprite(ChatScrollSprites.CONTROL);
        setPreferredSize(new Dimension(ChatScrollSprites.CONTROL.getWidth(), ChatScrollSprites.CONTROL.getHeight()));

        ImageIcon loggerButton = new ImageIcon(ChatScrollSprites.getSprite(ChatScrollSprites.LOGGER));
        JButton log = new JButton();
        log.setFocusPainted(false);
        log.setOpaque(false);
        log.setContentAreaFilled(false);
        log.setBorderPainted(false);
        log.setFocusable(false);
        log.setBorder(BorderFactory.createEmptyBorder());
        log.setBounds(16, 38, ChatScrollSprites.LOGGER.getWidth(),
                ChatScrollSprites.LOGGER.getHeight());
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ChatLogger.getInstance().toggle();
                if (ChatLogger.logging) {
                    log.setIcon(loggerButton);
                } else {
                    log.setIcon(null);
                }
            }
        });


        JButton up = new JButton();
        ImageIcon upButton = new ImageIcon(ChatScrollSprites.getSprite(ChatScrollSprites.UP_PRESSED));
        up.setBorderPainted(false);
        up.setContentAreaFilled(false);
        up.setFocusPainted(false);
        up.setOpaque(false);
        up.setFocusable(false);
        up.setSelectedIcon(upButton);
        up.setBounds(57, 19, ChatScrollSprites.LOGGER.getWidth(),
                ChatScrollSprites.LOGGER.getHeight());
        up.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Client.getInstance().getGui().getChatWindow().scrollUp(false);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                up.setIcon(upButton);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                up.setIcon(null);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                // do nothing
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                // do nothing
            }
        });

        JButton down = new JButton();
        ImageIcon downButton = new ImageIcon(ChatScrollSprites.getSprite(ChatScrollSprites.DOWN_PRESSED));
        down.setBorderPainted(false);
        down.setContentAreaFilled(false);
        down.setFocusPainted(false);
        down.setOpaque(false);
        down.setFocusable(false);
        down.setBounds(57, 57, ChatScrollSprites.LOGGER.getWidth(),
                ChatScrollSprites.LOGGER.getHeight());
        down.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Client.getInstance().getGui().getChatWindow().scrollDown(false);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                down.setIcon(downButton);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                down.setIcon(null);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                // Do nothing
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                // Do nothing
            }
        });

        add(log);
        add(up);
        add(down);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(img, 0, 0, null);
    }
}
