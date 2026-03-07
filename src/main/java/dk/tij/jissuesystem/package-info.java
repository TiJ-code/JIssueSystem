/**
 * The root package for the JIssueSystem library.
 *
 * <p>This package contains the main entry points and core components
 * for reporting issues to external providers such as GitHub.</p>
 *
 * <p>Typical usage:
 * <pre>{@code
 * IssueReporter reporter = IssueReporter.builder()
 *         .provider(IssueProviderType.GITHUB, "owner", "repo", token)
 *         .contract(LabelContract.githubDefault())
 *         .build();
 * reporter.initialise().join();
 * reporter.report(new Issue.Builder().title("Bug").body("Details").build())
 * }</pre></p>
 *
 * @since 0.1.0
 */
package dk.tij.jissuesystem;