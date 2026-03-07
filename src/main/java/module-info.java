/**
 * Module descriptor for JIssueSystem library.
 *
 * <p>Exports the API and core functionality for external consumption</p>
 *
 * <p>Requires:</p>
 * <ul>
 *     <li>{@code java.net.http} - for HTTP communication with issue providers</li>
 * </ul>
 */
module dk.tij.jissuesystem {
    requires java.net.http;

    exports dk.tij.jissuesystem;
    exports dk.tij.jissuesystem.api;
    exports dk.tij.jissuesystem.core;
    exports dk.tij.jissuesystem.utils;
    exports dk.tij.jissuesystem.provider;
    exports dk.tij.jissuesystem.provider.github;
}