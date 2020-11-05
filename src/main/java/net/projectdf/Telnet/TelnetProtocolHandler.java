package net.projectdf.Telnet;


import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.projectdf.GMCP.GMCPHandlers;
import net.projectdf.GMCP.GMCPSupports;

import java.io.IOException;
import java.awt.Dimension;
import java.lang.Byte;

/**
 * This is a telnet protocol handler. The handler needs implementations
 * for several methods to handle the telnet options and to be able to
 * read and write the buffer.
 * <P>
 * <B>Maintainer:</B> Marcus Meissner
 *
 * @version $Id$
 * @author  Matthias L. Jugel, Marcus Meissner
 */
public abstract class TelnetProtocolHandler {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /** debug level */
    private final static int debug = 0;

    /** temporary buffer for data-telnetstuff-data transformation */
    private byte[] tempbuf = new byte[0];

    /** the data sent on pressing <RETURN>  \n */
    private byte[] crlf = new byte[2];
    /** the data sent on pressing <LineFeed>  \r */
    private byte[] cr = new byte[2];

    /**
     * Create a new telnet protocol handler.
     */
    public TelnetProtocolHandler() {
        reset();

        crlf[0] = 13; crlf[1] = 10;
        cr[0] = 13; cr[1] = 0;
    }

    /**
     * Get the current terminal type for TTYPE telnet option.
     * @return the string id of the terminal
     */
    protected abstract String getTerminalType();

    /**
     * Get the current window size of the terminal for the
     * NAWS telnet option.
     * @return the size of the terminal as Dimension
     */
    protected abstract Dimension getWindowSize();

    /**
     * Set the local echo option of telnet.
     * @param echo true for local echo, false for no local echo
     */
    protected abstract void setLocalEcho(boolean echo);

    /**
     * Generate an EOR (end of record) request. For use by prompt displaying.
     */
    protected abstract void notifyEndOfRecord();

    /**
     * Send data to the remote host.
     * @param b array of bytes to send
     */
    protected abstract void write(byte[] b) throws IOException;

    /**
     * Send one byte to the remote host.
     * @param b the byte to be sent
     * @see #write(byte[] b)
     */
    private static byte[] one = new byte[1];
    private void write(byte b) throws IOException {
        one[0] = b;
        write(one);
    }

    /**
     * Reset the protocol handler. This may be necessary after the
     * connection was closed or some other problem occured.
     */
    public void reset() {
        neg_state = 0;
        receivedDX = new byte[256];
        sentDX = new byte[256];
        receivedWX = new byte[256];
        sentWX = new byte[256];
    }

    // ===================================================================
    // the actual negotiation handling for the telnet protocol follows:
    // ===================================================================

    /** state variable for telnet negotiation reader */
    private byte neg_state = 0;

    /** constants for the negotiation state */
    private final static byte STATE_DATA  = 0;
    private final static byte STATE_IAC   = 1;
    private final static byte STATE_IACSB = 2;
    private final static byte STATE_IACWILL       = 3;
    private final static byte STATE_IACDO = 4;
    private final static byte STATE_IACWONT       = 5;
    private final static byte STATE_IACDONT       = 6;
    private final static byte STATE_IACSBIAC      = 7;
    private final static byte STATE_IACSBDATA     = 8;
    private final static byte STATE_IACSBDATAIAC  = 9;
    private final static byte STATE_IACGMCPDATA = 10;

    /** What IAC SB <xx> we are handling right now */
    private byte current_sb;

    /** String to prepend to GMCP data for easier parsing */
    public static final String GMCP = "GMCP:";

    /** current SB negotiation buffer */
    private byte[] sbbuf;
    private byte[] subnegotiation; // to be passed to handle_sb();
    private int subnegOffset = 0;
    private int outOffset = 0;

