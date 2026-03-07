package dk.tij.jissuesystem.api;

import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Interface representing a provider for issue tracking systems.
 *
 * <p>Implementations of this interface should handle fetching labels from
 * a repository and reporting new issues.</p>
 *
 * <p>Typical implementations. {@link dk.tij.jissuesystem.provider.github.GitHubProvider}.</p>
 *
 * @since 0.2.0
 */
public interface IIssueProvider {
    /**
     * Fetches the set of labels available in the repository.
     *
     * @return a {@link CompletableFuture} that resolves to a set of {@link Label} objects
     */
    CompletableFuture<Set<Label>> fetchLabels();

    /**
     * Reports a new issue to a provider.
     *
     * @param issue the {@link Issue} to report
     * @return a {@link CompletableFuture} that resolves to the HTTP response from the provider
     */
    CompletableFuture<HttpResponse<String>> report(Issue issue);

    /**
     * Sets the payload builder used to construct API request payloads.
     *
     * @param payloadBuilder the {@link IPayloadBuilder} implementation
     */
    void payloadBuilder(IPayloadBuilder payloadBuilder);

    /**
     * Returns the currently configured payload builder
     *
     * @return the {@link IPayloadBuilder} instance used by this provider
     */
    IPayloadBuilder payloadBuilder();

    /**
     * Sets the label parser used to parse labels from API response.
     *
     * @param payloadBuilder the {@link ILabelParser} implementation
     */
    void labelParser(ILabelParser payloadBuilder);

    /**
     * Returns the currently configured label parser
     * @return the {@link ILabelParser} instance used by this provider
     */
    ILabelParser labelParser();
}
