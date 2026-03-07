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

public class IssueReporter {
    private final IIssueProvider provider;
    private final LabelContract contract;

    private volatile boolean initialised  = false;

    public IssueReporter(IIssueProvider provider, LabelContract contract) {
        this.provider = provider;
        this.contract = contract;
    }

    public CompletableFuture<Void> initialise() {
        return provider.fetchLabels()
                .thenAccept(labels -> {
                    contract.validate(labels);
                    initialised = true;
                });
    }

    public CompletableFuture<HttpResponse<String>> report(Issue issue) {
        if (!initialised) {
            throw new IllegalStateException("Reporter not initialised");
        }

        Issue enriched = enrich(issue);
        contract.validate(enriched.labels());

        return provider.report(enriched);
    }

    public IIssueProvider getProvider() {
        return provider;
    }

    public LabelContract getContract() {
        return contract;
    }

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private IIssueProvider provider;
        private LabelContract contract;

        public Builder provider(IIssueProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder provider(IssueProviderType type, String owner, String repo, String token) {
            this.provider = IssueProviderFactory.create(type, owner, repo, token);
            return this;
        }

        public Builder contract(LabelContract contract) {
            if (this.contract == null)
                this.contract = contract;
            else
                this.contract = this.contract.concat(contract);
            return this;
        }

        public IssueReporter build() {
            if (provider == null)
                throw new IllegalStateException("Provider must be set");

            if (contract == null)
                contract = LabelContract.DEFAULT_CONTRACT;

            return new IssueReporter(provider, contract);
        }
    }
}
