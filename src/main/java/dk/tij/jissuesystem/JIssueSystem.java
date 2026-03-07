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
@Deprecated(since = "0.0.2")
public class JIssueSystem {
    private final IssueReporter reporter;

    public JIssueSystem(String repoOwner, String repoName, String pat) {
        this.reporter = IssueReporter.builder()
                .provider(IssueProviderType.GITHUB, repoOwner, repoName, pat)
                .contract(LabelContract.DEFAULT_CONTRACT)
                .build();
        this.reporter.initialise().join();
    }

    /**
     * Legacy static method, mimics old usage
     */
    @Deprecated(since = "0.0.2")
    public static CompletableFuture<Integer> report(String repoOwner, String repoName, String pat, String title, String body) {
        return new JIssueSystem(repoOwner, repoName, pat).report(title, body);
    }

    /**
     * Legacy static method, mimics old usage
     */
    @Deprecated(since = "0.0.2")
    public final CompletableFuture<Integer> report(String title, String body) {
        Issue issue = new Issue.Builder()
                .title(title)
                .body(body)
                .labels(Set.of(new Label("automated-report")))
                .build();

        return reporter.report(issue).thenApply(HttpResponse::statusCode);
    }
}
