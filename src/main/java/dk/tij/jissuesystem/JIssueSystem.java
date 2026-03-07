package dk.tij.jissuesystem;

import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.core.IssueReporter;
import dk.tij.jissuesystem.core.LabelContract;
import dk.tij.jissuesystem.provider.IssueProviderType;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Legacy wrapper for pre-0.2.0 usage.
 * Internally uses IssueReporter and GitHubProver for 0.2.0 API.
 *
 * @author tij
 * @since 0.1.0
 */
@Deprecated(since = "0.2.0")
public class JIssueSystem {
    private final IssueReporter reporter;

    /**
     * Creates a new instance of the reporting system
     *
     * @param repoOwner the repository owner
     * @param repoName  the repository name
     * @param pat the personal access token or API token
     */
    @Deprecated(since = "0.2.0")
    public JIssueSystem(String repoOwner, String repoName, String pat) {
        this.reporter = IssueReporter.builder()
                .provider(IssueProviderType.GITHUB, repoOwner, repoName, pat)
                .contract(LabelContract.DEFAULT_CONTRACT)
                .build();
        this.reporter.initialise().join();
    }

    /**
     * Legacy static method, mimics old usage
     *
     * @param repoOwner the repository owner
     * @param repoName the repository name
     * @param pat the personal access token or API token
     * @param title the issue title
     * @param body the issue body
     * @return a {@link CompletableFuture} with the status code
     */
    @Deprecated(since = "0.2.0")
    public static CompletableFuture<Integer> report(String repoOwner, String repoName, String pat, String title, String body) {
        return new JIssueSystem(repoOwner, repoName, pat).report(title, body);
    }

    /**
     * Legacy static method, mimics old usage
     *
     * @param title the issue title
     * @param body the issue body
     * @return a {@link CompletableFuture} with the status code
     */
    @Deprecated(since = "0.2.0")
    public final CompletableFuture<Integer> report(String title, String body) {
        Issue issue = new Issue.Builder()
                .title(title)
                .body(body)
                .labels(Set.of(new Label("automated-report")))
                .build();

        return reporter.report(issue).thenApply(HttpResponse::statusCode);
    }

    /**
     * Returns the internal {@link IssueReporter} instance used for issue reporting
     *
     * @return the {@link IssueReporter} instance
     */
    public IssueReporter reporter() {
        return reporter;
    }
}
