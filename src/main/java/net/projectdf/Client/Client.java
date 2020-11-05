package net.projectdf.Client;

import java.net.SocketException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.login.Login;
import net.projectdf.GUI.uiComponents.ConnectionLost;
import net.projectdf.Telnet.TelnetWrapper;

import java.io.IOException;
import net.projectdf.TimerTasks.Ping;

public class Client {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private int TESTING = 0;

    // Make this a singleton so we can reference it within the GUI for sending commands and stuff
    private static Client instance = null;

    private static String SERVER;
    private static int PORT = 4000;

    private StandardGUI gui;
    private TelnetWrapper telnet;
    private LocalHost localHost;

    private boolean panic;
    private String panicMsg;

    private StringBuffer sb;

    public Client(StandardGUI gui) throws FailedLoginException {
        panic = false;
        panicMsg = "";

        sb = new StringBuffer();

        if (TESTING != 0) {
            SERVER = "localhost";
        } else {
            SERVER = "olmran.net";
        }

        Client.instance = this;
        this.gui = gui;
        this.gui.showWindow();

        telnet = new TelnetWrapper();
        try {
            telnet.connect(SERVER, PORT);

            startGameLoop();
        } catch (IOException e) { // Blanket exception handling sucks, but this is for logging purposes
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static Client getInstance() {
        return instance;
    }

    public StandardGUI getGui() {
        return this.gui;
    }

    private void startGameLoop() throws IOException, FailedLoginException {
        // Start a ping timer so if the socket is broken, it is handled potentially before the user has to enter a command
        Ping ping = new Ping();
        Timer t = new Timer();
        t.scheduleAtFixedRate(ping, 0, 5000);

        byte[] buffer;
        while(true) {
//            if (!gui.isVisible() && Login.loggedIn()) {
//                gui.showWindow(); // TODO: change this. StandardGUI should only show on player logged in, not account logged in
//            }
            buffer = new byte[2048];
            int b = telnet.read(buffer);

            if (panic) {
                throw new FailedLoginException(panicMsg);
            }

//            validateBuffer(b, buffer);
            for (int i = 0; i < b; i++) {
                sb.append((char) buffer[i]);
            }

            if (sb.length() > 0) {
                String msg = sb.toString();
                if (msg.charAt(0) == 91 && Character.isDigit(msg.charAt(1))) {
                    // For some reason the ANSI escape is sometimes missing from some messages
                    // This is a total bandaid, likely caused by read buffer being too small.
                    // This may not even be an issue now that the buffer is 4096.
                    logger.info("received malformed ANSI character"); // For monitoring purposes...
                    msg = "\u001B" + msg;
                }
                this.gui.getActionWindow().writeToActionWindow(msg);

                sb.delete(0, sb.length());
            }
        }
    }

//    private void validateBuffer(int length, byte[] buffer) {
//        boolean unhandledGMCP = false;
//        if (length == 1 && buffer[0] == 0) {
//            // Not sure why these null buffers are being sent from the server but this prevents them from
//            // writing to the client
//            return;
//        }
//        for (int i = 0; i < length; i++) {
//            if (buffer[i] < 0) {
//                logger.info("received an invalid byte");
//                unhandledGMCP = true;
//                break;
//            } // TODO: GMCP messages coming without ending bytes. need to add to new buffer, end them, handle them and continue
//            sb.append((char) buffer[i]);
//        }
//        if (unhandledGMCP) {
//            telnet.getHandler().validateAndNegotiateGMCP(buffer); // not sure if this will work
//            sb.delete(0, sb.length());
//        }
//    }

    public void write(String value) {
        try {
            telnet.send(value);
        }  catch (SocketException e) {
            ConnectionLost.getInstance().setVisible(true);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void disconnect() {
        if (localHost != null) {
            logger.info("disconnect stopping sync");
            localHost.stop();
        }
        try {
            logger.info("attempting to log out gracefully");
            write("logout");
            Thread.sleep(100);
            write("y");
            telnet.disconnect();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void sendGMCP(String data) throws IOException {
        getInstance().telnet.getHandler().sendGMCP(data);
    }

    public void startLocalHost() {
        localHost = LocalHost.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                localHost.start();
            }
        });
        t.start();
    }

    public void stopLocalHost() {
        if (localHost != null) {
            localHost.stop();
        }
    }

    public void sendAccountName() {
        write(Login.getAccountName());
    }

    public void sendPassword() {
        write(Login.getPassword());
    }

    public void loggedIn(boolean b) {
        Login.setLoggedIn(b);
    }

    public boolean isLoggedIn() {
        return Login.loggedIn();
    }

    public void panic(String msg) {
        panic = true;
        panicMsg = msg;
    }

}
