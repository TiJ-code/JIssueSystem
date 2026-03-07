package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.Label;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a contract of required labels for a repository.
 *
 * <p>Validates that certain labels exist and follows checking if a label is allowed
 * or merging with other contracts.</p>
 *
 * @since 0.2.0
 */
public class LabelContract {
    /**
     * Default contract including "automated-report" label.
     */
    public static final LabelContract DEFAULT_CONTRACT = new LabelContract(Set.of("automated-report"));

    private final Set<String> requiredLabels;

    /**
     * Creates a new contract with the specified required labels
     *
     * @param requiredLabels the set of label names required in the repository
     */
    public LabelContract(Set<String> requiredLabels) {
        this.requiredLabels = Set.copyOf(requiredLabels);
    }

    /**
     * Validates that all required labels exist in the given repository labels.
     *
     * @param repoLabels the set of labels in the repository
     * @throws IllegalStateException if any required label is missing
     */
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

    /**
     * Merges this contract with another, producing a new contract object.
     *
     * @param other the other contract
     * @return a new {@link LabelContract} containing all labels from both
     */
    public LabelContract concat(LabelContract other) {
        Set<String> concat = new HashSet<>(requiredLabels);
        concat.addAll(other.requiredLabels);
        return new LabelContract(concat);
    }

    /**
     * Checks whether a given label is allowed according to this contract.
     *
     * @param label the label to check
     * @return true if allowed, false otherwise
     */
    public boolean isAllowed(Label label) {
        return requiredLabels.contains(label.name());
    }

    /**
     * Returns all required label names.
     *
     * @return the set of required labels
     */
    public Set<String> getRequiredLabels() {
        return requiredLabels;
    }


    /**
     * Returns the default GitHub contract.
     *
     * @return a {@link LabelContract} with GitHub default labels
     */
    public static LabelContract githubDefault() {
        return DEFAULT_CONTRACT.concat(new LabelContract(Set.of("enhancement", "bug", "duplicate")));
    }
}
