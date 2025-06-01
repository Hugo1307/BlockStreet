package dev.hugog.minecraft.blockstreet.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class FormattingUtils {

    /**
     * Formats a double value into a human-readable string with appropriate units.
     *
     * <p>This method formats the given double value into a string representation that follows the following rules:
     * <ul>
     *     <li>If the value is less than 1,000, it is formatted with two decimal places (e.g., "123.45").</li>
     *     <li>If the value is between 1,000 and 999,999, it is divided by 1,000 and suffixed with "K" (e.g., "1.23 K").</li>
     *     <li>If the value is between 1,000,000 and 999,999,999, it is divided by 1,000,000 and suffixed with "M" (e.g., "1.23 M").</li>
     *     <li>If the value is between 1,000,000,000 and 999,999,999,999, it is divided by 1,000,000,000 and suffixed with "B" (e.g., "1.23 B").</li>
     *     <li>If the value is 1,000,000,000,000 or greater, it is divided by 1,000,000,000,000 and suffixed with "T" (e.g., "1.23 T").</li>
     * </ul>
     *
     * @param value the double value to format
     * @return a formatted string representation of the value
     */
    public static String formatDouble(double value) {
        if (Math.abs(value) < 1_000) {
            return String.format("%.2f", value);
        } else if (Math.abs(value) < 1_000_000) {
            return String.format("%.2f K", value / 1_000);
        } else if (Math.abs(value) < 1_000_000_000) {
            return String.format("%.2f M", value / 1_000_000);
        } else if (Math.abs(value) < 1_000_000_000_000L) {
            return String.format("%.2f B", value / 1_000_000_000);
        } else {
            return String.format("%.2f T", value / 1_000_000_000_000L);
        }
    }

}
