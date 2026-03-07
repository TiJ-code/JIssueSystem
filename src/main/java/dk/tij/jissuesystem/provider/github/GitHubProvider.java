package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.AbstractTokenProvider;
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

/**
 * GitHub issue provider implementation for the {@link IIssueProvider} interface.
 *
 * <p>Supports:
 * <ul>
 *     <li>Fetching all labels of a repository via {@code /labels} endpoint</li>
 *     <li>Reporting issues via the {@code repository_dispatch} event</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * IIssueProvider provider = new GitHubProvider("owner", "repo", "PAT");
 * Set<Label> labels = provider.fetchLabels().join();
 * HttpResponse<String> response = provider.report(issue).join();
 * }</pre>
 *
 * @since 0.2.0
 */
public class GitHubProvider extends AbstractTokenProvider implements IIssueProvider {
    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * Constructs a GitHubProvider for the given repository and access token.
     *
     * @param owner repository owner
     * @param repo repository name
     * @param pat personal access token
     */
    public GitHubProvider(String owner, String repo, String pat) {
        super(owner, repo, pat);
    }

    /**
     * Fetches all labels of the repository.
     *
     * @return a {@link CompletableFuture} containing a set of {@link Label} objects
     */
    @Override
    public CompletableFuture<Set<Label>> fetchLabels() {

        String url = String.format(
                "https://api.github.com/repos/%s/%s/labels",
                owner, repo
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "JIssueSystem")
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> GitHubLabelParser.parse(r.body()));
    }

    /**
     * Reports an issue to the repository via the {@code repository_dispatch} event.
     *
     * @param issue the issue to report
     * @return a {@link CompletableFuture} containing the HTTP response
     */
    @Override
    public CompletableFuture<HttpResponse<String>> report(Issue issue) {
        String url = String.format(
                "https://api.github.com/repos/%s/%s/dispatches",
                owner, repo
        );

        String payload = GitHubPayloadBuilder.build(issue);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .header("Content-Type", "application/json")
                .header("User-Agent", "JIssueSystem")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return client.sendAsync(req, HttpResponse.BodyHandlers.ofString());
    }
}
