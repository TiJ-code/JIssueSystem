package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.IPayloadBuilder;
import dk.tij.jissuesystem.api.Issue;
import dk.tij.jissuesystem.utils.NetUtils;

import java.util.stream.Collectors;

/**
 * Builds JSON payloads for creating GitHub issues via the {@code repository_dispatch} event.
 *
 * <p>This class is a singleton and provides a static {@link #build(Issue)} method
 * for convenience.
 *
 * <p>The payload includes:
 * <ul>
 *     <li>Issue title</li>
 *     <li>Issue body</li>
 *     <li>Labels as a JSON array</li>
 * </ul>
 *
 * @since 0.2.0
 */
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

    /**
     * Builds a JSON payload for the given issue.
     *
     * @param issue the issue to build a payload for
     * @return a JSON string representing the issue
     */
    public static String build(Issue issue) {
        return INSTANCE.buildPayload(issue);
    }

    @Override
    public String buildPayload(Issue issue) {
        final String labelPayload = issue.labels()
                .stream()
                .map(l -> "\"" + NetUtils.escape(l.name()) + "\"")
                .collect(Collectors.joining(","));

        return String.format(JSON_PAYLOAD,
                NetUtils.escape(issue.title()),
                NetUtils.escape(issue.body()),
                labelPayload
        );
    }
}
