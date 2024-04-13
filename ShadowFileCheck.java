import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShadowFileCheck implements Scanner {
    
    protected Report myreport;
    protected ShadowFileCheckThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new ShadowFileCheckThread();
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
        return "ShadowFilePasswordCheck";
    }
    @Override
    public Report getReport(){
        return this.myreport;
    }

}

class ShadowFileCheckThread extends Thread {
    protected static Report report; 

    @Override
    public void run() {
        runHashcat("sudo hashcat -a 0 -m 1800 /etc/shadow rockyou.txt", false);

        runHashcat("sudo hashcat -a 0 -m 1800 /etc/shadow rockyou.txt --show", true);
    }

    private static void runHashcat(String command, boolean printOutput) {
        try {
            String[] hashcatCommand = {"bash", "-c", command};

            ProcessBuilder processBuilder = new ProcessBuilder(hashcatCommand);

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (line.matches("^\\$\\d\\$.+:.+")) {
                    String[] parts = line.split(":", 2);
                    printWarning("Password found for hash " + parts[0] + ": " + parts[1]);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            String sout = output.toString();
            System.out.println("Output:\n" + sout);

            report = new Report(sout);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("ShadowFileCheck.txt"))) {
                writer.write(sout);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void printWarning(String message) {
        System.out.println("Warning: " + message);
    }
}