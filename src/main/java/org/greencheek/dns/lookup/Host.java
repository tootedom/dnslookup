package org.greencheek.dns.lookup;

/**
 * Represents a memcached host
 */
public class Host {
    private final String host;
    private final int port;

    public Host(String server, int port) {
        this.host = server;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return host + ":" + port;
    }
}
