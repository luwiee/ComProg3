package prelim.pexer3;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Date: 04/02/2023
 * Name: Lawrence T. Miguel II
 * Class: Client Program
 * <p>
 * This class is in compliance to the prelim exercise 3 of  the individual exercises wherein the following is asked for.
 *  Required:
 *  Create a client program that reads each data from the input file and sends over the read data to the
 * server. The server processes the data received and sends to the client program a response. The client
 * program should then print the response coming from the server. Sample responses from the server that
 * are printed by the client will be as follows
 * ----------------------------------------------
 * - Juan Dela Cruz, you may exercise your right to vote!
 * - Maria Makiling, you are still too young to vote!
 * -----------------------------------------------
 * If the client program can no longer read any data from the input file, it shall send the message
 * “bye” to the server. Upon receiving the message “bye” from the client, the server will no longer process
 * the data and terminates. The client, on the other hand, should not be expecting a response from the
 * server and therefore stops executing.
 * <p>
 * Solution:
 * Connect to the Server
 * Start an Input Stream to the xml file and start a while loop for all lines.
 * Send each line one by one to the Output Stream (PrintWriter out) of the serverSocket,
 * and then after sending the line, wait for a response in the Input Stream (Buffered Reader in) of the serverSocket in
 * a while loop, and output the received response if it is not 'done' if so break from the while loop and continue
 * sending another line. After sending all the lines send a 'bye' message to the server to end the connection.
 */
public class ClientExer3 {
    public static void main(String[] args) {
        File xml = new File("res/exer3.xml");

        String line;
        String serverLine;
        int port = 8000;
        String host = "localhost";
        System.out.println("Before sending the data: ");
        try (
                Socket serverSocket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true)
        ) {
            try (BufferedReader reader = new BufferedReader(new FileReader(xml))) {
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                    while ((serverLine = in.readLine()) != null) {
                        if (serverLine.equals("done")) break;
                        System.out.println(serverLine);
                    }

                }

                out.println("bye");
                System.out.println();
            }

            System.out.println("2: " + in.readLine());

        } catch (SocketException e) {
            System.out.println("Connection Closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
