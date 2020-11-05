package net.projectdf.Telnet;

import java.io.IOException;
import java.awt.Dimension;
import java.util.Arrays;


/**
 * The telnet wrapper is a sample class for how to use the telnet protocol
 * handler of the JTA source package. To write a program using the wrapper
 * you may use the following piece of code as an example:
 * <PRE>
 *   TelnetWrapper telnet = new TelnetWrapper();
 *   try {
 *     telnet.connect(args[0], 23);
 *     telnet.login("user", "password");
 *     telnet.setPrompt("user@host");
 *     telnet.waitfor("Terminal type?");
 *     telnet.send("dumb");
 *     System.out.println(telnet.send("ls -l"));
 *   } catch(java.io.IOException e) {
 *     e.printStackTrace();
 *   }
 * </PRE>
 * Please keep in mind that the password is visible for anyone who can
 * download the class file. So use this only for public accounts or if
 * you are absolutely sure nobody can see the file.
 * <P>
 * <B>Maintainer:</B> Matthias L. Jugel
 *
 * @version $Id$
 * @author Matthias L. Jugel, Marcus Mei�ner
 */
public class TelnetWrapper extends Wrapper {
    protected TelnetProtocolHandler handler;

    /** debugging level */
    private final static int debug = 0;

    public TelnetWrapper() {
        handler = new TelnetProtocolHandler() {
            /** get the current terminal type */
            public String getTerminalType() {
                return "vt320";
            }

            /** get the current window size */
            public Dimension getWindowSize() {
                return new Dimension(80, 25);
            }

            /** notify about local echo */
            public void setLocalEcho(boolean echo) {
                /* EMPTY */
            }

            /** write data to our back end */
            public void write(byte[] b) throws IOException {
                out.write(b);
            }

            /** sent on IAC EOR (prompt terminator for remote access systems). */
            public void notifyEndOfRecord() {
            }
        };
    }

    public TelnetProtocolHandler getHandler() {
        return handler;
    }

    public void connect(String host, int port) throws IOException {
        super.connect(host, port);
        handler.reset();
    }

    /**
     * Send a command to the remote host. A newline is appended and if
     * a prompt is set it will return the resulting data until the prompt
     * is encountered.
     * @param cmd the command
     * @return output of the command or null if no prompt is set
     */
    public void send(String cmd) throws IOException {
        byte arr[];
        arr = (cmd + "\n").getBytes();
        handler.transpose(arr);
    }

    /**
     * Read data from the socket and use telnet negotiation before returning
     * the data read.
     * @param b the input buffer to read in
     * @return the amount of bytes read
     */
    public int read(byte[] b) throws IOException {
        /* process all already read bytes */
        int n = 0;
//        byte[] buff = new byte[b.length];
//        do {
//            n = handler.negotiate(b);
//            if (n>0)
//                return n;
//        } while (n==0);
//
//        while (n <= 0) {
//            do {
//                n = handler.negotiate(b);
//                if (n > 0)
//                    return n;
//            } while (n == 0);
//            n = in.read(b);
//            if (n < 0)
//                return n;
//            handler.inputfeed(b, n);
//            n = handler.negotiate(b);
//        }
//        return n;

        byte[] buff = new byte[b.length];

        while (n == 0) {
            // Read from socket into byte[] buff
            n = in.read(buff);
            if (n < 0) {
                return n;
            }
            // Go through negotiation, non subnegotiation bytes are put into byte[] b
            n = handler.negotiates(b, buff);
        }

        return n;
    }
}
