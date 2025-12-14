import java.io.*;
import java.net.*;
import java.nio.file.*;


public class ProjectServer {
    public static void main(String[] args) {
        int PORT = 5000;
        String HTML_DIRECTORY = "./server_files/";
        
        System.out.println("=== PROJECT SERVER STARTED ===");
        System.out.println("Server is listening on port: " + PORT);
        System.out.println("HTML files directory: " + HTML_DIRECTORY);
        System.out.println("================================\n");
        
        try {
   
            ServerSocket serverSocket = new ServerSocket(PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                
               
                String request = in.readLine();
                System.out.println("\n[REQUEST RECEIVED] " + request);
                
                if (request != null && request.startsWith("GET_HTML:")) {
                    
                    String filename = request.substring(9).trim();
                    System.out.println("[PROCESSING] Requested file: " + filename);
                    
                    try {
                        File htmlFile = new File(HTML_DIRECTORY + filename);
                        
                        if (!htmlFile.exists()) {
                            out.println("ERROR: File not found - " + filename);
                            System.err.println("[ERROR] File not found: " + filename);
                        } else {
                            String content = new String(Files.readAllBytes(htmlFile.toPath()));
                            
                            out.println("SUCCESS");
                            out.println(content);
                            out.println("END_OF_FILE");
                            
                            System.out.println("[SUCCESS] File sent: " + filename + 
                                             " (" + htmlFile.length() + " bytes)");
                        }
                        
                    } catch (IOException e) {
                        out.println("ERROR: Unable to read file - " + e.getMessage());
                        System.err.println("[ERROR] Unable to read file: " + e.getMessage());
                    }
                    
                    String confirmation = in.readLine();
                    if (confirmation != null) {
                        System.out.println("\n=== CONFIRMATION MESSAGE RECEIVED ===");
                        System.out.println(confirmation);
                        
                        String line;
                        while (in.ready() && (line = in.readLine()) != null) {
                            System.out.println(line);
                        }
                        System.out.println("=====================================\n");
                    }
                }

                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Client disconnected\n");
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}