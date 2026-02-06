package se.skatteverket.apitest;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

abstract class BaseApiTest {
    protected static final String BASE_URL = "https://skatteverket.entryscape.net/rowstore";
    protected static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);

    protected HttpClient httpClient() {
        return HttpClientUtil.createClient(CONNECT_TIMEOUT);
    }

    protected URI buildUri(String path) {
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(BASE_URL + normalizedPath);
    }
}
