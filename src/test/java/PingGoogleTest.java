import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class PingGoogleTest {
    @Test
    @Timeout(10)
    void pingGoogleDotCom() throws Exception {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String countFlag = os.contains("win") ? "-n" : "-c";
        Process process = new ProcessBuilder("ping", countFlag, "1", "google.com")
                .redirectErrorStream(true)
                .start();
        boolean finished = process.waitFor(5, TimeUnit.SECONDS);
        assertTrue(finished, "ping did not finish in time");
        assertEquals(0, process.exitValue(), "ping failed");
    }
}
