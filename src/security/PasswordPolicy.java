package security;

public final class PasswordPolicy {
    public static final int MINIMUM_LENGTH = 12;
    public static final int MAXIMUM_LENGTH = 256;

    private PasswordPolicy() {
    }

    public static boolean isStrong(char[] password) {
        if (password == null
                || password.length < MINIMUM_LENGTH
                || password.length > MAXIMUM_LENGTH) {
            return false;
        }

        for (char character : password) {
            if (!Character.isWhitespace(character)) {
                return true;
            }
        }

        return false;
    }

    public static void requireStrong(char[] password) {
        if (!isStrong(password)) {
            throw new IllegalArgumentException(
                    "Passwords must be " + MINIMUM_LENGTH + "-" + MAXIMUM_LENGTH
                            + " characters and not only whitespace."
            );
        }
    }
}
