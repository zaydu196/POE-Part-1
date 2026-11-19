package com.mycompany.vybe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
 * Message class
 * Holds the details of one message (ID, number, recipient, text, hash and flag),
 * keeps track of the messages sent while the app is running,
 * and saves stored messages to (and reads them from) a JSON file.
 */
public class Message {

    // Message details
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag = "";   // "Sent", "Disregarded" or "Stored" once the user chooses

    // Shared by all messages
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;
    private static String fileName = "storedMessages.json";

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
                flag = "Sent";
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                flag = "Disregarded";
                return "Press 0 to delete the message.";
            case 3:
                flag = "Stored";
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

    // Adds this message to the JSON file, keeping the messages that were already stored there
    public void storeMessage() {
        ArrayList<Message> storedMessages = readStoredMessages();
        storedMessages.add(this);
        saveStoredMessages(storedMessages);
    }

    // Writes a list of messages to the JSON file (also used after a stored message is deleted)
    public static void saveStoredMessages(ArrayList<Message> storedMessages) {
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

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    // Reads the JSON file back into a list of messages, one field per line
    // File reading with Scanner adapted from W3Schools (n.d.)
    public static ArrayList<Message> readStoredMessages() {
        ArrayList<Message> storedMessages = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return storedMessages;   // nothing has been stored yet
        }

        String id = "";
        int number = 0;
        String recipient = "";

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                if (line.startsWith("\"messageID\"")) {
                    id = getJsonValue(line);
                } else if (line.startsWith("\"messageNumber\"")) {
                    number = Integer.parseInt(getJsonValue(line));
                } else if (line.startsWith("\"recipient\"")) {
                    recipient = getJsonValue(line);
                } else if (line.startsWith("\"message\"")) {
                    // "message" is the last field, so the message is complete here
                    Message message = new Message(id, number, recipient, getJsonValue(line));
                    message.flag = "Stored";
                    storedMessages.add(message);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading stored messages: " + e.getMessage());
        }

        return storedMessages;
    }

    // Takes a line like  "recipient": "+27718693002",  and returns  +27718693002
    private static String getJsonValue(String line) {
        String value = line.substring(line.indexOf(":") + 1).trim();

        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1);   // remove the comma at the end
        }
        if (value.startsWith("\"")) {
            value = value.substring(1, value.length() - 1);   // remove the quotes around it
        }

        return value.replace("\\\"", "\"");   // turn \" back into "
    }

    // Turns this message into a JSON object
    private String toJson() {
        return "  {\n"
                + "    \"messageID\": \"" + messageID + "\",\n"
                + "    \"messageNumber\": " + messageNumber + ",\n"
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

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getFlag() {
        return flag;
    }

    // Lets the unit tests use their own JSON file so the real one isn't touched
    public static void setFileName(String newFileName) {
        fileName = newFileName;
    }
}
