import dk.tij.jissuesystem.JIssueSystem;
import dk.tij.jissuesystem.provider.github.GitHubPayloadBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JIssueSystemTest {
    @Test
    void testReportIssue() throws Exception {
        JIssueSystem issueSystem = new JIssueSystem("TiJ-code", "JIssueSystem", TestUtils.getToken());

        issueSystem.reporter().getProvider().payloadBuilder(issue -> {
            String payload = GitHubPayloadBuilder.build(issue);
            payload = payload.replaceFirst("\\}\\s*\\}$", ",\"test_mode\":\"true\"}}");
            return payload;
        });

        var future = issueSystem.report("Test", "Hallo");

        Integer result = future.join();

        Assertions.assertNotNull(result, "The report result should not be null");
        Assertions.assertTrue(result == 200 || result == 204, "Issue could not be created!");
    }
}