package net.projectdf.Logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientLogger {

    private static FileHandler clientLog;
    private static SimpleFormatter clientLogFormatter;

    public static void setup() throws IOException {

        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        String logName = "client.log";

        clientLog = new FileHandler(logName);
        clientLogFormatter = new SimpleFormatter();
        clientLog.setFormatter(clientLogFormatter);
        logger.addHandler(clientLog);

    }

}
