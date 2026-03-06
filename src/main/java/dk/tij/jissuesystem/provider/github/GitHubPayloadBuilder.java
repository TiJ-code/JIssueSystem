package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.IPayloadBuilder;
import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.utils.NetUtils;

import java.util.stream.Collectors;

public class GitHubPayloadBuilder implements IPayloadBuilder {
    private GitHubPayloadBuilder() {}

    private static final GitHubPayloadBuilder INSTANCE = new GitHubPayloadBuilder();

    private static final String JSON_PAYLOAD = """
    {
        "event_type": "create-issue",
        "client_payload": {
            "title": "%s",
            "body": "%s",
            "labels": [%s]
        }
    }
    """;

    public static String build(Issue issue) {
        return INSTANCE.buildPayload(issue);
    }

    @Override
    public String buildPayload(Issue issue) {
        final String labelPayload = issue.labels()
                .stream()
                .map(l -> String.format("\"%s\"", l))
                .collect(Collectors.joining(","));

        return String.format(JSON_PAYLOAD,
                NetUtils.escape(issue.title()),
                NetUtils.escape(issue.body()),
                NetUtils.escape(labelPayload)
        );
    }
}
