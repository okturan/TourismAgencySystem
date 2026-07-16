package integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import business.AppUserManager;
import core.Db;
import entity.AppUser;

public final class AppUserSecurityIntegrationTest {
    public static void main(String[] args) throws Exception {
        Connection connection = Db.getInstance();
        assertCount(connection, "board_types", 7);
        assertCount(connection, "hotel_amenities", 7);
        assertCount(connection, "room_amenities", 5);
        assertCount(connection, "users", 0);
        assertCount(connection, "hotels", 0);
        assertCount(connection, "rooms", 0);
        assertCount(connection, "reservations", 0);

        AppUserManager manager = new AppUserManager();
        firstAdminIsAtomicAndHashed(connection, manager);
        legacyLoginMigratesConditionally(connection, manager);
        userChangesNeverExposeOrPersistPlaintext(connection, manager);
    }

    private static void firstAdminIsAtomicAndHashed(Connection connection, AppUserManager manager) {
        char[] password = "first-admin-password".toCharArray();
        char[] wrongPassword = "wrong-admin-password".toCharArray();
        try {
            assertTrue(!manager.hasAdmin(), "Fresh schema should not contain an admin");
            assertTrue(manager.createFirstAdmin(
                    "example-admin",
                    "Example",
                    "Admin",
                    "admin@example.com",
                    password
            ), "First admin should be created");
            assertTrue(!manager.createFirstAdmin(
                    "second-admin",
                    "Second",
                    "Admin",
                    "second-admin@example.com",
                    password
            ), "A second first-admin attempt must not create an account");

            String stored = storedPassword(connection, "example-admin");
            assertTrue(stored.startsWith("pbkdf2_sha256$600000$"), "Admin password should be hashed");
            assertTrue(!stored.contains("first-admin-password"), "Admin password must not be plaintext");
            assertTrue(manager.findByLogin("example-admin", wrongPassword) == null, "Wrong password must fail");

            AppUser authenticated = manager.findByLogin("example-admin", password);
            assertTrue(authenticated != null, "Correct password should authenticate");
            assertTrue(authenticated.getPassword() == null, "Authenticated entities must not expose hashes");
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(wrongPassword, '\0');
        }
    }

    private static void legacyLoginMigratesConditionally(Connection connection, AppUserManager manager)
            throws SQLException {
        String insert = "INSERT INTO users (username, password, first_name, last_name, email, role) "
                + "VALUES (?, ?, ?, ?, ?, 'staff')";
        try (PreparedStatement statement = connection.prepareStatement(insert)) {
            statement.setString(1, "legacy-user");
            statement.setString(2, "legacy-pass");
            statement.setString(3, "Legacy");
            statement.setString(4, "Example");
            statement.setString(5, "legacy@example.com");
            statement.executeUpdate();
        }

        char[] correct = "legacy-pass".toCharArray();
        char[] wrong = "wrong-legacy".toCharArray();
        try {
            assertTrue(manager.findByLogin("legacy-user", wrong) == null, "Wrong legacy password must fail");
            assertEquals("legacy-pass", storedPassword(connection, "legacy-user"));

            AppUser authenticated = manager.findByLogin("legacy-user", correct);
            assertTrue(authenticated != null, "Correct legacy password should authenticate once");
            String migrated = storedPassword(connection, "legacy-user");
            assertTrue(migrated.startsWith("pbkdf2_sha256$600000$"), "Legacy password should migrate");
            assertTrue(!"legacy-pass".equals(migrated), "Legacy plaintext must be replaced");
        } finally {
            Arrays.fill(correct, '\0');
            Arrays.fill(wrong, '\0');
        }
    }

    private static void userChangesNeverExposeOrPersistPlaintext(
            Connection connection,
            AppUserManager manager
    ) throws SQLException {
        AppUser staff = new AppUser();
        staff.setUsername("example-staff");
        staff.setFirstName("Example");
        staff.setLastName("Staff");
        staff.setEmail("staff@example.com");
        staff.setRole("staff");

        char[] initialPassword = "initial-staff-password".toCharArray();
        char[] changedPassword = "changed-staff-password".toCharArray();
        try {
            assertTrue(manager.createUser(staff, initialPassword), "New user should be created");
            String initialHash = storedPassword(connection, "example-staff");
            assertTrue(initialHash.startsWith("pbkdf2_sha256$600000$"), "New user should be hashed");

            AppUser editable = findUser(manager.findAll(), "example-staff");
            assertTrue(editable != null && editable.getPassword() == null, "Lists must scrub password hashes");
            editable.setFirstName("Updated");
            assertTrue(manager.updateUser(editable, new char[0]), "Blank edit should preserve the password");
            assertEquals(initialHash, storedPassword(connection, "example-staff"));

            assertTrue(manager.updateUser(editable, changedPassword), "Password change should succeed");
            String changedHash = storedPassword(connection, "example-staff");
            assertTrue(!initialHash.equals(changedHash), "Password change should replace the hash");
            assertTrue(manager.findByLogin("example-staff", initialPassword) == null, "Old password must stop working");
            assertTrue(manager.findByLogin("example-staff", changedPassword) != null, "New password should work");

            ArrayList<Object[]> rows = manager.formatDataForTable(manager.findAll());
            for (Object[] row : rows) {
                assertTrue(row.length == 6, "Admin table projection must contain six fields");
                for (Object value : row) {
                    assertTrue(!initialHash.equals(value) && !changedHash.equals(value), "Rows must not expose hashes");
                }
            }
        } finally {
            Arrays.fill(initialPassword, '\0');
            Arrays.fill(changedPassword, '\0');
        }
    }

    private static AppUser findUser(ArrayList<AppUser> users, String username) {
        for (AppUser user : users) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    private static void assertCount(Connection connection, String table, int expected) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + table)) {
            resultSet.next();
            if (resultSet.getInt(1) != expected) {
                throw new AssertionError("Expected " + expected + " rows in " + table);
            }
        }
    }

    private static String storedPassword(Connection connection, String username) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new AssertionError("User not found: " + username);
                }
                return resultSet.getString(1);
            }
        } catch (SQLException exception) {
            throw new AssertionError("Unable to read stored password", exception);
        }
    }

    private static void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
