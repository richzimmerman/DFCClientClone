package net.projectdf.TimerTasks;

import java.util.TimerTask;
import net.projectdf.Client.Client;

public class EnterMacro extends TimerTask {

    private String[] commands;
    private boolean enterLast;

    public EnterMacro(String macro) {
        this.commands = macro.split("&");
        enterLast = macro.trim().endsWith("&");
    }

    @Override
    public void run() {
        int last = commands.length - 1;
        for (int i = 0; i < commands.length; i++) {
            if (i == last && !enterLast) {
                Client.getInstance().getGui().getActionWindow().getInputField().setText(commands[i]);
                Client.getInstance().getGui().getActionWindow().getInputField().setCaretPosition(commands[i].length());
                continue; // This just bypasses the 100 ms sleep.. not that important
            }
            Client.getInstance().write(commands[i].trim());
            try {
                Thread.sleep(75L);
            } catch (InterruptedException e) {
                // pass
            }
        }
    }
}
