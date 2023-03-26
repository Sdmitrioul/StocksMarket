package ru.skroba.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseClient {
    private final String host;
    private final String path;
    
    protected final HttpClient client;
    
    protected BaseClient(final String host, final String path) {
        this.host = host;
        this.path = path;
        this.client = HttpClient.newHttpClient();
    }
    
    protected URI buildUri(Map<String, ?> parameters) throws URISyntaxException {
        return new URI(host + path + "?" + parameters.entrySet()
                .stream()
                .map((entry) -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&")));
    }
}
