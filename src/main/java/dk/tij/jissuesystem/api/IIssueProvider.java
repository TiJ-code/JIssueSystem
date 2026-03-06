package dk.tij.jissuesystem.api;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IIssueProvider {
    CompletableFuture<Set<Label>> fetchLabels();

    CompletableFuture<Integer> report(Issue issue);
}
