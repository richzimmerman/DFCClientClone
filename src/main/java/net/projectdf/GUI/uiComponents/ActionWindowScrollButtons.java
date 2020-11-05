package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.GUI.Sprites.GargoyleSprites;
import net.projectdf.Logging.ActionLogger;

public class ActionWindowScrollButtons extends JLayeredPane {

    private Image img;

    public ActionWindowScrollButtons() {
        setOpaque(false);
        setFocusable(false);
        img = GargoyleSprites.getSprite(GargoyleSprites.CONTROL);
        setPreferredSize(new Dimension(GargoyleSprites.GARGOYLE.getWidth(), img.getHeight(null)));

        ImageIcon loggerButton = new ImageIcon(GargoyleSprites.getSprite(GargoyleSprites.LOGGER));
        JButton log = new JButton();
        log.setFocusPainted(false);
        log.setOpaque(false);
        log.setContentAreaFilled(false);
        log.setBorderPainted(false);
        log.setFocusable(false);
        log.setBorder(BorderFactory.createEmptyBorder());
        log.setBounds(62, 3, GargoyleSprites.LOGGER.getWidth(),
                GargoyleSprites.LOGGER.getHeight());
        log.setIcon(loggerButton); // TODO: Delete this line when combat logging disabled by default
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ActionLogger.getInstance().toggle();
                if (ActionLogger.logging) {
                    log.setIcon(loggerButton);
                } else {
                    log.setIcon(null);
                }
            }
        });

        JButton up = new JButton();
        ImageIcon upButton = new ImageIcon(GargoyleSprites.getSprite(GargoyleSprites.UP_PRESSED));
        up.setBorderPainted(false);
        up.setContentAreaFilled(false);
        up.setFocusPainted(false);
        up.setOpaque(false);
        up.setFocusable(false);
        up.setSelectedIcon(upButton);
        up.setBounds(61, 38, GargoyleSprites.LOGGER.getWidth(),
                GargoyleSprites.LOGGER.getHeight());
        up.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Client.getInstance().getGui().getActionWindow().scrollUp(false);
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
        ImageIcon downButton = new ImageIcon(GargoyleSprites.getSprite(GargoyleSprites.DOWN_PRESSED));
        down.setBorderPainted(false);
        down.setContentAreaFilled(false);
        down.setFocusPainted(false);
        down.setOpaque(false);
        down.setFocusable(false);
        down.setBounds(61, 73, GargoyleSprites.LOGGER.getWidth(),
                GargoyleSprites.LOGGER.getHeight());
        down.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Client.getInstance().getGui().getActionWindow().scrollDown(false);
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

        JButton rtrn = new JButton();
        ImageIcon returnButton = new ImageIcon(GargoyleSprites.getSprite(GargoyleSprites.RETURN_PRESSED));
        rtrn.setBorderPainted(false);
        rtrn.setContentAreaFilled(false);
        rtrn.setFocusPainted(false);
        rtrn.setOpaque(false);
        rtrn.setFocusable(false);
        rtrn.setBounds(61, 108, GargoyleSprites.LOGGER.getWidth(),
                GargoyleSprites.LOGGER.getHeight());
        rtrn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JScrollPane scrollPane = Client.getInstance().getGui().getActionWindow()
                        .getScrollPane();
                SmartScroller scroller = Client.getInstance().getGui().getActionWindow()
                        .getScroller();
                scroller.setAdjustScrollBar(true);
                AdjustmentEvent evt = new AdjustmentEvent(scrollPane.getVerticalScrollBar(), 1, 1, 1);
                scroller.adjustmentValueChanged(evt);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                rtrn.setIcon(returnButton);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                rtrn.setIcon(null);
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
        add(rtrn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int widthDiff = GargoyleSprites.GARGOYLE.getWidth() - GargoyleSprites.CONTROL.getWidth();
        g.drawImage(img, widthDiff, 0, null);
    }

}
