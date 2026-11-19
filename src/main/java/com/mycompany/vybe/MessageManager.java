package com.mycompany.vybe;

import java.util.ArrayList;

/*
 * MessageManager class
 * Part 3: keeps the arrays of messages the user has entered and lets the user
 * search, delete and report on them from the Stored Messages menu.
 *
 * messageIDs, messageHashes, recipients, messageTexts and flags are parallel arrays,
 * so the same index always points to the same message.
 */
public class MessageManager {

    // Arrays from the task
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<Message> storedMessages = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> messageIDs = new ArrayList<>();

    // Extra parallel arrays so each ID/hash can be matched to its recipient, text and flag
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> messageTexts = new ArrayList<>();
    private final ArrayList<String> flags = new ArrayList<>();

    // The logged-in user, shown as the sender
    private final String sender;

    public MessageManager(String sender) {
        this.sender = sender;
    }

    // Adds a message to the arrays once the user has chosen to send, disregard or store it
    public void addMessage(Message message) {
        messageIDs.add(message.getMessageID());
        messageHashes.add(message.getMessageHash());
        recipients.add(message.getRecipient());
        messageTexts.add(message.getMessageText());
        flags.add(message.getFlag());

        switch (message.getFlag()) {
            case "Sent" -> sentMessages.add(message.getMessageText());
            case "Disregarded" -> disregardedMessages.add(message.getMessageText());
            case "Stored" -> storedMessages.add(message);
            default -> { }   // no flag yet, so it doesn't go in any of the three arrays
        }
    }

    // Reads the messages saved in the JSON file into the stored messages array
    // (used when the app starts, so messages stored last time are still there)
    public void loadStoredMessages() {
        for (Message message : Message.readStoredMessages()) {
            addMessage(message);
        }
    }

    // a. Sender and recipient of every stored message
    public String displaySenderAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }

        String output = "";
        for (Message message : storedMessages) {
            output += "Sender: " + sender + " | Recipient: " + message.getRecipient() + "\n";
        }
        return output;
    }

    // b. The longest stored message
    public String getLongestMessage() {
        String longest = "";

        for (int i = 0; i < messageTexts.size(); i++) {
            if (flags.get(i).equals("Stored") && messageTexts.get(i).length() > longest.length()) {
                longest = messageTexts.get(i);
            }
        }

        if (longest.isEmpty()) {
            return "There are no stored messages.";
        }
        return longest;
    }

    // c. Finds a message ID and returns the recipient and message that go with it
    public String searchByMessageID(String messageID) {
        int index = messageIDs.indexOf(messageID);

        if (index == -1) {
            return "Message ID not found.";
        }
        return "Recipient: " + recipients.get(index) + "\nMessage: " + messageTexts.get(index);
    }

    // d. Every sent or stored message for a particular recipient
    public ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < recipients.size(); i++) {
            boolean sentOrStored = flags.get(i).equals("Sent") || flags.get(i).equals("Stored");

            if (recipients.get(i).equals(recipient) && sentOrStored) {
                results.add(messageTexts.get(i));
            }
        }
        return results;
    }

    // e. Deletes a message from all the arrays (and the JSON file if it was stored) using its hash
    public String deleteByHash(String messageHash) {
        int index = messageHashes.indexOf(messageHash);

        if (index == -1) {
            return "Message hash not found.";
        }

        String text = messageTexts.get(index);
        String flag = flags.get(index);

        // Remove it from the parallel arrays at the same index so they stay lined up
        messageIDs.remove(index);
        messageHashes.remove(index);
        recipients.remove(index);
        messageTexts.remove(index);
        flags.remove(index);

        switch (flag) {
            case "Sent" -> sentMessages.remove(text);
            case "Disregarded" -> disregardedMessages.remove(text);
            case "Stored" -> {
                for (int i = 0; i < storedMessages.size(); i++) {
                    if (storedMessages.get(i).getMessageHash().equals(messageHash)) {
                        storedMessages.remove(i);
                        break;
                    }
                }
                Message.saveStoredMessages(storedMessages);   // update the JSON file too
            }
            default -> { }
        }

        return "Message: \"" + text + "\" successfully deleted.";
    }

    // f. Report with the full details of every sent and stored message
    public String displayReport() {
        String report = "";

        for (int i = 0; i < messageIDs.size(); i++) {
            if (flags.get(i).equals("Disregarded")) {
                continue;   // disregarded messages aren't part of the report
            }

            report += "Message ID: " + messageIDs.get(i) + "\n"
                    + "Message Hash: " + messageHashes.get(i) + "\n"
                    + "Recipient: " + recipients.get(i) + "\n"
                    + "Message: " + messageTexts.get(i) + "\n"
                    + "Status: " + flags.get(i) + "\n\n";
        }

        if (report.isEmpty()) {
            return "There are no messages to report on.";
        }
        return report;
    }

    public ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    public ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    public ArrayList<Message> getStoredMessages() {
        return storedMessages;
    }

    public ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public ArrayList<String> getMessageIDs() {
        return messageIDs;
    }
}
