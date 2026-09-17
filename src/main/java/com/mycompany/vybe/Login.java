package com.mycompany.vybe;

/*
 * Login class
 * Checks the username, password and cell phone number when a user creates an account, and lets the user log in with the same username and password.
 */
public class Login {

    // User details
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    // Saves the user's first and last name when the Login object is created
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Username must contain an underscore and be 5 characters or less
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Password must be at least 8 characters and have a capital letter, a number and a special character
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Checks each character in the password
        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            } else if (Character.isDigit(character)) {
                hasNumber = true;
            } else if (!Character.isLetter(character)) {
                hasSpecial = true;
            }
        }

        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    // Cell phone number must start with +27 followed by 9 digits
    // Regex adapted from validator.js (validatorjs, 2021)
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber.matches("^\\+27[0-9]{9}$");
    }

    // Returns the message for the username
    public String getUsernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    // Returns the message for the password
    public String getPasswordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    // Returns the message for the cell phone number
    public String getCellPhoneMessage(String cellPhoneNumber) {
        if (checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number successfully captured.";
        } else {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
    }

    // Returns the messages for all three details and saves the user
    // only if everything is correct
    public String registerUser(String username, String password, String cellPhoneNumber) {
        String message = getUsernameMessage(username) + "\n"
                + getPasswordMessage(password) + "\n"
                + getCellPhoneMessage(cellPhoneNumber);

        if (checkUserName(username) && checkPasswordComplexity(password) && checkCellPhoneNumber(cellPhoneNumber)) {
            this.username = username;
            this.password = password;
            this.cellPhoneNumber = cellPhoneNumber;
            message += "\nUser successfully registered.";
        }

        return message;
    }

    // Returns true if a user has been registered
    public boolean isRegistered() {
        return username != null;
    }

    // Returns true if the entered username and password match the registered ones
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(username) && enteredPassword.equals(password);
    }

    // Returns the welcome message or the error message
    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
