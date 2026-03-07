package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.Label;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing GitHub label JSON responses.
 *
 * <p>Extracts label names from GitHub API responses and converts them into {@link Label} objects.
 *
 * <p>Example usage:
 * <pre>{@code
 * Set<Label> labels = GitHubLabelParser.parse(jsonResponse);
 * }</pre>
 *
 * @since 0.2.0
 */
public class GitHubLabelParser {
    private static final Pattern NAME_PATTERN =
            Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");

    /**
     * Utility class for parsing GitHub label JSON responses.
     *
     * <p>Extracts label names from GitHub API responses and converts them into {@link Label} objects.
     *
     * <p>Example usage:
     * <pre>{@code
     * Set<Label> labels = GitHubLabelParser.parse(jsonResponse);
     * }</pre>
     *
     * @since 0.0.2
     */
    public static Set<Label> parse(String json) {
        Set<Label> labels = new HashSet<>();

        Matcher matcher = NAME_PATTERN.matcher(json);

        while (matcher.find()) {
            labels.add(new Label(matcher.group(1)));
        }

        return labels;
    }
}