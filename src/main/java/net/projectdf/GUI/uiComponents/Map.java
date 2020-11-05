package net.projectdf.GUI.uiComponents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import net.projectdf.GUI.Sprites.ControlSprites;
import net.projectdf.GUI.StandardGUI;
import net.projectdf.GUI.mapConfig.Mapper;
import net.projectdf.GUI.mapConfig.Room;
import org.json.JSONObject;

public class Map extends JPanel {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static HashMap<String, Object> mapData;
    private String currentMap;
    private BufferedImage background;
    private Image scaledBackground;
    private Image img;
    private int x = 0;
    private int y = 0;
    private static int width = 0;
    private static int height = 0;

    private MapPlayerDot playerDot;

    public Map() {
        setOpaque(false);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setFocusable(false);

        background = StandardGUI.getImage(GUIConstantsConfig.MAP_PARCHMENT);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;

        playerDot = new MapPlayerDot(ControlSprites.MAP_POINTER);
        add(playerDot, constraints);

        mapData = Mapper.getInstance().getMapData();
    }

    private void validateBackground() {
        if ( (width == 0 || height == 0) || (width != getWidth() || height != getHeight()) ){
            width = getWidth();
            height = getHeight();
            scaledBackground = background.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO: Try painting subimage at 0,0 and find the correct dimensions to center box. bitch
        super.paintComponent(g);
        validateBackground();
        g.drawImage(scaledBackground, 0, 0, null);
        if (img != null) {
            int centerX = this.getWidth() / 2;
            int centerY = this.getHeight() / 2;

            BufferedImage buff = (BufferedImage) img;

            int xOffset = 0;
            int yOffset = 0;

            int leftX = (x - centerX);
            int topY = (y - centerY);
            int rightX = (x + centerX);
            int bottomY = (y + centerY);

            int imgX = leftX;
            int imgY = topY;

            if (leftX < 0) {
                xOffset += Math.abs(leftX);
                imgX = 0;
            }
            if (topY < 0) {
                yOffset += Math.abs(topY);
                imgY = 0;
            }
            if (rightX > buff.getWidth()) {
                xOffset -= Math.abs(rightX - buff.getWidth());
                imgX += xOffset;
            }
            if (bottomY > buff.getHeight()) {
                yOffset -= Math.abs(bottomY - buff.getHeight());
                imgY += yOffset;
            }
            BufferedImage section = buff.getSubimage(imgX, imgY, this.getWidth(), this.getHeight());

            g.drawImage(section, xOffset, yOffset, this);
        }
    }

    public static void updateMapData(HashMap<String, Object> data) {
        mapData = data;
    }

    private void setMap(String map) {
        if (map != null && currentMap != null) {
            if (currentMap.equals(map)) {
                return;
            }
        }
        currentMap = map;
        if (map == null) {
            img = null;
            playerDot.setInMap(false);
            return;
        }

        playerDot.setInMap(true);
        img = StandardGUI.getImage(map.replace(".png", ".bmp"));
    }

    public void updateMap(String json) {
        synchronized (this) {
            return;
//            // TODO: uncomment once we have maps; Might be an NPE somewhere... we'll see later
//            JSONObject JSON = new JSONObject(json);
//            String roomId = JSON.getString("id");
//
//            ObjectMapper mapper = new ObjectMapper();
//            Object roomData = mapData.get(roomId);
//            String roomJSON = null;
//            try {
//                roomJSON = mapper.writeValueAsString(roomData);
//            } catch (JsonProcessingException e) {
//                logger.log(Level.SEVERE, e.getMessage(), e);
//            }
//            Room room = new Room(roomJSON);
//            if (room.getMap() == null) {
//                setMap(null);
//            } else {
//                String mapFile = room.getMap();
//                x = room.getX();
//                y = room.getY();
//                setMap(mapFile);
//            }
//            repaint();
        }
    }

}
