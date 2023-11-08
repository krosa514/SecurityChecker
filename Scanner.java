import java.io.IOException;

public interface Scanner {
    public Report Scan() throws IOException;
    public void start(); //call the Scan when you run using a thread, set a report data attribute once report is generated
    public void join();
    public String getName();
    public Report getReport();
}