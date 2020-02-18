/*
* Jared Jacobs and Josh Coffey
* This is our UDP Server for the first project for CS-415
* With this, we will be able to get requests from our client and send CS quotes from a file.
* Spring 2020
* @version 1.0
*/

import java.util.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class UDPServer {
    private static DatagramSocket socket;
    private static List<String> quotesList = new ArrayList<String>();
    private static Random random;
    private static final String requestCommand = "<REQUESTQUOTE>";
    private static DateFormat recieveddf;
	private static DateFormat startdf;
	private static DateFormat startTimedf;

    public static void main(String[] args) throws Exception {
        socket = new DatagramSocket(2015);
        loadQuotesFromFile("quote.csv");
        random = new Random();
		startTimedf = new SimpleDateFormat("hh:mmaa");
		startdf = new SimpleDateFormat("MMMM dd, yyyy");
        recieveddf = new SimpleDateFormat("MM/dd/yyyy hh:mmaa");
		String dateString = startTimedf.format(new Date()) + " on ";
		dateString += startdf.format(new Date());
		System.out.println("Server started at " + dateString);
        service();
    }

    private static void service() throws IOException {
        while (true) {
            byte[] buffer = new byte[512];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);
            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();
            String dateString = recieveddf.format(new Date()).toString();

            System.out.println("Request recieved from " + clientAddress.toString() + ": " + clientPort + " " + dateString);

            String recievedCommand = new String(buffer, 0, request.getLength());

            if (recievedCommand.equals(requestCommand)) {
                String quote = getRandomQuote();

                buffer = quote.getBytes();

                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);
            } else {
                DatagramPacket response = new DatagramPacket(new byte[1], 1, clientAddress, clientPort);
                socket.send(response);
            }
        }
    }

    private static void loadQuotesFromFile(String quoteFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(quoteFile));
        String quote;

        while ((quote = reader.readLine()) != null) {
            quotesList.add(quote);
        }

        reader.close();
    }

    private static String getRandomQuote() {
        int randomIndex = random.nextInt(quotesList.size());
        return quotesList.get(randomIndex);
    }

}
