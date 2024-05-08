import java.io.*;
import java.util.Scanner;

public class cipherCheck {
    private static String[] weakCiph = {"NULL", "ADH", "RC4", "3DES", "RC2", "RSA", "IDEA", "DHE", "ANON", "anon"};
    public static String result = "";

        public static void ciphChk() throws IOException {
            System.out.println("---Commands---");
            System.out.println("ca = check preset list of config files, cc = check specific file (needs filepath), exit = exit");
            System.out.println("List of generally good ciphers to use (OpenSSL):\nECDHE-PSK-CHACHA20-POLY1305, ECDHE-ECDSA-AES128-GCM-SHA256, ECDHE-RSA-AES128-GCM-SHA256, ECDHE-ECDSA-AES256-GCM-SHA384, ECDHE-RSA-AES256-GCM-SHA384, ECDHE-RSA-CHACHA20-POLY1305, ECDHE-ECDSA-AES-GCM-SHA256\n");
            System.out.println("A256-bit or higher encryption is preferred, but 128-bit encryprtion is okay.\nAny encryption with less than 128 bits poses a security risk!\n");
            
            String[] configFiles = {"/etc/ssl/openssl.cnf", "/etc/apache2/sites-available/ssl.conf", "/etc/apache2/sites-enabled/ssl.conf",
            "/etc/httpd/ssl.conf", "/etc/apache2/ssl.conf", "/etc/httpd/conf.d/ssl.conf", "/path/to/example/file/example_openssl.cnf"};
            
            // get user input for the commands
            Scanner userInput = new Scanner(System.in);
            String tmpInput = "";
            while (true) {
                tmpInput = userInput.nextLine();
                tmpInput = tmpInput.toLowerCase();    
                if (tmpInput.equals("exit")) { 
                    userInput.close();
                    return;
                }
                if (tmpInput.equals("ca") || tmpInput.equals("cc")) {
                    break;
                }
            }
            

            // check a bunch of config files for bad ciphers
            if (tmpInput.equals("ca")) {
                try {
                    for (String filename: configFiles) {
                        File tmpFile = new File(filename);
                        if (tmpFile.exists()) {
                            result = result + filename + "\n";
                            FileInputStream fstream = new FileInputStream(tmpFile);
                            DataInputStream in = new DataInputStream(fstream);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                
                            String line = "";
                
                            while ((line = reader.readLine()) != null) {
                                if (line.contains("CipherSuite")) {
                                    String[] tmp = line.split(" ");
                                    String listOfCiph = tmp[tmp.length - 1];
                                    String[] tmp2 = listOfCiph.split(":");
                
                                    for (int j = 0; j < tmp2.length; j++) {
                                        for (int i = 0; i < weakCiph.length; i++) {
                                            if (tmp2[j].contains(weakCiph[i])) {
                                                //this is needed to skip ciphers with ECDHE as .contains() picks up the DHE, which is flagged as bad
                                                if (i==7 && (tmp2[j].contains("ECDHE"))) {
                                                    continue;    
                                                }
                                                result = result + tmp2[j] + "|";
                                                j++;
                                            }
                                        }
                                    }
                                }
                            }
                            result = result + "\n";
                            reader.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unexpected error trying to scan file(s). Exiting...");
                }
            }

            // similar as before, but checking one file specified by user instead of a 
            // preset list
            if (tmpInput.equals("cc")) {
                try {
                    System.out.println("Enter filepath of config: ");
                    String filepathString = userInput.nextLine();
                    
                            
                        File tmpFile = new File(filepathString);
                        if (tmpFile.exists()) {
                            result = result + filepathString + "\n";
                            FileInputStream fstream = new FileInputStream(tmpFile);
                            DataInputStream in = new DataInputStream(fstream);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                
                            String line = "";
                
                            while ((line = reader.readLine()) != null) {
                                if (line.contains("CipherSuite")) {
                                    String[] tmp = line.split(" ");
                                    String listOfCiph = tmp[tmp.length - 1];
                                    String[] tmp2 = listOfCiph.split(":");
                
                                    for (int j = 0; j < tmp2.length; j++) {
                                        for (int i = 0; i < weakCiph.length; i++) {
                                            if (tmp2[j].contains(weakCiph[i])) {
                                                if (i==7 && (tmp2[j].contains("ECDHE"))) {
                                                    continue;    
                                                }
                                                result = result + tmp2[j] + "|";
                                                j++;
                                            }
                                        }
                                    }
                                }
                            }
                            result = result + "\n";
                            reader.close();
                        }
                    
                } catch (Exception e) {
                    System.out.println("Unexpected error trying to scan file(s). Exiting...");
                }
            }
            userInput.close();
            System.out.println();
        }
    }

