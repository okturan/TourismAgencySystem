package core;

import entity.AppUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class DatabaseConfigTest {
    public static void main(String[] args) {
        loadsSystemProperties();
        environmentTakesPrecedence();
        rejectsMissingValues();
        redactsApplicationPasswords();
    }

    private static void loadsSystemProperties() {
        Properties properties = properties("property-url", "property-user", "property-password");
        DatabaseConfig config = DatabaseConfig.from(new HashMap<String, String>(), properties);

        assertEquals("property-url", config.getUrl());
        assertEquals("property-user", config.getUser());
        assertEquals("property-password", config.getPassword());
    }

    private static void environmentTakesPrecedence() {
        Map<String, String> environment = new HashMap<String, String>();
        environment.put("TOURISM_DB_URL", "environment-url");
        environment.put("TOURISM_DB_USER", "environment-user");
        environment.put("TOURISM_DB_PASSWORD", "environment-password");

        DatabaseConfig config = DatabaseConfig.from(
                environment,
                properties("property-url", "property-user", "property-password")
        );

        assertEquals("environment-url", config.getUrl());
        assertEquals("environment-user", config.getUser());
        assertEquals("environment-password", config.getPassword());
    }

    private static void rejectsMissingValues() {
        try {
            DatabaseConfig.from(new HashMap<String, String>(), new Properties());
            throw new AssertionError("Expected missing configuration to be rejected");
        } catch (IllegalStateException expected) {
            if (!expected.getMessage().contains("TOURISM_DB_URL")) {
                throw new AssertionError("Missing configuration message was not actionable");
            }
        }
    }

    private static void redactsApplicationPasswords() {
        AppUser user = new AppUser();
        user.setUsername("demo-user");
        user.setPassword("password-sentinel");
        String rendered = user.toString();

        if (rendered.contains("password-sentinel") || !rendered.contains("[REDACTED]")) {
            throw new AssertionError("AppUser.toString() must not expose the password");
        }
    }

    private static Properties properties(String url, String user, String password) {
        Properties properties = new Properties();
        properties.setProperty("tourism.db.url", url);
        properties.setProperty("tourism.db.user", user);
        properties.setProperty("tourism.db.password", password);
        return properties;
    }

    private static void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }
}
