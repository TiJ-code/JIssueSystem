import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.core.IssueProviderFactory;
import dk.tij.jissuesystem.provider.IssueProviderType;
import dk.tij.jissuesystem.provider.github.GitHubProvider;

void main() {
    String pat = null;
    try {
        pat = Files.readString(Path.of(".env"));
    } catch (Exception _) {}

    GitHubProvider provider = (GitHubProvider) IssueProviderFactory.create(IssueProviderType.GITHUB,
                                                          "TiJ-code", "JIssueSystem", pat);

    var fetchedLabels = provider.fetchLabels().join();

    System.out.println(fetchedLabels.toString().replace(", ", "\n"));

    if (fetchedLabels.contains(new Label("automated-report")) && fetchedLabels.size() > 3)
        System.out.println("Successfully fetched labels!");

    Issue issueToReport = new Issue.Builder()
            .title("Test")
            .body("Report")
            .labels(Set.of(new Label("bug")))
            .build();

    provider.report(issueToReport)
            .thenAccept(System.out::println)
            .join();
}