package org.greencheek.dns.lookup;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by dominictootell on 14/09/2014.
 */
public class EnumeratedIPV4InterfaceAddressFinder implements IPV4InterfaceAddressFinder {

    @Override
    public InetAddress getAddress() {

        InetAddress addressToUse = null;
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return addressToUse;

        }

        for (NetworkInterface interface_ : Collections.list(interfaces)) {
            try {
                // we shouldn't care about loopback addresses
                if (interface_.isLoopback())
                    continue;

                // if you don't expect the interface to be up you can skip this
                // though it would question the usability of the rest of the code
                if (!interface_.isUp())
                    continue;

                // iterate over the addresses associated with the interface
                Enumeration<InetAddress> addresses = interface_.getInetAddresses();
                for (InetAddress address : Collections.list(addresses)) {
                    // look only for ipv4 addresses
                    if (address instanceof Inet6Address)
                        continue;


                    System.out.format("ni: %s, ia: %s\n", interface_, address);

                    // stops at the first *working* solution
                    ServerSocket checkBind = new ServerSocket(1234,10,address);
                    addressToUse = address;
                    checkBind.close();
                }
                if(addressToUse!=null) break;
            } catch (SocketException e) {
                continue;
            } catch (IOException e) {
                continue;
            }
        }
        return addressToUse;
    }

}
