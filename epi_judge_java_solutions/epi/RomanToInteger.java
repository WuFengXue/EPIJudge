package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 7.9 CONVERT FROM ROMAN TO DECIMAL
 * <p>
 * The Roman numeral representation of positive integers uses the symbols
 * I,V,X,L,C,D,M. Each symbol represents a value, with I being 1, V being 5, X
 * being 10, L being 50, C being 100, D being 500, and M being 1000.
 * <p>
 * In this problem we give simplified rules for representing numbers in this
 * system. Specifically, define a string over the Roman number symbols to be a
 * valid Roman number string if symbols appear in non-increasing order,with the
 * following exceptions allowed:
 * • I can immediately precede V and X.
 * • X can immediately precede L and C.
 * • C can immediately precede D and M.
 * <p>
 * Back-to-back exceptions are not allowed, e.g., IXC is invalid, as is CDM.
 * <p>
 * A valid complex Roman number string represents the integer which is the sum
 * of the symbols that do not correspond to exceptions; for the exceptions, add
 * the difference of the larger symbol and the smaller symbol.
 * <p>
 * For example, the strings "XXXXXIIIIIIIII", "LVIIII" and "LIX" are valid Roman
 * number strings representing 59. The shortest valid complex Roman number string
 * corresponding to the integer 59 is "LIX".
 * <p>
 * Write a program which takes as input a valid Roman number string s and returns
 * the integer it corresponds to.
 * <p>
 * Hint: Start by solving the problem assuming no exception cases.
 */
public class RomanToInteger {
    private static final Map<Character, Integer> T = new HashMap<>() {
        {
            put('I', 1);
            put('V', 5);
            put('X', 10);
            put('L', 50);
            put('C', 100);
            put('D', 500);
            put('M', 1000);
        }
    };

    @EpiTest(testDataFile = "roman_to_integer.tsv")

    public static int romanToInteger(String s) {
        return solTwo(s);
    }

    /**
     * 思路一：从左向右遍历，如果遇到异常情况，则特殊处理
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(String s) {
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i + 1 < s.length() && T.get(s.charAt(i)) < T.get(s.charAt(i + 1))) {
                sum -= T.get(s.charAt(i));
                sum += T.get(s.charAt(i + 1));
                i++;
            } else {
                sum += T.get(s.charAt(i));
            }
        }
        return sum;
    }

    /**
     * 思路二：从右向左遍历，每次都只需处理一位
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solTwo(String s) {
        int sum = T.get(s.charAt(s.length() - 1));
        for (int i = s.length() - 2; i >= 0; i--) {
            if (T.get(s.charAt(i)) < T.get(s.charAt(i + 1))) {
                sum -= T.get(s.charAt(i));
            } else {
                sum += T.get(s.charAt(i));
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RomanToInteger.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
