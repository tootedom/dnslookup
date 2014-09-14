package org.greencheek.dns.lookup;

import java.net.InetAddress;

/**
 * Returns the InetAddress for an interface, ipv4, on the current machine
 */
public interface IPV4InterfaceAddressFinder {

    /**
     * Returns an ipv4 address that is not 127.0.0.1
     * @return
     */
    InetAddress getAddress();
}
