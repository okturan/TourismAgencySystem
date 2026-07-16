package tools;

import java.io.Console;
import java.util.Arrays;

import business.AppUserManager;

public final class CreateAdmin {
    private CreateAdmin() {
    }

    public static void main(String[] args) {
        int exitCode = run();
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    private static int run() {
        Console console = System.console();
        if (console == null) {
            System.err.println("Run this command from an interactive terminal so the password can stay hidden.");
            return 1;
        }

        char[] password = null;
        char[] confirmation = null;
        try {
            String username = required(console, "Admin username: ");
            String firstName = required(console, "First name: ");
            String lastName = required(console, "Last name: ");
            String email = required(console, "Email: ");
            password = console.readPassword("Password (12+ characters): ");
            confirmation = console.readPassword("Confirm password: ");

            if (password == null || confirmation == null || !Arrays.equals(password, confirmation)) {
                throw new IllegalArgumentException("Passwords did not match.");
            }

            AppUserManager manager = new AppUserManager();
            if (!manager.createFirstAdmin(username, firstName, lastName, email, password)) {
                throw new IllegalArgumentException(
                        "No account was created. An admin may already exist, or a field conflicts with an existing user."
                );
            }

            System.out.println("First admin created. The password was stored as a salted hash.");
            return 0;
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            return 1;
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
