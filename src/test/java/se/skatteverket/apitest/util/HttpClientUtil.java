package se.skatteverket.apitest.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

public final class HttpClientUtil {
    private HttpClientUtil() {
    }

    public static HttpClient createClient(Duration connectTimeout) {
        return HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public static HttpRequest buildGet(URI uri, Duration timeout) {
        return HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("User-Agent", "skatteverket-api-test-suite/1.0")
                .GET()
                .build();
    }
}
