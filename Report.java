import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


class Report {
    private String output;
    public Report(){
    }
    // Constructor that takes a String argument
    public Report(String output) {
        this.output = output;
        this.openPorts = "";
    }

   @Override
    public String toString() {
        return output; // Return the output of the report
    }

    // Generate report after the scan is complete
    public void generateReport(String fileName, ArrayList<Report> arrReports) {
        /* 
        for (int i = 0; i < arrReports.size(); i++) {
            Report report = arrReports.get(i);
            String scannerName = "Scanner_" + (i + 1);
    
            // Check if report is null
            if (report != null) {
                // Write the report to a separate text file for each scanner
                writeReportsToFile("results.txt", arrReports);
            } else {
                System.out.println("Report generation failed for scanner: " + scannerName);
            }
        }
*/
        try {
            String line;
            StringBuilder jsonBuilder = new StringBuilder("{");
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            // Read lines from the text file
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                // Assuming each line contains a key-value pair separated by ':'
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    if (!firstLine) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\"").append(parts[0].trim()).append("\":\"").append(parts[1].trim()).append("\"\n");
                    firstLine = false;
                }
            }
            jsonBuilder.append("}");
            reader.close();

            // Write JSON object to output file
            FileWriter writer = new FileWriter("output.json");
            writer.write(jsonBuilder.toString());
            writer.close();

            System.out.println("Data converted successfully from text to JSON.");
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
    
        System.out.println("Individual reports are generated.");
        /*try (FileWriter fileWriter = new FileWriter("scan_results.txt")) {
            for (Report report : arrReports) {
                // Write each report to the text file
                fileWriter.write(report.toString());
                fileWriter.write("\n\n");
            }
            System.out.println("REPORT is generated and located at: scan_results.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        // Implementation specific to generating a report
        /* 
        String inputFile = "Results.txt"; // Change this to your input text file
        String outputFile = "Results.json"; // Change this to your desired output JSON file

        try {
            String line;
            StringBuilder jsonBuilder = new StringBuilder("{");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            // Read lines from the text file
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    if (!firstLine) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\"").append(parts[0].trim()).append("\":\"").append(parts[1].trim()).append("\"\n");
                    firstLine = false;
                }
            }
            jsonBuilder.append("}");
            reader.close();

            // Write JSON object to output file
            FileWriter writer = new FileWriter(outputFile);
            writer.write(jsonBuilder.toString());
            writer.close();

            System.out.println("Data converted successfully from text to JSON.");
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
        */
        System.out.println("Generating report: " + output);
    }
/* 
    public static void writeReportsToFile(String fileName, List<Report> reports) {
        fileName = "results.txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (Report report : reports) {
                String content = report.output; // Access the output directly
                if (content != null) {
                    fileWriter.write(content); // Write the report content to the file
                    fileWriter.write("\n\n"); // Add new lines between reports
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    // Generate an HTML report
    public void reportToHTML() {
        // Implementation specific to generating an HTML report
        System.out.println("Generating HTML report: " + output);
    }

    private String firewallStatus;
    private String openPorts;

    public String getFirewallStatus() {
        return firewallStatus;
    }

    public void setOpenPorts(String openPorts) {
        this.openPorts = openPorts;
    }

    public String getOpenPorts() {
        return openPorts;
    }

}
