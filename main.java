import java.util.ArrayList;
import java.io.*;
import java.util.*;

class VulnerabilityVigil {

    static StringBuilder jsonBuilder = new StringBuilder();

    protected static ArrayList<Scanner> selectScanner(){
        ArrayList<Scanner> allScanners = new ArrayList<>();
        Scanner vs = new VirusScanner();
        allScanners.add(vs);
        Scanner fws = new FirewallScanner();
        allScanners.add(fws);
        Scanner openvas = new ApacheScanner();
        allScanners.add(openvas);
        Scanner mysql = new PasswordCheck();
        allScanners.add(mysql);
        Scanner shadow = new ShadowFileCheck();
        allScanners.add(shadow);
        Scanner apache2 = new Apache2Check();
        allScanners.add(apache2);
        Scanner sysupdate = new SystemUpdate();
        allScanners.add(sysupdate);
        Scanner wafDet = new wafDetCheck();
        allScanners.add(wafDet);
        //add more

        ArrayList<Scanner> selectedScanners = new ArrayList<>();
        java.util.Scanner inputScanner = new java.util.Scanner(System.in);
        //StringBuilder jsonBuilder = new StringBuilder();
        while (true) {
            System.out.println("Type the name of the scanners you want to select (separated by spaces), 'help' for available scanners, or 'exit' to quit: ");
            String line = inputScanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                jsonBuilder.append("\n]");

                try (FileWriter writer = new FileWriter("all_reports.json", true)) {
                    writer.write(jsonBuilder.toString());
                    //System.out.println("Reports generated and saved to " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.exit(0);
            } else if (line.equalsIgnoreCase("help")) {
                // Clear the screen
                System.out.print("\033[H\033[2J");  
                System.out.flush();  
                // Print available scanners
                System.out.println("Available scanners:");
                for (Scanner scanner : allScanners) {
                    System.out.println(scanner.getName());
                }
            } else {
                String [] words = line.split(" ");
                for(String name : words){
                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting...");
                        jsonBuilder.append("\n]");

                        try (FileWriter writer = new FileWriter("all_reports.json", true)) {
                            writer.write(jsonBuilder.toString());
                            //System.out.println("Reports generated and saved to " + fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.exit(0);
                    }
                    //jsonBuilder.append(",");
                    for (Scanner scanner : allScanners) {
                        //jsonBuilder.append(",");
                        if (scanner.getName().equalsIgnoreCase(name)) {
                            selectedScanners.add(scanner);
                            break;
                        }
                    }
                    //jsonBuilder.append(",");
                }
                //jsonBuilder.append(","); 
                return selectedScanners;
            }
        }
    }
    
    // Main class
    public static void main(String[] args) {
        //StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        boolean first_record = true;
        while (true) {
            
            // Clear the screen
            System.out.print("\033[H\033[2J");  
            System.out.flush();  

            // Print the title
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("// Vulnerability Vigil");
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            
            // Print instructions
            System.out.println("If you want to find out the commands, type help.");

            //1. get the list of scanners to run
            ArrayList<Scanner> arrScanner = selectScanner();

            //2. run each scanner
            for(int i=0; i<arrScanner.size(); i++){
                arrScanner.get(i).start();
            }
            ArrayList<Report> arrReports = new ArrayList<>();
            for(int i=0; i<arrScanner.size(); i++){
                arrScanner.get(i).join();
                Report report = arrScanner.get(i).getReport();
                arrReports.add(report);
            }
            Report report = new Report();
            if (first_record == true) {
                first_record = false;
                report.generateReport("all_reports.json", arrReports,jsonBuilder);
            //jsonBuilder.append(","); 
            }
            else {
                jsonBuilder.append(",\n"); 
                report.generateReport("all_reports.json", arrReports,jsonBuilder); 
            }
        }
    }
}
