package prelim.pexer3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Date: 04/02/2023
 * Name: Lawrence T. Miguel II
 * Class: Server Program
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
     * Listen on the Server Port for Socket Connections
     * Upon Connection check input from the client socket
     * Upon line receive process the line received
     * Check if the line contains the substring <citizen> which is the start of adding to the stringBuilder by
     *  turning on the readyAdd boolean and adding the succeeding lines received from the client
     *  And upon the line that contains the substring </citizen> this marks the end of an object xml as such call
     *  the function that converts a string of xml to a Citizen Object. And then do the checking for the object and
     *  send the output to the client. Upon receiving the string 'bye' terminate the server.
 */
public class ServerExer3 {
    public static void main(String[] args) {
        int port = 8000;
        System.out.println("Server is running!");

        boolean readyAdd = false; // boolean that signifies whether to add new xml lines
        StringBuilder stringBuilder = new StringBuilder(); // Contains the xml string received
        String line; // The line from the client
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {

            System.out.println("Client Connected!"); // Output that a client has successfully connected!

            while ((line = in.readLine()) != null) {
                if (line.equals("bye")) {
                    System.out.println("Terminating the Server.");
                    System.exit(0); // Terminate the server, when the line is 'bye'
                }
                if (line.contains("<citizen>")) readyAdd = true; // Start adding lines to the xml string
                if (readyAdd) stringBuilder.append(line).append("\n"); // If ready append the lines

                // End of a xml object
                if (line.contains("</citizen>")) {
                    readyAdd = false; // Stop appending lines
                    Citizen citizen = parseXmlLine(stringBuilder.toString()); // Convert the xml String
                    stringBuilder = new StringBuilder(); // Empty String Builder

                    // Make sure that the citizen object is not null
                    if (citizen != null) {
                        String addString = citizen.getAge() >= 18 ? "you may exercise your right to vote!"
                                : "you are still too young to vote!";
                        out.println(citizen.getName() + ", " + addString); // Send message to client
                    }
                }
                out.println("done"); // Tells the client we are done with processing the current line, send another


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for converting a String that contains the xml of a Citizen Object to a Citizen Object
     * @param xmlLine The String to be converted to a Citizen object
     * @return A Citizen Object converted from the xml String
     */
    private static Citizen parseXmlLine(String xmlLine) {
        try {
            // Initialize DB Factory and Builder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Create a document by parsing the XML String
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlLine)));
            doc.getDocumentElement().normalize(); // Separate the text nodes

            NodeList nodeList = doc.getElementsByTagName("citizen"); // Get the citizen element
            // Iterate through all the node list (Though at run time will only run once)
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i); // Get node at index i

                // Checking if current node is an ELEMENT NODE
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // Get the name and age from the xml node
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                    return new Citizen(name, age); // Return a new Citizen Object from the parsed info
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Exception while parsing xml line");
            System.out.println(e.getMessage());
        }
        return null;
    }
}


