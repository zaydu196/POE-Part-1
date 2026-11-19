package com.mycompany.vybe;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Vybe class
 * Main class of the app. Lets the user create an account, log in,
 * send messages through QuickChat, and manage their stored messages.
 */
public class Vybe {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("===== Welcome to Vybe: Create an Account =====");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        Login login = new Login(firstName, lastName);

        // Keep asking until all the details are correct
        while (!login.isRegistered()) {
            System.out.print("Enter a username: ");
            String username = input.nextLine();

            System.out.print("Enter a password: ");
            String password = input.nextLine();

            System.out.print("Enter your cell phone number (e.g. +27831234567): ");
            String cellPhoneNumber = input.nextLine();

            System.out.println(login.registerUser(username, password, cellPhoneNumber));
            System.out.println();
        }

        System.out.println("===== Vybe Login =====");

        boolean loggedIn = false;

        // Keep asking until the user logs in successfully
        while (!loggedIn) {
            System.out.print("Enter your username: ");
            String username = input.nextLine();

            System.out.print("Enter your password: ");
            String password = input.nextLine();

            loggedIn = login.loginUser(username, password);
            System.out.println(login.returnLoginStatus(loggedIn));
            System.out.println();
        }

        // ===== Part 2: QuickChat (only reached once the user has logged in) =====
        System.out.println("Welcome to QuickChat.");

        // Part 3: the arrays of messages, starting with any messages stored in the JSON file
        MessageManager manager = new MessageManager(login.getFullName() + " (" + login.getCellPhoneNumber() + ")");
        manager.loadStoredMessages();

        System.out.print("How many messages would you like to send? ");
        int numMessages = readNumber(input);

        int messagesEntered = 0;
        int option = 0;

        // Keep showing the menu until the user chooses to quit
        while (option != 3) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            option = readNumber(input);

            switch (option) {
                case 1:
                    if (messagesEntered >= numMessages) {
                        System.out.println("You have already entered all " + numMessages + " messages.");
                        break;
                    }

                    // Let the user enter the rest of their messages
                    while (messagesEntered < numMessages) {
                        System.out.println();
                        System.out.println("--- Message " + (messagesEntered + 1) + " of " + numMessages + " ---");
                        Message message = enterMessage(input, messagesEntered);
                        System.out.println(message.messageIDCreated());

                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose an option: ");
                        int choice = readNumber(input);

                        while (choice < 1 || choice > 3) {
                            System.out.print("Invalid option, please choose 1, 2 or 3: ");
                            choice = readNumber(input);
                        }

                        System.out.println(message.sentMessage(choice));
                        manager.addMessage(message);

                        if (choice == 1) {
                            System.out.println(message.getMessageDetails());
                        } else if (choice == 2) {
                            while (!input.nextLine().equals("0")) {
                                System.out.println("Press 0 to delete the message.");
                            }
                            System.out.println("Message disregarded.");
                        }

                        messagesEntered++;
                    }

                    System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
                    break;
                case 2:
                    System.out.println(Message.printMessages());
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    break;
                case 4:
                    storedMessagesMenu(input, manager);
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, 3 or 4.");
            }
        }

        input.close();
    }

    // Part 3: the Stored Messages menu, which keeps showing until the user goes back
    public static void storedMessagesMenu(Scanner input, MessageManager manager) {
        int option = 0;

        while (option != 7) {
            System.out.println();
            System.out.println("--- Stored Messages ---");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using its message hash");
            System.out.println("6) Display a report of all messages");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");
            option = readNumber(input);

            switch (option) {
                case 1:
                    System.out.println(manager.displaySenderAndRecipients());
                    break;
                case 2:
                    System.out.println("Longest stored message: " + manager.getLongestMessage());
                    break;
                case 3:
                    System.out.print("Enter the message ID: ");
                    System.out.println(manager.searchByMessageID(input.nextLine().trim()));
                    break;
                case 4:
                    System.out.print("Enter the recipient's cell number: ");
                    ArrayList<String> results = manager.searchByRecipient(input.nextLine().trim());

                    if (results.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    }
                    for (String text : results) {
                        System.out.println("\"" + text + "\"");
                    }
                    break;
                case 5:
                    System.out.print("Enter the message hash: ");
                    System.out.println(manager.deleteByHash(input.nextLine().trim().toUpperCase()));
                    break;
                case 6:
                    System.out.println(manager.displayReport());
                    break;
                case 7:
                    break;   // goes back to the main menu
                default:
                    System.out.println("Invalid option, please choose 1 to 7.");
            }
        }
    }

    // Asks for the recipient and message until both are correct, then returns the message
    public static Message enterMessage(Scanner input, int messageNumber) {
        while (true) {
            System.out.print("Enter the recipient's cell number (e.g. +27718693002): ");
            String recipient = input.nextLine();

            System.out.print("Enter your message: ");
            String text = input.nextLine();

            Message message = new Message(messageNumber, recipient, text);

            String cellStatus = message.checkRecipientCell();
            String lengthStatus = message.checkMessageLength();
            System.out.println(cellStatus);
            System.out.println(lengthStatus);

            if (cellStatus.equals("Cell phone number successfully captured.")
                    && lengthStatus.equals("Message ready to send.")) {
                return message;
            }

            if (!lengthStatus.equals("Message ready to send.")) {
                System.out.println("Please enter a message of less than 250 characters.");
            }
            System.out.println();
        }
    }

    // Reads a whole number from the user and keeps asking if they type something else
    public static int readNumber(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
    }
}
