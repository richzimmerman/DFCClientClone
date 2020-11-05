package net.projectdf.GMCP;

import net.projectdf.Client.Client;

import java.io.IOException;

public class GMCPActions {
    // TODO: These might need to be requests to call them in succession. Look into that

    public static void sendPing() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.core_ping.name());
    }

    public static void getCharVitals() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_vitals.name());
    }

    public static void getCharTimer() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_timer.name());
    }

    public static void getCharStatusVars() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_statusvars.name());
    }

    public static void getCharStatus() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_status.name());
    }

    public static void getCharBase() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_base.name());
    }

    public static void getCharMaxStats() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_maxstats.name());
    }

    public static void getCharWorth() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_worth.name());
    }

    public static void getCharItemsInv() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_items_inv.name());
    }

    public static void getCharItemsContents() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_items_contents.name());
    }

    public static void getCharSkills() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.char_skills_get.name());
    }

    public static void getGroup() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.group.name());
    }

    public static void getRoom() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.room_info.name());
    }

    public static void getRoomItemsInv() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.room_items_inv.name());
    }

    public static void getRoomItemsContents() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.room_items_contents.name());
    }

    public static void getRoomMobiles() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.room_mobiles.name());
    }

    public static void getRoomPlayers() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.room_players.name());
    }

    public static void getCommChannel() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.comm_channel.name());
    }

    public static void getCommChannelPlayers() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.comm_channel_players.name());
    }

    public static void charRefresh() throws IOException {
        getCharStatus();
        getCharBase();
//        getCharWorth();
        getCharVitals();
    }

    public static void getIdols() throws IOException {
        Client.sendGMCP(GMCPSupports.GMCPCommand.allIdols.name());
    }

}
