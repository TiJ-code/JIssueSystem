package dk.tij.jissuesystem;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class JIssueSystem {
    private final String repoOwner;
    private final String repoName;
    private final String pat;

    public JIssueSystem(String repoOwner, String repoName, String pat) {
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.pat = pat;
    }

    public static CompletableFuture<Integer> report(String repoOwner, String repoName, String pat, String title, String body) {
        return new JIssueSystem(repoOwner, repoName, pat).report(title, body);
    }

    public final CompletableFuture<Integer> report(String title, String body) {
        final String url = String.format("https://api.github.com/repos/%s/%s/dispatches", repoOwner, repoName);

        String escapedTitle = escape(title);
        String escapedBody = escape(body + getDiagnostics());

        String jsonPayload = String.format("""
    {
        "event_type": "create-issue",
        "client_payload": {
            "title": "%s",
            "body": "%s"
        }
    }
    """, escapedTitle, escapedBody);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + pat.trim())
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("Content-Type", "application/json")
                .header("User-Agent", "JIssueSystem-Library")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    System.out.println(response);
                    if (response.statusCode() >= 400) {
                        System.err.println("GitHub Message: " + response.body());
                    }
                    return response.statusCode();
                });
    }

    private static String getDiagnostics() {
        return String.format("""
        \n
        ---
        **Environment Info:**
        * **OS:** %s (%s)
        * **Java:** %s (%s)
        """,
            System.getProperty("os.name"), System.getProperty("os.arch"),
            System.getProperty("java.version"), System.getProperty("java.vendor")
        );
    }

    private static String escape(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
