package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.api.IIssueProvider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GitHubProvider implements IIssueProvider {
    private final String owner;
    private final String repo;
    private final String pat;

    private final HttpClient client = HttpClient.newHttpClient();

    public GitHubProvider(String owner, String repo, String pat) {
        this.owner = owner;
        this.repo = repo;
        this.pat = pat;
    }

    @Override
    public CompletableFuture<Set<Label>> fetchLabels() {

        String url = String.format(
                "https://api.github.com/repos/%s/%s/labels",
                owner, repo
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + pat)
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "JIssueSystem")
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> GitHubLabelParser.parse(r.body()));
    }

    @Override
    public CompletableFuture<HttpResponse<String>> report(Issue issue) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/dispatches",
                owner, repo
        );

        String payload = GitHubPayloadBuilder.build(issue);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + pat)
                .header("Accept", "application/vnd.github+json")
                .header("Content-Type", "application/json")
                .header("User-Agent", "JIssueSystem")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
    }
}
