package dk.tij.jissuesystem.utils;

public final class DeviceUtils {
    private DeviceUtils() {}

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
