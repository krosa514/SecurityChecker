import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Quarantine {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
        // Source file path
        String sourceFilePath = System.getProperty("user.home") + "/A.txt";

        // Destination directory path
        String destinationDirectoryPath = System.getProperty("user.home") + "/test";

        // Command to change file permissions using sudo
        String chmodCommand = "sudo chmod 777 " + sourceFilePath;

        // Command to move the file using sudo
        String moveCommand = "sudo mv " + sourceFilePath + " " + destinationDirectoryPath;

        try {
            // Create a process builder for chmod command
            ProcessBuilder chmodProcessBuilder = new ProcessBuilder(chmodCommand.split("\\s+"));
            chmodProcessBuilder.redirectErrorStream(true);

            // Start the chmod process
            // Start the chmod process
            Process chmodProcess = chmodProcessBuilder.start();
            BufferedReader chmodReader = new BufferedReader(new InputStreamReader(chmodProcess.getInputStream()));
            String chmodOutput;
            while ((chmodOutput = chmodReader.readLine()) != null) {
                System.out.println(chmodOutput);
            }
            int chmodExitCode = chmodProcess.waitFor();


            // Check if chmod was successful
            if (chmodExitCode != 0) {
                System.err.println("Error changing file permissions.");
                return; // Exit the program if chmod fails
            }

            // Create a process builder for move command
            ProcessBuilder moveProcessBuilder = new ProcessBuilder(moveCommand.split("\\s+"));
            moveProcessBuilder.redirectErrorStream(true);

            // Start the move process
            Process moveProcess = moveProcessBuilder.start();
            int moveExitCode = moveProcess.waitFor();

            // Check if move was successful
            if (moveExitCode == 0) {
                System.out.println("File moved successfully.");
            } else {
                System.err.println("Error moving file.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
