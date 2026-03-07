package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.Label;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LabelContract {
    public static final LabelContract DEFAULT_CONTRACT = new LabelContract(Set.of("automated-report"));

    private final Set<String> requiredLabels;

    public LabelContract(Set<String> requiredLabels) {
        this.requiredLabels = Set.copyOf(requiredLabels);
    }

    public void validate(Set<Label> repoLabels) {
        Set<String> existing = repoLabels.stream()
                .map(Label::name)
                .collect(Collectors.toSet());
        Set<String> missing = requiredLabels.stream()
                .filter(l -> !existing.contains(l))
                .collect(Collectors.toSet());

        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Repository missing required labels: " + String.join(", ", missing)
            );
        }
    }

    public LabelContract concat(LabelContract other) {
        Set<String> concat = new HashSet<>(requiredLabels);
        concat.addAll(other.requiredLabels);
        return new LabelContract(concat);
    }

    public boolean isAllowed(Label label) {
        return requiredLabels.contains(label.name());
    }

    public Set<String> getRequiredLabels() {
        return requiredLabels;
    }

    public static LabelContract githubDefault() {
        return DEFAULT_CONTRACT.concat(new LabelContract(Set.of("enhancement", "bug", "duplicate")));
    }
}
