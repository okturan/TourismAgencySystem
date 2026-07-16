package security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    private static final String SCHEME = "pbkdf2_sha256";
    private static final String SEPARATOR = "$";
    private static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    public static String hash(char[] password) {
        PasswordPolicy.requireStrong(password);

        return encode(password);
    }

    public static String hashLegacy(char[] password) {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("A legacy password must not be empty.");
        }

        return encode(password);
    }

    private static String encode(char[] password) {

        byte[] salt = new byte[SALT_BYTES];
        SECURE_RANDOM.nextBytes(salt);
        byte[] derivedKey = derive(password, salt, ITERATIONS);

        return SCHEME + SEPARATOR
                + ITERATIONS + SEPARATOR
                + Base64.getEncoder().encodeToString(salt) + SEPARATOR
                + Base64.getEncoder().encodeToString(derivedKey);
    }

    public static boolean verify(char[] candidate, String storedValue) {
        if (candidate == null || storedValue == null) {
            return false;
        }

        if (!storedValue.startsWith(SCHEME + SEPARATOR)) {
            return constantTimeLegacyMatch(candidate, storedValue);
        }

        String[] parts = storedValue.split("\\$", -1);
        if (parts.length != 4 || !SCHEME.equals(parts[0])) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);
            if (iterations < 1 || iterations > 1_000_000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            if (salt.length != SALT_BYTES || expected.length != KEY_BITS / 8) {
                return false;
            }
            byte[] actual = derive(candidate, salt, iterations);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static boolean isEncoded(String storedValue) {
        return storedValue != null && storedValue.startsWith(SCHEME + SEPARATOR);
    }

    public static boolean needsRehash(String storedValue) {
        if (!isEncoded(storedValue)) {
            return true;
        }

        String[] parts = storedValue.split("\\$", -1);
        if (parts.length != 4) {
            return false;
        }

        try {
            return Integer.parseInt(parts[1]) < ITERATIONS;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations) {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, KEY_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(keySpec)
                    .getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("PBKDF2-HMAC-SHA256 is unavailable.", exception);
        } finally {
            keySpec.clearPassword();
        }
    }

    private static boolean constantTimeLegacyMatch(char[] candidate, String storedValue) {
        int maximumLength = Math.max(candidate.length, storedValue.length());
        int difference = candidate.length ^ storedValue.length();

        for (int index = 0; index < maximumLength; index++) {
            char candidateCharacter = index < candidate.length ? candidate[index] : 0;
            char storedCharacter = index < storedValue.length() ? storedValue.charAt(index) : 0;
            difference |= candidateCharacter ^ storedCharacter;
        }

        return difference == 0;
    }
}
