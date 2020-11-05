package net.projectdf.TimerTasks;

import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Logger;
import net.projectdf.GMCP.GMCPActions;

public class Ping extends TimerTask {
    /*
    The purpose of this class is to handle broken connections sooner. We're not worried about a response, but a broken
    socket will not throw an exception until an attempt to send data to the server happens. This is better than relying
    on the player to enter a command... if they're AFK, this will initiate handling the disconnect for them
     */

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void run() {
        try {
            // TODO: this might need to be an actual command since ping stops after awhile
            GMCPActions.sendPing();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

}
