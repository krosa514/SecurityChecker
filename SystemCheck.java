import java.io.IOException;

public interface SystemCheck {
    public Report Check() throws IOException;
    public void start();
    public void join();
    public String getName();
    public Report getReport();
}