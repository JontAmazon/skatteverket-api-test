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
        String query = "testpersonnummer=" + URLEncoder.encode("^" + birthdate + "\\d{4}$", StandardCharsets.UTF_8);
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

    /** Given a huge input value for _limit:
     *  a) verify the response metadata says/claims the actual limit used is smaller; defaulted to some max cap.
     *  b) we don't trust the metadata 100%, instead we count the number of results and verify it's fewer than
     *  the requested input number.
     */
    @Test
    void limitAboveMaxCapsToMax() throws Exception {
        int requestedLimit = Integer.MAX_VALUE;
        URI limitedUri = buildUri(DATASET_PATH + "/json?_limit=" + requestedLimit);

        HttpResponse<String> response = client.send(
                HttpClientUtil.buildGet(limitedUri, REQUEST_TIMEOUT),
                HttpResponse.BodyHandlers.ofString()
        );
        LOGGER.log(Level.FINE, () -> HttpLogUtil.format(response));

        String body = response.body();
        AssertionsUtil.assertStatus(response, 200);

        JsonNode root = parseJson(body);
        assertTrue(root.isObject(), "Expected JSON object root. Body snippet: " + TextUtil.snippet(body, 300));

        int responseClaimedLimit = root.path("limit").asInt(-1);
        LOGGER.log(Level.INFO, () -> "requestedLimit=" + requestedLimit
                + ", responseClaimedLimit=" + responseClaimedLimit);
        assertTrue(
                responseClaimedLimit < requestedLimit,
                "Expected server to clamp limit below requested _limit=" + requestedLimit
                        + ". Response claimed limit=" + responseClaimedLimit
                        + ". Body snippet: " + TextUtil.snippet(body, 600)
        );

        // b) Verify results is a non-empty array, items contain "testpersonnummer", and size is leq responseClaimedLimit.
        JsonNode results = root.path("results");
        assertTrue(results.isArray(), "Expected results array. Body snippet: " + TextUtil.snippet(body, 300));
        assertTrue(
                results.size() > 0,
                "Expected results array to contain at least 1 item. Body snippet: " + TextUtil.snippet(body, 300)
        );
        JsonNode firstResult = results.get(0);
        assertTrue(
                firstResult.isObject() && firstResult.has("testpersonnummer"),
                "Expected results[0] to be an object with key 'testpersonnummer'. Body snippet: "
                        + TextUtil.snippet(body, 600)
        );
        assertTrue(
                results.size() <= responseClaimedLimit,
                "Expected result count to be lesser or equal to the claimed cap for _limit"
                        + ". Response claimed cap=" + responseClaimedLimit
                        + ". Actual result count=" + results.size()
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
