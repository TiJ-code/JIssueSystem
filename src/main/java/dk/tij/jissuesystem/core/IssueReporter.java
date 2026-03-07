package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.provider.IssueProviderType;
import dk.tij.jissuesystem.utils.DeviceUtils;

import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * High-level utility for reporting issues to a repository via an {@link IIssueProvider}.
 *
 * <p>Manages initialisation, label validation, and automatic enrichment of issue
 * bodies with diagnostic information.</p>
 *
 * <p>Supports a fluent {@link Builder} for convenient construction.</p>
 *
 * @since 0.2.0
 */
public class IssueReporter {
    private final IIssueProvider provider;
    private final LabelContract contract;

    private volatile boolean initialised  = false;

    /**
     * Creates a new {@link IssueReporter}
     *
     * @param provider the issue provider
     * @param contract the label contract to enforce
     */
    public IssueReporter(IIssueProvider provider, LabelContract contract) {
        this.provider = provider;
        this.contract = contract;
    }

    /**
     * Initialises the reporter by fetching repository labels and validating them
     * against the contract.
     *
     * @return a {@link CompletableFuture} that completes when initialisation finishes
     */
    public CompletableFuture<Void> initialise() {
        return provider.fetchLabels()
                .thenAccept(labels -> {
                    contract.validate(labels);
                    initialised = true;
                });
    }

    /**
     * Reports an issue after enriching it with diagnostics and validating against contract
     *
     * @param issue the issue to report
     * @return a {@link CompletableFuture} with the HTTP response
     * @throws IllegalStateException if the reporter is not initialised
     */
    public CompletableFuture<HttpResponse<String>> report(Issue issue) {
        if (!initialised) {
            throw new IllegalStateException("Reporter not initialised");
        }

        Issue enriched = enrich(issue);
        contract.validate(enriched.labels());

        return provider.report(enriched);
    }

    /**
     * Returns the underlying provider
     *
     * @return the {@link IIssueProvider} instance
     */
    public IIssueProvider getProvider() {
        return provider;
    }

    /**
     * Returns the label contract.
     *
     * @return the {@link LabelContract} instance
     */
    public LabelContract getContract() {
        return contract;
    }

    /**
     * Enriches an issue with required labels and device diagnostics.
     * @param issue the issue to enrich
     * @return the enriched issue
     */
    private Issue enrich(Issue issue) {
        Set<Label> labels = new HashSet<>(issue.labels());
        contract.getRequiredLabels().forEach(r -> labels.add(new Label(r)));

        final String enrichedBody = "%s\n%s"
                .formatted(issue.body(), DeviceUtils.getDiagnostics());

        return new Issue.Builder()
                .title(issue.title())
                .body(enrichedBody)
                .labels(labels)
                .build();
    }

    /**
     * Returns a new builder for {@link IssueReporter}
     * @return a new issue reporter
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for {@link IssueReporter} with fluent API.
     */
    public static class Builder {
        private IIssueProvider provider;
        private LabelContract contract;

        /**
         * Creates a new {@link Builder} instance for constructing an {@link IssueReporter}.
         *
         * <p>Use the builder methods to configure the issue provider and label contract
         * before calling {@link #build()} to create the reporter instance.</p>
         *
         * <p>Example usage:</p>
         * <pre>{@code
         * IssueReporter reporter = IssueReporter.builder()
         *     .provider(IssueProviderType.GITHUB, "owner", "repo", "token")
         *     .contract(LabelContract.DEFAULT_CONTRACT)
         *     .build();
         * }</pre>
         */
        public Builder() {}

        /**
         * Sets the provider instance directly.
         *
         * @param provider the provider
         * @return this builder
         */
        public Builder provider(IIssueProvider provider) {
            this.provider = provider;
            return this;
        }

        /**
         * Sets the provider via type, owner, repo, and token.
         *
         * @param type  the provider type
         * @param owner the repository owner
         * @param repo  the repository name
         * @param token the personal access token or API token
         * @return this builder
         */
        public Builder provider(IssueProviderType type, String owner, String repo, String token) {
            this.provider = IssueProviderFactory.create(type, owner, repo, token);
            return this;
        }

        /**
         * Sets or merges a {@link LabelContract}
         *
         * @param contract the contract to set or merge
         * @return this builder
         */
        public Builder contract(LabelContract contract) {
            if (this.contract == null)
                this.contract = contract;
            else
                this.contract = this.contract.concat(contract);
            return this;
        }

        /**
         * Builds the {@link IssueReporter}
         * @return the issue reporter
         */
        public IssueReporter build() {
            if (provider == null)
                throw new IllegalStateException("Provider must be set");

            if (contract == null)
                contract = LabelContract.DEFAULT_CONTRACT;

            return new IssueReporter(provider, contract);
        }
    }
}
