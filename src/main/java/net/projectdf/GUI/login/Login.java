package net.projectdf.GUI.login;

import net.projectdf.GUI.utils.Encoder;

public class Login {

    // TODO: Add save to cfg.
    private static String accountName = "Rieldfc";
    private static String password = "somedonkus1234";

    private static boolean loggedIn;

    public static void reset() {
        accountName = null;
        password = null;
    }

    public static String getAccountName() {
        return Encoder.get(accountName);
//        return accountName;
    }

    public static String getPassword() {
        return Encoder.get(password);
//        return password;
    }

    public static void setAccountName(String s) {
        accountName = Encoder.encode(s);
    }

    public static void setPassword(String s) {
        password = Encoder.encode(s);
    }

    public static boolean loggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean b) {
        loggedIn = b;
    }

}
