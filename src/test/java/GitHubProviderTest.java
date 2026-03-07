import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.core.IssueReporter;
import dk.tij.jissuesystem.core.LabelContract;
import dk.tij.jissuesystem.provider.IssueProviderType;
import dk.tij.jissuesystem.provider.github.GitHubPayloadBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class GitHubProviderTest {

    static IssueReporter reporter;

    @BeforeAll
    static void setup() throws Exception {
        String pat = TestUtils.getToken();

        reporter = IssueReporter.builder()
                .provider(IssueProviderType.GITHUB, "TiJ-code", "JIssueSystem", pat)
                .contract(LabelContract.DEFAULT_CONTRACT)
                .build();

        reporter.getProvider().payloadBuilder(issue -> {
            String payload = GitHubPayloadBuilder.build(issue);
            payload = payload.replaceFirst("\\}\\s*\\}$", ",\"test_mode\":\"true\"}}");
            return payload;
        });

        reporter.initialise().join();
    }

    @Test
    void testLabelFetching() {
        var fetchedLabels = reporter.getProvider().fetchLabels().join();

        assertTrue(fetchedLabels.contains(new Label("automated-report")), "Must contain automated-report label");
        assertTrue(fetchedLabels.size() > 3, "Must have more than 3 labels");
    }

    @Test
    void testReporting() {
        Issue issueToReport = new Issue.Builder()
                .title("Test")
                .body("Report")
                .labels(Set.of(new Label("bug")))
                .build();

        AtomicInteger statusCode = new AtomicInteger(-1);
        reporter.report(issueToReport)
                .thenAccept(res -> statusCode.set(res.statusCode()))
                .join();

        assertTrue(statusCode.get() < 400, "Status code must be less than 400");
    }
}