package tools;

import java.io.Console;
import java.util.Arrays;

import business.AppUserManager;

public final class CreateAdmin {
    private CreateAdmin() {
    }

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.err.println("Run this command from an interactive terminal so the password can stay hidden.");
            System.exit(1);
        }

        String username = required(console, "Admin username: ");
        String firstName = required(console, "First name: ");
        String lastName = required(console, "Last name: ");
        String email = required(console, "Email: ");
        char[] password = console.readPassword("Password (12+ characters): ");
        char[] confirmation = console.readPassword("Confirm password: ");

        try {
            if (password == null || confirmation == null || !Arrays.equals(password, confirmation)) {
                System.err.println("Passwords did not match.");
                System.exit(1);
            }

            AppUserManager manager = new AppUserManager();
            if (!manager.createFirstAdmin(username, firstName, lastName, email, password)) {
                System.err.println("No account was created. An admin may already exist, or a field conflicts with an existing user.");
                System.exit(1);
            }

            System.out.println("First admin created. The password was stored as a salted hash.");
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
            if (confirmation != null) {
                Arrays.fill(confirmation, '\0');
            }
        }
    }

    private static String required(Console console, String prompt) {
        String value = console.readLine(prompt);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin profile fields must not be blank.");
        }
        return value.trim();
    }
}
