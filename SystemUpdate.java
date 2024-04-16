import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemUpdate implements Scanner {

    protected Report myreport;
    protected SystemUpdateCheckThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new SystemUpdateCheckThread();
        this.mythread.start();
    }; 

    @Override
    public void join(){
        try {
            this.mythread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.myreport = this.mythread.report;
    }

    @Override
    public String getName(){
        return "SystemUpdateCheck";
    }
    @Override
    public Report getReport(){
        return this.myreport;
    }
}



class SystemUpdateCheckThread extends Thread {
    protected Report report;
    StringBuilder result = new StringBuilder();

    @Override
    public void run() { 
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
                    result.append("Updates are available. You may need to update your system. Please run the command sudo apt list --upgradable to see them");
                } else {
                    System.out.println("Your system is up to date.");
                    result.append("Your System is up to date");
                }
            }
            this.report = new Report(result.toString());
            this.report.setName("System Update Check");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}