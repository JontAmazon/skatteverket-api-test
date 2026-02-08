package se.skatteverket.apitest.util;

import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;

public final class AssertionsUtil {
    private AssertionsUtil() {
    }

    public static void assertStatus(HttpResponse<String> response, int expectedStatus) {
        String body = response == null ? null : response.body();
        int actualStatus = response == null ? -1 : response.statusCode();
        Assertions.assertEquals(
                expectedStatus,
                actualStatus,
                "Unexpected status code. Body snippet: " + TextUtil.snippet(body, 300)
        );
    }
}
