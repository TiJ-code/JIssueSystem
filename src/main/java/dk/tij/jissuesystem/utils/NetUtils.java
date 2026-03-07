package dk.tij.jissuesystem.utils;

public final class NetUtils {
    private NetUtils() {}

    private static final String EMPTY = "";

    public static String escape(String input) {
        if (input == null || input.isEmpty())
            return EMPTY;

        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
