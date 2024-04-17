import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.regex.*;
import java.io.*;

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
    StringBuilder result = new StringBuilder();
    @Override
    public void run() {
        try {
            // Command to run firewall scan
            String cmd = "nmap -sV --script vulners localhost";

            String[] command = { "bash", "-c", cmd };

            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean vulnerabilitiesDetected = false;
            String port = "";
            while ((line = reader.readLine()) != null) {
                if (line.matches("^\\d+/tcp.*")) {
                    port = line.split("/")[0];
                }
                if (line.contains("vulners:") && !port.isEmpty()) {
                    vulnerabilitiesDetected = true;
                    System.out.println("Vulnerabilities detected on port " + port);
                    result.append("Vulnerabilities detected on port " + port +  "\n" );
                }
            }

            // Print message if no vulnerabilities were detected
            if (!vulnerabilitiesDetected) {
                System.out.println("No vulnerabilities detected by NMap.");
                result.append("No vulnerabilities detected by NMap");
            }
            
            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
            this.report = new Report(result.toString());
            this.report.setName("Firewall Vulnerability Scanner");
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

