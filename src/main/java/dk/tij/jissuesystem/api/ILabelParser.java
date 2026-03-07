package dk.tij.jissuesystem.api;

import java.util.Set;

/**
 * Functional interface for parsing labels from a raw string input.
 *
 * <p>Implementations are responsible for converting arbitrary string
 * representations into a set of {@link Label} objects.</p>
 *
 * <p>This is typically used when receiving labels from an API response
 * or user input that needs to be transformed into strongly-typed {@link Label} objects.</p>
 *
 * @since 0.2.0
 */
@FunctionalInterface
public interface ILabelParser {
    /**
     * Parses a raw string of labels into a set of {@link Label} objects.
     *
     * <p>Implementations may split the string, remove whitespace, and create
     * {@link Label} instances accordingly.</p>
     *
     * @param rawLabels the raw string containing label information, e.g., "bug, enhancement"
     * @return a set of {@link Label} objects representing the parsed labels
     */
    Set<Label> parse(String rawLabels);
}