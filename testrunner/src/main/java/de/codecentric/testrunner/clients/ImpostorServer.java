package de.codecentric.testrunner.clients;

/**
 * Created by afitz on 24.03.16.
 */
public class ImpostorServer {

    private String host;

    private int port;

    public ImpostorServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
