package net.projectdf.GUI.uiComponents;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import net.projectdf.GUI.Sprites.CharacterPortraits;
import net.projectdf.GUI.Sprites.ControlSprites;
import net.projectdf.GUI.StandardGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import net.projectdf.Account.Player;
import net.projectdf.TimerTasks.TimerBar;
import org.json.JSONArray;
import org.json.JSONObject;

public class Control extends JLayeredPane {

    private Image img;
    private CharStatsTextField stats;
    private CharacterPortraitComponent character;
    private StatusBar healthBar;
    private StatusBar fatBar;
    private StatusBar powerBar;
    private StatusBar timer;

    private static boolean timerRunning = false;
    private static TimerBar timerBarTask;

    private DirectionButton north;
    private DirectionButton east;
    private DirectionButton south;
    private DirectionButton west;
    private DirectionButton ne;
    private DirectionButton nw;
    private DirectionButton se;
    private DirectionButton sw;
    private DirectionButton up;
    private DirectionButton out;
    private DirectionButton down;

    private IdolIcon chaosStrength;
    private IdolIcon chaosKnowledge;
    private IdolIcon chaosPower;

    private IdolIcon goodStrength;
    private IdolIcon goodKnowledge;
    private IdolIcon goodPower;

    private IdolIcon evilStrength;
    private IdolIcon evilKnowledge;
    private IdolIcon evilPower;

    private Map<String, DirectionButton> directions;
    private Map<Integer, Map<Integer, IdolIcon>> idols;

