package se.skatteverket.apitest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import se.skatteverket.apitest.util.AssertionsUtil;
import se.skatteverket.apitest.util.RequestResponseLogUtil;

import static org.junit.jupiter.api.Assertions.*;

class PersonalNumberApiTest extends BaseApiTest {
    private static final Logger LOGGER = Logger.getLogger(PersonalNumberApiTest.class.getName());
    private static final String DATASET_PATH = "/dataset/b4de7df7-63c0-4e7e-bb59-1f156a591763";
    private URI uri;
    private HttpClient client;

    @BeforeEach
    void setUp() {
        uri = buildUri(DATASET_PATH);
        client = httpClient();
    }

    @Test
    @Timeout(20)
    void smokeTest() throws Exception {
        HttpResponse<String> response = client.send(
                HttpClientUtil.buildGet(uri, REQUEST_TIMEOUT),
                HttpResponse.BodyHandlers.ofString()
        );
        LOGGER.log(Level.FINE, () -> RequestResponseLogUtil.format(response, 500));

        String body = response.body();
        AssertionsUtil.assertStatus(response, 200);

        System.out.println("=== First 300 characters of response body ===");
        int previewLimit = Math.min(300, body.length());
        System.out.println(body.substring(0, previewLimit));
    }

}
