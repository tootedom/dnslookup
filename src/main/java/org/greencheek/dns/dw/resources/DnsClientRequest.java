package org.greencheek.dns.dw.resources;

import com.codahale.metrics.annotation.Timed;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * Created by dominictootell on 14/09/2014.
 */
@Path("/request")
@Produces(MediaType.TEXT_PLAIN)
public class DnsClientRequest {

    private final Client client;
    private final String url;

    public DnsClientRequest(Client client, String url) {
        this.client = client;
        this.url = url;
    }

    @GET
    @Timed
    public String sayHello() {
        WebResource resource = client.resource(url);

        ClientResponse response = resource.get(ClientResponse.class);

        try {
            String s = response.getEntity(String.class);
            return s;
        } finally {
            if(response!=null) {
                response.close();
            }
        }
    }
}
