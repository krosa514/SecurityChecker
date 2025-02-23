import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        // Add more scanners

        ArrayList<Scanner> selectedScanners = new ArrayList<>();
        java.util.Scanner inputScanner = new java.util.Scanner(System.in);
        
        // Loop until exit command is received
        while (true) {
            //System.out.println("Type the name of the scanners you want to select (separated by spaces), 'help' for available scanners, or 'exit' to quit: ");
            String line = inputScanner.nextLine();
            
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                jsonBuilder.append("\n]");
                saveReport();
                System.exit(0);
            } else if (line.equalsIgnoreCase("help")) {
                // Clear the screen
                clearScreen();
                
                // Print available scanners
                System.out.println("Available scanners:");
                for (Scanner scanner : allScanners) {
                    System.out.println(scanner.getName());
                }
            } //else if (line.equalsIgnoreCase("purge")) {
                // Run the Purge command
                //runJavaFile("Purge");
             //else if (line.equalsIgnoreCase("recover")) {
                // Run the Recovery command
              //  runJavaFile("Recovery");
             else {
                // Process scanner selection
                String[] words = line.split(" ");
                for (String name : words) {
                    if (name.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting...");
                        jsonBuilder.append("\n]");
                        saveReport();
                        System.exit(0);
                    }
                    for (Scanner scanner : allScanners) {
                        if (scanner.getName().equalsIgnoreCase(name)) {
                            selectedScanners.add(scanner);
                            break;
                        }
                    }
                }
                return selectedScanners;
            }
        }
    }

    // Helper method to clear the screen
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Helper method to run a Java file
    private static void runJavaFile(String fileName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", fileName);
            pb.inheritIO(); // Redirects standard output and error to the current Java process
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Helper method to save the report
    private static void saveReport() {
        try (FileWriter writer = new FileWriter("all_reports.json", true)) {
            writer.write(jsonBuilder.toString());
            System.out.println("Reports generated and saved to all_reports.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main class
    public static void main(String[] args) {
        jsonBuilder.append("[\n");
        boolean first_record = true;
        
        String fileName = "all_reports.json";
        Path filePath = Paths.get(fileName);
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                System.out.println("Deleted existing all_reports.json file.");
            } catch (Exception e) {
                System.out.println("Error deleting existing all_reports.json file: " + e.getMessage());
            }
        }

        //String cmd = "sudo rm all_reports.json";
        while (true) {
            
            // Clear the screen
            System.out.print("\033[H\033[2J");  
            System.out.flush();  

            // Print the title
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("Vulnerability Vigil");
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
            System.out.println("Hello. Welcome to Vulnerability Vigil. We are here to help you keep yourself safe. We have a multitude of scanners and check for you to use. To run the scans and checks please write the commands. To do more than one check or scan write the commands separated by a single space. Please type help all undercase to get a list of the commands. Please Type exit all undercase if you wish to stop Vulnerability Vigil. Exit and open the About.txt to learn more about the scanners and project as a whole.");
            // Print instructions
           // System.out.println("If you want to find out the commands, type help.");

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
            }
            else {
                jsonBuilder.append(",\n"); 
                report.generateReport("all_reports.json", arrReports,jsonBuilder); 
            }
        }
    }
}
