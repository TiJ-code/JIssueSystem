package dk.tij.jissuesystem.utils;

/**
 * Utility class for network-related string operations.
 *
 * <p>Currently provides string escaping for JSON payloads to ensure special characters
 * do not break the format.
 *
 * <p>All methods are static; instantiation is prevented.
 *
 * @since 0.2.0
 */
public final class NetUtils {
    private NetUtils() {}

    private static final String EMPTY = "";

    /**
     * Escapes characters in a string for safe inclusion in JSON payloads.
     *
     * <p>Replaces backslashes, quotes, control characters, and line breaks with
     * appropriate escape sequences.
     *
     * @param input the raw string to escape
     * @return the escaped string; returns empty string if input is null or empty
     */
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
