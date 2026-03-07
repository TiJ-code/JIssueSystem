package dk.tij.jissuesystem.provider.generic;

import dk.tij.jissuesystem.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * A configurable {@link IIssueProvider} implementation for interacting with
 * arbitrary issue tracking APIs.
 *
 * <p>The {@link GenericProvider} allows consumers to specify custom API endpoints
 * and optionally provide lambda-based strategies for parsing labels and building
 * issue payloads.</p>
 *
 * <p>This provider is useful for interacting with services that expose
 * REST APIs but do not have a dedicated provider implementation in this library.</p>
 *
 * <h2>Configuration</h2>
 * Instances should be created using the {@link Builder}:
 *
 * <pre>{@code
 * GenericProvider provider = GenericProvider.builder()
 *     .owner("owner")
 *     .repo("repo")
 *     .token("api-token")
 *     .labelsEndpoint(URI.create("https://api.example.com/labels"))
 *     .issueEndpoint(URI.create("https://api.examples.com/issues"))
 *     .build();
 * }</pre>
 *
 * <p>Custom label parsing and payload generation logic may also be supplied:</p>
 *
 * <pre>{@code
 * GenericProvider provider = GenericProvider.builder()
 *     .owner("owner")
 *     .repo("repo")
 *     .token("api-token")
 *     .labelsEndpoint(URI.create("https://api.example.com/labels"))
 *     .issueEndpoint(URI.create("https://api.examples.com/issues"))
 *     .labelParser(raw -> ...)
 *     .payloadBuilder(issue -> ...)
 *     .build();
 * }</pre>
 *
 * <p>Requests are executed asynchronously using {@link HttpClient}</p>
 *
 * @since 0.2.0
 */
public class GenericProvider extends AbstractTokenProvider implements IIssueProvider {
    private static final HttpClient CLIENT =  HttpClient.newHttpClient();

    private URI labelsEndpoint;
    private URI issueEndpoint;

    /**
     * Constructs a new {@link GenericProvider} with the specified repository context
     * and authentication token.
     *
     * <p>Initialises default label parser ({@link GenericLabelParser}) and payload builder
     * ({@link GenericPayloadBuilder}).</p>
     *
     * @param owner the repository owner, organisation, or namespace
     * @param repo the repository or project name
     * @param token the authentication token for API requests
     * @throws IllegalArgumentException if any of the parameters are {@code null} or blank
     */
    protected GenericProvider(String owner, String repo, String token) {
        super(owner, repo, token);
        this.labelParser = new GenericLabelParser();
        this.payloadBuilder = new GenericPayloadBuilder();
    }

    /**
     * Configures the API endpoints used by this provider.
     *
     * <p>This method is primarily intended for internal use by the {@link Builder}.
     * It initialises the endpoints required for retrieving labels and creating issues.</p>
     *
     * @param labelsEndpoint the endpoint used to retrieve repository labels
     * @param issueEndpoint  the endpoint used to create issues
     * @return this provider instance
     *
     * @throws NullPointerException if either endpoint is {@code null}
     */
    public GenericProvider configure(URI labelsEndpoint, URI issueEndpoint) {
        this.labelsEndpoint = Objects.requireNonNull(labelsEndpoint);
        this.issueEndpoint = Objects.requireNonNull(issueEndpoint);

        return this;
    }

