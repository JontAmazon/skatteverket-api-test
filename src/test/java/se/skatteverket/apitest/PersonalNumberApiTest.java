package se.skatteverket.apitest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.skatteverket.apitest.util.AssertionsUtil;
import se.skatteverket.apitest.util.HttpLogUtil;
import se.skatteverket.apitest.util.TextUtil;

import static org.junit.jupiter.api.Assertions.*;

class PersonalNumberApiTest extends BaseApiTest {
    private static final Logger LOGGER = Logger.getLogger(PersonalNumberApiTest.class.getName());
    private static final String DATASET_PATH = "/dataset/b4de7df7-63c0-4e7e-bb59-1f156a591763";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final HttpClient client = httpClient();

    @Test
    void filtersByBirthdate_returnsResults() throws Exception {
        String birthdate = "19950805";
        String query = "testpersonnummer=" + URLEncoder.encode("^" + birthdate, StandardCharsets.UTF_8);
        URI filteredUri = buildUri(DATASET_PATH + "/json?" + query);

        HttpResponse<String> response = client.send(
                HttpClientUtil.buildGet(filteredUri, REQUEST_TIMEOUT),
                HttpResponse.BodyHandlers.ofString()
        );
        LOGGER.log(Level.FINE, () -> HttpLogUtil.format(response));

        String body = response.body();
        AssertionsUtil.assertStatus(response, 200);

        JsonNode root = parseJson(body);
        assertTrue(root.isObject(), "Expected JSON object root. Body snippet: " + TextUtil.snippet(body, 300));

        int resultCount = root.path("resultCount").asInt(-1);
        assertTrue(
                resultCount >= 1,
                "Expected at least 1 result for birthdate " + birthdate
                        + ". resultCount=" + resultCount
                        + ". Body snippet: " + TextUtil.snippet(body, 600)
        );

        JsonNode results = root.path("results");
        assertTrue(results.isArray(), "Expected results array. Body snippet: " + TextUtil.snippet(body, 300));
        assertTrue(
                results.size() > 0,
                "Expected results array to contain at least 1 item. Body snippet: " + TextUtil.snippet(body, 300)
        );

        // Verify ALL results have the given birthdate.
        String firstMismatch = null;
        for (JsonNode item : results) {
            String testpersonnummer = item.path("testpersonnummer").asText("");
            if (!testpersonnummer.startsWith(birthdate)) {
                firstMismatch = testpersonnummer;
                break;
            }
        }
        assertNull(
                firstMismatch,
                "Expected all testpersonnummer values to start with " + birthdate
                        + ". First mismatch: " + firstMismatch
                        + ". Body snippet: " + TextUtil.snippet(body, 600)
        );
    }

    private static JsonNode parseJson(String body) {
        if (body == null) {
            fail("Expected response body, got <null>.");
        }
        try {
            return MAPPER.readTree(body);
        } catch (Exception e) {
            fail("Response body is not valid JSON. Body snippet: " + TextUtil.snippet(body, 600), e);
            return null;
        }
    }
}
