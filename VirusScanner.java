import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
public class VirusScanner implements Scanner {

    //protected Report myreport = null;
    protected VirusScanThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new VirusScanThread();
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
        return "VirusScanner";
    }
    @Override
    public Report getReport(){
        return this.myreport;
    }
}

class VirusScanThread extends Thread {
    protected Report report;

    @Override
    public void run() {
        try {
            // Command to run clamav with sudo
            String cmd = "sudo clamscan -r /";

            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(cmd.split("\\s+"));

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

            // Your Report object (assuming Report class has a constructor that takes a String)
            Report rpt = new Report(sout);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}