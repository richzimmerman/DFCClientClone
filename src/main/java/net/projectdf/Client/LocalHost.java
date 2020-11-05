package net.projectdf.Client;

import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LocalHost {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static LocalHost instance;

    private static final int[] PORTS = {46178, 50191, 54912};
    private static int boundPort = 0;
    private static boolean running = false;
    private HttpServer server;

    public static LocalHost getInstance() {
        if (instance == null) {
            instance = new LocalHost();
        }
        return instance;
    }

    public void start() {
        synchronized (this) {
            if (running) {
                return;
            }
            if (boundPort != 0) {
                try {
                    run(boundPort);
                    return;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }

            for (int i = 0; i < PORTS.length; i++) {
                try {
                    run(PORTS[i]);
                    boundPort = PORTS[i];
                    running = true;
                    break;
                } catch (BindException e) {
                    if (!checkPort(PORTS[i])) {
                        System.exit(1);
                    }
                } catch (IOException ex) {
                    logger.warning(ex.getMessage());
                }
            }
            if (server == null) {
                logger.warning("unable to bind a port!");
                System.exit(100);
            }
        }
    }

    private boolean checkPort(int port) {
        try {
            String endpoint = "http://127.0.0.1:" + port + "/ping";
            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            if (content.toString().equals("olmran")) {
                logger.warning("logged into another instance of the client already.");
                return false;
            }
        } catch (FileNotFoundException e) {
            // Do nothing. this is hitting a port that does not have a /ping API route
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return true;
    }

    public void stop() {
        server.stop(0);
        running = false;
    }

    public void run(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/ping", (e -> {
            String resp = "olmran";
            e.sendResponseHeaders(200, resp.getBytes().length);
            OutputStream output = e.getResponseBody();
            output.write(resp.getBytes());
            output.flush();
            e.close();
        }));
        server.setExecutor(null);
        server.start();
    }
}
