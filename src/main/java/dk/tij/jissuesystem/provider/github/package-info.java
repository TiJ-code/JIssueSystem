/**
 * GitHub-specific implementations for JIssueSystem.
 *
 * <p>This package contains:
 * <ul>
 *     <li>{@link dk.tij.jissuesystem.provider.github.GitHubProvider} - communicates with GitHub repository
 *     dispatch events</li>
 *     <li>{@link dk.tij.jissuesystem.provider.github.GitHubLabelParser} - parses labels from GitHub API responses.</li>
 *     <li>{@link dk.tij.jissuesystem.provider.github.GitHubPayloadBuilder} - builds JSON payloads for
 *     repository_dispatch events.</li>
 * </ul></p>
 *
 * <p>These classes are used internally by {@link dk.tij.jissuesystem.core.IssueReporter}
 * when the GitHub provider is selected.</p>
 *
 * @since 0.2.0
 */
package dk.tij.jissuesystem.provider.github;