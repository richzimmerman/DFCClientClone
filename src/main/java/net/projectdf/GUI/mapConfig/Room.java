package net.projectdf.GUI.mapConfig;

import org.json.JSONObject;

public class Room {

    private String mapFile;
    private int x;
    private int y;

    public Room(String json) {
        JSONObject JSON = new JSONObject(json);
        this.mapFile = JSON.getString("mapFile");
        this.x = JSON.getInt("x");
        this.y = JSON.getInt("y");
    }

    public String getMap() {
        return this.mapFile != null ? "/maps/" + this.mapFile : null;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
