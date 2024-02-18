import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
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
            String[] command = { "bash", "-c", cmd  + " | tee output.txt"};

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            String sout = output.toString();
            System.out.println("Output:\n" + sout);

            Report rpt = new Report(sout);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}