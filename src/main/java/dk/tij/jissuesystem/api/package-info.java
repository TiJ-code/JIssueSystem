/**
 * Contains the public API interfaces and data classes for the JIssueSystem library.
 *
 * <p>This package defines contracts for issue providers, label parsing,
 * payload building, and the core {@link dk.tij.jissuesystem.api.Issue} and {@link dk.tij.jissuesystem.api.Label}
 * objects.</p>
 *
 * <p>All provider implementations should implement {@link dk.tij.jissuesystem.api.IIssueProvider} to integrate
 * with {@link dk.tij.jissuesystem.core.IssueReporter}</p>
 *
 * @since 0.2.0
 */
package dk.tij.jissuesystem.api;