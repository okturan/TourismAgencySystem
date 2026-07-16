package security;

import java.util.Arrays;

public final class PasswordHasherTest {
    public static void main(String[] args) {
        hashesAreSaltedAndVerifiable();
        legacyValuesAreRecognizedWithoutWeakFallbacks();
        malformedHashesFailClosed();
        passwordPolicyHasReasonableBounds();
    }

    private static void hashesAreSaltedAndVerifiable() {
        char[] password = "correct horse battery staple".toCharArray();
        char[] wrongPassword = "wrong horse battery staple".toCharArray();
        try {
            String firstHash = PasswordHasher.hash(password);
            String secondHash = PasswordHasher.hash(password);

            assertTrue(firstHash.startsWith("pbkdf2_sha256$600000$"));
            assertTrue(!firstHash.equals(secondHash));
            assertTrue(PasswordHasher.verify(password, firstHash));
            assertTrue(!PasswordHasher.verify(wrongPassword, firstHash));
            assertTrue(PasswordHasher.isEncoded(firstHash));
            assertTrue(!PasswordHasher.needsRehash(firstHash));
            assertTrue(!firstHash.contains("correct horse battery staple"));
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(wrongPassword, '\0');
        }
    }

    private static void legacyValuesAreRecognizedWithoutWeakFallbacks() {
        char[] legacyPassword = "legacy-password".toCharArray();
        try {
            assertTrue(PasswordHasher.verify(legacyPassword, "legacy-password"));
            assertTrue(!PasswordHasher.verify(legacyPassword, "different-password"));
            assertTrue(!PasswordHasher.isEncoded("legacy-password"));
            assertTrue(PasswordHasher.needsRehash("legacy-password"));

            String migratedHash = PasswordHasher.hashLegacy(legacyPassword);
            assertTrue(PasswordHasher.isEncoded(migratedHash));
            assertTrue(PasswordHasher.verify(legacyPassword, migratedHash));
        } finally {
            Arrays.fill(legacyPassword, '\0');
        }
    }

    private static void malformedHashesFailClosed() {
        char[] password = "correct horse battery staple".toCharArray();
        try {
            assertTrue(!PasswordHasher.verify(password, "pbkdf2_sha256$not-a-number$salt$hash"));
            assertTrue(!PasswordHasher.verify(password, "pbkdf2_sha256$600000$%%%$%%%"));
            assertTrue(!PasswordHasher.verify(password, "pbkdf2_sha256$1000001$AA==$AA=="));
            assertTrue(!PasswordHasher.verify(password, "pbkdf2_sha256$600000$AA==$AA=="));
            assertTrue(!PasswordHasher.verify(password, "pbkdf2_sha256$600000$missing-part"));
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private static void passwordPolicyHasReasonableBounds() {
        assertTrue(!PasswordPolicy.isStrong("short".toCharArray()));
        assertTrue(!PasswordPolicy.isStrong("            ".toCharArray()));
        assertTrue(PasswordPolicy.isStrong("twelve-chars!".toCharArray()));

        char[] oversized = new char[PasswordPolicy.MAXIMUM_LENGTH + 1];
        Arrays.fill(oversized, 'a');
        assertTrue(!PasswordPolicy.isStrong(oversized));
        Arrays.fill(oversized, '\0');
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}
