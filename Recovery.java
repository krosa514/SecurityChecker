import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Recovery{
    public static void main(String[] args) {
        // File to be moved from the quarantine directory
        String fileToMove = System.getProperty("user.home") + "/test/A.txt";

        // Destination directory path
        String destinationDirectory = System.getProperty("user.home") + "/recovered/";

        // Command to move the file using sudo mv
        String moveCommand = "sudo mv " + fileToMove + " " + destinationDirectory;

        try {
            // Create the destination directory if it doesn't exist
            File recoveredDir = new File(destinationDirectory);
            if (!recoveredDir.exists()) {
                recoveredDir.mkdirs(); // Create the directory and any necessary parent directories
                System.out.println("Created 'recovered' directory.");
            }

            // Create a process builder for sudo mv command
            ProcessBuilder moveProcessBuilder = new ProcessBuilder(moveCommand.split("\\s+"));
            moveProcessBuilder.redirectErrorStream(true);

            // Start the move process
            Process moveProcess = moveProcessBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(moveProcess.getInputStream()));

            // Read and print the output of the move process
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the move process to finish
            int exitCode = moveProcess.waitFor();
            if (exitCode == 0) {
                System.out.println("File moved to 'recovered' directory successfully.");
            } else {
                System.err.println("Error moving file to 'recovered' directory.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
