package dk.tij.jissuesystem.provider;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.provider.generic.GenericProvider;
import dk.tij.jissuesystem.provider.github.GitHubProvider;

/**
 * Enumerates the supported issue provider types.
 *
 * <p>Each value supplies a {@link ProviderCreator} used to instantiate
 * the corresponding {@link IIssueProvider} implementation.</p>
 *
 * <p>This enum is primarily used by
 * {@link dk.tij.jissuesystem.core.IssueProviderFactory} to create provider
 * instances without exposing implementation classes.</p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * IIssueProvider provider = IssueProviderFactory.create(
 *     IssueProviderType.GITHUB,
 *     "owner",
 *     "repo",
 *     "token"
 * );
 * }</pre>
 *
 * @since 0.2.0
 */
public enum IssueProviderType {
    /** Invalid issue provider type. */
    NONE(null),
    /** GitHub issue provider. */
    GITHUB(GitHubProvider::new);

    /** Factory used to create the provider instance for this type. */
    public final ProviderCreator creator;

    IssueProviderType(ProviderCreator creator) {
        this.creator = creator;
    }

    /**
     * Functional interface used to construct {@link IIssueProvider} instances.
     */
    @FunctionalInterface
    public interface ProviderCreator {
        /**
         * Creates a new issue provider.
         *
         * @param owner repository owner or organization
         * @param repo  repository name
         * @param token authentication token
         * @return a configured {@link IIssueProvider}
         */
        IIssueProvider create(String owner, String repo, String token);
    }
}
