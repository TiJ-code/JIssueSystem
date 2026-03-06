module dk.tij.jissuesystem {
    requires java.net.http;

    exports dk.tij.jissuesystem;
    exports dk.tij.jissuesystem.provider;
    exports dk.tij.jissuesystem.provider.github;
    exports dk.tij.jissuesystem.api;
    exports dk.tij.jissuesystem.core;
}