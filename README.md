# Vybe

Vybe is a console-based chat app built for the PROG5121 Portfolio of Evidence.

**Student:** Zaydan Suleman (ST10535212)

## Part 1: Registration and Login

- Create an account with a first name, last name, username, password and South African cell phone number.
- The username must contain an underscore and be no more than five characters long.
- The password must be at least eight characters long and contain a capital letter, a number and a special character.
- The cell phone number must start with the +27 international code followed by nine digits.
- Log in with the same username and password to receive a welcome message.

## Part 2: Sending Messages (QuickChat)

- Messages can only be sent after logging in successfully.
- The user chooses how many messages they want to send, then uses a menu to Send Messages, Show recently sent messages (Coming Soon) or Quit.
- Each message gets a random ten-digit Message ID and a Message Hash (e.g. `00:0:HITONIGHT`).
- The recipient number must use the +27 international code, and the message can't be more than 250 characters.
- After writing a message the user can Send, Disregard or Store it. Stored messages are saved to `storedMessages.json`.
- The full details of each sent message are shown, and the total number of messages sent is displayed at the end.

## How to run

- Run the `Vybe` class to start the app in the terminal.
- Run `LoginTest` and `MessageTest` (or `mvn test`) to run the unit tests.
- The tests also run automatically on GitHub with every push (GitHub Actions).

## References

JSON.org. n.d. *Introducing JSON*. Available at: https://www.json.org/json-en.html [Accessed 19 October 2026].

validatorjs. 2021. *validator.js: Changed regex for BW* [Source code]. GitHub. Available at: https://github.com/validatorjs/validator.js/commit/ff1e91eb8b68f0cba8b77b8c84d1ee9334b2453e [Accessed 17 September 2026].
