package org.greencheek.dns;

import org.greencheek.dns.lookup.AddressByNameHostResolver;
import org.greencheek.dns.lookup.Host;
import org.greencheek.dns.lookup.HostResolver;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dominictootell on 10/09/2014.
 *
 * networkaddress.cache.ttl (default: -1)
 Specified in java.security to indicate the caching policy for successful
 name lookups from the name service. The value is specified as as integer
 to indicate the number of seconds to cache the successful lookup.

 A value of -1 indicates "cache forever".

 networkaddress.cache.negative.ttl (default: 10)
 Specified in java.security to indicate the caching policy for un-successful
 name lookups from the name service. The value is specified as as integer to
 indicate the number of seconds to cache the failure for un-successful lookups.

 A value of 0 indicates "never cache". A value of -1 indicates "cache forever".
 *
 * sun.net.inetaddr.ttl
 This is a sun private system property which corresponds to networkaddress.cache.ttl.
 It takes the same value and has the same meaning, but can be set as a command-line
 option. However, the preferred way is to use the security property mentioned above.

 sun.net.inetaddr.negative.ttl
 This is a sun private system property which corresponds to networkaddress.cache.negative.ttl.
 It takes the same value and has the same meaning, but can be set as a command-line option.
 However, the preferred way is to use the security property mentioned above.
 */
public class Output {

    private static final int pttl = Integer.getInteger("pttl",0);
    private static final int nttl = Integer.getInteger("nttl",10);

    static {
        java.security.Security.setProperty("networkaddress.cache.ttl", ""+pttl);
        java.security.Security.setProperty("networkaddress.cache.negative.ttl",""+nttl);
    }


    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final HostResolver hostResolver = new AddressByNameHostResolver();

    private static final String hostToLookup = System.getProperty("host","www.test.local.com");
    private static final int port = 11111;

    public static void main(String[] args) {
        final AtomicInteger num = new AtomicInteger(1);
        Host host = new Host(hostToLookup,port);
        executor.scheduleAtFixedRate(()-> {
            System.out.println(num.getAndIncrement() + ") " +host + " = " + hostResolver.returnSocketAddressesForHostNames(Collections.<Host>singletonList(host)));
            System.out.flush();
        }
        ,0,Integer.getInteger("millis",1000), TimeUnit.MILLISECONDS);

    }
}
