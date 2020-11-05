package net.projectdf.GUI.uiComponents;

import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.Logging.ChatLogger;

public class ChatWindow extends JPanel {

    private ChatLogger chatLogger = ChatLogger.getInstance();

    private final int chatBufferMax = 3000;

    private JScrollPane scrollPane;
    private SmartScroller scroller;
    private final GUITextArea textArea;

    public ChatWindow(BorderLayout borderLayout, Dimension dimension) {
        super(borderLayout);
        setPreferredSize(dimension);
        setOpaque(ClientConfiguration.getInstance().getCustomBackground());
        setBackground(ClientConfiguration.getInstance().getBackgroundColor());
        setBorder(new PerforatedBorder(PerforatedBorder.GREY));

        textArea = new GUITextArea(chatBufferMax);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL) {
            @Override
            public boolean isVisible() {
                return true;
            }
        });
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(80);
        scrollPane.setPreferredSize(dimension.getSize());

        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int movement = e.getWheelRotation();
                if (movement < 0) {
                    scrollUp(true);
                } else {
                    scrollDown(true);
                }
            }
        });

        textArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isMiddleMouseButton(mouseEvent)) {
                    scroller.setAdjustScrollBar(true);
                    AdjustmentEvent evt = new AdjustmentEvent(scrollPane.getVerticalScrollBar(), 1, 1, 1);
                    scroller.adjustmentValueChanged(evt);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Not important
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                // Not important
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                // Not important
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                // Not important
            }
        });

        // This will keep the scroll pane at the bottom but allow you to scroll up as well
        scroller = new SmartScroller(scrollPane);

        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.setOpaque(false);

        ChatScrollControl scrollControl = new ChatScrollControl();
        scrollPanel.add(scrollControl, BorderLayout.SOUTH);

        add(scrollPane);
        add(scrollPanel, BorderLayout.LINE_END);

        scrollControl.setAlignmentY(BOTTOM_ALIGNMENT);
    }

    public GUITextArea getTextArea() {
        return textArea;
    }

    public void writeToChatWindow(String channel, String data) {
        synchronized (this) {
            chatLogger.info(data);
            textArea.appendChatChannel(channel, data);
        }
    }

    public void scrollUp(boolean isMouseWheel) {
        int value = scrollPane.getVerticalScrollBar().getValue();
        int increment = scrollPane.getVerticalScrollBar().getUnitIncrement();
        if (!isMouseWheel) {
            increment *= 1.5;
        }
        scroller.setAdjustScrollBar(false);
        scrollPane.getVerticalScrollBar().setValue(value - increment);
    }

    public void scrollDown(boolean isMouseWheel) {
        int value = scrollPane.getVerticalScrollBar().getValue();
        int increment = scrollPane.getVerticalScrollBar().getUnitIncrement();
        if (!isMouseWheel) {
            increment *= 1.5;
        }
        int max = scrollPane.getVerticalScrollBar().getMaximum();
        int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
        if (value + extent >= max) {
            scroller.setAdjustScrollBar(true);
        }
        scrollPane.getVerticalScrollBar().setValue(value + increment);
    }

}
