package se.skatteverket.apitest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

final class HttpClientUtil {
    private HttpClientUtil() {
    }

    static HttpClient createClient(Duration connectTimeout) {
        return HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    static HttpRequest buildGet(URI uri, Duration timeout) {
        return HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("User-Agent", "skatteverket-api-test-suite/1.0")
                .GET()
                .build();
    }
}
