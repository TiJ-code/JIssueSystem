/**
 * Core functionality for issue reporting and management.
 *
 * <p>This package includes:</p>
 * <ul>
 *     <li>{@link dk.tij.jissuesystem.core.IssueReporter} - a high-level reporter that handles labels,
 *     diagnostics, and dispatching issues to providers.</li>
 *     <li>{@link dk.tij.jissuesystem.core.IssueProviderFactory} - a factory to create
 *     {@link dk.tij.jissuesystem.api.IIssueProvider} implementations</li>
 *     <li>{@link dk.tij.jissuesystem.core.LabelContract} - defines required and allowed labels for a repository</li>
 * </ul>
 *
 * <p>Developers typically interact with this package to initialise reporters and
 * submit issues programmatically</p>
 *
 * @since 0.2.0
 */
package dk.tij.jissuesystem.core;