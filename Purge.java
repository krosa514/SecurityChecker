import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Purge {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Ask the user for the path to purge
            System.out.print("Enter the path to purge: ");
            String path = reader.readLine();

            File file = new File(path);
            if (!file.exists()) {
                System.err.println("Error: File or directory does not exist.");
                return;
            }

            String deleteCommand;
            if (file.isDirectory()) {
                // If it's a directory, delete it and its contents using sudo rm -rf
                deleteCommand = "sudo rm -rf " + path;
            } else {
                // If it's a file, delete the file using sudo rm
                deleteCommand = "sudo rm " + path;
            }

            // Create a process builder for the delete command
            ProcessBuilder deleteProcessBuilder = new ProcessBuilder(deleteCommand.split("\\s+"));
            deleteProcessBuilder.redirectErrorStream(true);

            // Start the delete process
            Process deleteProcess = deleteProcessBuilder.start();
            reader = new BufferedReader(new InputStreamReader(deleteProcess.getInputStream()));

            // Read and print the output of the delete process
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the delete process to finish
            int exitCode = deleteProcess.waitFor();
            if (exitCode == 0) {
                System.out.println("File or directory deleted successfully.");
            } else {
                System.err.println("Error deleting file or directory.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

