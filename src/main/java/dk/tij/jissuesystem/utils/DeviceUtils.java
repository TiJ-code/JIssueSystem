package dk.tij.jissuesystem.utils;

import java.util.List;

/**
 * Utility class for retrieving device and environment information.
 *
 * <p>Provides diagnostics that can be attached to issues to help with debugging.</p>
 *
 * <p>All methods are static; instantiation is prevented.</p>
 *
 * @since 0.2.0
 */
public final class DeviceUtils {
    private static final String SEPARATOR = "\n---\n";

    private static final String ENVIRONMENT_INFO_FORMAT = """
            **Environment Info:**
            * **OS:** %s (%s)
            * **Java:** %s (%s)
            """;

    private DeviceUtils() {}

    /**
     * Returns a formatted string containing custom information and environment diagnostics,
     * that include operating system name, architecture, and Java runtime information.
     *
     * @param customInfo additional diagnostic entries to include before the
     *                   environment information
     * @return a multi-line string with environment info
     */
    public static String getDiagnostics(List<String> customInfo) {
        StringBuilder diagnostics = new StringBuilder(SEPARATOR);

        if (customInfo != null && !customInfo.isEmpty()) {
            diagnostics.append(String.join("\n", customInfo))
                    .append("\n\n");
        }

        diagnostics.append(getEnvironmentInfo());

        return diagnostics.toString();
    }

    private static String getEnvironmentInfo() {
        return ENVIRONMENT_INFO_FORMAT.formatted(
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                System.getProperty("java.version"),
                System.getProperty("java.vendor")
        );
    }
}
