import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.core.IssueReporter;
import dk.tij.jissuesystem.core.LabelContract;
import dk.tij.jissuesystem.provider.generic.GenericProvider;
import dk.tij.jissuesystem.utils.NetUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GenericProviderTest {
    static GenericProvider provider;
    static IssueReporter reporter;

    @BeforeAll
    static void setup() {
        String pat = TestUtils.getToken();

        URI labelsEndpoint = URI.create("https://api.github.com/repos/TiJ-code/JIssueSystem/labels");
        URI issueEndpoint  = URI.create("https://api.github.com/repos/TiJ-code/JIssueSystem/dispatches");

        provider = GenericProvider.builder()
                .owner("TiJ-code")
                .repo("JIssueSystem")
                .token(pat)
                .labelsEndpoint(labelsEndpoint)
                .issueEndpoint(issueEndpoint)
                .labelParser(raw -> {
                    Set<Label> labels = new HashSet<>();
                    Matcher matcher = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"").matcher(raw);
                    while (matcher.find()) {
                        labels.add(new Label(matcher.group(1)));
                    }
                    return labels;
                })
                .payloadBuilder(issue -> {
                    String labelPayload = issue.labels().stream()
                            .map(l -> "\"" + NetUtils.escape(l.name()) + "\"")
                            .collect(Collectors.joining(","));
                    return String.format("""
                            {
                                "event_type": "create-issue",
                                "client_payload": {
                                    "title": "%s",
                                    "body": "%s",
                                    "labels": [%s],
                                    "test_mode": "true"
                                }
                            }
                            """, NetUtils.escape(issue.title()), NetUtils.escape(issue.body()), labelPayload);
                })
                .build();

        reporter = IssueReporter.builder()
                .provider(provider)
                .contract(LabelContract.DEFAULT_CONTRACT)
                .build();

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
                .title("Generic Provider Test")
                .body("Report")
                .labels(Set.of(new Label("bug")))
                .build();

        AtomicReference<HttpResponse<String>> response = new AtomicReference<>();
        reporter.report(issueToReport)
                .thenAccept(response::set)
                .join();

        int status = response.get().statusCode();
        assertTrue(status == 200 || status == 204, "Reporting must succeed with 200 or 204");
    }
}
