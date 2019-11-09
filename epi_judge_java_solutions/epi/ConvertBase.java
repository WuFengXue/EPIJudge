package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 7.2 BASE CONVERSION
 * In the decimal number system, the position of a digit is used to signify the power
 * of 10 that digit is to be multiplied with. For example, "314" denotes the number
 * 3 X 100 + 1 X 10 + 4 X 1. The base b number system generalizes the decimal number
 * system: the string "ak-1ak-2 ...a1a0", where 0 < ai < b, denotes in base-b the integer
 * a0 x b^0 + a1 x b^1 + a2 x b^2 + ... + ak-1 x b^(k-1)
 * <p>
 * Write a program that performs base conversion. The input is a string, an integer b1,and
 * another integer b2. The string represents be an integer in base b2. The output should
 * be the string representing the integer in base b2. Assume 2 < b1,b2 < 16. Use "A" to
 * represent 10, "B" for 11, ... , and "F" for 15. (For example, if the string is "615",
 * b1 is 7 and b2 is 13,then the result should be "1A7",since
 * 6x7^2 + 1x7 + 5 = 1X13^2 + 10X13 + 7.)
 * <p>
 * Hint: What base can you easily convert to and from?
 */
public class ConvertBase {
    @EpiTest(testDataFile = "convert_base.tsv")

    public static String convertBase(String numAsString, int b1, int b2) {
        return solTwo(numAsString, b1, b2);
    }

    /**
     * 思路一：先转成10进制，再转成新的进制（采用递归）
     * <p>
     * 时间复杂度：O(n(1 + log b2(b1)))
     * <p>
     * 空间复杂度：O(1)
     */
    private static String solOne(String numAsString, int b1, int b2) {
        boolean isNegative = numAsString.startsWith("-");
        int numAsInt = 0;
        for (int i = isNegative ? 1 : 0; i < numAsString.length(); i++) {
            numAsInt *= b1;
            numAsInt += Character.isDigit(numAsString.charAt(i))
                    ? numAsString.charAt(i) - '0'
                    : numAsString.charAt(i) - 'A' + 10;
        }
        return (isNegative ? "-" : "")
                + (numAsInt == 0 ? "0" : constructFromBase(numAsInt, b2));
    }

    private static String constructFromBase(int numAsInt, int base) {
        return numAsInt == 0
                ? ""
                : constructFromBase(numAsInt / base, base)
                + (char) (numAsInt % base < 10 ? '0' + numAsInt % base : 'A' + numAsInt % base - 10);
    }

    /**
     * 思路二：先转成10进制，再转成新的进制（采用迭代）
     * <p>
     * 时间复杂度：O(n(1 + log b2 (b1)))
     * <p>
     * 空间复杂度：O(1)
     */
    private static String solTwo(String numAsString, int b1, int b2) {
        boolean isNegative = numAsString.startsWith("-");
        int numAsInt = 0;
        for (int i = isNegative ? 1 : 0; i < numAsString.length(); i++) {
            numAsInt *= b1;
            numAsInt += Character.isDigit(numAsString.charAt(i))
                    ? numAsString.charAt(i) - '0'
                    : numAsString.charAt(i) - 'A' + 10;
        }
        long divider = 1;
        while (divider <= numAsInt) {
            divider *= b2;
        }
        if (divider > 1) {
            divider /= b2;
        }
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        while (divider > 0) {
            int num = (int) (numAsInt / divider);
            sb.append((char) (num < 10 ? '0' + num : 'A' + num - 10));
            numAsInt %= divider;
            divider /= b2;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ConvertBase.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
