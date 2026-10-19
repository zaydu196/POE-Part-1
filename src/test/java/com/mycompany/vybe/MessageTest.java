package com.mycompany.vybe;

import org.junit.Test;
import static org.junit.Assert.*;

/*
 * MessageTest class
 * Unit tests for the Message class, using the test data from the task.
 */
public class MessageTest {

    // Test data from the task
    Message message1 = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
    Message message2 = new Message("0098765432", 1, "08575975889", "Hi Keegan, did you receive the payment?");

    // ---------- Message length ----------

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        // Builds a message that is 260 characters long
        String longText = "";
        for (int i = 0; i < 260; i++) {
            longText += "a";
        }

        Message longMessage = new Message(0, "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", longMessage.checkMessageLength());
    }

    // ---------- Recipient number ----------

    @Test
    public void testRecipientCellSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCellFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    // ---------- Message hash ----------

    @Test
    public void testMessageHash() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        Message[] messages = {message1, message2};
        String[] expectedHashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }

    // ---------- Message ID ----------

    @Test
    public void testMessageIDCreated() {
        Message message = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertEquals("Message ID generated: " + message.getMessageID(), message.messageIDCreated());
    }

    // ---------- Send / Disregard / Store ----------

    @Test
    public void testSendMessage() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // ---------- Total messages ----------

    @Test
    public void testReturnTotalMessages() {
        int before = Message.returnTotalMessages();

        message1.sentMessage(1);   // Message 1: Send
        message2.sentMessage(2);   // Message 2: Discard

        // Only the sent message should be counted
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages() {
        message1.sentMessage(1);
        assertTrue(Message.printMessages().contains("Hi Mike, can you join us for dinner tonight?"));
    }
}
