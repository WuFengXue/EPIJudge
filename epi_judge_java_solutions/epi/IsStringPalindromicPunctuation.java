package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 7.5 TEST PALINDROMICITY
 * <p>
 * For the purpose of this problem, define a palindromic string to be a string which
 * when all the nonalphanumeric are removed it reads the same front to back ignoring
 * case. For example, "A man, a plan, a canal, Panama." and "Able was I, ere I saw
 * Elba!" are palindromic, but "Ray a Ray" is not.
 * <p>
 * Implement a function which takes as input a string s and returns true if s is a
 * palindromic string.
 * <p>
 * Hint: Use two indices.
 */
public class IsStringPalindromicPunctuation {
    @EpiTest(testDataFile = "is_string_palindromic_punctuation.tsv")

    public static boolean isPalindrome(String s) {
        return solTwo(s);
    }

    /**
     * 思路一：先移除其他字符，然后再与翻转后的字符串比较
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solOne(String s) {
        // 利用正则移除其他字符
        s = s.replaceAll("\\W|_", "");
        // 利用 StringBuilder 翻转字符串
        return s.equalsIgnoreCase(new StringBuilder(s).reverse().toString());
    }

    /**
     * 思路二：使用两个指针分别从头部和尾部开始移动，跳过其他字符，依次比较头尾字符
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static boolean solTwo(String s) {
        // i moves forward, and j moves backward.
        int i = 0, j = s.length() - 1;
        while (i < j) {
            // i and j both skip non-alphanumeric characters.
            while (!Character.isLetterOrDigit(s.charAt(i)) && i < j) {
                i++;
            }
            while (!Character.isLetterOrDigit(s.charAt(j)) && i < j) {
                j--;
            }
            if (Character.toLowerCase(s.charAt(i))
                    != Character.toLowerCase(s.charAt(j))) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsStringPalindromicPunctuation.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
