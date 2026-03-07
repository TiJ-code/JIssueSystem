import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {
    public static String getToken() {
        try {
            return Files.readString(Path.of(".env"));
        } catch (Exception _) {}
        return null;
    }


}
