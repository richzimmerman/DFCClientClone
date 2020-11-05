package net.projectdf.GMCP;

import java.util.regex.Pattern;

public class GMCPSupports {

    public static String[] supportedGMCPMessages = {
            "core_ping",
            "comm.channel",
            "charTimer",
            "charInfo",
            "charVitals",
            "char_status",
            "char_base",
            "switch.base",
            "char_vitals",
            "char_worth",
            "room_info",
            "roomInfo",
            "xpUpdate",
            "allIdols",
            "idolUpdate",
            "char_statusvars",
            "char_status",
            "accountprompt",
            "passwordprompt",
            "loggedin",
            "accountnotfound",
            "maximumplayers",
            "banned",
            "invalidpassword",
            "pendinglogin",
            "loginspam",
            "accountnotrecognized",
            "accountdata",
    };

    /* GMCP "Headers" for various requests */
    public enum GMCPCommand {
        // TODO: trim the fat
        core_ping,
        char_vitals,
        charVitals,
        char_timer,
        charTimer,
        charInfo,
        char_statusvars,
        char_status,
        char_base,
        charbase,
        char_maxstats,
        char_worth,
        char_items_inv, // means they want updates, dude
        char_items_contents,
        char_skills_get,
        xpUpdate,
        group,
        allIdols,
        idolUpdate,
        room_info,
        roomInfo, // means they want room.wrongdir
        room_items_inv,
        room_items_contents,
        room_mobiles,
        room_players,
        comm_channel,
        comm_channel_players,

    }

    // TODO: this is fucking dumb
    public static Pattern[] GMCPPatterns = new Pattern[] {
            Pattern.compile(GMCPCommand.char_vitals.name().replace("_", ".")),
            Pattern.compile("charTimer"),
            Pattern.compile("charInfo"),
            Pattern.compile("charVitals"),
            Pattern.compile("comm.channel"),
            Pattern.compile(GMCPCommand.char_statusvars.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.char_status.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.char_base.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.charbase.name()),
            Pattern.compile(GMCPCommand.char_maxstats.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.char_worth.name().replace("_", ".")),
            Pattern.compile("char\\.items\\.list"),
            Pattern.compile(GMCPCommand.char_items_contents.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.char_skills_get.name().replace("_", ".")),
            Pattern.compile("xpUpdate"),
            Pattern.compile(GMCPCommand.allIdols.name()),
            Pattern.compile(GMCPCommand.idolUpdate.name()),
            Pattern.compile("roomInfo"),
            Pattern.compile(GMCPCommand.room_items_inv.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.room_items_contents.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.room_mobiles.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.room_players.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.comm_channel.name().replace("_", ".")),
            Pattern.compile(GMCPCommand.comm_channel_players.name().replace("_", ".")),
    };

}
