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
            StringBuilder output = new StringBuilder();
            this.report = new Report("");
    
            // Command to check Apache on port 80
            String apacheCmd = "netstat -tuln | grep ':80 '";
            Process apacheProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", apacheCmd});
            BufferedReader apacheReader = new BufferedReader(new InputStreamReader(apacheProcess.getInputStream()));
            String apacheLine;
            while ((apacheLine = apacheReader.readLine()) != null) {
                output.append("Port 80 is enabled: ").append(apacheLine).append("\n");
            }
            apacheProcess.waitFor();
    
            // Command to check HTTPS and SSL configuration
            String sslCmd = "apache2ctl -S | grep -i 'ssl'";
            Process sslProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", sslCmd});
            BufferedReader sslReader = new BufferedReader(new InputStreamReader(sslProcess.getInputStream()));
            String sslLine;
            while ((sslLine = sslReader.readLine()) != null) {
                output.append("SSL configured: ").append(sslLine).append("\n");
            }
            sslProcess.waitFor();
    
            // Command to check the list of crypto suites
            String cryptoCmd = "apache2ctl -M | grep -i 'ssl_module'";
            Process cryptoProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", cryptoCmd});
            BufferedReader cryptoReader = new BufferedReader(new InputStreamReader(cryptoProcess.getInputStream()));
            String cryptoLine;
            while ((cryptoLine = cryptoReader.readLine()) != null) {
                output.append("SSL module loaded: ").append(cryptoLine).append("\n");
            }
            cryptoProcess.waitFor();
    
            // Command to check file owners and permission bits
            String permissionCmd = "ls -l /var/www";
            Process permissionProcess = Runtime.getRuntime().exec(new String[]{"bash", "-c", permissionCmd});
            BufferedReader permissionReader = new BufferedReader(new InputStreamReader(permissionProcess.getInputStream()));
            String permissionLine;
            while ((permissionLine = permissionReader.readLine()) != null) {
                String[] parts = permissionLine.split("\\s+");
                if (parts.length >= 3) {
                    String fileOwner = parts[2];
                    String permissionBits = parts[0];
                    output.append("File owner: ").append(fileOwner).append(", Permission bits: ").append(permissionBits).append("\n");
                }
            }
            permissionProcess.waitFor();
    
            // Set the Report object in the Thread
            this.report = new Report(output.toString());
            System.out.println("Output:\n" + output.toString());
    
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
