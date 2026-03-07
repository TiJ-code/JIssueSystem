package dk.tij.jissuesystem.api;

import java.util.Set;

/**
 * Represents an issue to report to a tracking system.
 *
 * <p>Immutable object constructed via {@link Builder}</p>
 *
 * @since 0.2.0
 */
public class Issue {
    private final String title;
    private final String body;
    private final Set<Label> labels;

    private Issue(Builder b) {
        this.title = b.title;
        this.body = b.body;
        this.labels = b.labels;
    }

    /**
     * Returns the title of the issue
     *
     * @return issue title
     */
    public String title() {
        return title;
    }

    /**
     * Returns the body of the issue
     *
     * @return issue body
     */
    public String body() {
        return body;
    }

    /**
     * Returns the labels associated with this issue
     *
     * @return a set of @{@link Label} objects
     */
    public Set<Label> labels() {
        return labels;
    }

    /**
     * Builder for creating {@link Issue} objects.
     */
    public static class Builder {
        private String title;
        private String body;
        private Set<Label> labels;

        /**
         * Sets the title of the issue
         *
         * @param title the issue title
         * @return this builder
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the body of the issue
         *
         * @param body the issue body
         * @return this builder
         */
        public Builder body(String body) {
            this.body = body;
            return this;
        }

        /**
         * Sets labels by parsing a raw string using a provided {@link ILabelParser}.
         *
         * @param labelParser the parser class
         * @param raw         the raw label string
         * @return this builder
         */
        public Builder labels(Class<? extends ILabelParser> labelParser, String raw) {
            try {
                return labels(labelParser.getDeclaredConstructor().newInstance().parse(raw));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to instantiate label parser", e);
            }
        }

        /**
         * Sets the labels directly.
         *
         * @param labels the set of @{@link Label} objects
         * @return this builder
         */
        public Builder labels(Set<Label> labels) {
            this.labels = Set.copyOf(labels);
            return this;
        }

        /**
         * Builds the {@link Issue} instance.
         *
         * @return a new {@link Issue}
         * @throws IllegalStateException if the title is null or blank
         */
        public Issue build() {
            if (title == null || title.isBlank())
                throw new IllegalStateException("title is required");

            if (body == null)
                body = "";

            if (labels == null)
                labels = Set.of();

            return new Issue(this);
        }
    }
}
