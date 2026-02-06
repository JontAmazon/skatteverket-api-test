package se.skatteverket.apitest;

final class ResponsePrinter {
    private ResponsePrinter() {
    }

    static void printFirstLines(String body, int maxLines) {
        String[] lines = body.split("\\R", -1);
        int limit = Math.min(maxLines, lines.length);
        for (int i = 0; i < limit; i++) {
            System.out.println(lines[i]);
        }
    }
}
