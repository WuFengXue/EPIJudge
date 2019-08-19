package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.9 CHECK IF A DECIMAL INTEGER IS A PALINDROME
 * <p>
 * A palindromic string is one which reads the same forwards and backwards, e.g., "redivider".
 * In this problem, you are to write a program which determines if the decimal representation
 * of an integer is a palindromic string. For example, your program should return true for the
 * inputs 0,1,7,11,121,333, and 2147447412, and false for the inputs -1,12,100, and 2147483647.
 * <p>
 * Write a program that takes an integer and determines if that integer's representation as a decimal
 * string is a palindrome.
 * <p>
 * Hint: It's easy to come up with a simple expression that extracts the least significant digit.
 * Can you find a simple expression for the most significant digit?
 */
public class IsNumberPalindromic {

    @EpiTest(testDataFile = "is_number_palindromic.tsv")
    public static boolean isPalindromeNumber(int x) {
        return solThree(x);
    }

    /**
     * 思路一：将数字转换为字符串，再逐一比较头尾字符
     * <p>
     * 时间复杂度：O(n)，n 为 x 的位数（Math.floor(Math.log10(x)) + 1）
     * 空间复杂度：O(n)，n 为 x 的位数（Math.floor(Math.log10(x)) + 1）
     */
    private static boolean solOne(int x) {
        // 带负号，直接返回 false
        if (x <= 0) {
            return x == 0;
        }

        // 使用 StringBuffer 的 charAt 方法进行字符比较
        StringBuffer sb = new StringBuffer(String.valueOf(x));
        for (int i = 0, j = sb.length() - 1; i < j; i++, j--) {
            if (sb.charAt(i) != sb.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 思路二：与反转后的数字进行比较
     * <p>
     * 时间复杂度：O(n)，n 为 x 的位数
     */
    private static boolean solTwo(int x) {
        if (x <= 0) {
            return x == 0;
        }

        return x == reverseInt(x);
    }

    /**
     * 思路三：直接逐一比较 MSD 和 LSD，MSD 的计算公式：x / 10 ^ (n - 1)，n = floor(log10(x)) + 1，
     * LSD 的计算公式：x % 10
     * <p>
     * 时间复杂度：O(n)，n 为 x 的位数
     * 空间复杂度：O(1)
     */
    private static boolean solThree(int x) {
        if (x <= 0) {
            return x == 0;
        }

        final int numDigits = (int) (Math.floor(Math.log10(x)) + 1);
        int msdMask = (int) Math.pow(10, numDigits - 1);
        while (x != 0) {
            if (x / msdMask != x % 10) {
                return false;
            }
            // 移除 MSD
            x %= msdMask;
            // 移除 LSD
            x /= 10;
            msdMask /= 100;
        }
        return true;
    }

    /**
     * 反转数字
     * <p>
     * 时间复杂度：O(n)，n 为 x 的位数
     * 空间复杂度：O(1)
     */
    private static int reverseInt(int x) {
        int result = 0;
        int xRemaining = Math.abs(x);
        while (xRemaining > 0) {
            result = 10 * result + xRemaining % 10;
            xRemaining /= 10;
        }
        return x < 0 ? -result : result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsNumberPalindromic.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
