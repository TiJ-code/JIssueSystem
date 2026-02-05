import dk.tij.jissuesystem.JIssueSystem;

void main() {
    String pat = null;
    try {
        pat = Files.readString(Path.of(".env"));
    } catch (Exception _) {}

    JIssueSystem issueSystem = new JIssueSystem("TiJ-code", "JIssueSystem", pat);
    issueSystem.report("Test", "Hallo")
            .thenAccept(status -> {
                System.out.println(status);
            }).join();
}