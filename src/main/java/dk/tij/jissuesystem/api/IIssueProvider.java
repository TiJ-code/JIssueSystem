package dk.tij.jissuesystem.api;

import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IIssueProvider {
    CompletableFuture<Set<Label>> fetchLabels();

    CompletableFuture<HttpResponse<String>> report(Issue issue);
}