    /** IAC - init sequence for telnet negotiation. */
    private final static byte IAC  = (byte)255;
    /** [IAC] End Of Record */
    private final static byte EOR  = (byte)239;
    /** [IAC] WILL */
    private final static byte WILL  = (byte)251;
    /** [IAC] WONT */
    private final static byte WONT  = (byte)252;
    /** [IAC] DO */
    private final static byte DO    = (byte)253;
    /** [IAC] DONT */
    private final static byte DONT  = (byte)254;
    /** [IAC] Sub Begin */
    private final static byte SB  = (byte)250;
    /** [IAC] Sub End */
    private final static byte SE  = (byte)240;
    /** Telnet option: binary mode */
    private final static byte TELOPT_BINARY= (byte)0;  /* binary mode */
    /** Telnet option: echo text */
    private final static byte TELOPT_ECHO  = (byte)1;  /* echo on/off */
    /** Telnet option: sga */
    private final static byte TELOPT_SGA   = (byte)3;  /* supress go ahead */
    /** Telnet option: End Of Record */
    private final static byte TELOPT_EOR   = (byte)25;  /* end of record */
    /** Telnet option: Negotiate About Window Size */
    private final static byte TELOPT_NAWS  = (byte)31;  /* NA-WindowSize*/
    /** Telnet option: Terminal Type */
    private final static byte TELOPT_TTYPE  = (byte)24;  /* terminal type */
    /** Telnet custom option: GMCP */
    private final static byte TELOPT_GMCP  =  (byte)201;

    private final static byte CUSTOM_CLIENT_BYTE = (byte)222;

    private final static byte[] IACWILL  = { IAC, WILL };
    private final static byte[] IACWONT  = { IAC, WONT };
    private final static byte[] IACDO    = { IAC, DO      };
    private final static byte[] IACDONT  = { IAC, DONT };
    private final static byte[] IACSB  = { IAC, SB };
    private final static byte[] IACSBGMCP = { IAC, SB, TELOPT_GMCP };
    private final static byte[] IACSE  = { IAC, SE };

    /** Telnet option qualifier 'IS' */
    private final static byte TELQUAL_IS = (byte)0;
    /** Telnet option qualifier 'SEND' */
    private final static byte TELQUAL_SEND = (byte)1;

    /** What IAC DO(NT) request do we have received already ? */
    private byte[] receivedDX;
    /** What IAC WILL/WONT request do we have received already ? */
    private byte[] receivedWX;
    /** What IAC DO/DONT request do we have sent already ? */
    private byte[] sentDX;
    /** What IAC WILL/WONT request do we have sent already ? */
    private byte[] sentWX;

    /**
     * Handle an incoming IAC SB &lt;type&gt; &lt;bytes&gt; IAC SE
     * @param type type of SB
     * @param sbdata byte array as &lt;bytes&gt;
     */
    private void handle_sb(byte type, byte[] sbdata)
            throws IOException {
        if(debug > 1)
            System.err.println("TelnetIO.handle_sb("+type+")");
        switch (type) {
            case TELOPT_TTYPE:
                if (sbdata.length>0 && sbdata[0]==TELQUAL_SEND) {
                    /* FIXME: need more logic here if we use
                     * more than one terminal type
                     */
                    String ttype = "ANSI";
//                    if(ttype == null) ttype = "dumb";
                    byte[] b = new byte[2+1+1+ttype.getBytes().length+2];
                    b[0] = IAC;
                    b[1] = SB;
                    b[2] = TELOPT_TTYPE;
                    b[3] = TELQUAL_IS;
                    System.arraycopy(ttype.getBytes(),0,b,4,ttype.getBytes().length);
                    b[4+ttype.getBytes().length] = IAC;
                    b[5+ttype.getBytes().length] = SE;
                    write(b);
                }
                break;
            case TELOPT_GMCP:
                String data = new String(sbdata);
                if (data.split("_").length > 1) {
                    data = data.replaceFirst("_", ".");
                }
//                System.out.println(data);
                GMCPHandlers.handleGMCP(data);
                break;
            default:
                System.err.println("Unexpected SB " + type);
                break;
        }
    }

