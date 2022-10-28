package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

class ServerSocketWorker implements Runnable {
    private static final Logger logger = LogManager.getLogger(ServerSocketWorker.class);

    private final ServerSocket server;

    ServerSocketWorker(final ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        final Socket client = tryClientSocket(server);
        final InputStreamReader inputStreamReader = tryInputStreamReader(client);
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = tryLine(bufferedReader);

        while (!line.isEmpty()) {
            logger.info(line);
            line = tryLine(bufferedReader);
        }

        handleRequest(client);
        tryClientClose(client);
    }

    private void handleRequest(final Socket client) {
        final Date today = new Date();
        final String response = "HTTP/1.1 200 OK\r\n\r\n" + today;

        try {
            client.getOutputStream().write(response.getBytes("UTF-8"));
        } catch (IOException e) {
            logger.warn("Failed to create an output stream");
            logger.warn(e);
        }
    }

    private void tryClientClose(final Socket client) {
        try {
            client.close();
        } catch (IOException e) {
            logger.warn("Failed to close a client socket");
            logger.warn(e);
        }
    }

    private String tryLine(final BufferedReader reader) {
        String line = "";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            logger.warn("Failed to read from BufferedReader");
            logger.warn(e);
        }
        return line;
    }

    private InputStreamReader tryInputStreamReader(final Socket client) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(client.getInputStream());
        } catch (IOException e) {
            logger.warn("Failed to create an InputStreamReader from the client socket");
            logger.warn(e);
        }
        return reader;
    }

    private Socket tryClientSocket(final ServerSocket server) {
        Socket client = null;
        try {
            client = server.accept();
        } catch (IOException e) {
            logger.warn("Failed to accept a client socker");
            logger.warn(e);
        }
        return client;
    }
}
