package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.ILabelParser;
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
public class GitHubLabelParser implements ILabelParser {
    private static final Pattern NAME_PATTERN =
            Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");

    /**
     * Creates a new {@link GitHubLabelParser} instance.
     *
     * <p>This parser can be used to extract label names from GitHub API JSON responses
     * and convert them into {@link Label} objects.</p>
     *
     * <p>Typically, instances are used with a {@link ILabelParser} reference
     * when configuring GitHub-related issue providers.</p>
     *
     * <p>Example usage:
     * <pre>{@code
     * ILabelParser parser = new GitHubLabelParser();
     * Set<Label> labels = parser.parse(jsonResponse);
     * }</pre>
     */
    public GitHubLabelParser() {}

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
     * @param json the JSON response content
     * @return a set of {@link Label} objects
     *
     * @since 0.0.2
     */
    @Override
    public Set<Label> parse(String json) {
        Set<Label> labels = new HashSet<>();

        Matcher matcher = NAME_PATTERN.matcher(json);

        while (matcher.find()) {
            labels.add(new Label(matcher.group(1)));
        }

        return labels;
    }
}