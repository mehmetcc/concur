package server;

public class ServerBuilder {
    private Integer workerCount = 64;
    private Integer port = 8080;

    ServerBuilder() {
    }

    public ServerBuilder workerCount(final Integer workerCount) {
        this.workerCount = workerCount;
        return this;
    }

    public ServerBuilder port(final Integer port) {
        this.port = port;
        return this;
    }

    public Server build() {
        return new Server(this.workerCount, this.port);
    }
}
