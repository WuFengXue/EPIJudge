package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 7.11 WRITE A STRING SINUSOIDALLY
 * <p>
 * We illustrate what it means to write a string in sinusoidal fashion by means
 * of an example. The string "Hello_World!" written in sinusoidal fashion is
 *  e   _    l
 * H 1 o W r d (Here denotes a blank.)
 *  1   o   !
 * <p>
 * Define the snake-string of s to be the left-right top-to-bottom sequence in which
 * characters appear when s is written in sinusoidal fashion. For example, the
 * snake-string string for "HelloWorld!" is "e_lHloWrdlo!".
 * <p>
 * Write a program which takes as input a string s and returns the snake-string of s.
 * <p>
 * Hint: Try concrete examples, and look for periodicity.
 */
public class SnakeString {
    @EpiTest(testDataFile = "snake_string.tsv")

    public static String snakeString(String s) {
        return solOne(s);
    }

    /**
     * 思路一：通过观察得出规律：每 4 个为一组，之后都是重复之前的模式
     * <p>
     * 时间复杂度：O(n)
     */
    private static String solOne(String s) {
        StringBuilder result = new StringBuilder();
        // Outputs the first row, i.e., s[1], s[5], s[9], ...
        for (int i = 1; i < s.length(); i += 4) {
            result.append(s.charAt(i));
        }
        // Outputs the second row, i.e., s[0], s[2], s[4], ...
        for (int i = 0; i < s.length(); i += 2) {
            result.append(s.charAt(i));
        }
        // Outputs the third row, i.e., s[3], s[7], s[11], ...
        for (int i = 3; i < s.length(); i += 4) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SnakeString.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
