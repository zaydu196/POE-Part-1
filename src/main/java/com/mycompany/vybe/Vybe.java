package com.mycompany.vybe;

import java.util.Scanner;

/*
 * Vybe class
 * Main class of the app. Lets the user create an account and then log in to it.
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
    }
}
