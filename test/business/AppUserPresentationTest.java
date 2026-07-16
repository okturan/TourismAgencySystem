package business;

import entity.AppUser;

public final class AppUserPresentationTest {
    public static void main(String[] args) {
        AppUser user = new AppUser();
        user.setId(7);
        user.setUsername("example-admin");
        user.setPassword("password-hash-sentinel");
        user.setFirstName("Example");
        user.setLastName("Admin");
        user.setEmail("admin@example.com");
        user.setRole("admin");

        Object[] row = AppUserManager.formatUserRow(user);
        if (row.length != 6) {
            throw new AssertionError("Admin table rows must contain six non-password fields");
        }
        for (Object value : row) {
            if ("password-hash-sentinel".equals(value)) {
                throw new AssertionError("Admin table rows must not expose password hashes");
            }
        }
        if (user.toString().contains("password-hash-sentinel")) {
            throw new AssertionError("AppUser.toString() must not expose password hashes");
        }
    }
}