    // TODO: handle buffers with ending bytes in the middle of the buffer
//    public void validateAndNegotiateGMCP(byte[] buffer) {
//        /** This method will evaluate the provided buffer, looking for malformed GMCP messages.
//         * If the beginning 3 bytes are not a GMCP subnegotiation, it will be discarded. The ending bytes will also
//         * be added if need be, and then renegotiated()ed as a valid telnet subnegotiation.
//         */
//        int length = findLength(buffer);
//        if (length == 3) {
//            // If buffer only contains the 3 escape bytes and no other data, discard. Might be unnessecary;
//            return;
//        }
//
//        byte[] start = Arrays.copyOfRange(buffer, 0, 3);
//        // Grab the last 2 bytes in the array so we can see if they're properly closed subnegotiations
//        byte[] end = Arrays.copyOfRange(buffer, length-2, length);
//
//        if (!Arrays.equals(start, IACSBGMCP)) {
//            // This buffer doesn't start with a subnegotiation escape, we will discard it for now.
//            return;
//        }
//
//        byte[] newBuffer;
//        if (!Arrays.equals(end, IACSE)) {
//            // If ending byte is IAC then only add SE, otherwise add IACSE and negotiate.
//            if (end[1] == -1) {
//                newBuffer = addBytes(buffer, new byte[]{ SE });
//            } else {
//                newBuffer = addBytes(buffer, IACSE);
//            }
//        } else {
//            newBuffer = Arrays.copyOfRange(buffer, 0, length);
//        }
//
//        try {
//            negotiate(newBuffer);
//        } catch (IOException e) {
//            logger.log(Level.SEVERE, e.getMessage(), e);
//        }
//    }

//    private int findLength(byte[] buffer) {
//        int length = buffer.length;
//        for (int i = 0; i < buffer.length; i++) {
//            if (buffer[i] == 0) {
//                length = i;
//                break;
//            }
//        }
//        return length;
//    }

    /**
     * Do not send any notifications at startup. We do not know,
     * whether the remote client understands telnet protocol handling,
     * so we are silent.
     * (This used to send IAC WILL SGA, but this is false for a compliant
     *  client.)
     */
    public void startup() throws IOException {
    }
    /**
     * Transpose special telnet codes like 0xff or newlines to values
     * that are compliant to the protocol. This method will also send
     * the buffer immediately after transposing the data.
     * @param buf the data buffer to be sent
     */
    public void transpose(byte[] buf) throws IOException {
        int i;

        byte[] nbuf,xbuf;
        int nbufptr=0;
        nbuf = new byte[buf.length*2]; // FIXME: buffer overflows possible

        for (i = 0; i < buf.length ; i++) {
            switch (buf[i]) {
                // Escape IAC twice in stream ... to be telnet protocol compliant
                // this is there in binary and non-binary mode.
                case IAC:
                    nbuf[nbufptr++]=IAC;
                    nbuf[nbufptr++]=IAC;
                    break;
                // We need to heed RFC 854. LF (\n) is 10, CR (\r) is 13
                // we assume that the Terminal sends \n for lf+cr and \r for just cr
                // linefeed+carriage return is CR LF */
                case 10:	// \n
                    if (receivedDX[TELOPT_BINARY + 128 ] != DO) {
                        while (nbuf.length - nbufptr < crlf.length) {
                            xbuf = new byte[nbuf.length*2];
                            System.arraycopy(nbuf,0,xbuf,0,nbufptr);
                            nbuf = xbuf;
                        }
                        for (int j=0;j<crlf.length;j++)
                            nbuf[nbufptr++]=crlf[j];
                        break;
                    } else {
                        // copy verbatim in binary mode.
                        nbuf[nbufptr++]=buf[i];
                    }
                    break;
                // carriage return is CR NUL */
                case 13:	// \r
                    if (receivedDX[TELOPT_BINARY + 128 ] != DO) {
                        while (nbuf.length - nbufptr < cr.length) {
                            xbuf = new byte[nbuf.length*2];
                            System.arraycopy(nbuf,0,xbuf,0,nbufptr);
                            nbuf = xbuf;
                        }
                        for (int j=0;j<cr.length;j++)
                            nbuf[nbufptr++]=cr[j];
                    } else {
                        // copy verbatim in binary mode.
                        nbuf[nbufptr++]=buf[i];
                    }
                    break;
                // all other characters are just copied
                default:
                    nbuf[nbufptr++]=buf[i];
                    break;
            }
        }
        xbuf = new byte[nbufptr];
        System.arraycopy(nbuf,0,xbuf,0,nbufptr);
        write(xbuf);
    }

    public void setCRLF(String xcrlf) { crlf = xcrlf.getBytes(); }
    public void setCR(String xcr) { cr = xcr.getBytes(); }

