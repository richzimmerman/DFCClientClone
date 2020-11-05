package net.projectdf.Account;

import java.util.logging.Logger;
import net.projectdf.Client.Client;
import net.projectdf.Client.LocalHost;
import net.projectdf.TimerTasks.TimedLogin;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class Player {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static Player instance = null;

    private String NAME;
    private String RACE; // Synonymous with the "subclass" attribute in game.
    private String GENDER;
    private String TITLE;
    private String GUILD;
    private int LEVEL;
    private int NEXT_LEVEL;
    private int XP_TO_LEVEL;
    private int REALM;
    private int currentHP;
    private int maxHP;
    private int currentPower;
    private int maxPower;
    private int currentFat;
    private int maxFat;
    private float gold;
    private int trainingPoints;
    private int practicePoints;
    private int questPoints;

    public Player() {
        getCharacterInfo();
        instance = this;
    }

    private void getCharacterInfo() {
        TimedLogin timedLogin = new TimedLogin();
        Timer timer = new Timer();
        timer.schedule(timedLogin, 500);
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void resetCharacter() {
        synchronized (this) {
            NAME = null;
            RACE = null;
            GENDER = null;
            TITLE = null;
            GUILD = null;
            getCharacterInfo();
        }
    }

    // JSON: {"gold":651.0,"qp":30,"trains":104,"pracs":305}
    public void setWealth(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);
            gold = JSON.getInt("gold");
            trainingPoints = JSON.getInt("trains");
            practicePoints = JSON.getInt("pracs");
            questPoints = JSON.getInt("qp");
        }
    }

    // JSON: {"hp":930,"maxhp":930,"mana":448,"maxmana":448,"moves":1337,"maxmoves":1337}
    public void setVitals(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);
            currentHP = JSON.getInt("hp");
            maxHP = JSON.getInt("maxhp");
            currentPower = JSON.getInt("mana");
            maxPower = JSON.getInt("maxmana");
            currentFat = JSON.getInt("moves");
            maxFat = JSON.getInt("maxmoves");
        }
    }

    // JSON: {"name":"Riel","class":"Archon","subclass":"Archon","race":"PlayerRace", "realm": 0, "perlevel":290098112,
    // "pretitle":"*,Maniacal Bombadier","clan":"Olmran Imperial Legion"}
    public void setBase(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);
            NAME = JSON.getString("name");
            RACE = JSON.getString("subclass");
            GENDER = JSON.getString("gender");
            REALM = JSON.getInt("realm");
            TITLE = JSON.getString("pretitle");
            GUILD = JSON.getString("clan");


            // This initiates the localhost to allow Archons to play on multiple clients and
            // limits non-Archons to a single client
            if (RACE.equals("Archon")) {
                Client.getInstance().stopLocalHost();
            } else {
                Client.getInstance().startLocalHost();
            }
        }
    }

    // JSON: {"level":130,"tnl":2147483647,"hunger":1000,"thirst":500,"fatigue":0,"state":3,"pos":"Standing"}
    public void setStatus(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);
            LEVEL = JSON.getInt("level");
            NEXT_LEVEL = LEVEL + 1;
            XP_TO_LEVEL = JSON.getInt("tnl");
            try {
                TITLE = JSON.getString("pretitle");
            } catch (JSONException e) {
                // Do nothing. base char_status does not send title. LVL up char_status does
                // to handle title changes on lvl up.
            }
        }
    }

    public void setXpToLevel(String json) {
        synchronized (this) {
            JSONObject JSON = new JSONObject(json);

            XP_TO_LEVEL = JSON.getInt("exp");
        }
    }

    public String getName() {
        return NAME;
    }

    public int getLevel() {
        return LEVEL;
    }

    public String getRace() {
        return RACE;
    }

    public String getTitle() {
        return TITLE;
    }

    public String getGuild() {
        return GUILD;
    }

    public int getRealm() {
        return REALM;
    }

    public String getGender() {
        return GENDER;
    }

    public int getXpToLevel() {
        return XP_TO_LEVEL;
    }

    public int getNextLevel() {
        return NEXT_LEVEL;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxFat() {
        return maxFat;
    }

    public int getCurrentFat() {
        return currentFat;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

}
