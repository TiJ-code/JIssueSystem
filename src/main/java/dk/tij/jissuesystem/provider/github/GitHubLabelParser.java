package dk.tij.jissuesystem.provider.github;

import dk.tij.jissuesystem.api.Label;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubLabelParser {

    private static final Pattern NAME_PATTERN =
            Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");

    public static Set<Label> parse(String json) {
        Set<Label> labels = new HashSet<>();

        Matcher matcher = NAME_PATTERN.matcher(json);

        while (matcher.find()) {
            labels.add(new Label(matcher.group(1)));
        }

        return labels;
    }
}