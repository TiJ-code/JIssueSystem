package dk.tij.jissuesystem.api;

import java.util.Set;

/**
 * Interface for parsing labels from a raw string input.
 *
 * <p>Implementations are responsible for converting arbitrary string
 * representations into a set of @{@link Label} objects.</p>
 *
 * @since 0.2.0
 */
public interface ILabelParser {
    /**
     * Parses a raw string of labels into a set of {@link Label} objects.
     *
     * @param rawLabels the raw string containing label information
     * @return a set of {@link Label} objects
     */
    Set<Label> parse(String rawLabels);
}
