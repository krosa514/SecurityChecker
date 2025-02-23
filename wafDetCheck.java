import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wafDetCheck implements Scanner {
    
    protected Report myreport = null;
    protected wafDetCheckThread mythread = null;

    @Override
    public Report Scan() {
        this.start();
        this.join();
        return this.myreport;
    }

    @Override
    public void start(){
        this.mythread = new wafDetCheckThread();
        this.mythread.start();
    }; 

    @Override
    public void join(){
        try {
            this.mythread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.myreport = this.mythread.report;
    }

    @Override
    public String getName(){
        return "WAFDetection";
    }
    @Override
    public Report getReport(){
        return this.myreport;
    }
}


class wafDetCheckThread extends Thread {
    protected Report report;
    static String[] wafName = {"ACE XML Gateway", "aeSecure",
        "AireeCDN", "Airlock", "Alert Logic", "AliYunDun", "Anquanbao", "AnYu", "Approach", "AppWall", "Armor Defense", "ArvanCloud",
        "ASP.NET Generic", "ASPA Firewall", "Astra", "AWS Elastic Load Balancer", "AzionCDN", "Azure Front Door",
        "Barikode", "Barracuda", "Bekchy", "Beluga CDN", "BIG-IP Local Traffic Manager",
        "BinarySec", "BitNinja", "BlockDoS", "Bluedon", "BulletProof Security Pro", "CacheWall", "CacheFly CDN",
        "Comodo cWatch", "CdnNS Application Gateway", "ChinaCache Load Balancer", "Chuang Yu Shield", "Cloudbric",
        "Cloudflare", "Cloudfloor", "Cloudfront", "CrawlProtect", "DataPower", "DenyALL","Distil", "DOSarrest", "DotDefender",
        "DynamicWeb Injection Check", "Edgecast", "Eisoo Cloud Firewall", "Expression Engine", "BIG-IP AppSec Manager",
        "BIG-IP AP Manager", "Fastly", "FirePass", "FortiWeb", "GoDaddy Website Protection", "Greywizard",
        "Huawei Cloud Firewall", "HyperGuard", "Imunify360", "Incapsula", "IndusGuard", "Instart DX",
        "ISA Server", "Janusec Application Gateway", "Jiasule", "Kona SiteDefender", "KS-WAF",
        "KeyCDN", "LimeLight CDN", "LiteSpeed", "Open-Resty Lua Nginx", "Oracle Cloud", "Malcare",
        "MaxCDN", "Mission Control Shield", "ModSecurity", "NAXSI", "Nemesida", "NevisProxy", "NetContinuum",
        "NetScaler AppFirewall", "Newdefend", "NexusGuard Firewall", "NinjaFirewall", "NullDDoS Protection", "NSFocus",
        "OnMessage Shield", "Palo Alto Next Gen Firewall", "PerimeterX", "PentaWAF", "pkSecurity IDS",
        "PT Application Firewall", "PowerCDN", "Profense", "Puhui", "Qcloud", "Qiniu", "Qrator","Reblaze",
        "RSFirewall", "RequestValidationMode", "Sabre Firewall", "Safe3 Web Firewall", "Safedog", "Safeline", "SecKing", "eEye SecureIIS",
        "SecuPress WP Security", "SecureSphere", "Secure Entry", "SEnginx", "ServerDefender VP", "Shield Security",
        "Shadow Daemon", "SiteGround", "SiteGuard", "Sitelock", "SonicWall", "UTM Web Protection",
        "Squarespace", "SquidProxy IDS", "StackPath", "Sucuri CloudProxy", "Tencent Cloud Firewall",
        "Teros", "Trafficshield", "TransIP Web Firewall", "URLMaster SecurityCheck",
        "URLScan", "UEWaf", "Varnish", "Viettel", "VirusDie", "Wallarm", "WatchGuard",
        "WebARX", "WebKnight", "WebLand", "RayWAF", "WebSEAL", "WebTotem", "West263 CDN",
        "Wordfence", "WP Cerber Security", "WTS-WAF", "360WangZhanBao", "XLabs Security WAF",
        "Xuanwudun","Yundun", "Yunsuo", "Yunjiasu", "YXLink", "Zenedge", "ZScaler"};

        // results stored here
    public String result = "";
    

    @Override
    public void run() {     
        System.out.println("---WARNING---");
        System.out.println("This program uses wafw00f, which uses your IP when testing for a WAF.");
        System.out.println("This may get some unwanted eyes on you depending on the site tested.");
        System.out.println("Proceed with caution.");

        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("Enter website url: ");
        String input = sc.nextLine();
        //no special reason for this, limiting the url length within the bounds of the majority of sites
        while (input.length() > 70) {
            System.out.println("The site length is too long.");
            System.out.print("Enter website url: ");
            input = sc.nextLine();
        }
        // System.out.println("Just a test, you entered: " + input);

        //setting up for the command to be used
        ProcessBuilder procBuild = new ProcessBuilder();
        procBuild.command("bash", "-c", "wafw00f " + input + "");

        try {
            Process process = procBuild.start();

            //reading output from command here
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);

                //checking for WAF in line
                if (line.contains("+")) {
                    //System.out.println(line);

                    //detect the WAF here
                    for (int i=0; i < wafName.length; i++) {

                        Pattern pattern = Pattern.compile(wafName[i]);
                        Matcher match = pattern.matcher(line);

                        boolean found = match.find();
                        if (found) {
                            //System.out.println("WAF Detected: It's name is: " + wafName[i]);
                            result = result + "WAF Detected for " + input + ". It's name is: " + wafName[i];
                        }
                    }
                }
            }
            if (result == "") {
                //System.out.println("No WAF detected.");
                result = result + "Either no WAF detected or url is invalid.";
            }

            this.report = new Report(result);
            this.report.setName("Web Application Firewall Detection");
            int exitcode = process.waitFor();
            if (exitcode == 0)
                System.out.println("Success");
            else
                System.out.println("Something may have went wrong with WAF detection.");
            // System.out.println(exitcode);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    
}