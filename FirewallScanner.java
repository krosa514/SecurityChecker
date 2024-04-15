import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileWriter;

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
            // Command to run firewall scan
            String cmd = "nmap --script vulners localhost";

            String[] command = { "bash", "-c", cmd };

            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Get the output as a string
            String sout = output.toString();
            System.out.println("Output:\n" + sout);

            //// Write the output to a text file//
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("FirewallResults.txt"))) {
                writer.write(sout);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create a Report object with the output
            report = new Report(sout);
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

