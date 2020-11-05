package net.projectdf.GUI.uiComponents;

import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import net.projectdf.Configuration.ClientConfiguration;

public class InputField extends JTextField {

    public InputField() {
        super();

        Font f = ClientConfiguration.getInstance().getFont();
        setFont(f);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 0));
        setForeground(Color.WHITE);
        setCaretColor(Color.WHITE);
        getActionMap().put(DefaultEditorKit.deletePrevCharAction, new CustomDeleteAction());

    }

    public void changFont(Font f) {
        if (f.getSize() > 18) {
            f = new Font(f.getName(), Font.PLAIN, 18);
        }
        setFont(f);
    }

    static class CustomDeleteAction extends TextAction {

        /**
         * Creates this object with the appropriate identifier.
         */
        CustomDeleteAction() {
            super(DefaultEditorKit.deletePrevCharAction);
        }

        /**
         * The operation to perform when this action is triggered.
         *
         * @param e the action event
         */
        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if ((target != null) && (target.isEditable())) {
                try {
                    Document doc = target.getDocument();
                    Caret caret = target.getCaret();
                    int dot = caret.getDot();
                    int mark = caret.getMark();
                    if (dot != mark) {
                        doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
                    } else if (dot > 0) {
                        int delChars = 1;

                        if (dot > 1) {
                            String dotChars = doc.getText(dot - 2, 2);
                            char c0 = dotChars.charAt(0);
                            char c1 = dotChars.charAt(1);

                            if (c0 >= '\uD800' && c0 <= '\uDBFF' &&
                                    c1 >= '\uDC00' && c1 <= '\uDFFF') {
                                delChars = 2;
                            }
                        }

                        doc.remove(dot - delChars, delChars);
                    }
                } catch (BadLocationException bl) {
                    // Do nothing
                }
            }
        }
    }

}
