package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.provider.IssueProviderType;

/**
 * Factory for creating {@link IIssueProvider} instances based on the provider type.
 *
 * <p>Supports instantiating providers using reflection. Validates constructor parameters
 * before creating the provider instance.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * IIssueProvider provider = IssueProviderFactory.create(
 *      IssueProviderType.GITHUB, "owner", "repo", "token"
 * };</pre>
 *
 * @since 0.2.0
 */
public class IssueProviderFactory {
    private IssueProviderFactory() {}

    /**
     * Creates an {@link IIssueProvider} for the given type, repository, and token.
     *
     * @param type  the type of the provider
     * @param owner the repository owner
     * @param repo  the repository name
     * @param token the personal access token or API token
     * @throws NullPointerException     if {@code type} is null
     * @throws IllegalArgumentException if {@code owner}, {@code repo}, or {@code token} are null or blank
     * @throws RuntimeException         if the provider cannot be instantiated
     * @return
     */
    public static IIssueProvider create(IssueProviderType type, String owner, String repo, String token) {
        if (type == null)
            throw new NullPointerException("type cannot be null");

        if (owner == null || owner.isBlank())
            throw new IllegalArgumentException("owner cannot be null or blank");

        if (repo == null || repo.isBlank())
            throw new IllegalArgumentException("owner cannot be null or blank");

        if (token == null || token.isBlank())
            throw new IllegalArgumentException("owner cannot be null or blank");

        Class<? extends IIssueProvider> providerClazz = type.providerClass;

        try {
            return providerClazz
                    .getDeclaredConstructor(String.class, String.class, String.class)
                    .newInstance(owner, repo, token);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate " + providerClazz.getName(), e);
        }
    }
}
