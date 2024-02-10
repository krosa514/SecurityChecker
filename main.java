// Vulnerability Vigil

import java.io.IOException;
import java.util.ArrayList;

class VulnerabilityVigil {
    /*enum scanType {
        FULL,
        QUICK,
        CUST
    } */

    /**
     * Interact with user and return an array list of scanners
     * @return
     */
    protected static ArrayList<Scanner> selectScanner(){
        ArrayList<Scanner> allScanners = new ArrayList<>();
        Scanner vs = new VirusScanner();
        allScanners.add(vs);
        Scanner fws = new FirewallScanner();
        allScanners.add(fws);
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

    /**
     * Stub function for main
     */
    protected static void main2(){
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
    
    // Main class
    public static void main(String[] args) {
        main2();
        
        // ignore below this was test code
        /*
        scanType urType = scanType.FULL;
        switch (urType) {
            case FULL:
                fullScan Test1 = new fullScan();
                try {
                    Test1.Scan();
                } catch (IOException e) {
                    return;
                }
                System.out.print(Test1.getOutput());
                break;
            case QUICK:
                System.out.println("The quickscan.");
                break;
            case CUST:
                System.out.println("The customscan.");
                break;
            default:
                break;
        }
        */
    }

    // Select scanners
    public void selectScanners() {
        return;
    }
    // Keeps an array of scanners for the user to choose
    // ArrayList<Scanners> Scanners = new ArrayList<Scanners>();
}
