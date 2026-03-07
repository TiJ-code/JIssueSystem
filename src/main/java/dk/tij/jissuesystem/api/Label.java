package dk.tij.jissuesystem.api;

/**
 * Represents a label associated with an {@link Issue}.
 *
 * <p>This is an immutable value class that holds the name of a label.
 * Labels are typically used to categorise or tag issues in an issue tracking system,
 * for example "bug", "enhancement", or "automated-report".</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * Label bugLabel = new Label("bug");
 * Label automatedLabel = new Label("automated-report");
 * }</pre>
 *
 * @param name the name of the label
 * @since 0.2.0
 */
public record Label(String name) {
}