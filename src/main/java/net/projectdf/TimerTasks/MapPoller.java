package net.projectdf.TimerTasks;

import java.util.TimerTask;
import net.projectdf.GUI.mapConfig.Mapper;

public class MapPoller extends TimerTask {

    public MapPoller() {

    }

    @Override
    public void run() {
        long currentUpdate = Mapper.getInstance().getLastUpdated();
        long lastUpdated = Mapper.getInstance().getMapLastUpdated();
        if (lastUpdated > currentUpdate) {
            Mapper.getInstance().updateMapData();
//            Mapper.getInstance().setLastUpdated(lastUpdated);
        }
    }
}
