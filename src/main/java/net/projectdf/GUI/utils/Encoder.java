package net.projectdf.GUI.utils;

import java.util.Base64;

public class Encoder {

    public static String get(String encoded) {
        byte[] e = Base64.getDecoder().decode(encoded);
        return new String(e);
    }

    public static String encode(String decoded) {
        String e = Base64.getEncoder().encodeToString(decoded.getBytes());
        return e;
    }
}
