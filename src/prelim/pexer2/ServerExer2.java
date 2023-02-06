package prelim.pexer2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Date: 04/02/2023
 * Name: Lawrence T. Miguel II
 * <p>
 * This class is in compliance to the prelim exercise 2 of  the individual exercises wherein the following is asked for.
 * Problem:
 * This class should be a modification of the server program
 * presented in Sample program 1s (SampleServer1) of module 2, but it will be able to accept as many
 * clients as possible. Make the server to run “continuously” and will stop executing only if the user will
 * manually stop the server.
 * <p>
 * Solution:
 * Create a Client Handler Class that implements Runnable
 * In the main method run the server
 * In a while loop listen on the port and accept socket connections and create ClientHandler Class and pass
 * The Client Socket as a parameter.
 * Run the ClientHandler in a new thread
 */
public class ServerExer2 {
    private static final int MAX_THREADS = 10;
    private ServerSocket server;
    private Executor executor;

    public ServerExer2(int port) {
        try {
            server = new ServerSocket(port);
            executor = Executors.newFixedThreadPool(MAX_THREADS);
            System.out.println("Server started on port: " + port);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void start() {
        // Infinite Loop to run indefinitely
        do {
            try {
                // Get Client Socket
                Socket client = server.accept();

                // Output that a new client has connected
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                // Create a new ClientHandler object and pass the socket object of the new connection
                ClientHandler clientSock = new ClientHandler(client);

                // Use the executor to run a task for that ClientHandler Object that contains the client socket object
                executor.execute(clientSock);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (true);
    }

    public static void main(String[] args) {
        int port = 8080;
        ServerExer2 server = new ServerExer2(port);
        server.start();
    }

    /**
     * Client Handler Class that implements Runnable to be able to run in an independent thread
     * This is where the original code for processing input from the client has been moved into to
     * support multiple clients.
     */
    private class ClientHandler implements Runnable {
        private final Socket clientSocket; // Contains the passed Client Socket upon instantiation of ClientHandler

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                out.println("What is your name? ");
                // server accepts input from client
                String name = in.readLine();
                int age;
                while (true) {
                    out.println("What is your age? ");
                    try {
                        age = Integer.parseInt(in.readLine());
                        out.println();
                        if (age <= 0) {
                            throw new NumberFormatException();
                        } else {
                            break;
                        }

                    } catch (NumberFormatException nfe) {
                        out.println("Please enter a valid age.");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (age >= 18) {
                    out.println(name + ", you may exercise your right to vote!");
                } else {
                    out.println(name + ", you are still too young to vote!");
                }
                out.println("Thank you and good day.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