    /**
     * Handle telnet protocol negotiation. The buffer will be parsed
     * and necessary actions are taken according to the telnet protocol.
     * See <A HREF="RFC-Telnet-URL">RFC-Telnet</A>
     * @param nbuf the byte buffer put out after negotiation
     * @return number of bytes processed, 0 for none, and -1 for end of buffer.
     */
//    public int negotiate(byte nbuf[])
//            throws IOException
//    {
//        int count = tempbuf.length;
//        byte[] buf = tempbuf;
//        byte sendbuf[] = new byte[3];
//        byte b,reply;
//        int boffset = 0, noffset = 0;
//        boolean dobreak = false;
//
//        if (count == 0) 	// buffer is empty.
//            return -1;
//
//        while(!dobreak && (boffset < count) && (noffset < nbuf.length)) {
//            b=buf[boffset++];
//            // of course, byte is a signed entity (-128 -> 127)
//            // but apparently the SGI Netscape 3.0 doesn't seem
//            // to care and provides happily values up to 255
//            if (b>=128)
//                b=(byte)((int)b-256);
//            if(debug > 2) {
//                Byte B = new Byte(b);
//                System.err.print("byte: " + B.intValue()+ " ");
//            }
//            switch (neg_state) {
//                case STATE_DATA:
//                    if (b == IAC) {
//                        neg_state = STATE_IAC;
////                        dobreak = true; // leave the loop so we can sync.
//                    } else {
//                        nbuf[noffset++] = b;
//                    }
//                    break;
//                case STATE_IAC:
//                    switch (b) {
//                        case IAC:
//                            if(debug > 2) System.err.print("IAC ");
//                            neg_state = STATE_DATA;
//                            nbuf[noffset++]=IAC;
//                            break;
//                        case WILL:
//                            if(debug > 2) System.err.print("WILL ");
//                            neg_state = STATE_IACWILL;
//                            break;
//                        case WONT:
//                            if(debug > 2) System.err.print("WONT ");
//                            neg_state = STATE_IACWONT;
//                            break;
//                        case DONT:
//                            if(debug > 2) System.err.print("DONT ");
//                            neg_state = STATE_IACDONT;
//                            break;
//                        case DO:
//                            if(debug > 2) System.err.print("DO ");
//                            neg_state = STATE_IACDO;
//                            break;
//                        case EOR:
//                            if(debug > 1) System.err.print("EOR ");
//                            notifyEndOfRecord();
//                            dobreak = true; // leave the loop so we can sync.
//                            neg_state = STATE_DATA;
//                            break;
//                        case SB:
//                            if(debug > 2) System.err.print("SB ");
//                            neg_state = STATE_IACSB;
//                            break;
//                        case SE:
//                            dobreak = true;
//                            break;
//                        default:
//                            if(debug > 2) System.err.print("<UNKNOWN "+b+" > ");
//                            neg_state = STATE_DATA;
//                            break;
//                    }
//                    break;
//                case STATE_IACWILL:
//                    switch(b) {
//                        case TELOPT_ECHO:
//                            if(debug > 2) System.err.println("ECHO");
//                            reply = DONT;
////                            setLocalEcho(false);
//                            break;
//                        case TELOPT_SGA:
//                            if(debug > 2) System.err.println("SGA");
//                            reply = DO;
//                            break;
//                        case TELOPT_EOR:
//                            if(debug > 2) System.err.println("EOR");
//                            reply = DO;
//                            break;
//                        case TELOPT_BINARY:
//                            if(debug > 2) System.err.println("BINARY");
//                            reply = DO;
//                            break;
//                        case TELOPT_GMCP:
//                        case CUSTOM_CLIENT_BYTE:
//                            reply = DO;
//                            break;
//                        default:
//                            if(debug > 2) System.err.println("<UNKNOWN,"+b+">");
//                            reply = DONT;
//                            break;
//                    }
//                    if(debug > 1) System.err.println("<"+b+", WILL ="+WILL+">");
//                    if (reply != sentDX[b+128] || WILL != receivedWX[b+128]) {
//                        sendbuf[0]=IAC;
//                        sendbuf[1]=reply;
//                        sendbuf[2]=b;
//                        write(sendbuf);
//                        sentDX[b+128] = reply;
//                        receivedWX[b+128] = WILL;
//                        if (b == TELOPT_GMCP) {
//                            addGMCPSupport();
//                        }
//                    }
//                    neg_state = STATE_DATA;
//                    break;
//                case STATE_IACWONT:
//                    switch(b) {
//                        case TELOPT_ECHO:
//                            if(debug > 2) System.err.println("ECHO");
////                            setLocalEcho(true);
//                            reply = DONT;
//                            break;
//                        case TELOPT_SGA:
//                            if(debug > 2) System.err.println("SGA");
//                            reply = DO;
//                            break;
//                        case TELOPT_EOR:
//                            if(debug > 2) System.err.println("EOR");
//                            reply = DONT;
//                            break;
//                        case TELOPT_BINARY:
//                            if(debug > 2) System.err.println("BINARY");
//                            reply = DONT;
//                            break;
//                        case CUSTOM_CLIENT_BYTE:
//                            reply = DO;
//                            break;
//                        default:
//                            if(debug > 2) System.err.println("<UNKNOWN,"+b+">");
//                            reply = DONT;
//                            break;
//                    }
//                    if(reply != sentDX[b+128] || WONT != receivedWX[b+128]) {
//                        sendbuf[0]=IAC;
//                        sendbuf[1]=reply;
//                        sendbuf[2]=b;
//                        write(sendbuf);
//                        sentDX[b+128] = reply;
//                        receivedWX[b+128] = WILL;
//                    }
//                    neg_state = STATE_DATA;
//                    break;
//                case STATE_IACDO:
//                    switch (b) {
//                        case TELOPT_ECHO:
//                            if(debug > 2) System.err.println("ECHO");
//                            reply = WONT;
////                            setLocalEcho(true);
//                            break;
//                        case TELOPT_SGA:
//                            if(debug > 2) System.err.println("SGA");
//                            reply = WILL;
//                            break;
//                        case TELOPT_TTYPE:
//                            if(debug > 2) System.err.println("TTYPE");
//                            reply = WILL;
//                            break;
//                        case TELOPT_BINARY:
//                            if(debug > 2) System.err.println("BINARY");
//                            reply = WILL;
//                            break;
//                        case TELOPT_NAWS:
//                            if(debug > 2) System.err.println("NAWS");
//                            Dimension size = getWindowSize();
//                            receivedDX[b] = DO;
//                            if(size == null) {
//                                byte wontbuf[] = { IAC, WONT, TELOPT_NAWS };
//                                // this shouldn't happen
//                                write(wontbuf);
//                                reply = WONT;
//                                sentWX[b] = WONT;
//                                break;
//                            }
//                            reply = WILL;
//                            sentWX[b] = WILL;
//                            sendbuf[0]=IAC;
//                            sendbuf[1]=WILL;
//                            sendbuf[2]=TELOPT_NAWS;
//                            write(sendbuf);
//                            byte nawsbuf[] = {
//                                    IAC,SB,TELOPT_NAWS,
//                                    (byte) (size.width >> 8),
//                                    (byte) (size.width & 0xff),
//                                    (byte) (size.height >> 8),
//                                    (byte) (size.height & 0xff),
//                                    IAC,SE };
//                            write (nawsbuf);
//                            break;
//                        default:
//                            if(debug > 2) System.err.println("<UNKNOWN,"+b+">");
//                            reply = WONT;
//                            break;
//                    }
//                    if(reply != sentWX[128+b] || DO != receivedDX[128+b]) {
//                        sendbuf[0]=IAC;
//                        sendbuf[1]=reply;
//                        sendbuf[2]=b;
//                        write(sendbuf);
//                        sentWX[b+128] = reply;
//                        receivedDX[b+128] = DO;
//                    }
//                    neg_state = STATE_DATA;
//                    break;
//                case STATE_IACDONT:
//                    switch (b) {
//                        case TELOPT_ECHO:
//                            if(debug > 2) System.err.println("ECHO");
//                            reply = WONT;
//                            setLocalEcho(false);
//                            break;
//                        case TELOPT_SGA:
//                            if(debug > 2) System.err.println("SGA");
//                            reply = WONT;
//                            break;
//                        case TELOPT_NAWS:
//                            if(debug > 2) System.err.println("NAWS");
//                            reply = WONT;
//                            break;
//                        case TELOPT_BINARY:
//                            if(debug > 2) System.err.println("BINARY");
//                            reply = WONT;
//                            break;
//                        default:
//                            if(debug > 2) System.err.println("<UNKNOWN,"+b+">");
//                            reply = WONT;
//                            break;
//                    }
//                    if(reply != sentWX[b+128] || DONT != receivedDX[b+128]) {
//                        byte replybuf[] = {IAC, reply, b};
//                        write(replybuf);
//                        sentWX[b+128] = reply;
//                        receivedDX[b+128] = DONT;
//                    }
//                    neg_state = STATE_DATA;
//                    break;
//                case STATE_IACSBIAC:
//                    if(debug > 2) System.err.println(""+b+" ");
//                    if (b == IAC) {
//                        sbbuf = new byte[0];
//                        current_sb = b;
//                        neg_state = STATE_IACSBDATA;
//                    } else {
//                        System.err.println("(bad) "+b+" ");
//                        neg_state = STATE_DATA;
//                    }
//                    break;
//                case STATE_IACSB:
//                    if(debug > 2) System.err.println(""+b+" ");
//                    switch (b) {
//                        case IAC:
//                            neg_state = STATE_IACSBIAC;
//                            break;
//                        default:
//                            current_sb = b;
//                            sbbuf = new byte[0];
//                            neg_state = STATE_IACSBDATA;
//                            break;
//                    }
//                    break;
//                case STATE_IACSBDATA:
//                    if (debug > 2) System.err.println(""+b+" ");
//                    switch (b) {
//                        case IAC:
//                            neg_state = STATE_IACSBDATAIAC;
//                            break;
//                        default:
//                            byte[] xsb = new byte[sbbuf.length+1];
//                            System.arraycopy(sbbuf,0,xsb,0,sbbuf.length);
//                            sbbuf = xsb;
//                            sbbuf[sbbuf.length-1] = b;
//                            break;
//                    }
//                    break;
//                case STATE_IACSBDATAIAC:
//                    if (debug > 2) System.err.println(""+b+" ");
//                    switch (b) {
//                        case IAC:
//                            neg_state = STATE_IACSBDATA;
//                            byte[] xsb = new byte[sbbuf.length+1];
//                            System.arraycopy(sbbuf,0,xsb,0,sbbuf.length);
//                            sbbuf = xsb;
//                            sbbuf[sbbuf.length-1] = IAC;
//                            break;
//                        case SE:
//                            handle_sb(current_sb,sbbuf);
//                            current_sb = 0;
//                            neg_state = STATE_DATA;
//                            break;
//                        case SB:
//                            neg_state = STATE_IACSB;
//                            break;
//                        default:
//                            neg_state = STATE_DATA;
//                            break;
//                    }
//                    break;
//                default:
//                    if (debug > 1)
//                        System.err.println("This should not happen: "+neg_state+" ");
//                    neg_state = STATE_DATA;
//                    break;
//            }
//        }
//        // shrink tempbuf to new processed size.
//        byte[] xb = new byte[count-boffset];
//        System.arraycopy(tempbuf,boffset,xb,0,count-boffset);
//        tempbuf = xb;
//        return noffset;
//    }

