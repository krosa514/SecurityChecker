public class VirusScanner implements Scanner {

    @Override
    public Report Scan() {
        ScanThread thread = new ScanThread();
        thread.start();
        thread.join();
        String result = thread.cmd_result;
        Report report = new Report();
        return report;
    }
}

class ScanThread extends Thread {
    
    protected String cmd_result;
    
    @Override
    public void run() {
        Process proc = Process("clamscan -i -v -r --log=logfile.txt /")
        proc.start();
        // Add line to get stdout from log file
    }
}