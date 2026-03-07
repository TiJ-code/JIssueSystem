package dk.tij.jissuesystem.api;

/**
 * Base implementation for {@link IIssueProvider} implementations that
 * authenticate using a token.
 *
 * <p>This abstract class stores the common repository context required by most
 * issue tracking APIs:</p>
 *
 * <ul>
 *     <li><b>owner</b> - The repository owner, organisation, or namespace.</li>
 *     <li><b>repo</b> - The repository or project name.</li>
 *     <li><b>token</b> - The authentication token used for API requests.</li>
 * </ul>
 *
 * <p>Subclasses are responsible for implementing the actual communication
 * with the issue provider API.</p>
 *
 * <p>This class performs basic validation to ensure none of the required
 * values are {@code null} or blank.</p>
 *
 * <p>Typical subclasses include providers for services such as GitHub,
 * GitLab, or custom APIs.</p>
 *
 * @see IIssueProvider
 */
public abstract class AbstractTokenProvider implements IIssueProvider {
    /** Repository owner or organisation name */
    protected final String owner;
    /** Repository or project name */
    protected final String repo;
    /** Authentication token used for API requests */
    protected final String token;

    /**
     * Creates a new token-based issue providers.
     *
     * @param owner the repository owner, organisation, or namespace
     * @param repo  the repository or project name
     * @param token the authentication token used to access the provider API
     *
     * @throws IllegalStateException if any parameter is {@code null} or blank
     */
    protected AbstractTokenProvider(String owner, String repo, String token) {
        if (owner == null || owner.isBlank())
            throw new  IllegalArgumentException("owner cannot be null or blank");

        if (repo == null || repo.isBlank())
            throw new  IllegalArgumentException("repo cannot be null or blank");

        if (token == null || token.isBlank())
            throw new  IllegalArgumentException("token cannot be null or blank");

        this.owner = owner;
        this.repo = repo;
        this.token = token;
    }
}
