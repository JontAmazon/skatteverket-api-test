package se.skatteverket.apitest.util;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public final class HttpLogUtil {
    private HttpLogUtil() {
    }

    public static String format(HttpResponse<String> response, int maxBodyChars) {
        if (response == null) {
            return "HTTP response: <null>";
        }

        StringBuilder sb = new StringBuilder(256);
        HttpRequest request = response.request();
        if (request != null) {
            sb.append("HTTP request: ")
                    .append(request.method())
                    .append(" ")
                    .append(request.uri())
                    .append(System.lineSeparator());
            appendHeaders(sb, request.headers().map());
        }

        sb.append("HTTP response: ")
                .append("status=")
                .append(response.statusCode())
                .append(System.lineSeparator());
        appendHeaders(sb, response.headers().map());
        sb.append("Body snippet: ")
                .append(TextUtil.snippet(response.body(), maxBodyChars));

        return sb.toString();
    }

    private static void appendHeaders(StringBuilder sb, Map<String, List<String>> headers) {
        if (headers == null || headers.isEmpty()) {
            sb.append("Headers: <none>").append(System.lineSeparator());
            return;
        }
        sb.append("Headers:").append(System.lineSeparator());
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(String.join(", ", entry.getValue()))
                    .append(System.lineSeparator());
        }
    }
}
