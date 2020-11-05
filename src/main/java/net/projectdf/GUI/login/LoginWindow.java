package net.projectdf.GUI.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;

import static net.projectdf.GUI.StandardGUI.getImage;

public class LoginWindow extends JFrame {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static LoginWindow instance;

    private LoginPrompt loginPrompt;

    public LoginWindow() {
        instance = this;

        setTitle("ProjectDF");

        Image icon = getImage(GUIConstantsConfig.ICON);
        setIconImage(icon);

        ContentPane contentPane = new ContentPane();
        setContentPane(contentPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(639, 479));
        setMaximumSize(new Dimension(639, 479));

        pack();
    }

    public static LoginWindow getInstance() {
        if (instance == null) {
            instance = new LoginWindow();
        }
        return instance;
    }

    private void loginPrompt() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    loginPrompt = LoginPrompt.getInstance();
                    loginPrompt.showWindow();
                    instance.addWindowFocusListener(new WindowAdapter() {
                        public void windowGainedFocus(WindowEvent e) {
                            loginPrompt.toFront();
                        }
                    });

                    instance.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentMoved(ComponentEvent componentEvent) {
                            super.componentMoved(componentEvent);
                            loginPrompt.setLocationRelativeTo(instance);
                            loginPrompt.toFront();
                        }
                    });
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        });
        t.start();
    }

    public void showWindow() {
        setLocationRelativeTo(null);
        setVisible(true);
        loginPrompt();
    }

    public void hideWindow() {
        setVisible(false);
    }

}
