import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMoveExample {
    public static void main(String[] args) {
        // Source file path
        String sourceFilePath = System.getProperty("user.home") + "/A.txt";

        // Destination directory path
        String destinationDirectoryPath = System.getProperty("user.home") + "/test";

        try {
            // Create Path objects for source file and destination directory
            Path sourcePath = Paths.get(sourceFilePath);
            Path destinationDirectory = Paths.get(destinationDirectoryPath);

            // If destination directory does not exist, create it
            if (!Files.exists(destinationDirectory)) {
                Files.createDirectories(destinationDirectory);
            }

            // Move the file
            Path destinationPath = destinationDirectory.resolve(sourcePath.getFileName());
            Files.move(sourcePath, destinationPath);

            System.out.println("File moved successfully.");
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
        }
    }
}

























