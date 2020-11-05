package net.projectdf.GUI.uiComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import net.projectdf.Client.Client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.Logging.ActionLogger;
import net.projectdf.TimerTasks.EnterMacro;

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


public class ActionWindow extends JPanel {

    private final static ActionLogger actionLogger = ActionLogger.getInstance();
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static boolean CONSUME_KEY_EVENT = false;

    private final int actionBufferMax = 150000; // Not sure what a good number is here

    private final int inputBufferMax = 10;
    private final List<String> inputBuffer = new ArrayList<>();
    private static int inputIndex = 0;

    private static long lastInput = 0;

    private JScrollPane scrollPane;
    private SmartScroller scroller;
    private final GUITextArea textArea;
    private InputField input;

    private boolean SNEAKING = false; // Not a fan of this being here..

    public ActionWindow(Dimension dimension) {
        setLayout(new BorderLayout());
        setOpaque(ClientConfiguration.getInstance().getCustomBackground());
        setBackground(ClientConfiguration.getInstance().getBackgroundColor());
        setBorder(new PerforatedBorder(PerforatedBorder.BLUE));
        setPreferredSize(dimension);

        textArea = new GUITextArea(actionBufferMax);

        JPanel inputArea = new JPanel(new RelativeLayout(RelativeLayout.X_AXIS));
        inputArea.setOpaque(false);
        inputArea.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.WHITE));

        input = new InputField();

        input.addActionListener(e -> {
            String inputText = input.getText().trim();
            addToInputBuffer(inputText);
            if (ClientConfiguration.getInstance().getPersistLastCommand()) {
                input.selectAll();
                inputIndex = 0;
            } else {
                input.setText("");
                inputIndex = -1;
            }
            actionLogger.info(inputText + "\n"); // Log echo regardless of whether or not its toggled for better visibility
            if (ClientConfiguration.getInstance().getLocalEcho()) {
                writeToActionWindow("\u001b[47m" + inputText + "\u001B[0m\n\n");
            }
            if (inputText.equals("mrbagginzisawesome")) {
                writeToActionWindow(MRB);
            } else {
                sendInput(inputText);
            }
        });
        addNumPadDirections();

        inputArea.add(input, 10f);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.setFocusable(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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

        ActionWindowControl actionWindowControl = new ActionWindowControl();

        JPanel actionWindow = new JPanel(new BorderLayout());
        actionWindow.setOpaque(false);
        actionWindow.setFocusable(false);

        JPanel scrollControl = new JPanel(new BorderLayout());
        scrollControl.setOpaque(false);
        scrollControl.add(actionWindowControl, BorderLayout.SOUTH);

        actionWindow.add(scrollPane);
        actionWindow.add(scrollControl, BorderLayout.LINE_END);

        add(actionWindow, BorderLayout.CENTER);
        add(inputArea, BorderLayout.SOUTH);

        setPreferredSize(dimension);
    }

    public void sendInput(String inputText) {
        // This limits the # of inputs sent to the server for nore more than 1 per 50 milliseconds to prevent
        // overloading the server and lagging people.
        long now = System.currentTimeMillis();
        if (now - lastInput >= 50L) {
            lastInput = now;
            Client.getInstance().write(inputText);
        }
    }

    public void clear() throws BadLocationException {
        int len = textArea.getDocument().getLength();
        textArea.getDocument().remove(0, len);
    }

    public InputField getInputField() {
        return this.input;
    }

    public JScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public SmartScroller getScroller() {
        return this.scroller;
    }

    public GUITextArea getTextArea() {
        return this.textArea;
    }

    public void getPreviousInput() {
        try {
            if (inputIndex == inputBuffer.size() - 1) {
                inputIndex = -1;
                input.setText("");
            } else {
                inputIndex++;
                String previousAction = inputBuffer.get(inputIndex);
                input.setText(previousAction);
                input.selectAll();
            }
        } catch (IndexOutOfBoundsException e) {
            // My cheap check that input buffer size is 0
        }
    }

    public void getNextInput() {
        try {
            if (inputIndex == 0) {
                inputIndex = -1;
            } else if (inputIndex < 0) {
                inputIndex = inputBuffer.size() - 1;
            } else {
                inputIndex--;
            }
            String previousAction;
            if (inputIndex < 0) {
                previousAction = "";
            } else {
                previousAction = inputBuffer.get(inputIndex);
            }
            input.setText(previousAction);
            input.selectAll();
        } catch (IndexOutOfBoundsException e) {
            // My cheap check that input buffer size is 0
        }
    }

    private void addToInputBuffer(String inputText) {
        if (inputText.equals("")) {
            return;
        }
        if (inputBuffer.size() > 0) { // Dedupe the consecutive identical commands in the input buffer list
            String previousAction = inputBuffer.get(0);
            if (inputText.equals(previousAction)) {
                return;
            }
        }
        if (inputBuffer.size() == inputBufferMax) {
            inputBuffer.remove(inputBufferMax-1);
        }
        inputBuffer.add(0, inputText);
    }

    private void addNumPadDirections() {
        logger.info("detected OS: " + System.getProperty("os.name"));

        input.addKeyListener(new KeyAdapter() {

            private boolean isFunctionKey(KeyEvent e) {
                Integer[] functionKeys = {VK_F1, VK_F2, VK_F3, VK_F4, VK_F5, VK_F6, VK_F7, VK_F8,
                        VK_F9, VK_F10, VK_F11, VK_F12};
                Set<Integer> F_KEYS = new HashSet<>(Arrays.asList(functionKeys));
                return F_KEYS.contains(e.getKeyCode());
            }

            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (CONSUME_KEY_EVENT) {
                    keyEvent.consume();
                    CONSUME_KEY_EVENT = false;
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                String numpadInput = SNEAKING ? "sneak " : "";

                if ((keyEvent.isControlDown() || isFunctionKey(keyEvent)) && Client.getInstance().isLoggedIn()) {
                    Object macro = ClientConfiguration.getInstance().getMacro(keyEvent.getKeyCode());
                    if (macro != null && !macro.equals("")) {
                        String command = (String) macro;
                        EnterMacro e = new EnterMacro(command);
                        Timer t = new Timer();
                        t.schedule(e, 0);
                        keyEvent.consume();
                    }
                    if (!isFunctionKey(keyEvent)) {
                        CONSUME_KEY_EVENT = true;
                    }
                    return;
                }

                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        input.setText("");
                        break;
                    case KeyEvent.VK_UP:
                        getPreviousInput();
                        break;
                    case KeyEvent.VK_DOWN:
                        getNextInput();
                        break;
                    case KeyEvent.VK_NUMPAD0:
                        sendInput("look");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD1:
                        sendInput(numpadInput + "sw");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD2:
                        sendInput(numpadInput + "s");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD3:
                        sendInput(numpadInput + "se");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        scrollDown(false);
                        break;
                    case KeyEvent.VK_NUMPAD4:
                        sendInput(numpadInput + "w");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD5:
                        SNEAKING = !SNEAKING;
                        if (SNEAKING) {
                            Client.getInstance().getGui().getActionWindow()
                                    .writeToActionWindow("You are now auto-sneaking.\n\n");
                        } else {
                            Client.getInstance().getGui().getActionWindow()
                                    .writeToActionWindow("You are no longer auto-sneaking.\n\n");
                        }
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD6:
                        sendInput(numpadInput + "e");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD7:
                        sendInput(numpadInput + "nw");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD8:
                        sendInput(numpadInput + "n");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_NUMPAD9:
                        sendInput(numpadInput + "ne");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        scrollUp(false);
                        break;
                    case KeyEvent.VK_SUBTRACT:
                        if (keyEvent.getKeyLocation() == 4) {
                            sendInput(numpadInput + "up");
                            CONSUME_KEY_EVENT = true;
                        }
                        break;
                    case KeyEvent.VK_ADD:
                        if (keyEvent.getKeyLocation() == 4) {
                            sendInput(numpadInput + "down");
                            CONSUME_KEY_EVENT = true;
                        }
                        break;
                    case KeyEvent.VK_MULTIPLY:
                        if (keyEvent.getKeyLocation() != 4) {
                            break;
                        }
                        numpadInput = SNEAKING ? "sneak " : "go ";
                        sendInput(numpadInput + "exit");
                        CONSUME_KEY_EVENT = true;
                        break;
                    case KeyEvent.VK_DECIMAL:
                        sendInput("hide");
                        CONSUME_KEY_EVENT = true;
                        break;
                    default:
                        super.keyPressed(keyEvent);
                        break;
                }

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                // do nothing
            }

        });
    }

    public void writeToActionWindow(String data) {
        actionLogger.info(data);
        textArea.appendANSI(data);
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

    private String MRB =
                    "#++++++++++++++++''''''''''''';;;;;;;;;;;:\n" +
                    "###+++++++++++++++''''''''''''';;;;;;;;;;;\n" +
                    "++++++++++++++++++'''''''''';;;;;;;;;;;;;:\n" +
                    "++++++++++''''++++++''';;;;;;;;;::::::::::\n" +
                    "++++++++'''''+++######+';;;;::::::::,,,,,,\n" +
                    "++++'+++'''+++++##++++++';;;::::,,,,,,,,,,\n" +
                    "''''''+#+'+++####+''';'+##+'::,,,,,,,,,,,,\n" +
                    "'''''''+#++#+###++''';;'#@##':,,,,,,,,,,,.\n" +
                    "''''''''######@@#+++';;;+##++:,,,,,,,,,,..\n" +
                    "''''''';+##@@@@@#++''';;'++;+':,,,,.......\n" +
                    "''';;;;;'+###@@@@#+'''';;'';'+;,.,,.......\n" +
                    "''';;;;'++'+##@@@#+''++';'';;+':,.........\n" +
                    ";;;;;'+###'+#+#@@@+'''+';;';:'+;,.........\n" +
                    ";;;;'+####+++#+#@@+';''';;;::'+;,.........\n" +
                    ";;'+###+##+++#++#++''''+';;:,'+;,,:::,,...\n" +
                    ";'#@###+##+++####''+#';'++'::++;:;;:,,...`\n" +
                    "'+#@@###@@++++###+''++';'++;;#+;;;:,.....`\n" +
                    "#####+##@@#'+#++##+;;'+';;;'''''':,.....``\n" +
                    "@#+++++####++++++##+;:;'+;;+':;;':.......`\n" +
                    "#++++++++####++++##++'::'++':,::':.......`\n" +
                    "+'++#++++++###+'+++''#'::'+;,:;'':......``\n" +
                    "+++##++++++#@#+'';;'+##+;'+;;''':.......``\n" +
                    "#####++++####@#''';;;'##++++#':,,......```\n" +
                    "#@##+'##@#####@+'+'::;''++++#',........``.\n" +
                    "@@+'++#######@#+''+;:'';++';'+:........```\n" +
                    "@#''+#@###++#@#+''';;'';;;;;'#+:,.....````\n" +
                    "#+'+###++#++####+'''';'';:;'###+;...``````\n" +
                    "#++###+''+#####@#';''++#;:;;+#+#',.```````\n" +
                    "++#+'++'''+####@#';;+#++':;;'++#',.```````\n" +
                    "'++;;+';;;'+####@+;;;+''';;''+#@+:.`.`````\n" +
                    ";';;'+';;;;''++#@#';;;;'+''++++##;..``````\n" +
                    ":;;'+#+';;;;;;;'+#+;;;:'++++##+##'.`````.`\n" +
                    ";;'+@#';;;;;::::;+#';;;''+++##++#'.````.``\n" +
                    "''+##+;''';;:::::'##+';'''++##++#'..``````\n" +
                    "++#@#+'++'';:,,,:+##+''+';+####+#'.```````\n" +
                    "##@@##++#+';::,:;+#+'+++;;+####+#'..``...`\n" +
                    "@@@@###+++';;;;;'##++##+;:'####+#',`......\n" +
                    "@@@@@#++'';;;'''++++##+';:;#+++'#',.......\n";

}
