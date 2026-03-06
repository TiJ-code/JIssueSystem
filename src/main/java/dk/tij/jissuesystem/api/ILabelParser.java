package dk.tij.jissuesystem.api;

import java.util.Set;

public interface ILabelParser {
    Set<Label> parse(String rawLabels);
}
