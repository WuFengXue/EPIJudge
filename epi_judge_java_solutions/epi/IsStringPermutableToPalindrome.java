package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 13.1 TEST FOR PALINDROMIC PERMUTATIONS
 * <p>
 * A palindrome is a string that reads the same forwards and backwards,
 * e.g., "level", "rotator", and "foobaraboof".
 * <p>
 * Write a program to test whether the letters forming a string can be
 * permuted to form a palindrome. For example, "edified" can be permuted
 * to form "deified".
 * <p>
 * Hint: Find a simple characterization of strings that can be permuted
 * to form a palindrome.
 */
public class IsStringPermutableToPalindrome {
    @EpiTest(testDataFile = "is_string_permutable_to_palindrome.tsv")

    public static boolean canFormPalindrome(String s) {
        return solTwo(s);
    }

    /**
     * 思路二：在思路一上进行优化，用一个集合存储出现次数为奇数的字符，
     * 遍历字符串的字符：
     * <p>
     * 如果该字符已在集合中，则将其移除
     * <p>
     * 如果该字符未在集合中，则将其添加到集合中
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(c)，c 为包含字符的类型数
     */
    private static boolean solTwo(String s) {
        Set<Character> charsWithOddFrequency = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (charsWithOddFrequency.contains(c)) {
                charsWithOddFrequency.remove(c);
            } else {
                charsWithOddFrequency.add(c);
            }
        }
        return charsWithOddFrequency.size() <= 1;
    }

    /**
     * 思路一：观察法，要构成回文结构，最多只有一种字符的数量为奇数。
     * <p>
     * 用一个表存储每种字符出现的次数，最终再统计出现次数为奇数的字符的数量
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(c)，c 为包含字符的类型数
     */
    private static boolean solOne(String s) {
        Map<Character, Integer> charFrequencies = new HashMap<>();
        // Compute the frequency of each char in s.
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (charFrequencies.containsKey(c)) {
                charFrequencies.put(c, charFrequencies.get(c) + 1);
            } else {
                charFrequencies.put(c, 1);
            }
        }

        // A string can be permuted as a palindrome if and only if the
        // number of chars whose frequencies is odd is at most 1.
        int oddCount = 0;
        for (Map.Entry<Character, Integer> p : charFrequencies.entrySet()) {
            if (p.getValue() % 2 == 1 && ++oddCount > 1) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsStringPermutableToPalindrome.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
