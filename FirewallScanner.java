import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
public class FirewallScanner implements Scanner {

    protected Report myreport = null;
    protected FirewallScanThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new FirewallScanThread();
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
        return "FirewallScanner";
    }
    @Override
    public Report getReport(){
        return this.myreport;
    }
}

class FirewallScanThread extends Thread {
    protected Report report;

    @Override
    public void run() {
        try {
            // Run ufw (UncomplicatedFirewall) for scanner
            String cmd = "sudo ufw status verbose";
         // String[] command = {"bash", "-c", cmd + " | tee results/FirewallScanResults.txt"};
            String[] command = {"bash", "-c", cmd};
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            boolean isPortInfo = false;
            this.report = new Report("");

        while ((line = reader.readLine()) != null) { 
        output.append(line).append("\n");

        if (line.trim().endsWith("Anywhere") || line.trim().contains("Anywhere (v6)")) {
            isPortInfo = true;
            // Parse port information
            String[] tokens = line.trim().split("\\s+");
       
            if (tokens.length >= 2) {
                try {
                    String portInfo = tokens[0];
                    int slashIndex = portInfo.indexOf('/');
                    int port = Integer.parseInt(slashIndex != -1 ? portInfo.substring(0, slashIndex) : portInfo);

                    System.out.println("Detected Port: " + port);

                    if (!isValidPort(port)) {
                        report.setOpenPorts(report.getOpenPorts() + port + ", ");
                        System.out.println("Unnecessary Port Detected: " + port);
                    } else {
                        System.out.println("Valid Port for web server: " + port);
                    }
                } catch (NumberFormatException e) {
                    // Print the exception for troubleshooting
                    System.out.println("Error parsing port: " + e.getMessage());
                }       
 
            }
        }
    }
    
            // Set the Report object in the Thread
            this.report = new Report(output.toString());
            report.setOpenPorts(report.getOpenPorts().length() > 2 ?
                    report.getOpenPorts().substring(0, report.getOpenPorts().length() - 2) :
                    "No open ports detected");

            int exitCode = process.waitFor();
            System.out.println("Output:\n" + output.toString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidPort(int port) {
        // Replace this with your actual list of valid ports
        // Add more valid ports
        int[] validPorts = {80, 443};

        for (int validPort : validPorts) {
            if (port == validPort) {
                return true;
            } else if (port > 1024) {
                return false;
            }
        }

        return true;
    }
}

