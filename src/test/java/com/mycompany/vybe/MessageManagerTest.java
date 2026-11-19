package com.mycompany.vybe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * MessageManagerTest class
 * Unit tests for Part 3 (the arrays and the Stored Messages features),
 * using the five test messages from the task.
 */
public class MessageManagerTest {

    MessageManager manager;

    // Test data messages 1 to 5
    Message message1;
    Message message2;
    Message message3;
    Message message4;
    Message message5;

    // Before each test: start with an empty test JSON file and fill the arrays with the test data
    @Before
    public void setUp() {
        Message.setFileName("testStoredMessages.json");
        new File("testStoredMessages.json").delete();

        manager = new MessageManager("Kyle Smith (+27838968976)");

        message1 = new Message("0012345601", 0, "+27834557896", "Did you get the cake?");
        message2 = new Message("0012345602", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message("0012345603", 2, "+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message("0838884567", 3, "0838884567", "It is dinner time!");
        message5 = new Message("0012345605", 4, "+27838884567", "Ok, I am leaving without you.");

        addTestMessage(message1, 1);   // Sent
        addTestMessage(message2, 3);   // Stored
        addTestMessage(message3, 2);   // Disregard
        addTestMessage(message4, 1);   // Sent
        addTestMessage(message5, 3);   // Stored
    }

    // Gives the message its flag (1 = Send, 2 = Disregard, 3 = Store) and adds it to the arrays
    private void addTestMessage(Message message, int choice) {
        message.sentMessage(choice);
        manager.addMessage(message);
    }

    // ---------- Arrays correctly populated ----------

    @Test
    public void testSentMessagesArray() {
        assertEquals(Arrays.asList("Did you get the cake?", "It is dinner time!"), manager.getSentMessages());
    }

    @Test
    public void testDisregardedMessagesArray() {
        assertEquals(Arrays.asList("Yohoooo, I am at your gate."), manager.getDisregardedMessages());
    }

    @Test
    public void testMessageHashesAndIDsArrays() {
        assertEquals(5, manager.getMessageHashes().size());
        assertEquals("00:0:DIDCAKE", manager.getMessageHashes().get(0));
        assertEquals(Arrays.asList("0012345601", "0012345602", "0012345603", "0838884567", "0012345605"),
                manager.getMessageIDs());
    }

    @Test
    public void testStoredMessagesReadFromJsonFile() {
        // A new manager reads the stored messages back from the JSON file
        MessageManager newManager = new MessageManager("Kyle Smith (+27838968976)");
        newManager.loadStoredMessages();

        ArrayList<Message> stored = newManager.getStoredMessages();
        assertEquals(2, stored.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", stored.get(0).getMessageText());
        assertEquals("Ok, I am leaving without you.", stored.get(1).getMessageText());
        assertEquals("+27838884567", stored.get(1).getRecipient());
        assertEquals(message2.getMessageHash(), stored.get(0).getMessageHash());
    }

    // ---------- Stored Messages features ----------

    @Test
    public void testDisplaySenderAndRecipients() {
        String expected = "Sender: Kyle Smith (+27838968976) | Recipient: +27838884567\n"
                + "Sender: Kyle Smith (+27838968976) | Recipient: +27838884567\n";
        assertEquals(expected, manager.displaySenderAndRecipients());
    }

    @Test
    public void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.", manager.getLongestMessage());
    }

    @Test
    public void testSearchByMessageID() {
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!", manager.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        assertEquals("Message ID not found.", manager.searchByMessageID("1111111111"));
    }

    @Test
    public void testSearchByRecipient() {
        assertEquals(Arrays.asList("Where are you? You are late! I have asked you to be on time.", "Ok, I am leaving without you."),
                manager.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteByHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                manager.deleteByHash(message2.getMessageHash()));

        // It should be gone from the arrays and from the JSON file
        assertEquals(Arrays.asList("Ok, I am leaving without you."), manager.searchByRecipient("+27838884567"));
        assertEquals(1, Message.readStoredMessages().size());
    }

    @Test
    public void testDeleteByHashNotFound() {
        assertEquals("Message hash not found.", manager.deleteByHash("99:9:NOTHERE"));
    }

    @Test
    public void testDisplayReport() {
        String report = manager.displayReport();

        // Every sent message is in the report with its hash, recipient and message
        Message[] sent = {message1, message4};
        for (Message message : sent) {
            assertTrue(report.contains("Message Hash: " + message.getMessageHash()));
            assertTrue(report.contains("Recipient: " + message.getRecipient()));
            assertTrue(report.contains("Message: " + message.getMessageText()));
        }

        // The disregarded message is left out
        assertFalse(report.contains("Yohoooo, I am at your gate."));
    }
}
