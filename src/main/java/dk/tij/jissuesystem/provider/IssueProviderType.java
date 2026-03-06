package dk.tij.jissuesystem.provider;

import dk.tij.jissuesystem.api.IIssueProvider;
import dk.tij.jissuesystem.provider.github.GitHubProvider;

public enum IssueProviderType {
    OTHER(null),
    GITHUB(GitHubProvider.class);

    public final Class<? extends IIssueProvider> providerClass;

    IssueProviderType(Class<? extends IIssueProvider> providerClass) {
        this.providerClass = providerClass;
    }
}
