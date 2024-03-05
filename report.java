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