    public int negotiates(byte[] outBuffer, byte[] inData) throws IOException {
        byte[] optResponse = new byte[3];
        byte reply;
        if (subnegotiation == null) {
            subnegotiation = new byte[inData.length];
        }
        outOffset = 0;
        boolean exit = false;
        for (int i = 0; i < inData.length; i++) {
            byte b = inData[i];
            if (b == 0) {
                exit = true;
            }
            if (exit) {
                break;
            }
            switch(neg_state) {
                case STATE_DATA:
                    if (b == IAC) {
                        neg_state = STATE_IAC;
                    } else {
                        outBuffer[outOffset] = b;
                        outOffset++;
                    }
                    break;
                case STATE_IAC:
                    switch(b) {
                        case IAC:
                            neg_state = STATE_DATA;
                            outBuffer[i] = IAC;
                            break;
                        case WILL:
                            neg_state = STATE_IACWILL;
                            break;
                        case WONT:
                            neg_state = STATE_IACWONT;
                            break;
                        case DONT:
                            neg_state = STATE_IACDONT;
                            break;
                        case DO:
                            neg_state = STATE_IACDO;
                            break;
                        case EOR:
                            // End of record. CoffeeMUD doesn't use this so we should be ok.
                            break;
                        case SB:
                            neg_state = STATE_IACSB;
                            break;
                        case SE:
                            exit = true;
                            break;
                        default:
                            neg_state = STATE_DATA;
                            break;
                    }
                    break;
                case STATE_IACWILL:
                    switch(b) {
                        case TELOPT_ECHO:
                        case TELOPT_EOR:
                            reply = DONT;
                            break;
                        case TELOPT_BINARY:
                        case TELOPT_GMCP:
                        case CUSTOM_CLIENT_BYTE:
                            reply = DO;
                            break;
                        default:
                            reply = DONT;
                            break;
                    }
                    optResponse[0] = IAC;
                    optResponse[1] = reply;
                    optResponse[2] = b;
                    write(optResponse);
                    if (b == TELOPT_GMCP) {
                        addGMCPSupport();
                    }
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACWONT:
                    switch(b) {
                        case CUSTOM_CLIENT_BYTE:
                            reply = DO;
                            break;
                        default:
                            reply = DONT;
                            break;
                    }
                    optResponse[0]=IAC;
                    optResponse[1]=reply;
                    optResponse[2]=b;
                    write(optResponse);
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACDO:
                    switch (b) {
//                        case TELOPT_ECHO:
//                            reply = WONT;
//                            break;
//                        case TELOPT_TTYPE:
//                            reply = WONT;
//                            break;
//                        case TELOPT_BINARY:
//                            reply = WONT;
//                            break;
//                        case TELOPT_NAWS:
////                            Dimension size = getWindowSize();
////                            if(size == null) {
//                            reply = WONT;
//                            break;
//                            }
//                            reply = WILL;
//                            optResponse[0]=IAC;
//                            optResponse[1]=WILL;
//                            optResponse[2]=TELOPT_NAWS;
//                            write(optResponse);
//                            byte nawsbuf[] = {
//                                    IAC,SB,TELOPT_NAWS,
//                                    (byte) (size.width >> 8),
//                                    (byte) (size.width & 0xff),
//                                    (byte) (size.height >> 8),
//                                    (byte) (size.height & 0xff),
//                                    IAC,SE };
//                            write(nawsbuf);
//                            break;
                        default:
                            reply = WONT;
                            break;
                    }
                    optResponse[0]=IAC;
                    optResponse[1]=reply;
                    optResponse[2]=b;
                    write(optResponse);
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACDONT:
                    switch (b) {
                        default:
                            reply = WONT;
                            break;
                    }
                    byte replybuf[] = {IAC, reply, b};
                    write(replybuf);
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACSB:
                    neg_state = STATE_IACSBDATA;
                    break;
                case STATE_IACSBDATA:
                    switch(b) {
                        case IAC:
                            neg_state = STATE_IACSBDATAIAC;
                            break;
                        default:
                            subnegotiation[subnegOffset] = b;
                            subnegOffset++;
                            break;
                    }
                    break;
                case STATE_IACSBDATAIAC:
                    switch(b) {
                        case SE:
                            // TODO: handle_sb( subnegotiation );
                            handle_gmcp(subnegotiation, subnegOffset);
                            subnegOffset = 0;
                            neg_state = STATE_DATA;
                            break;
                        default:
                            neg_state = STATE_DATA;
                            break;
                    }
                    break;
                default:
                    neg_state = STATE_DATA;
                    break;
            }
        }
        if (outOffset == 0) {
            return -1;
        }
        // Return offset + 1 to indicate 'length' of outBuffer;
        return outOffset;
    }