    /**
     * Retrieves the set of labels available for the configured repository.
     *
     * <p>The request is executed asynchronously using {@link HttpClient}.
     * The raw API response is passed to the configured label parser to
     * produce a set of {@link Label} objects.</p>
     *
     * @return a {@link CompletableFuture} containing the parsed labels.
     *
     * @throws IllegalStateException if the provide has not been configured
     */
    @Override
    public CompletableFuture<Set<Label>> fetchLabels() {
        ensureConfigured();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(labelsEndpoint)
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> labelParser.parse(r.body()));
    }

    /**
     * Reports a new issue to the configured issue endpoint.
     *
     * <p>The issue is converted into a request payload using the configured
     * payload builder and submitted via an asynchronous HTTP POST request.</p>
     *
     * @param issue the issue to report
     * @return a {@link CompletableFuture} containing the HTTP response
     *
     * @throws NullPointerException  if {@code issue} is {@code null}
     * @throws IllegalStateException if the provider has not been configured
     */
    @Override
    public CompletableFuture<HttpResponse<String>> report(Issue issue) {
        Objects.requireNonNull(issue, "issue cannot be null");

        ensureConfigured();

        String payload = payloadBuilder.buildPayload(issue);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(issueEndpoint)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Creates a new {@link Builder} used to construct a {@link GenericProvider}.
     *
     * @return a new {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Ensures the provider has been properly configured before use.
     *
     * @throws IllegalStateException if required configuration fields
     *                               have not been initialised
     */
    private void ensureConfigured() {
        if (labelsEndpoint == null || issueEndpoint == null ||
            labelParser == null || payloadBuilder == null) {
            throw new IllegalStateException("GenericProvider must be configured before use");
        }
    }

    /**
     * Builder used to construct {@link GenericProvider} instances.
     *
     * <p>The builder allows configuration of required repository metadata,
     * API endpoints, and optional customisation hooks for parsing labels
     * and constructing issue payloads.</p>
     *
     * <p>If no custom parser or payload builder is supplied, default
     * implementations are used.</p>
     */
    public static class Builder {
        private String owner, repo, token;
        private URI labelsEndpoint,  issueEndpoint;
        private ILabelParser labelParser;
        private IPayloadBuilder payloadBuilder;

        /**
         * Creates a new {@link Builder} instance for constructing a {@link GenericProvider}.
         *
         * <p>Use the builder methods to configure repository information, endpoints,
         * and optional custom parsers or payload builders before calling {@link #build()}.</p>
         */
        public Builder() {}

        /**
         * Sets the repository owner, organisation or namespace
         *
         * @param owner repository owner
         * @return this builder
         */
        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Sets the repository or project name
         *
         * @param repo repository name
         * @return this builder
         */
        public Builder repo(String repo) {
            this.repo = repo;
            return this;
        }

        /**
         * Sets the personal access token or API token
         *
         * @param token personal access token or API token
         * @return this builder
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * Sets the endpoint used to retrieve repository labels.
         *
         * @param labelsEndpoint URI of the labels API endpoint
         * @return this builder
         */
        public Builder labelsEndpoint(URI labelsEndpoint) {
            this.labelsEndpoint = labelsEndpoint;
            return this;
        }

        /**
         * Sets the endpoint used to create issues.
         *
         * @param issueEndpoint URI of the issue API endpoint
         * @return this builder
         */
        public Builder issueEndpoint(URI issueEndpoint) {
            this.issueEndpoint = issueEndpoint;
            return this;
        }

        /**
         * Provides a custom function for parsing label responses returned
         * from the labels endpoint.
         *
         * @param parser function that converts raw API response text into a
         *               set of {@link Label} objects
         * @return this builder
         */
        public Builder labelParser(ILabelParser parser) {
            this.labelParser = parser;
            return this;
        }

        /**
         * Provides a custom function for generating the request payload
         * used when creating issues.
         *
         * @param payloadBuilder function that converts an {@link Issue}
         *                       into a JSON request payload
         * @return this builder
         */
        public Builder payloadBuilder(IPayloadBuilder payloadBuilder) {
            this.payloadBuilder = payloadBuilder;
            return this;
        }

        /**
         * Builds a configured {@link GenericProvider}.
         *
         * @return a new {@link GenericProvider}
         *
         * @throws NullPointerException if required configuration values are missing
         */
        public GenericProvider build() {
            Objects.requireNonNull(owner, "owner must be set");
            Objects.requireNonNull(repo, "repo must be set");
            Objects.requireNonNull(token, "token must be set");
            Objects.requireNonNull(labelsEndpoint, "labelsEndpoint must be set");
            Objects.requireNonNull(issueEndpoint, "issueEndpoint must be set");

            GenericProvider provider = new GenericProvider(owner, repo, token).configure(labelsEndpoint, issueEndpoint);

            if (labelParser != null)
                provider.labelParser(labelParser);

            if (payloadBuilder != null)
                provider.payloadBuilder(payloadBuilder);

            return provider;
        }
    }
}
