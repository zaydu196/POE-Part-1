package com.mycompany.vybe;

import org.junit.Test;
import static org.junit.Assert.*;

/*
 * LoginTest class
 * Unit tests for the Login class, using the test data from the task.
 */
public class LoginTest {

    // A new Login object is created for each test
    Login login = new Login("Kyle", "Smith");

    // ---------- assertEquals tests ----------

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertEquals("Username successfully captured.", login.getUsernameMessage("kyl_1"));

        // Register and log in with kyl_1 to check the welcome message
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        boolean loggedIn = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus(loggedIn));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.getUsernameMessage("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        assertEquals("Password successfully captured.", login.getPasswordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.getPasswordMessage("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        assertEquals("Cell number successfully captured.", login.getCellPhoneMessage("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                login.getCellPhoneMessage("08966553"));
    }

    // ---------- assertTrue / assertFalse tests ----------

    @Test
    public void testLoginSuccessful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "password"));
    }

    @Test
    public void testCheckUserNameTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserNameFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testCheckPasswordComplexityTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexityFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCheckCellPhoneNumberTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCheckCellPhoneNumberFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
