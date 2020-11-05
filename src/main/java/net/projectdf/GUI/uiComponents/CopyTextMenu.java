package net.projectdf.GUI.uiComponents;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CopyTextMenu extends JPopupMenu {

    JMenuItem copy;

    public CopyTextMenu(String highlightedData) {
        copy = new JMenuItem("Copy text");

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                StringSelection data = new StringSelection(highlightedData);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(data, data);
            }
        });

        add(copy);

    }

}
