import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.core.IssueProviderFactory;
import dk.tij.jissuesystem.core.IssueReporter;
import dk.tij.jissuesystem.core.LabelContract;
import dk.tij.jissuesystem.provider.IssueProviderType;
import dk.tij.jissuesystem.provider.github.GitHubProvider;

import java.net.http.HttpResponse;

void main() {
    String pat = null;
    try {
        pat = Files.readString(Path.of(".env"));
    } catch (Exception _) {}

    IssueReporter reporter = IssueReporter.builder()
            .provider(IssueProviderType.GITHUB, "TiJ-code", "JIssueSystem", pat)
            .contract(LabelContract.DEFAULT_CONTRACT)
            .build();

    reporter.initialise().join();

    if (testLabelFetching(reporter)) {
        System.out.println("Fetching labels successful!");
    }

    if (testReporting(reporter)) {
        System.out.println("Reporting successful!");
    }
}

boolean testReporting(IssueReporter reporter) {
    Issue issueToReport = new Issue.Builder()
            .title("Test")
            .body("Report")
            .labels(Set.of(new Label("bug")))
            .build();

    AtomicInteger statusCode = new AtomicInteger(-1);
    reporter.report(issueToReport)
            .thenAccept(res -> statusCode.set(res.statusCode()))
            .join();

    return statusCode.get() < 400 ;
}

boolean testLabelFetching(IssueReporter reporter) {
    var fetchedLabels = reporter.getProvider().fetchLabels().join();

    System.out.println(fetchedLabels.toString().replace(", ", "\n"));

    return fetchedLabels.contains(new Label("automated-report")) && fetchedLabels.size() > 3;
}