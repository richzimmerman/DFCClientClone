package net.projectdf.GUI.uiComponents;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.projectdf.Client.Client;
import net.projectdf.Configuration.ClientConfiguration;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.Color;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUITextArea extends JTextPane {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final int maxBuffer;

    private final Pattern hasText = Pattern.compile("^[\\s\n\r]*$");

    private long lastInput = 0;

    static final Color D_Black   = Color.getHSBColor( 0.000f, 0.000f, 0.000f );
    static final Color D_Red     = Color.getHSBColor( 0.000f, 1.000f, 0.502f );
    static final Color D_Blue    = Color.getHSBColor( 0.667f, 1.000f, 0.502f );
    static final Color D_Magenta = Color.getHSBColor( 0.833f, 1.000f, 0.502f );
    static final Color D_Green   = Color.getHSBColor( 0.333f, 1.000f, 0.502f );
    static final Color D_Yellow  = Color.getHSBColor( 0.167f, 1.000f, 0.502f );
    static final Color D_Cyan    = Color.getHSBColor( 0.500f, 1.000f, 0.502f );
    static final Color D_White   = Color.getHSBColor( 0.000f, 0.000f, 0.753f );
    static final Color B_Black   = Color.getHSBColor( 0.000f, 0.000f, 0.502f );
    static final Color B_Red     = Color.getHSBColor( 0.000f, 1.000f, 1.000f );
    static final Color B_Blue    = Color.getHSBColor( 0.667f, 1.000f, 1.000f );
    static final Color B_Magenta = Color.getHSBColor( 0.833f, 1.000f, 1.000f );
    static final Color B_Green   = Color.getHSBColor( 0.333f, 1.000f, 1.000f );
    static final Color B_Yellow  = Color.getHSBColor( 0.167f, 1.000f, 1.000f );
    static final Color B_Cyan    = Color.getHSBColor( 0.500f, 1.000f, 1.000f );
    static final Color B_White   = Color.getHSBColor( 0.000f, 0.000f, 1.000f );
    static final Color cReset    = Color.getHSBColor( 0.000f, 0.000f, 1.000f );
    static Color colorCurrent    = cReset;
    String remaining = "";

    public GUITextArea(int maxBuffer) {
        super();
        this.maxBuffer = maxBuffer;

        Font f = ClientConfiguration.getInstance().getFont();
        setFont(f);
        setOpaque(false);
        setForeground(Color.WHITE);
        setEditable(false);
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        DefaultCaret caret = (DefaultCaret)getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // Do nothing out of the ordinary
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Do nothing out of the ordinary
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    String data = getSelectedText();
                    if (data != null) {
                        CopyTextMenu menu = new CopyTextMenu(data);
                        menu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                    }
                }
                Client.getInstance().getGui().getActionWindow().getInputField().requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                // Do nothing out of the ordinary
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                // Do nothing out of the ordinary
            }
        });
    }

    public void appendANSI(String s) { // convert ANSI color codes first
        long now = System.currentTimeMillis();
        try {
            int len = getDocument().getLength();
            if (len + s.length() > maxBuffer) {
                if (now - lastInput > 800) { // This prevents the SmartScroller from adjusting too often and acting weird... hopefully.
                    // This should delete up to the first newline after the # of chars > maxBuffer so there's no weird text offsets
                    // Note: might be an \r afterwards... need to test
                    int index = getDocument().getText(0, len).indexOf("\n", s.length());
                    getDocument().remove(0, index);
                }
            }
        } catch (BadLocationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        List<String> highlightStrings = ClientConfiguration.getInstance().getHighlightStrings();
        for (String hls : highlightStrings) {
            s = s.replace(hls, hls.toUpperCase());
        }

        lastInput = now;

        int aPos = 0;   // current char position in addString
        int aIndex = 0; // index of next Escape sequence
        int mIndex = 0; // index of "m" terminating Escape sequence
        String tmpString = "";
        boolean stillSearching = true; // true until no more Escape sequences
        String addString = remaining + s;
        remaining = "";

        if (addString.length() > 0) {
            aIndex = addString.indexOf("\u001B"); // find first escape
            if (aIndex == -1) { // no escape/color change in this string, so just send it with current color
                append(colorCurrent,addString);
                return;
            }
            // otherwise There is an escape character in the string, so we must process it

            if (aIndex > 0) { // Escape is not first char, so send text up to first escape
                tmpString = addString.substring(0,aIndex);
                append(colorCurrent, tmpString);
                aPos = aIndex;
            }
            // aPos is now at the beginning of the first escape sequence

            stillSearching = true;
            while (stillSearching) {
                mIndex = addString.indexOf("m",aPos); // find the end of the escape sequence
                if (mIndex < 0) { // the buffer ends halfway through the ansi string!
                    remaining = addString.substring(aPos,addString.length());
                    stillSearching = false;
                    continue;
                }
                else {
                    tmpString = addString.substring(aPos,mIndex+1);
                    colorCurrent = getANSIColor(tmpString);
                }
                aPos = mIndex + 1;
                // now we have the color, send text that is in that color (up to next escape)

                aIndex = addString.indexOf("\u001B", aPos);

                if (aIndex == -1) { // if that was the last sequence of the input, send remaining text
                    tmpString = addString.substring(aPos,addString.length());
                    append(colorCurrent, tmpString);
                    stillSearching = false;
                    continue; // jump out of loop early, as the whole string has been sent now
                }

                // there is another escape sequence, so send part of the string and prepare for the next
                tmpString = addString.substring(aPos,aIndex);
                aPos = aIndex;
                append(colorCurrent, tmpString);

            } // while there's text in the input buffer
        }
    }

    public Color getANSIColor(String ANSIColor) {
        switch (ANSIColor) {
            case "\u001B[30m":
            case "\u001B[0;30m":
            case "\u001B[40m":
                return D_Black;
            case "\u001B[1;30m":
                return B_Black;
            case "\u001B[31m":
            case "\u001B[0;31m":
            case "\u001B[41m":
                return D_Red;
            case "\u001B[1;31m":
                return B_Red;
            case "\u001B[32m":
            case "\u001B[0;32m":
            case "\u001B[42m":
                return D_Green;
            case "\u001B[1;32m":
                return B_Green;
            case "\u001B[33m":
            case "\u001B[0;33m":
            case "\u001B[43m":
                return D_Yellow;
            case "\u001B[1;33m":
                return B_Yellow;
            case "\u001B[34m":
            case "\u001B[0;34m":
            case "\u001B[44m":
                return D_Blue;
            case "\u001B[1;34m":
                return B_Blue;
            case "\u001B[35m":
            case "\u001B[0;35m":
            case "\u001B[45m":
                return D_Magenta;
            case "\u001B[1;35m":
                return B_Magenta;
            case "\u001B[36m":
            case "\u001B[0;36m":
            case "\u001B[46m":
                return D_Cyan;
            case "\u001B[1;36m":
                return B_Cyan;
            case "\u001B[37m":
            case "\u001B[0;37m":
            case "\u001B[47m":
                return D_White;
            case "\u001B[0":
            case "\u001B[0m":
            case "\u001B[0;0m":
                return cReset;
            default:
                return B_White; // TODO: Default color setting
        }
    }

    public void append(Color c, String s) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        try {
            int len = getDocument().getLength();
            Matcher m = hasText.matcher(s);
            if (!m.find() || !s.equals("")) {
                getDocument().insertString(len, s, aset);
            }
        } catch (BadLocationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void changeFont(Font font) {
        this.setFont(font);
    }

    public void appendChatChannel(String channel, String data) {
        Color channelColor = ClientConfiguration.getInstance().getColor(channel);
        if (channelColor == null) {
            channelColor = Color.WHITE;
        }
        // For Plan/Guild/Party cases.
        if (data.equals("")) {
            return; // WARNING: unable to handle: comm.channel {"chan":"say","msg":"","player":"Lymseia, purveyor of exotic goods"}
        }
        int channelIndex = data.indexOf("]", 1);
        String line = data.substring(channelIndex+1);

        switch (channel.toLowerCase()) {
            case "gsend":
                // [Guild] Name: 'message'
                append(getANSIColor("default"), "[");
                append(channelColor, "Guild");
                append(getANSIColor("default"), "]" + line);
                break;
            case "plan":
                // [Plan] Name: 'message'
                append(getANSIColor("default"), "[");
                append(channelColor, "Plan");
                append(getANSIColor("default"), "]" + line);
                break;
            case "death":
                // whole line colored
            case "say":
                // whole line colored
            case "tell":
                // whole line colored
                append(channelColor, data);
                break;
            case "party":
                // [Party] Name: 'message'
                append(getANSIColor("default"), "[");
                append(channelColor, "Party");
                append(getANSIColor("default"), "]" + line);
                break;
            default:
                appendANSI(data);
        }
        appendANSI("\n");
    }

}