    public Control() {
        setBackground(Color.BLACK);

        img = StandardGUI.getImage(GUIConstantsConfig.CONTROL);

        JPanel s = new JPanel();
        s.setOpaque(false);
        s.setBounds((int)(img.getWidth(this) * .34f),12,150,180);
        stats = new CharStatsTextField();
        s.add(stats);
        add(s, 1, 0);

        character = new CharacterPortraitComponent();
        character.setBounds((int)(img.getWidth(this) * .2f), 9, CharacterPortraits.getWidth(), CharacterPortraits.getHeight());
        add(character, 0, 0);

        healthBar = new StatusBar(ControlSprites.HEALTH_BAR);
        healthBar.setBounds(10, 9, ControlSprites.HEALTH_BAR.getWidth(), ControlSprites.HEALTH_BAR.getHeight());
        add(healthBar, 0, 0);

        fatBar = new StatusBar(ControlSprites.FAT_BAR);
        fatBar.setBounds(34, 9, ControlSprites.FAT_BAR.getWidth(), ControlSprites.FAT_BAR.getHeight());
        add(fatBar, 0, 0);

        powerBar = new StatusBar(ControlSprites.POWER_BAR);
        powerBar.setBounds(58, 9, ControlSprites.POWER_BAR.getWidth(), ControlSprites.POWER_BAR.getHeight());
        add(powerBar,0 ,0);

        timer = new StatusBar(ControlSprites.TIMER_BAR);
        timer.setBounds((int)(img.getWidth(this) * .68f), 92, ControlSprites.TIMER_BAR.getWidth(), ControlSprites.TIMER_BAR.getHeight());
        add(timer, 0, 0);

        timer.updateStatusBar(0, 0);

        north = new DirectionButton("north", ControlSprites.NORTH_AVAIL, ControlSprites.NORTH_UNAVAIL, ControlSprites.NORTH_CLICKED);
        north.setBounds(358, 21, ControlSprites.NORTH_AVAIL.getWidth(), ControlSprites.NORTH_AVAIL.getHeight());
        add(north, 1, 0);

        east = new DirectionButton("east", ControlSprites.EAST_AVAIL, ControlSprites.EAST_UNAVAIL, ControlSprites.EAST_CLICKED);
        east.setBounds(384, 48, ControlSprites.EAST_AVAIL.getWidth(), ControlSprites.EAST_AVAIL.getHeight());
        add(east, 1, 0);

        south = new DirectionButton("south", ControlSprites.SOUTH_AVAIL, ControlSprites.SOUTH_UNAVAIL, ControlSprites.SOUTH_CLICKED);
        south.setBounds(358, 74, ControlSprites.SOUTH_AVAIL.getWidth(), ControlSprites.SOUTH_AVAIL.getHeight());
        add(south, 1, 0);

        west = new DirectionButton("west", ControlSprites.WEST_AVAIL, ControlSprites.WEST_UNAVAIL, ControlSprites.WEST_CLICKED);
        west.setBounds(331, 48, ControlSprites.WEST_AVAIL.getWidth(), ControlSprites.WEST_AVAIL.getHeight());
        add(west, 1, 0);

        ne = new DirectionButton("ne", ControlSprites.NE_AVAIL, ControlSprites.NE_UNAVAIL, ControlSprites.NE_CLICKED);
        ne.setBounds(387, 31, ControlSprites.NE_AVAIL.getWidth(), ControlSprites.NE_AVAIL.getHeight());
        add(ne, 1, 0);

        nw = new DirectionButton("nw", ControlSprites.NW_AVAIL, ControlSprites.NW_UNAVAIL, ControlSprites.NW_CLICKED);
        nw.setBounds(341, 31, ControlSprites.NW_AVAIL.getWidth(), ControlSprites.NW_AVAIL.getHeight());
        add(nw, 1, 0);

        se = new DirectionButton("se", ControlSprites.SE_AVAIL, ControlSprites.SE_UNAVAIL, ControlSprites.SE_CLICKED);
        se.setBounds(387, 79, ControlSprites.SE_AVAIL.getWidth(), ControlSprites.SE_AVAIL.getHeight());
        add(se, 1, 0);

        sw = new DirectionButton("sw", ControlSprites.SW_AVAIL, ControlSprites.SW_UNAVAIL, ControlSprites.SW_CLICKED);
        sw.setBounds(341, 79, ControlSprites.SW_AVAIL.getWidth(), ControlSprites.SW_AVAIL.getHeight());
        add(sw, 1, 0);

        up = new DirectionButton("up", ControlSprites.UP_AVAIL, ControlSprites.UP_UNAVAIL, ControlSprites.UP_CLICKED);
        up.setBounds(428, 90, ControlSprites.UP_AVAIL.getWidth(), ControlSprites.UP_AVAIL.getHeight());
        add(up, 1, 0);

        out = new DirectionButton("out", ControlSprites.OUT_AVAIL, ControlSprites.OUT_UNAVAIL, ControlSprites.OUT_CLICKED);
        out.setBounds(428, 107, ControlSprites.OUT_AVAIL.getWidth(), ControlSprites.OUT_AVAIL.getHeight());
        add(out, 1, 0);

        down = new DirectionButton("down", ControlSprites.DOWN_AVAIL, ControlSprites.DOWN_UNAVAIL, ControlSprites.DOWN_UNAVAIL);
        down.setBounds(428, 125, ControlSprites.DOWN_AVAIL.getWidth(), ControlSprites.DOWN_AVAIL.getHeight());
        add(down, 1, 0);

        directions = new HashMap<String, DirectionButton>() {{
            put("N", north);
            put("NW", nw);
            put("NE", ne);
            put("E", east);
            put("W", west);
            put("S", south);
            put("SW", sw);
            put("SE", se);
            put("U", up);
            put("D", down);
        }};

        chaosStrength = new IdolIcon("chaosStrength", ControlSprites.IDOL_Str_C_C, ControlSprites.IDOL_Str_C_G, ControlSprites.IDOL_Str_C_E);
        chaosStrength.setBounds(372, 110, ControlSprites.IDOL_Str_C_C.getWidth(), ControlSprites.IDOL_Str_C_C.getHeight());
        add(chaosStrength, 1, 0);

        chaosKnowledge = new IdolIcon("chaosKnowledge", ControlSprites.IDOL_Know_C_C, ControlSprites.IDOL_Know_C_G, ControlSprites.IDOL_Know_C_E);
        chaosKnowledge.setBounds(364, 124, ControlSprites.IDOL_Know_C_C.getWidth(), ControlSprites.IDOL_Know_C_C.getHeight());
        add(chaosKnowledge, 1, 0);

        chaosPower = new IdolIcon("chaosPower", ControlSprites.IDOL_Pow_C_C, ControlSprites.IDOL_Pow_C_G, ControlSprites.IDOL_Pow_C_E);
        chaosPower.setBounds(357, 110, ControlSprites.IDOL_Pow_C_C.getWidth(), ControlSprites.IDOL_Pow_C_C.getHeight());
        add(chaosPower, 1, 0);

        goodStrength = new IdolIcon("goodStrength", ControlSprites.IDOL_Str_G_C, ControlSprites.IDOL_Str_G_G, ControlSprites.IDOL_Str_G_E);
        goodStrength.setBounds(404, 110, ControlSprites.IDOL_Str_G_C.getWidth(), ControlSprites.IDOL_Str_G_C.getHeight());
        add(goodStrength, 1, 0);

        goodKnowledge = new IdolIcon("goodKnowledge", ControlSprites.IDOL_Know_G_C, ControlSprites.IDOL_Know_G_G, ControlSprites.IDOL_Know_G_E);
        goodKnowledge.setBounds(396, 124, ControlSprites.IDOL_Know_G_C.getWidth(), ControlSprites.IDOL_Know_G_C.getHeight());
        add(goodKnowledge, 1, 0);

        goodPower = new IdolIcon("goodPower", ControlSprites.IDOL_Pow_G_C, ControlSprites.IDOL_Pow_G_G, ControlSprites.IDOL_Pow_G_E);
        goodPower.setBounds(389, 110, ControlSprites.IDOL_Pow_G_C.getWidth(), ControlSprites.IDOL_Pow_G_C.getHeight());
        add(goodPower, 1, 0);

        evilStrength = new IdolIcon("evilStrength", ControlSprites.IDOL_Str_E_C, ControlSprites.IDOL_Str_E_G, ControlSprites.IDOL_Str_E_E);
        evilStrength.setBounds(340, 110, ControlSprites.IDOL_Str_E_C.getWidth(), ControlSprites.IDOL_Str_E_C.getHeight());
        add(evilStrength, 1, 0);

        evilKnowledge = new IdolIcon("evilKnowledge", ControlSprites.IDOL_Know_E_C, ControlSprites.IDOL_Know_E_G, ControlSprites.IDOL_Know_E_E);
        evilKnowledge.setBounds(332, 124, ControlSprites.IDOL_Know_E_C.getWidth(), ControlSprites.IDOL_Know_E_C.getHeight());
        add(evilKnowledge, 1, 0);

        evilPower = new IdolIcon("evilPower", ControlSprites.IDOL_Pow_E_C, ControlSprites.IDOL_Pow_E_G, ControlSprites.IDOL_Pow_E_E);
        evilPower.setBounds(325, 110, ControlSprites.IDOL_Pow_E_C.getWidth(), ControlSprites.IDOL_Pow_E_C.getHeight());
        add(evilPower, 1, 0);

        idols = new HashMap<Integer, Map<Integer, IdolIcon>>() {{
            // realm first, then idol.
            // realms: Evils: 1, Chaos: 2, Good: 3
            // types: Strength: 1, Knowledge: 2, Power: 3
            put(1, new HashMap<Integer, IdolIcon>(){{
                put(1, evilStrength);
                put(2, evilKnowledge);
                put(3, evilPower);
            }});
            put(2, new HashMap<Integer, IdolIcon>(){{
                put(1, chaosStrength);
                put(2, chaosKnowledge);
                put(3, chaosPower);
            }});
            put(3, new HashMap<Integer, IdolIcon>(){{
                put(1, goodStrength);
                put(2, goodKnowledge);
                put(3, goodPower);
            }});
        }};

    }

