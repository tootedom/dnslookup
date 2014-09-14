package org.greencheek.dns.lookup;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;

import static org.junit.Assert.*;

public class EnumeratedIPV4InterfaceAddressFinderTest {


    @Test
    public void testReturnsAddress() {
        IPV4InterfaceAddressFinder finder = new EnumeratedIPV4InterfaceAddressFinder();

        InetAddress inet = finder.getAddress();

        assertNotNull("Are you sure you have a configure network interface with connectivity?",inet);

        assertTrue(inet instanceof Inet4Address);
    }

}