package se.skatteverket.apitest;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import se.skatteverket.apitest.util.HttpClientUtil;

abstract class BaseApiTest {
    protected static final String BASE_URL = "https://skatteverket.entryscape.net/rowstore";
    protected static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);
    private static final HttpClient CLIENT = HttpClientUtil.createClient(CONNECT_TIMEOUT);

    static {
        try {
            Path logDir = Paths.get("target", "test-logs");
            Files.createDirectories(logDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create target/test-logs directory", e);
        }
    }

    protected HttpClient httpClient() {
        return CLIENT;
    }

    protected URI buildUri(String path) {
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(BASE_URL + normalizedPath);
    }
}
