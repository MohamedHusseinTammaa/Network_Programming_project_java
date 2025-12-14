import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.awt.Desktop;


public class ProjectClient {
    public static void main(String[] args) {
        String SERVER_ADDRESS = "localhost"; 
        int SERVER_PORT = 5000;
        String OUTPUT_DIRECTORY = "./client_files/";
        
        System.out.println("=== PROJECT CLIENT STARTED ===");
        System.out.println("Connecting to server: " + SERVER_ADDRESS + ":" + SERVER_PORT);
        System.out.println("===============================\n");
        
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the HTML filename to request (e.g., test.html): ");
            String filename = userInput.readLine();
            
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("\n[CONNECTED] Successfully connected to server");
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        
            System.out.println("[REQUEST] Requesting file: " + filename);
            out.println("GET_HTML:" + filename);
            
            String response = in.readLine();
            
            if ("SUCCESS".equals(response)) {
                System.out.println("[RESPONSE] Server accepted request");
                
                StringBuilder htmlContent = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null && !line.equals("END_OF_FILE")) {
                    htmlContent.append(line).append("\n");
                }
                
                File outputDir = new File(OUTPUT_DIRECTORY);
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }
                
                File outputFile = new File(OUTPUT_DIRECTORY + filename);
                Files.write(outputFile.toPath(), htmlContent.toString().getBytes());
                
                System.out.println("[SUCCESS] HTML file received and saved: " + 
                                 outputFile.getAbsolutePath());
                
                String confirmationMessage = "I RECEIVED THE INFORMATION OF STUDENT'S PROJECT GROUP\n"; 
                out.println(confirmationMessage);
                System.out.println("\n[CONFIRMATION SENT] Message sent to server:");
                System.out.println(confirmationMessage);
                
            } else {
                System.err.println("[ERROR] " + response);
            }
            

            in.close();
            out.close();
            socket.close();
            
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_ADDRESS);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== CLIENT TERMINATED ===");
    }
}