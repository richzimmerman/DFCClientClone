package net.projectdf.GUI.mapConfig;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.GUI.uiComponents.Map;
import net.projectdf.TimerTasks.MapPoller;

public class Mapper {

    private HashMap<String, Object> mapData;
    private long lastUpdated;

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String MAP_DATA_ENDPOINT = "http://olmran.net:5252/"; //"http://localhost:5252/";

    private static Mapper instance;
    private static Timer timer;

    public Mapper() {
        instance = this;
        updateMapData();
        lastUpdated = System.currentTimeMillis();

        MapPoller poller = new MapPoller();
        timer = new Timer();
        timer.scheduleAtFixedRate(poller, 0, 60 * 1000);
    }

    public static Mapper getInstance() {
        if (instance == null) {
            new Mapper();
        }
        return instance;
    }

    public HashMap<String, Object> getMapData() {
        return mapData;
    }

    public void updateMapData() {
        try {
            URL url = new URL(MAP_DATA_ENDPOINT + "maps");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JsonParser parser = new JsonFactory().createParser(content.toString());
            mapData = new ObjectMapper().readValue(parser, HashMap.class);
            Map.updateMapData(mapData);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public long getMapLastUpdated() {
        try {
            URL url = new URL(MAP_DATA_ENDPOINT + "lastUpdated");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return Long.parseLong(content.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return Long.parseLong(null);
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long l) {
        lastUpdated = l;
    }

}
