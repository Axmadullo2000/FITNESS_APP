package com.gym.util;

/**
 * Normalizes Uzbek phone numbers to the canonical form: +998XXXXXXXXX
 *
 * Examples:
 *   "90 123 45 67"    → "+99890123456 7" → "+998901234567"
 *   "998901234567"    → "+998901234567"
 *   "+998 90 123 45 67" → "+998901234567"
 *   "901234567"       → "+998901234567"
 *   "admin"           → "admin"  (non-phone strings returned unchanged)
 */
public final class PhoneUtils {

    private PhoneUtils() {}

    public static String normalize(String input) {
        if (input == null) return null;

        // Remove all whitespace characters
        String digits = input.replaceAll("\\s+", "");

        // If it looks like a phone (starts with +, or is mostly digits)
        if (digits.startsWith("+998")) {
            return digits;                          // already canonical
        }
        if (digits.startsWith("998") && digits.length() >= 11) {
            return "+" + digits;                    // 998XXXXXXXXX → +998XXXXXXXXX
        }
        if (digits.matches("\\d{9}")) {
            return "+998" + digits;                 // 9-digit local → +998XXXXXXXXX
        }
        if (digits.matches("\\d{12}") && digits.startsWith("998")) {
            return "+" + digits;
        }

        // Not recognizable as a phone (e.g. "admin") — return with spaces stripped only
        return digits;
    }
}
