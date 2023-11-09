public class VirusScanner implements Scanner {

    protected Report myreport = null;
    protected ScanThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new ScanThread();
        this.mythread.start();
    }; 

    @Override
    public void join(){
        this.mythread.join();
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

class ScanThread extends Thread {
    protected Report report;
    @Override
    public void run() {
        String clamav = "/usr/bin/clamav";
        String cmd = clamav + " -v /usr/bin";
        Process proc = ProcessBuilder.start(cmd);
        proc.waitFor();
        //try to use buffer reader to read out all the output
        String sout = null;
        Report rpt = new Report();
        //set up rpt contents by parsing sout
    }
}