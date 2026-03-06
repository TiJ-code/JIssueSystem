package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.Label;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LabelContract {
    private final Set<String> requiredLabels;

    public LabelContract(Set<String> requiredLabels) {
        this.requiredLabels = Set.copyOf(requiredLabels);
    }

    public void validate(Set<Label> repoLabels) {
        Set<String> existing = repoLabels.stream()
                .map(Label::name)
                .collect(Collectors.toSet());

        for (String required : requiredLabels) {
            if (!existing.contains(required)) {
                throw new IllegalStateException(
                        "Repository missing required label: " + required
                );
            }
        }
    }

    public boolean isAllowed(String label) {
        return requiredLabels.contains(label);
    }
}
