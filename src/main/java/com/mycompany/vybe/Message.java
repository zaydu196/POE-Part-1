package com.mycompany.vybe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
 * Message class
 * Holds the details of one message (ID, number, recipient, text and hash)
 * and keeps track of all the messages that were sent or stored while the app is running.
 */
public class Message {

    // Message details
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Shared by all messages
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // Creates a message with a randomly generated message ID
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    // Creates a message with a given message ID (used in the unit tests)
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Builds a random ten-digit number one digit at a time
    public static String generateMessageID() {
        Random random = new Random();
        String id = "";

        for (int i = 0; i < 10; i++) {
            id += random.nextInt(10);
        }

        return id;
    }

    // Message ID must not be more than ten characters
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Returns the message shown when the message ID is created
    public String messageIDCreated() {
        return "Message ID generated: " + messageID;
    }

    // Recipient number must start with the +27 code followed by 9 digits
    // (same check as checkCellPhoneNumber() in the Login class from Part 1)
    public String checkRecipientCell() {
        if (recipient.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Message must not be more than 250 characters
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int extra = messageText.length() - 250;
            return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
        }
    }

    // Hash = first two numbers of the ID : message number : first and last word, all in caps
    // e.g. 00:0:HITONIGHT
    public String createMessageHash() {
        String[] words = messageText.trim().split(" ");

        // Removes punctuation like commas and question marks from the words
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Handles what the user chose to do with the message
    // 1 = Send, 2 = Disregard, 3 = Store
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option, please choose 1, 2 or 3.";
        }
    }

    // Returns the details of every message sent while the program is running
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }

        String output = "";
        for (Message message : sentMessages) {
            output += message.getMessageDetails() + "\n";
        }

        return output;
    }

    // Returns the total number of messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Adds the message to the stored list and saves all stored messages to a JSON file
    public void storeMessage() {
        storedMessages.add(this);

        String json = "[\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            json += storedMessages.get(i).toJson();

            // comma between objects, but not after the last one
            if (i < storedMessages.size() - 1) {
                json += ",";
            }
            json += "\n";
        }
        json += "]";

        try (FileWriter writer = new FileWriter("storedMessages.json")) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    // Turns this message into a JSON object
    private String toJson() {
        return "  {\n"
                + "    \"messageID\": \"" + messageID + "\",\n"
                + "    \"messageHash\": \"" + messageHash + "\",\n"
                + "    \"recipient\": \"" + recipient + "\",\n"
                + "    \"message\": \"" + messageText.replace("\"", "\\\"") + "\"\n"
                + "  }";
    }

    // Full details in the order: Message ID, Message Hash, Recipient, Message
    public String getMessageDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
