package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

/**
 * 7.12 IMPLEMENT RUN-LENGTH ENCODING
 * <p>
 * Run-length encoding (RLE) compression offers a fast way to do efficient
 * on-the-fly compression and decompression of strings. The idea is simple—encode
 * successive repeated characters by the repetition count and the character. For
 * example, the RLE of "aaaabcccaa" is "4a1b3c2a". The decoding of "3e4f2e" returns
 * "eeeffffee".
 * <p>
 * Implement run-length encoding and decoding functions. Assume the string to be
 * encoded consists of letters of the alphabet, with no digits, and the string to
 * be decoded is a valid encoding.
 * <p>
 * Hint: This is similar to converting between binary and string representations.
 */
public class RunLengthCompression {

    public static String decoding(String s) {
        return decodingSolOne(s);
    }

    private static String decodingSolOne(String s) {
        int count = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                count = count * 10 + c - '0';
            }
            // c is a letter of alphabet.
            else {
                // Appends count copies of c to result.
                while (count > 0) {
                    result.append(c);
                    count--;
                }
            }
        }
        return result.toString();
    }

    public static String encoding(String s) {
        return encodingSolOne(s);
    }

    private static String encodingSolOne(String s) {
        int count = 1;
        StringBuilder ss = new StringBuilder();
        for (int i = 1; i <= s.length(); i++) {
            if (i == s.length() || s.charAt(i) != s.charAt(i - 1)) {
                // Found new character so write the count of previous character.
                ss.append(count);
                ss.append(s.charAt(i - 1));
                count = 1;
            }
            // s.charAt(i) == s.charAt(i - 1).
            else {
                count++;
            }
        }
        return ss.toString();
    }

    @EpiTest(testDataFile = "run_length_compression.tsv")
    public static void rleTester(String encoded, String decoded)
            throws TestFailure {
        if (!decoding(encoded).equals(decoded)) {
            throw new TestFailure("Decoding failed");
        }
        if (!encoding(decoded).equals(encoded)) {
            throw new TestFailure("Encoding failed");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RunLengthCompression.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
