package dk.tij.jissuesystem.provider;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.provider.github.GitHubProvider;

/**
 * Enum representing the types of issue providers supported by the system.
 *
 * <p>Each enum value may be associated with a concrete {@link IIssueProvider} implementation.
 * The factory {@link dk.tij.jissuesystem.core.IssueProviderFactory} uses this type to
 * instantiate the correct provider.
 *
 * <p>Example usage:
 * <pre>{@code
 * IIssueProvider provider = IssueProviderFactory.create(
 *     IssueProviderType.GITHUB, "owner", "repo", "token"
 * );
 * }</pre>
 *
 * @since 0.2.0
 */
public enum IssueProviderType {
    /** Placeholder for an unsupported or custom provider. */
    OTHER(null),
    /** GitHub issue provider. */
    GITHUB(GitHubProvider.class);

    /** The class implementing {@link IIssueProvider} for this type */
    public final Class<? extends IIssueProvider> providerClass;

    IssueProviderType(Class<? extends IIssueProvider> providerClass) {
        this.providerClass = providerClass;
    }
}