    private void handle_gmcp(byte[] subData, int length) {
        byte[] copy = Arrays.copyOfRange(subData, 0, length);
        String data = new String(copy);
        if (data.split("_").length > 1) {
            data = data.replaceFirst("_", ".");
        }
        Arrays.fill(subnegotiation, (byte)0);
        GMCPHandlers.handleGMCP(data);
    }

//    public void inputfeed(byte[] b, int len) {
//        byte[] xb = new byte[tempbuf.length+len];
//
//        System.arraycopy(tempbuf,0,xb,0,tempbuf.length);
//        System.arraycopy(b,0,xb,tempbuf.length,len);
//        tempbuf = xb;
//    }

    public void sendGMCP(String data) throws IOException {
        byte[] input = data.getBytes();
        byte[] msg = new byte[3 + input.length + 2];
        msg[0]=IAC;
        msg[1]=SB;
        msg[2]=TELOPT_GMCP;
        msg = addBytes(msg, input);
        msg = addBytes(msg, new byte[]{IAC, SE});
        write(msg);
    }

    private void addGMCPSupport() throws IOException {
        // This method will add support for each GMCP message used
        final StringBuilder sb = new StringBuilder("core_supports_set [");
        for (int i = 0; i < GMCPSupports.supportedGMCPMessages.length; i++) {
            // why is this so ugly
            sb.append("\"").append(GMCPSupports.supportedGMCPMessages[i]).append("\"");
            if (i != GMCPSupports.supportedGMCPMessages.length -1) {
                sb.append(",");
            }
        }
        sb.append("]");
        sendGMCP(sb.toString());
    }

    private byte[] addBytes(byte[] a, byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
