package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.8 REVERSE DIGITS
 * <p>
 * Write a program which takes an integer and returns the integer corresponding to the digits
 * of the input written in reverse order. For example, the reverse of 42 is 24, and the reverse
 * of -314 is -413.
 * <p>
 * Hint: How would you solve the same problem if the input is presented as a string?
 */
public class ReverseDigits {
    @EpiTest(testDataFile = "reverse_digits.tsv")
    public static long reverse(int x) {
        return solTwo(x);
    }

    /**
     * 每次取出 LSD，然后添加到新的数字中
     * <p>
     * 时间复杂度：O(n)，n 为 x 的位数（十进制）
     */
    private static long solTwo(int x) {
        long result = 0;
        int xRemaining = Math.abs(x);
        while (xRemaining > 0) {
            result = result * 10 + xRemaining % 10;
            xRemaining /= 10;
        }
        return x < 0 ? -result : result;
    }

    /**
     * 先转成字符串，然后反转字符串，最后再转成整形
     * <p>
     * 时间复杂度：O(n / 2)，n 为 x 的位数（十进制）
     */
    private static long solOne(int x) {
        StringBuilder xSb = new StringBuilder(String.valueOf(Math.abs(x)));
        char tmpChar;
        for (int i = 0, j = xSb.length() - 1; i < j; i++, j--) {
            tmpChar = xSb.charAt(i);
            xSb.setCharAt(i, xSb.charAt(j));
            xSb.setCharAt(j, tmpChar);
        }
        long result = Long.valueOf(xSb.toString());
        return x < 0 ? -result : result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ReverseDigits.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
