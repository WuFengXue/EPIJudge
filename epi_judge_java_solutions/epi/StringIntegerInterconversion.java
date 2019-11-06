package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;

/**
 * 7.1 INTER-CONVERT STRINGS AND INTEGERS
 * <p>
 * A string is a sequence of characters. A string may encode an integer, e.g., "123"
 * encodes 123. In this problem, you are to implement methods that take a string
 * representing an integer and return the corresponding integer, and vice versa. Your
 * code should handle negative integers. You cannot use library functions like stoi in
 * C++ and parseInt in Java.
 * Implement string/integer inter-conversion functions.
 * <p>
 * Hint: Build the result one digit at a time.
 */
public class StringIntegerInterconversion {

    public static String intToString(int x) {
        return itsSolTwo(x);
    }

    /**
     * intToString 思路一：每次取一个字符，添加到头部（需要移动数组）
     */
    private static String itsSolOne(int x) {
        // 转成长整形以规避 Math.abs(Integer.MIN_VALUE) 问题
        long absX = Math.abs((long) x);
        StringBuilder sb = new StringBuilder();
        do {
            sb.insert(0, (char) ('0' + absX % 10));
            absX /= 10;
        } while (absX != 0);
        if (x < 0) {
            sb.insert(0, '-');
        }
        return sb.toString();
    }

    /**
     * intToString 思路二：每次取一个字符，添加到尾部，全部添加完毕后再反转字符串
     */
    private static String itsSolTwo(int x) {
        boolean isNegative = (x < 0);
        StringBuilder sb = new StringBuilder();
        do {
            // 对余数取绝对值可以规避 Math.abs(Integer.MIN_VALUE) 问题
            sb.append((char) ('0' + Math.abs(x % 10)));
            x /= 10;
        } while (x != 0);
        if (isNegative) {
            // Adds the negative sign back.
            sb.append('-');
        }
        sb.reverse();
        return sb.toString();
    }

    public static int stringToInt(String s) {
        return stiSolOne(s);
    }

    /**
     * stringToInt 思路一：每次添加一位，每位对应的数字为：（10 ^ i） * d(i)
     */
    private static int stiSolOne(String s) {
        boolean isNegative = s.charAt(0) == '-';
        int result = 0;
        int numDigits = isNegative ? s.length() - 1 : s.length();
        for (int i = isNegative ? 1 : 0; i < s.length(); i++) {
            final int digit = s.charAt(i) - '0';
            // 需先转成整形，超过整形上限的浮点型转整形会始终保留在 Integer.MAX_VALUE
            result += (int) (digit * Math.pow(10, --numDigits));
        }
        return isNegative ? -result : result;
    }

    /**
     * stringToInt 思路二：每次添加一位，添加前将之前的结果乘以十，再加上新的数字，
     * 每位对应的数字为： d(i)
     */
    private static int stiSolTwo(String s) {
        int result = 0;
        for (int i = s.charAt(0) == '-' ? 1 : 0; i < s.length(); i++) {
            final int digit = s.charAt(i) - '0';
            result = result * 10 + digit;
        }
        return s.charAt(0) == '-' ? -result : result;
    }

    @EpiTest(testDataFile = "string_integer_interconversion.tsv")
    public static void wrapper(int x, String s) throws TestFailure {
        if (!intToString(x).equals(s)) {
            throw new TestFailure("Int to string conversion failed");
        }
        if (stringToInt(s) != x) {
            throw new TestFailure("String to int conversion failed");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "StringIntegerInterconversion.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
