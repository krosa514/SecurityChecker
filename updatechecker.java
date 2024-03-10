import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class updatechecker {

    public static void main(String[] args) {
        try {
            // Execute apt update command with sudo
            Process process = Runtime.getRuntime().exec("sudo apt update");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean updatesAvailable = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.toLowerCase().contains("upgradable")) {
                    updatesAvailable = true;
                }
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Check if there were any errors
            if (exitCode != 0) {
                System.err.println("Error occurred while checking for updates.");
            } else {
                if (updatesAvailable) {
                    System.out.println("Updates are available. You may need to update your system.");
                } else {
                    System.out.println("Your system is up to date.");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
