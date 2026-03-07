package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.provider.IssueProviderType;

import java.util.Objects;

/**
 * Factory for creating {@link IIssueProvider} instances.
 *
 * <p>The factory delegates provider construction to the
 * {@link IssueProviderType} enum, which supplies a
 * {@link dk.tij.jissuesystem.provider.IssueProviderType.ProviderCreator}
 * for each supported provider.</p>
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
public class IssueProviderFactory {
    private IssueProviderFactory() {}

    /**
     * Creates an {@link IIssueProvider} for the given type, repository, and token.
     *
     * @param type  the provider type
     * @param owner the repository owner or organisation
     * @param repo  the repository name
     * @param token the personal access token or API token
     * @throws UnsupportedOperationException if {@code code} is {@code NONE}
     * @throws NullPointerException          if {@code type} is {@code null}
     * @throws IllegalArgumentException      if {@code owner}, {@code repo}, or {@code token} are {@code null} or blank
     * @throws RuntimeException              if the provider cannot be instantiated
     * @return the created {@link IIssueProvider} instance
     */
    public static IIssueProvider create(IssueProviderType type, String owner, String repo, String token) {
        Objects.requireNonNull(type, "type cannot be null");

        if (IssueProviderType.NONE.equals(type)) {
            throw new UnsupportedOperationException("Cannot create an issue provider for a NONE");
        }

        return type.creator.create(owner, repo, token);
    }
}
