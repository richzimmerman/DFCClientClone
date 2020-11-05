package net.projectdf;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.projectdf.Client.Client;
import net.projectdf.Client.FailedLoginException;
import net.projectdf.Configuration.ClientConfiguration;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.account.AccountWindow;
import net.projectdf.GUI.login.LoginWindow;
import net.projectdf.GUI.mapConfig.Mapper;
import net.projectdf.GUI.uiComponents.GUIConstantsConfig;
import net.projectdf.GUI.uiComponents.OutDatedClientAlert;
import net.projectdf.Logging.ClientLogger;


public class App {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static LoginWindow loginWindow;
    private static AccountWindow accountWindow;
    private static Client client;
    private static StandardGUI standardGui;

    private static JFrame currentWindow;

    public static void main(String[] args) {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font fixedSys = getFont(GUIConstantsConfig.DEFAULT_FONT);
            Font ubuntu = getFont(GUIConstantsConfig.UBUNTU_FONT);
            env.registerFont(fixedSys);
            env.registerFont(ubuntu);

            ClientLogger.setup();
            logger.info("Running version: " + GUIConstantsConfig.version);

            new Mapper(); // Pull in map data

            new ClientConfiguration();

            loginWindow = new LoginWindow();
            standardGui = new StandardGUI();
            accountWindow = new AccountWindow();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!OutDatedClientAlert.currentVersion()) {
                        new OutDatedClientAlert();
                    }
                }
            });
            t.start();

             while (true) {
//               1. create login window here.
                 loginWindow.showWindow();
                 currentWindow = loginWindow;
                 Thread.sleep(500);
                 synchronized (loginWindow) {
                     loginWindow.wait();
                 }
//               2. char selection screen here

                 try {
                     loginWindow.hideWindow();
                     // TODO: Clear out the action window textarea immediately before logging on to a character
                     // Create the connection and sign in with credentials from loginWindow
                     client = new Client(standardGui);
                     //currentWindow = accountWindow;
                     // Show the account selection window
                     //accountWindow.showWindow();
                     // TODO: trigger next window based on GMCP trigger.
                     standardGui.showWindow();

                 } catch (FailedLoginException ex) {
                     JOptionPane.showMessageDialog(LoginWindow.getInstance(), ex.getMessage());
                 } catch (Exception e) {
                     logger.log(Level.SEVERE, e.getMessage(), e);
                 } finally {
                     if (client != null) {
                         client.disconnect();
                     }
                     standardGui.hideWindow();
                     loginWindow.showWindow();
                 }
             }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static Font getFont(final String filePath) {
        final InputStream font = App.class.getResourceAsStream(filePath);
        try {
            return Font.createFont(Font.TRUETYPE_FONT, font);
        } catch (IOException | FontFormatException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public static JFrame getCurrentWindow() {
        return currentWindow;
    }

}
