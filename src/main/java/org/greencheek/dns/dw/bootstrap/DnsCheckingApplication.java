package org.greencheek.dns.dw.bootstrap;

import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.greencheek.dns.dw.configuration.DnsCheckingConfiguration;
import org.greencheek.dns.dw.resources.DnsClientRequest;
import org.greencheek.dns.lookup.EnumeratedIPV4InterfaceAddressFinder;
import org.greencheek.dns.server.HttpHelloWorldServer;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dominictootell on 14/09/2014.
 */
public class DnsCheckingApplication extends Application<DnsCheckingConfiguration> {

    private static final int pttl = Integer.getInteger("pttl",10);
    private static final int nttl = Integer.getInteger("nttl",10);

    static {
        System.setProperty("sun.net.inetaddr.ttl",""+pttl);
        System.setProperty("sun.net.inetaddr.negative.ttl",""+nttl);
        java.security.Security.setProperty("networkaddress.cache.ttl", ""+pttl);
        java.security.Security.setProperty("networkaddress.cache.negative.ttl",""+nttl);
    }

    public static void main(String[] args) throws Exception {
        new DnsCheckingApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<DnsCheckingConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(DnsCheckingConfiguration config,
                    Environment environment) {

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


        InetAddress loopback = InetAddress.getLoopbackAddress();
        InetAddress otherInterface = new EnumeratedIPV4InterfaceAddressFinder().getAddress();

        if(otherInterface==null) throw new RuntimeException("unable to start, cannot find a second interface");

        HttpHelloWorldServer localHostServer = new HttpHelloWorldServer(loopback,config.getPort());
        HttpHelloWorldServer otherServer = new HttpHelloWorldServer(otherInterface,config.getPort());

        JerseyClientConfiguration jconfig = config.getJerseyClientConfiguration();

        // the is actually the time that connections can idle.
        jconfig.setKeepAlive(Duration.seconds(pttl));

        // This sets the max time the connections in the connection pool
        // are keep alive until they are recreated.
        //
        // a server can set Keep-Alive: timeout=<sec>
        // with a duration less than ttl to have the connect re-established earlier
        //
        // a server can still send Connection: close to have the connection re-established
        // on each request
        //
        jconfig.setTimeToLive(Duration.seconds(pttl));

        final Client client = new JerseyClientBuilder(environment).using(jconfig).build(getName());

        final DnsClientRequest resource = new DnsClientRequest(client,config.getUrl());
        final AtomicInteger num = new AtomicInteger(1);


        environment.jersey().register(resource);

        environment.lifecycle().manage( new Managed() {
            @Override
            public void start() throws Exception {

            }

            @Override
            public void stop() throws Exception {
                localHostServer.shutdown();
            }
        });

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {

            }

            @Override
            public void stop() throws Exception {
                otherServer.shutdown();
            }
        });

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {

            }

            @Override
            public void stop() throws Exception {
                executor.shutdownNow();
            }
        });

        executor.scheduleAtFixedRate(()-> {
            System.out.println(num.getAndIncrement() + ") " + resource.sayHello());
            System.out.flush();
        },0,Integer.getInteger("millis",1000), TimeUnit.MILLISECONDS);

    }
}
