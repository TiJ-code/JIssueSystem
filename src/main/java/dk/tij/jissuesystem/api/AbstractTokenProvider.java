package dk.tij.jissuesystem.api;

/**
 * Abstract base class for token-based {@link IIssueProvider} implementations.
 *
 * <p>Stores repository context and authentication token for subclasses. Provides
 * default implementations for managing payload builders and label parsers.</p>
 *
 * <p>Subclasses are responsible for implementing API-specific communication
 * to fetch labels and report issues.</p>
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

    /** Builder used to construct API request payloads */
    protected IPayloadBuilder payloadBuilder;
    /** Parser used to extract labels from API responses */
    protected ILabelParser labelParser;

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

    /**
     * Sets the payload builder used to construct API request payloads.
     *
     * @param payloadBuilder the {@link IPayloadBuilder} to use
     */
    @Override
    public void payloadBuilder(IPayloadBuilder payloadBuilder) {
        this.payloadBuilder = payloadBuilder;
    }

    /**
     * Sets the label parser used to parse labels from API responses.
     *
     * @param labelParser the {@link ILabelParser} to use
     */
    @Override
    public void labelParser(ILabelParser labelParser) {
        this.labelParser = labelParser;
    }

    /**
     * Returns the currently configured payload builder.
     *
     * @return the {@link IPayloadBuilder} instance
     */
    @Override
    public IPayloadBuilder payloadBuilder() {
        return payloadBuilder;
    }

    /**
     * Returns the currently configured label parser.
     *
     * @return the {@link ILabelParser} instance
     */
    @Override
    public ILabelParser labelParser() {
        return labelParser;
    }
}
