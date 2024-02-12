import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class OpenVASScanner implements Scanner {

    protected Report myreport = null;
    protected OpenVASScanThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new OpenVASScanThread();
        this.mythread.start();
    }; 

    @Override
    public void join(){
        try {
            this.mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.myreport = this.mythread.report;
    }

    @Override
    public String getName(){
        return "OpenVASScanner";
    }

    @Override
    public Report getReport(){
        return this.myreport;
    }
}

class OpenVASScanThread extends Thread {
    protected Report report;

    @Override
    public void run() {
        try {
            // Run OpenVAS CLI to start a scan
            String cmd = "sudo omp -u <username> -w <password> --xml='<create_task><name>ScanName</name><config id=\"<config_id>\"/><target id=\"<target_id>\"/></create_task>'";
            // Needs edits
            
            ProcessBuilder processBuilder = new ProcessBuilder(cmd.split("\\s+"));
            
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

            // Edit
            Report rpt = new Report(sout);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
