package org.greencheek.dns.dw.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by dominictootell on 14/09/2014.
 */
public class DnsCheckingConfiguration  extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private String url = "http://api.localhost.com";

    @Valid
    @NotNull
    @JsonProperty
    private int port = 1234;

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }


    public String getUrl() {
        return url + ":" + getPort();
    }

    public int getPort() {
        return port;
    }
}