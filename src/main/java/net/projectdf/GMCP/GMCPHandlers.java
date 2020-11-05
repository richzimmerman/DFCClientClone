package net.projectdf.GMCP;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import net.projectdf.Account.Account;
import net.projectdf.Client.Client;
import net.projectdf.Account.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.json.JSONObject;

public class GMCPHandlers {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final static String validJson = "\\{([^}]*?)\\}";

    /**
     * Each method here is looked up by the GMCP header/package and called accordingly with the JSON string passed as a param
     * from the handleGMCP method (which may be null).
     *
     * The methods will then act upon the JSON - update UI components or whatever. Though a bunch seem unnecessary.
     */

    public static void charvitals(String json) {
        Player.getInstance().setVitals(json);
        Client.getInstance().getGui().getUserDisplay().getControl().updateVitals();
    }

    public static void chartimer(String json) {
        Client.getInstance().getGui().getUserDisplay().getControl().updateTimer(json);
    }

    public static void charstatusvars(String json) { // {"level":"130","race":"PlayerRace","guild":"Olmran Imperial Legion"}
        // TODO: Do we need this?
        System.out.println(json);
    }

    public static void charstatus(String json) {
//        System.out.println(json);
        Player.getInstance().setStatus(json.replace("pretitle\":\"*,", "pretitle\":\""));
        Client.getInstance().getGui().getUserDisplay().getControl().updateStats();
    }

    public static void charinfo(String json) {
        // JSON is not used but has to conform with how methods are invoked using reflection
        Player.getInstance().resetCharacter(); // This will instantiate the player class, causing it to ask the server for char info
    }

    public static void charbase(String json) {
        Player.getInstance().setBase(json.replace("pretitle\":\"*,", "pretitle\":\""));
        Client.getInstance().getGui().getUserDisplay().getControl().updateStats();
    }

    public static void charworth(String json) {
        Player.getInstance().setWealth(json);
    }

    public static void charitemslist(String json) { //{"location":"inv","items":[{"id":371693355,"name":"a knitted weapon belt","attrib":"wc",}]}
        // TODO
        System.out.println(json);
    }

    public static void xpupdate(String json) {
        Player.getInstance().setXpToLevel(json);
        Client.getInstance().getGui().getUserDisplay().getControl().updateStats();
    }

    public static void group(String json) { // {"groupname":"Riels group","leader":"Riel","status":"Private","count":1,"members":[{"name":"Riel",{"info":{"hp":930,"mhp":930,"mn":448,"mmn":448,"mv":1337,"mmv":1337,"lvl":130,"tnl":2147483647}]}
        // TODO
        System.out.println(json);
    }

    public static void allidols(String json) { // {"idols": [{"idolRealm":3,"idolType":2,"location":3},{"idolRealm":2,"idolType":3,"location":2},{"idolRealm":2,"idolType":1,"location":2},{"idolRealm":1,"idolType":3,"location":1},{"idolRealm":3,"idolType":3,"location":3},{"idolRealm":1,"idolType":1,"location":2},{"idolRealm":3,"idolType":1,"location":3},{"idolRealm":2,"idolType":2,"location":2},{"idolRealm":1,"idolType":2,"location":1},]}
        Client.getInstance().getGui().getUserDisplay().getControl().updateIdols(json);
    }

    public static void idolupdate(String json) {
        Client.getInstance().getGui().getUserDisplay().getControl().updateIdol(json);
    }

    public static void roominfo(String json) { // {"id":"Arachenlair Forest#28","zone":"Arachenlair Forest","exits":{"N":697515491,"S":697515492,"E":1130879151,"W":697515401}}
        Client.getInstance().getGui().getUserDisplay().getControl().updateDirectionsList(json);
        Client.getInstance().getGui().getUserDisplay().getMap().updateMap(json);
    }

    public static void roomitemslist(String json) { // {"location":"room","items":[{"id":881464052,"name":"an elegantly-sculpted granite archway","attrib":"c",}]}
        // TODO
        System.out.println(json);
    }

    // Room Mobs...
    public static void roommobiles(String json) { // {"an armadillo":"an armadillo","a large, grey tortoise":"a large, grey tortoise",}
        // TODO
        System.out.println(json);
    }

    public static void roomplayers(String json) { // {"Riel":"Riel,Maniacal Bombadier",}
        // TODO
        System.out.println(json);
    }

    public static void commquest(String json) { // {"action": "status", "status": "ready" }  Likely to change?
        // TODO
        System.out.println(json);
    }

    public static void commchannel(String json) {
        JSONObject JSON = new JSONObject(json);
        String channel = JSON.getString("chan");
        if (channel.equals("INFO") || channel.equals("WIZINFO")) {
            Client.getInstance().getGui().getActionWindow().writeToActionWindow(JSON.getString("msg")
                    .replaceAll("\n", " ").replaceAll("\r", "") + "\n");
        } else {
            String msg = JSON.getString("msg").replaceAll("\n", " ").replaceAll("\r", "");
            Client.getInstance().getGui().getChatWindow().writeToChatWindow(channel, msg);
        }
    }

    public static void switchbase(String json) {
        Player.getInstance().resetCharacter();
    }

    public static void coreping(String json) {
        // The server seems to stop responding to these after some time. We're using the ping message to catch broken
        // sockets sooner, so we're not concerned with handling the response if there is one
        return;
    }

    public static void accountprompt(String json) {
        Client.getInstance().sendAccountName();
    }

    public static void passwordprompt(String json) {
        Client.getInstance().sendPassword();
    }

    public static void loggedin(String json) {
        Client.getInstance().loggedIn(true);
        try {
            Client.getInstance().getGui().getActionWindow().clear();
        } catch (BadLocationException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public static void accountnotrecognized(String json) {
        Client.getInstance().panic("Account not found.");
    }

    public static void accountnotfound(String json) {
        Client.getInstance().panic("Account not found.");
        Client.getInstance().write("N");
    }

    public static void maximumplayers(String json) {
        Client.getInstance().panic("The maximum player limit has been reached for your IP address.");
    }

    public static void banned(String json) {
        Client.getInstance().panic("Your account is banned.");
    }

    public static void invalidpassword(String json) {
        Client.getInstance().panic("Invalid password.");
    }

    public static void pendinglogin(String json) {
        Client.getInstance().panic("A previous login is still pending. Please be patient.");
    }

    public static void loginspam(String json) {
        Client.getInstance().panic("Your address is temporarily blocked due to excessive invalid connections.");
    }

    public static void accountdata(String json) {
//        System.out.println(json);
//        Client.getInstance().notify();
        Account.getInstance().setAccountData(json);
    }

    public static void handleGMCP(String data) {
//        System.out.println(data);
        if (data == null || data.equals("")) {
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] jsonString = data.trim().split(" ", 2);
                String command = jsonString[0];
                String json = null;
                if (jsonString.length > 1) {
                    json = jsonString[1].trim();
                    // TODO: Fix this. match JSON.
//                    if (!json.equals("null") && !json.matches(validJson)) {
//                        logger.info("got invalid json: " + json);
//                        return;
//                    }
                }

                String[] commandParts = command.toLowerCase().split("\\.");
                String commandMethod = String.join("", commandParts);

                try {
                    Method method = GMCPHandlers.class.getMethod(commandMethod, String.class);
                    method.invoke(GMCPHandlers.class, json);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    logger.warning("unable to handle: " + data);
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        thread.start();
    }

}
