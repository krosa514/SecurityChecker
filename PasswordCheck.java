import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PasswordCheck implements Scanner {
    
    protected Report myreport;
    protected PasswordCheckThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new PasswordCheckThread();
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
        return "MySQLPasswordCheck";
    }
    @Override
    public Report getReport(){
        return this.myreport;
    }
}


class PasswordCheckThread extends Thread {
    protected Report report;

    @Override
    public void run() { 
    try {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter the root password for MySQL server login: ");
        String rootPassword = scanner.nextLine();

        String command = String.format("mysql -u root -p%s -e 'SELECT user, host, authentication_string FROM mysql.user'", rootPassword);
        Process process = new ProcessBuilder("bash", "-c", command).start();
        

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();
        boolean hasDefaultPassword = false;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
            String[] userInfo = line.split("\\s+");
//          System.out.println(Arrays.toString(userInfo));
            if(userInfo.length == 2) {
                hasDefaultPassword = true;
                System.out.println("Warning: found a default password pattern in the authentication string for User: " + userInfo[0]);
            }
            if (userInfo.length >= 3) {
                if(userInfo[2].matches("(?i).*\\s*(NULL|'')\\s*.*")) {
                    hasDefaultPassword = true;
                    System.out.println("Warning: found a default password pattern in the authentication string for User: " + userInfo[0]);

                }
            }
        }
        

        int exitCode = process.waitFor();
        System.out.println("Exit Code: " + exitCode);
        String sout = output.toString();
        System.out.println("Output:\n" + sout);
        Report rpt = new Report(sout);

        scanner.close();
        
        if(!hasDefaultPassword) { 
            System.out.println("MySQL server setup is good. No default passwords.");
        }

    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        
    }
}


}