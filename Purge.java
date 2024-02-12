import java.io.File;

public class DeleteQuarantineFiles {
    public static void main(String[] args) {
        // Specify the path to the quarantine directory
        String quarantineDirectoryPath = "/path/to/quarantine";

        // Create a File object representing the quarantine directory
        File quarantineDirectory = new File(quarantineDirectoryPath);

        // Ensure the specified path points to a directory
        if (quarantineDirectory.isDirectory()) {
            // Get a list of files within the directory
            File[] files = quarantineDirectory.listFiles();

            // Delete each file in the directory
            if (files != null) {
                for (File file : files) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        System.out.println("Deleted file: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            } else {
                System.out.println("No files found in the quarantine directory.");
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }
    }
}
