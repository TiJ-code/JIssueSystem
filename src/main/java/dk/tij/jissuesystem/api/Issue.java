package dk.tij.jissuesystem.api;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Issue {
    private final String title;
    private final String body;
    private final Set<Label> labels;

    private Issue(Builder b) {
        this.title = b.title;
        this.body = b.body;
        this.labels = b.labels;
    }

    public String title() {
        return title;
    }

    public String body() {
        return body;
    }

    public Set<Label> labels() {
        return labels;
    }

    public static class Builder {
        private String title;
        private String body;
        private Set<Label> labels;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder labels(Class<? extends ILabelParser> labelParser, String raw) {
            try {
                return labels(labelParser.getDeclaredConstructor().newInstance().parse(raw));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to instantiate label parser", e);
            }
        }

        public Builder labels(Set<Label> labels) {
            this.labels = Set.copyOf(labels);
            return this;
        }

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
