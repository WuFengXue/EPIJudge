package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 7.3 COMPUTE THE SPREADSHEET COLUMN ENCODING
 * <p>
 * Spreadsheets often use an alphabetical encoding of the successive columns.
 * Specifically, columns are identified by "A", "B", "C", ..., "X", "Y", "Z", "AA",
 * "AB", ..., "ZZ", "AAA", "AAB", ....
 * <p>
 * Implement a function that converts a spreadsheet column id to the corresponding
 * integer, with "A" corresponding to 1. For example, you should return 4 for "D", 27
 * for "AA", 702 for "ZZ", etc. How would you test your code?
 * <p>
 * Hint: There are 26 characters in ["A", "Z"], and each can be mapped to an integer.
 */
public class SpreadsheetEncoding {
    @EpiTest(testDataFile = "spreadsheet_encoding.tsv")

    public static int ssDecodeColID(final String col) {
        return solOne(col);
    }

    /**
     * 思路一：等价于 26 进制转 10 进制，'A' 对应 1，以此类推
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(final String col) {
        int result = 0;
        for (int i = 0; i < col.length(); i++) {
            char c = col.charAt(i);
            result = result * 26 + c - 'A' + 1;
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SpreadsheetEncoding.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
