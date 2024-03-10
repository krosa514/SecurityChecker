import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Apache2Check implements Scanner {

    protected Report myreport = null;
    protected Apache2CheckThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start() {
        this.mythread = new Apache2CheckThread();
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
        return "Apache2Check";
    }

    @Override
    public Report getReport() {
        return this.myreport;
    }
}

class Apache2CheckThread extends Thread {
    protected Report report;

    @Override
    public void run() {
        try {
            String apacheUsername = "www-data";

            boolean hasNologin = hasNologinShell(apacheUsername);

            String result = hasNologin ? "The Apache2 account has nologin." : "The Apache2 account does not have nologin.";
            System.out.println("Output:\n" + result);
            Report rpt = new Report(result);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasNologinShell(String username) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/etc/passwd"));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(":");
            if (fields.length >= 7 && fields[0].equals(username) && fields[6].equals("/usr/sbin/nologin")) {
                reader.close();
                return true;
            }
        }

        reader.close();
        return false;
    }
}
