package dk.tij.jissuesystem.core;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.provider.IssueProviderType;

public class IssueProviderFactory {
    private IssueProviderFactory() {}

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
