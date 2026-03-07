import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {
    public static String getToken() {
        String token = System.getenv("PAT_TOKEN");
        if (token != null)
            return token;

        try {
            return Files.readString(Path.of(".env"));
        } catch (Exception _) {}
        return null;
    }


}
