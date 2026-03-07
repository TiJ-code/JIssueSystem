package dk.tij.jissuesystem.api;

/**
 * Functional interface for building a provider-specific payload
 * to report an {@link Issue}.
 *
 * <p>Implementations generate the payload used to submit issues
 * to a specific provider, e.g., GitHub repository_dispatch events
 * or GitLab API requests.</p>
 *
 * @since 0.2.0
 */
@FunctionalInterface
public interface IPayloadBuilder {
    /**
     * Builds a provider-specific payload string for a given {@link Issue}.
     *
     * <p>This payload is typically a JSON string or other format required
     * by the issue tracking API.</p>
     *
     * @param issue the issue to report
     * @return the payload string suitable for submission to the provider
     */
    String buildPayload(Issue issue);
}