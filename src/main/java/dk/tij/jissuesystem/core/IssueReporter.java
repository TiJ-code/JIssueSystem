package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.api.Issue;

import java.util.concurrent.CompletableFuture;

public class IssueReporter {
    private final IIssueProvider provider;
    private final LabelContract contract;

    public IssueReporter(IIssueProvider provider, LabelContract contract) {
        this.provider = provider;
        this.contract = contract;
    }

    public CompletableFuture<Void> initialise() {
        return provider.fetchLabels().thenAccept(contract::validate);
    }

    public CompletableFuture<Integer> report(Issue issue) {
        return provider.report(issue);
    }
}