    public CharStatsTextField getStatsField() {
        return stats;
    }

    public CharacterPortraitComponent getCharacter() {
        return character;
    }

    public StatusBar getHealthBar() {
        return healthBar;
    }

    public StatusBar getFatBar() {
        return fatBar;
    }

    public StatusBar getPowerBar() {
        return powerBar;
    }

    public StatusBar getTimer() {
        return timer;
    }

    public void updateDirectionsList(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);

            JSONObject exit = JSON.getJSONObject("exits");
            Set<String> directionsAvailable = exit.keySet();
            if (!directionsAvailable.isEmpty()) {
                for (Map.Entry<String, DirectionButton> entry : directions.entrySet()) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean exitAvailable = directionsAvailable.contains(entry.getKey());
                            entry.getValue().isAvailable(exitAvailable);
                        }
                    });
                    thread.start();
                }
            } else {
                for (Map.Entry<String, DirectionButton> entry : directions.entrySet()) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            entry.getValue().isAvailable(false);
                        }
                    });
                    thread.start();
                }
            }
        }
    }

    public void updateVitals() {
        synchronized (this) {
            Player p = Player.getInstance();
            getHealthBar().updateStatusBar(p.getMaxHP(), p.getCurrentHP());
            getPowerBar().updateStatusBar(p.getMaxPower(), p.getCurrentPower());
            getFatBar().updateStatusBar(p.getMaxFat(), p.getCurrentFat());
        }
    }

    public void updateStats() {
        synchronized (this) {
            getStatsField().refresh();
        }
    }

    public void updateIdol(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);
            int idolRealm = JSON.getInt("idolRealm");
            int idolType = JSON.getInt("idolType");
            int idolLocation = JSON.getInt("location");
            IdolIcon idol = idols.get(idolRealm).get(idolType);
            idol.changeLocation(idolLocation);
        }
    }

    public void updateIdols(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);

            JSONArray currentIdols = JSON.getJSONArray("idols");
            for (int i = 0; i < currentIdols.length(); i++) {
                JSONObject j = currentIdols.getJSONObject(i);
                int idolRealm = j.getInt("idolRealm");
                int idolType = j.getInt("idolType");
                int idolLocation = j.getInt("location");
                IdolIcon idol = idols.get(idolRealm).get(idolType);
                idol.changeLocation(idolLocation);
            }
        }
    }

    public void setTimerRunning(boolean running) {
        timerRunning = running;
    }


    public void updateTimer(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);
            long timerMillis = JSON.getLong("timer");

            TimerBar.setTimer(timerMillis);
            if (!timerRunning) {
                setTimerRunning(true);
                timerBarTask = new TimerBar();
                TimerBar.setTimer(timerMillis);
                Timer t = new Timer();
                t.schedule(timerBarTask, 0);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    public Image getImg() {
        return img;
    }

}
