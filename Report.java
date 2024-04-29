
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

    }

   @Override
    public String toString() {
        return output.replace("\n", " ");// Return the output of the report
    }

    // Generate report after the scan is complete
    public void generateReport(String fileName, ArrayList<Report> arrReports, StringBuilder jsonBuilder) {
        //StringBuilder jsonBuilder = new StringBuilder();
        //StringBuilder jsonBuilder = new StringBuilder();
        //jsonBuilder.append("[\n");

        // Iterate over reports and generate JSON
        for (int i = 0; i < arrReports.size(); i++) {
            Report report = arrReports.get(i);

            jsonBuilder.append("{\n");
            jsonBuilder.append("\"scanner\": \"").append(report.getName()).append("\",\n");
            jsonBuilder.append("\"output\": \"").append(report.getOutput()).append("\"\n");
            jsonBuilder.append("}");

            // Add a comma if it's not the last element
            //if (i < arrReports.size() - 1) {
            //    jsonBuilder.append(",\n");
            //}
        }

        //jsonBuilder.append("\n]");

        /* 
        // Write JSON data to file
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(jsonBuilder.toString());
            System.out.println("Reports generated and saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    

    public String getOutput() {
        return output.replace("\n", " ");
    }

    public String getName() {
        // Implement this based on your Scanner interface
        return name;
    }
    // Generate an HTML report
    public void reportToHTML() {
        // Implementation specific to generating an HTML report
        System.out.println("Generating HTML report: " + output);
    }

    private String firewallStatus;
    private String name;

    public String getFirewallStatus() {
        return firewallStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    

}
