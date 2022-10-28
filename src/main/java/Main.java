import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final Config config = ConfigFactory.load("application.conf");

    public static void main(String[] args) {
        Integer port = config.getInt("server.port");
        Integer workerCount = config.getInt("server.worker-count");

        Server.builder()
                .workerCount(workerCount)
                .port(port)
                .build()
                .serve();
    }
}
