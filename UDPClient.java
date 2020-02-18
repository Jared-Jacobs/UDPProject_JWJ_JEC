import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient {

    private static final String endCommand = "<END>";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Syntax: UDPClient <ip> <port>");
            return;
        }

        String hostip = args[0];
        int port = Integer.parseInt(args[1]);
        Scanner scan = new Scanner(System.in);

        try {
            InetAddress address = InetAddress.getByName(hostip);
            DatagramSocket socket = new DatagramSocket();
            boolean running = true;

            while (running) {
                System.out.print("Enter a command: ");
                String command = scan.nextLine();

                if (command.equals(endCommand)) {
                    running = false;
                } else {

                    byte[] buffer = command.getBytes();
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
                    socket.send(request);

                    buffer = new byte[512];
                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    socket.receive(response);

                    String quote = new String(buffer, 0, response.getLength());

                    System.out.println();
                    System.out.println(quote);
                    System.out.println();
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
        scan.close();
        System.out.println("Client closed");
    }
}