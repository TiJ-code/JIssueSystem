package dk.tij.jissuesystem.provider.generic;

import dk.tij.jissuesystem.api.ILabelParser;
import dk.tij.jissuesystem.api.Label;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link ILabelParser} used by {@link GenericProvider}.
 *
 * <p>This parser extracts label names from a raw JSON response using
 * a simple pattern match. The expected format is typical for many
 * REST APIs returning label objects containing a {@code "name"} field.</p>
 *
 * <p>The parsing strategy may be replaced at runtime using {@link #setParser(Function)}.</p>
 *
 * <p>This class is simple to allow easy customization for APIs with different label formats.</p>
 *
 * @since 0.2.0
 */
public class GenericLabelParser implements ILabelParser {
    private static final Pattern GENERIC_JSON_LABEL_PATTERN = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");

    private Function<String, Set<Label>> parser;

    /**
     * Creates a {@link GenericLabelParser} using the default parsing strategy.
     *
     * <p>The default strategy searches for JSON "name" fields and converts
     * them into {@link Label} instances.</p>
     */
    public GenericLabelParser() {
        this.parser = GenericLabelParser::defaultParser;
    }

    /**
     * Sets a custom parser function.
     *
     * <p>This allows the caller to define how raw label response data
     * should be converted into a set of {@link Label} objects.</p>
     *
     * @param parser a function converting a raw response string into a {@link Set} of {@link Label}
     */
    public void setParser(Function<String, Set<Label>> parser) {
        this.parser = parser;
    }

    /**
     * Parses the raw label response into a set of {@link Label} instances.
     *
     * @param rawLabels raw API response string
     * @return a set of parsed {@link Label} objects
     */
    @Override
    public Set<Label> parse(String rawLabels) {
        return parser.apply(rawLabels);
    }

    /**
     * Default parsing strategy that extracts "name" values from a JSON response.
     *
     * @param raw raw JSON containing labels
     * @return a set of {@link Label} extracted from the input
     */
    private static Set<Label> defaultParser(String raw) {
        Set<Label> labels = new HashSet<>();
        Matcher matcher = GENERIC_JSON_LABEL_PATTERN.matcher(raw);

        while (matcher.find()) {
            labels.add(new Label(matcher.group(1)));
        }

        return labels;
    }
}