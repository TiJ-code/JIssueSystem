package dk.tij.jissuesystem;

import dk.tij.jissuesystem.utils.DeviceUtils;
import dk.tij.jissuesystem.utils.NetUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * @author tij
 * @since 0.1.0
 */
@Deprecated(since = "0.0.2")
public class JIssueSystem {
    private final String repoOwner;
    private final String repoName;
    private final String pat;

    public JIssueSystem(String repoOwner, String repoName, String pat) {
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.pat = pat;
    }

    @Deprecated(since = "0.0.2")
    public static CompletableFuture<Integer> report(String repoOwner, String repoName, String pat, String title, String body) {
        return new JIssueSystem(repoOwner, repoName, pat).report(title, body);
    }

    @Deprecated(since = "0.0.2")
    public final CompletableFuture<Integer> report(String title, String body) {
        final String url = String.format("https://api.github.com/repos/%s/%s/dispatches", repoOwner, repoName);

        String escapedTitle = NetUtils.escape(title);
        String escapedBody = NetUtils.escape(body + DeviceUtils.getDiagnostics());

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
}
