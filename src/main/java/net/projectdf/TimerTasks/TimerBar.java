package net.projectdf.TimerTasks;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.Client.Client;

public class TimerBar extends TimerTask {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static long timerMillis;
    private static long remainingMillis;
    private static TimerBar instance;

    public TimerBar() {
    }

    @Override
    public void run() {
        while (remainingMillis > 1) {
            Client.getInstance().getGui().getUserDisplay().getControl().getTimer()
                    .updateStatusBar((int)timerMillis, (int)remainingMillis);
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }

            if (remainingMillis < 20L) {
                remainingMillis = 1;
            } else {
                remainingMillis -= 20L;
            }
        }
        // Set the timer bar 1 last time at 1
        Client.getInstance().getGui().getUserDisplay().getControl().getTimer()
                .updateStatusBar((int)timerMillis, (int)remainingMillis);
        Client.getInstance().getGui().getUserDisplay().getControl().setTimerRunning(false);
    }

    public static void setTimer(long millis) {
        timerMillis = millis;
        remainingMillis = millis;
    }

}
