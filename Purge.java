import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Purge {
    public static void main(String[] args) {
        // File to be deleted in the quarantine directory
        String fileToDelete = System.getProperty("user.home") + "/test/A.txt";

        // Command to delete the file using sudo rm
        String deleteCommand = "sudo rm " + fileToDelete;

        try {
            // Create a process builder for sudo rm command
            ProcessBuilder deleteProcessBuilder = new ProcessBuilder(deleteCommand.split("\\s+"));
            deleteProcessBuilder.redirectErrorStream(true);

            // Start the delete process
            Process deleteProcess = deleteProcessBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(deleteProcess.getInputStream()));

            // Read and print the output of the delete process
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the delete process to finish
            int exitCode = deleteProcess.waitFor();
            if (exitCode == 0) {
                System.out.println("File deleted successfully.");
            } else {
                System.err.println("Error deleting file.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
