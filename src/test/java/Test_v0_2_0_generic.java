import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.api.Label;
import dk.tij.jissuesystem.core.IssueReporter;
import dk.tij.jissuesystem.core.LabelContract;
import dk.tij.jissuesystem.provider.generic.GenericProvider;
import dk.tij.jissuesystem.provider.github.GitHubLabelParser;
import dk.tij.jissuesystem.provider.github.GitHubPayloadBuilder;
import dk.tij.jissuesystem.utils.NetUtils;

import java.net.http.HttpResponse;

void main() {
    String pat = null;
    try {
        pat = Files.readString(Path.of(".env"));
    } catch (Exception _) {}

    URI labelsEndpoint = URI.create("https://api.github.com/repos/TiJ-code/JIssueSystem/labels");
    URI issueEndpoint  = URI.create("https://api.github.com/repos/TiJ-code/JIssueSystem/dispatches");

    GenericProvider provider = GenericProvider.builder()
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
                final String labelPayload = issue.labels()
                        .stream()
                        .map(l -> "\"" + NetUtils.escape(l.name()) + "\"")
                        .collect(Collectors.joining(","));

                return String.format("""
                {
                    "event_type": "create-issue",
                    "client_payload": {
                        "title": "%s",
                        "body": "%s",
                        "labels": [%s]
                    }
                }
                """,
                        NetUtils.escape(issue.title()),
                        NetUtils.escape(issue.body()),
                        labelPayload
                );
            })
            .build();

    IssueReporter reporter = IssueReporter.builder()
            .provider(provider)
            .contract(LabelContract.DEFAULT_CONTRACT)
            .build();

    reporter.initialise().join();

    if (testLabelFetching(reporter)) {
        System.out.println("Fetching labels successful!");
    }

    var response = testReporting(reporter);
    if (response.statusCode() == 200 || response.statusCode() == 204) {
        System.out.println("Reporting successful!");
    } else {
        System.err.println(response.body());
    }
}

HttpResponse<String> testReporting(IssueReporter reporter) {
    Issue issueToReport = new Issue.Builder()
            .title("Generic Provider Test")
            .body("Report")
            .labels(Set.of(new Label("bug")))
            .build();


    System.out.println(((GenericProvider)reporter.getProvider()).payloadBuilder.buildPayload(issueToReport));

    AtomicReference<HttpResponse<String>> response = new AtomicReference<>();
    reporter.report(issueToReport)
            .thenAccept(response::set)
            .join();

    return response.get();
}

boolean testLabelFetching(IssueReporter reporter) {
    var fetchedLabels = reporter.getProvider().fetchLabels().join();

    System.out.println(fetchedLabels.toString().replace(", ", "\n"));

    return fetchedLabels.contains(new Label("automated-report")) && fetchedLabels.size() > 3;
}