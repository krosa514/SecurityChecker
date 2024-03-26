// Vulnerability Vigil

import java.io.IOException;
import java.util.ArrayList;

class VulnerabilityVigil {

    protected static ArrayList<Scanner> selectScanner(){
        ArrayList<Scanner> allScanners = new ArrayList<>();
        Scanner vs = new VirusScanner();
        allScanners.add(vs);
        Scanner fws = new FirewallScanner();
        allScanners.add(fws);
        Scanner apache = new ApacheScanner();
        allScanners.add(apache);
        Scanner mysql = new PasswordCheck();
        allScanners.add(mysql);
        Scanner shadow = new ShadowFileCheck();
        allScanners.add(shadow);
        Scanner apache2 = new Apache2Check();
        allScanners.add(apache2);
        Scanner update = new SystemUpdate();
        allScanners.add(update);
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
        // NEED a report function which writes an array of reports as one HTML 
        //3. generate the report
        System.out.println("REPORT is generated and located at: ");

    }


}
