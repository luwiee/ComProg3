/**
 * Date: 2/2/2023
 * Name: Lawrence T. Miguel II
 *
 * This class is in compliance to the prelim exercise 1 of  the individual exercises wherein the following is asked for.
 * Problem
     * Create a program that will look up a variable number of hostnames/IP addresses:
     * • The program will ask the name of a host to search and then immediately display the hostnames
     * and IP addresses of the host given by the user
     * • The program will then ask the user if he/she wants to search for another host (if yes, repeat the
     * process above, otherwise, exit the program)
 *
 * Solution:
 * 1. Get host from user input
 * 2. In a try catch, get the INetAddress Arayy using the getAllByName() function and catch the exception and return
 *      to Step 1 if invalid host.
 * 3. Show the number of ip address of the host by getting the length of the array
 * 4. Iterate through the InetAdress Objects of the Array and Output their host address using getHostAddress()
 *
 */
package prelim;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class pexer1 {
    public static void main(String[] args) {
        run();
    }


    private static void run() {
        Scanner kbd = new Scanner(System.in); // For getting user input
        int currentHostNumber = 1; // Stores the current host number

        // To run infinitely until user says no
        while (true){
            // Get the Input Host from the user
            System.out.print("Host " + currentHostNumber + " - Type IP address/Hostname: ");
            String inputHost = kbd.nextLine();
            InetAddress[] add;

            // Try catch for exception and continue to the next iteration if not valid
            try{
                add =  InetAddress.getAllByName(inputHost);
            }catch (UnknownHostException e){
                System.out.println("Invalid Host, Try again!");
                continue;
            }

            currentHostNumber++; // Increment the current host as this is valid host

            // Show the number of addresses found for the host
            System.out.print("    Number of Hosts/IPs: " + add.length); // Get the number by the length of the array

            // Header prints
            System.out.println();
            System.out.println("    Host name       Ip Address: ");

            // Iterate through all addresses
            for (InetAddress address: add){
                    System.out.println("    "+inputHost+"       "+ address.getHostAddress()); // Show by the function
            }

            // Ask if Search for another Host
            System.out.print("Search another [y/n]? ");
            if (kbd.nextLine().toLowerCase().charAt(0) == 'n') {
                return; // return to the main method
            }
            System.out.println();
        }
    }


}
