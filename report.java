import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


class Report {
    private String output;
    public Report(){
    }
    // Constructor that takes a String argument
    public Report(String output) {
        this.output = output;
        this.openPorts = "";
    }

    // Generate report after the scan is complete
    public void generateReport() {
        // Implementation specific to generating a report
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
        System.out.println("Generating report: " + output);
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
