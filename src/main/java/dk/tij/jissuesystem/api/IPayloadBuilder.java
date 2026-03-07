package dk.tij.jissuesystem.api;

/**
 * Interface for building a JSON payload to report an {@link Issue}
 *
 * <p>Implementations generate the provider-specific payload used
 * to submit issues, e.g., GitHub repository_dispatch events.</p>
 *
 * @since 0.2.0
 */
public interface IPayloadBuilder {
    /**
     * Builds a payload string for a given {@link Issue}
     *
     * @param issue the issue to report
     * @return the provider-specific payload as a string.
     */
    String buildPayload(Issue issue);
}
