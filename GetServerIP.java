import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Program to obtain the IP address of the server
 */
public class GetServerIP {
    public static void main(String[] args) {
        try {
            // Get local host address
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("=== SERVER IP ADDRESS INFORMATION ===");
            System.out.println("Host Name: " + localhost.getHostName());
            System.out.println("IP Address: " + localhost.getHostAddress());
            System.out.println("====================================");
            
            // You can also get IP by hostname
            if (args.length > 0) {
                InetAddress serverAddress = InetAddress.getByName(args[0]);
                System.out.println("\nSpecified Server Information:");
                System.out.println("Host Name: " + serverAddress.getHostName());
                System.out.println("IP Address: " + serverAddress.getHostAddress());
            }
            
        } catch (UnknownHostException e) {
            System.err.println("Unable to get IP address: " + e.getMessage());
            e.printStackTrace();
        }
    }
}