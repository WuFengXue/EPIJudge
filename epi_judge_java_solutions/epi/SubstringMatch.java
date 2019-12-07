package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 7.13 FIND THE FIRST OCCURRENCE OF A SUBSTRING
 * <p>
 * A good string search algorithm is fundamental to the performance of many
 * applications. Several clever algorithms have been proposed for string search,
 * each with its own trade-offs. As a result, there is no single perfect answer.
 * If someone asks you this question in an interview, the best way to approach
 * this problem would be to work through one good algorithm in detail and discuss
 * at a high level other algorithms.
 * <p>
 * Given two strings s (the "search string") and t (the "text"), find the first
 * occurrence of s in t.
 * <p>
 * Hint: Form a signature from a string.
 */
public class SubstringMatch {
    @EpiTest(testDataFile = "substring_match.tsv")

    // Returns the index of the first character of the substring if found, -1
    // otherwise.
    public static int rabinKarp(String t, String s) {
        return solTwo(t, s);
    }

    /**
     * 思路一：遍历 t 字符串，逐个比较，如果不匹配则移动一个字符重新比较
     * <p>
     * 时间复杂度：O(m * n)
     */
    private static int solOne(String t, String s) {
        for (int i = 0; i < t.length(); i++) {
            int j = 0;
            while (i + j < t.length()
                    && j < s.length()
                    && t.charAt(i + j) == s.charAt(j)) {
                j++;
            }
            if (j == s.length()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 思路二：比较文本的子字符串和搜索字符串的哈希值，如果不相等则移动一个字符，重新计算
     * 新字符串的哈希值，然后再重新比较（注意：需考虑哈希冲突的场景）
     * <p>
     * 时间复杂度：O(m + n)
     */
    private static int solTwo(String t, String s) {
        // s is not a substring of t.
        if (t.length() < s.length()) {
            return -1;
        }

        final int BASE = 26;
        // Hash codes for the substring of t and s.
        int tHash = 0;
        int sHash = 0;
        // BASE ^ |s|.
        int powerS = 1;
        for (int i = 0; i < s.length(); i++) {
            powerS *= i > 0 ? BASE : 1;
            tHash = tHash * BASE + t.charAt(i);
            sHash = sHash * BASE + s.charAt(i);
        }
        for (int i = s.length(); i < t.length(); i++) {
            // Checks the two substrings are actually equal or not, to protect
            // against hash collision.
            if (tHash == sHash && t.substring(i - s.length(), i).equals(s)) {
                // Found a match.
                return i - s.length();
            }
            // Uses rolling hash to compute the new hash code.
            tHash -= t.charAt(i - s.length()) * powerS;
            tHash = tHash * BASE + t.charAt(i);
        }
        // Tries to match s and t.substring(t.length() - s.length()).
        if (tHash == sHash && t.substring(t.length() - s.length()).equals(s)) {
            return t.length() - s.length();
        }
        // s is not a substring of t.
        return -1;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SubstringMatch.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
