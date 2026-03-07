package dk.tij.jissuesystem.utils;

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
    private DeviceUtils() {}

    /**
     * Returns a formatted string containing environment diagnostics,
     * including operating system name, architecture, and Java runtime information.
     *
     * @return a multi-line string with environment info
     */
    public static String getDiagnostics() {
        return String.format("""
        \n
        ---
        **Environment Info:**
        * **OS:** %s (%s)
        * **Java:** %s (%s)
        """,
                System.getProperty("os.name"), System.getProperty("os.arch"),
                System.getProperty("java.version"), System.getProperty("java.vendor")
        );
    }
}
