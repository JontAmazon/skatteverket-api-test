package se.skatteverket.apitest.util;

public final class TextUtil {
    private TextUtil() {
    }

    public static String snippet(String body, int maxChars) {
        if (body == null || body.isEmpty()) {
            return "<empty>";
        }
        if (body.length() <= maxChars) {
            return body;
        }
        return body.substring(0, maxChars) + "...";
    }
}
