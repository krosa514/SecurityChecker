public class ApacheScanner implements Scanner {

    @Override
    public Report Scan() {
        Report report = new Report();
        return report;
    }
    public void start(){} //call the Scan when you run using a thread, set a report data attribute once report is generated
    public void join(){}
    public String getName(){
        return "0";
    }
    public Report getReport(){
        Report report = new Report();
        return report;
    }
}

class ApacheScanThread extends Thread {

}