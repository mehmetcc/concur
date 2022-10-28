package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private final Integer workerCount;

    private final Integer port;

    private final ExecutorService pool;

    Server(final Integer workerCount, final Integer port) {
        this.workerCount = workerCount;
        this.port = port;

        this.pool = Executors.newFixedThreadPool(this.workerCount);

        logger.info("Starting server on port: {}", port);
    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

    public void serve() {
        final ServerSocket server = tryServerSocket(this.port);
        while (true) {
            final ServerSocketWorker worker = new ServerSocketWorker(server);

            pool.execute(worker);
        }
    }

    private ServerSocket tryServerSocket(final Integer port) {
        ServerSocket socket = null; // lmao java is soo bad
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            logger.warn("Failed to create a new socket.");
            logger.warn(e);
        }

        return socket;
    }

    public Integer getWorkerCount() {
        return workerCount;
    }

    public Integer getPort() {
        return port;
    }
}
