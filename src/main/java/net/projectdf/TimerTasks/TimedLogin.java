package net.projectdf.TimerTasks;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.Client.Client;
import net.projectdf.GMCP.GMCPActions;

import java.io.IOException;
import java.util.TimerTask;
import net.projectdf.Account.Player;

public class TimedLogin extends TimerTask {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public TimedLogin() {

    }

    @Override
    public void run() {
        try {
            GMCPActions.charRefresh();
            GMCPActions.getIdols();
            GMCPActions.getRoom();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        do {
            Client.getInstance().getGui().getUserDisplay().getControl().getStatsField().updateCharacterPortrait();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                logger.info("TimedLogin has been interrupted");
            }
        } while (Player.getInstance().getRace() == null || Client.getInstance().getGui().getUserDisplay().getControl()
                .getCharacter().getCurrentPortrait() == null || !Player.getInstance().getRace().equals(Client.getInstance().getGui().getUserDisplay().getControl()
                .getCharacter().getCurrentPortrait()));

    }

}
