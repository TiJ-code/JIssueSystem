package dk.tij.jissuesystem.provider.generic;

import dk.tij.jissuesystem.api.IPayloadBuilder;
import dk.tij.jissuesystem.api.Issue;

import java.util.function.Function;

/**
 * Default implementation of {@link IPayloadBuilder} used by {@link GenericProvider}.
 *
 * <p>This builder generates a simple JSON payload containing the issue's title
 * and body. This is suitable for most APIs that accept a minimal issue representation.</p>
 *
 * <p>Consumers may provide a custom payload generation function to adapt
 * to different API requirements using {@link #setBuilder(Function)}.</p>
 *
 * @since 0.2.0
 */
public class GenericPayloadBuilder implements IPayloadBuilder {
    private Function<Issue, String> builder;

    /**
     * Creates a {@link GenericPayloadBuilder} using the default
     * payload generation strategy.
     *
     * <p>The default strategy produces a JSON object with "title" and "body"
     * fields.</p>
     */
    public GenericPayloadBuilder() {
        this.builder = GenericPayloadBuilder::defaultBuilder;
    }

    /**
     * Sets a custom function for building issue payloads.
     *
     * @param builder a function that converts an {@link Issue} instance
     *                into a request payload string
     */
    public void setBuilder(Function<Issue, String> builder) {
        this.builder = builder;
    }

    /**
     * Builds the payload for a given issue.
     *
     * <p>This method applies either the default or custom builder function
     * to generate the request body string.</p>
     *
     * @param issue the issue to convert into a payload
     * @return the resulting payload string
     */
    @Override
    public String buildPayload(Issue issue) {
        return builder.apply(issue);
    }

    /**
     * Default payload builder function.
     *
     * @param issue the issue to convert
     * @return a JSON string containing the issue's title and body
     */
    private static String defaultBuilder(Issue issue) {
        return """
        {
            "title": "%s",
            "body": "%s"
        }
        """.formatted(issue.title(), issue.body());
    }
}