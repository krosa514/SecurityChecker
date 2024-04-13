
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


class Report {
    //private static final String REPORT_FILENAME = "report.json";
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
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        // Iterate over reports and generate JSON
        for (int i = 0; i < arrReports.size(); i++) {
            Report report = arrReports.get(i);

            jsonBuilder.append("{\n");
            jsonBuilder.append("\"scanner\": \"").append(report.getName()).append("\",\n");
            jsonBuilder.append("\"output\": \"").append(report.getOutput()).append("\"\n");
            jsonBuilder.append("}");

            if (i < arrReports.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }

        jsonBuilder.append("\n]");

        // Write JSON data to file
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(jsonBuilder.toString());
            System.out.println("Reports generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getOutput() {
        return output;
    }

    public String getName() {
        // Implement this based on your Scanner interface
        return "";
    }
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
