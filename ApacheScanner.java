import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ApacheScanner implements Scanner {

    protected Report myreport = null;
    protected ApacheScanThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start() {
        this.mythread = new ApacheScanThread();
        this.mythread.start();
    }

    @Override
    public void join() {
        try {
            this.mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.myreport = this.mythread.report;
    }

    @Override
    public String getName() {
        return "ApacheScanner";
    }

    @Override
    public Report getReport() {
        return this.myreport;
    }
}

class ApacheScanThread extends Thread {
    protected Report report;

    @Override
    public void run() {
        try {
            // Command to check Apache on port 80
            String apacheCmd = "netstat -tuln | grep ':80 '";
            // Command to check HTTPS and SSL configuration
            String sslCmd = "apache2ctl -S | grep -i 'ssl'";
            // Command to check the list of crypto suites
            String cryptoCmd = "apache2ctl -M | grep -i 'ssl_module'";
            // Command to check file owners and permission bits
            String permissionCmd = "ls -l /var/www";

            String[] commands = {apacheCmd, sslCmd, cryptoCmd, permissionCmd};
            StringBuilder output = new StringBuilder();
            this.report = new Report("");

            for (String cmd : commands) {
                Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                process.waitFor();
            }

            // Set the Report object in the Thread
            this.report = new Report(output.toString());
            System.out.println("Output:\n" + output.toString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
