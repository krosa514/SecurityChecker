// Vulnerability Vigil

import java.util.ArrayList;
import java.io.*;
import java.util.*;

class VulnerabilityVigil {

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
        //add more

        ArrayList<Scanner> selectedScanners = new ArrayList<>();
        System.out.println("Type numeric values separated by spaces to select scanners: ");
        for(int i=0; i<allScanners.size(); i++){
            System.out.println(i + ": " + allScanners.get(i).getName());
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String line = scanner.nextLine();
        String [] words = line.split(" ");
        for(int i=0; i<words.length; i++){
            int idx =Integer.parseInt( words[i] );
            selectedScanners.add(allScanners.get(idx));
        }
        return selectedScanners;
    }
    
    // Main class
    public static void main(String[] args) {
        // main2();
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
        report.generateReport("all_reports.json", arrReports);
        // NEED a report function which writes an array of reports as one HTML 
        //3. generate the report
        //for (int i = 0; i < arrReports.size(); i++) {
        //    Report report = arrReports.get(i);
        //    String scannerName = "Scanner_" + (i + 1);
        //    if (report != null) {
        //        report.generateReport();
        //    } else {
        //        System.out.println("Report generation failed for scanner: " + scannerName);
        //   }
        //    System.out.println("REPORT is generated and located at: ");
        //}

            // Check if report is null
        //    if (report != null) {
                // Write the report to a separate text file for each scanner
        //        writeReportsToFile("results.txt", arrReports);
        //    } else {
        //       System.out.println("Report generation failed for scanner: " + scannerName);
        //    }
        //}

        
        
        //Report report = new Report("results");
        //report.generateReport("results.txt", arrReports);
        //Report reportInstance = new Report(); // Create an instance of Report class
        //reportInstance.generateReport(arrScanner, arrReports);
        //generateIndividualReports(arrScanner, arrReports);
    }
    
    
    /* 
    private static void generateIndividualReports(ArrayList<Scanner> arrScanner, ArrayList<Report> arrReports) {
    for (int i = 0; i < arrScanner.size(); i++) {
        Scanner scanner = arrScanner.get(i);
        Report report = arrReports.get(i);

        // Check if report is null
        if (report != null) {
            // Write the report to a separate text file for each scanner
            writeReportToFile(scanner.getName() + "_results.txt", report.toString());
        } else {
            System.out.println("Report generation failed for scanner: " + scanner.getName());
        }
    }

    System.out.println("Individual reports are generated.");
}
*/
    // Method to write report to a text file
    /* 
    private static void writeReportToFile(String fileName, String content) {
        fileName = "scan_results.txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(content);
            System.out.println("Report generated: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
}
