package net.projectdf.Logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ActionLogger {

    private static ActionLogger instance;

    private static Pattern ansi = Pattern.compile("\\u001B\\[[0-9]+(;[0-9]+)?m");

    public static boolean logging = false;

    private static Logger logger;

    private FileHandler actionLog;
    private InGameLoggingFormatter actionFormatter;

    private ActionLogger() {
        logger = Logger.getLogger("ActionLogger");
        logger.setLevel(Level.INFO);
        actionFormatter = new InGameLoggingFormatter();
        toggle();
    }

    public static ActionLogger getInstance() {
        if (instance == null) {
            instance = new ActionLogger();
        }
        return instance;
    }

    public void info(String data) {
        if (logging) {
            String sanitizedData = sanitizeANSI(data);
            logger.info(sanitizedData);
        }
    }

    public void toggle() {
        logging = !logging;
        if (logging) {
            long millis = System.currentTimeMillis();
            String actionLogName = String.format("PDF-%d.log", millis);
            try {
                actionLog = new FileHandler(actionLogName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            actionLog.setFormatter(actionFormatter);
            logger.addHandler(actionLog);
        } else {
            clearHandlers();
        }
    }

    private String sanitizeANSI(String data) {
        // This removes the ANSI characters from logs
        return data.replaceAll(ansi.toString(), "");
    }

    private void clearHandlers() {
        // Clear handlers for when initiating a new log.
        Handler[] handlers = logger.getHandlers();
        for (Handler h : handlers) {
            h.close();
            logger.removeHandler(h);
        }
    }
}
