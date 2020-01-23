package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 9.3 TEST A STRING OVER FOR WELL-FORMEDNESS
 * <p>
 * A string over the characters "{,},(,),[,]" is said to be well-formed if the different
 * types of brackets match in the correct order.
 * For example, "([]){()}" is well-formed, as is "[()[]()()]". However, "{)"
 * and "[()[]{()()" are not well-formed,
 * <p>
 * Write a program that tests if a string made up of the characters '(', ')', '[',
 * and "}' is well-formed.
 * <p>
 * Hint: Which left parenthesis does a right parenthesis match with?
 */
public class IsValidParenthesization {
    @EpiTest(testDataFile = "is_valid_parenthesization.tsv")

    public static boolean isWellFormed(String s) {
        return solOne(s);
    }

    /**
     * 思路一：遍历全部元素，遇到左符号，则将其推送到堆栈中，遇到右符号，则取出堆栈中的第一个元素
     * 进行比较判断，如果遍历结束堆栈为空则匹配成功。
     * <p>
     * 时间复杂度：O(n)
     */
    private static boolean solOne(String s) {
        Deque<Character> leftChars = new LinkedList<>();
        char[] chars = s.toCharArray();
        for (char ch : chars) {
            if (ch == '(' || ch == '[' || ch == '{') {
                leftChars.addFirst(ch);
            } else {
                // Unmatched right char.
                if (leftChars.isEmpty()) {
                    return false;
                }
                // Mismatched chars.
                if ((ch == ')' && leftChars.peekFirst() != '(')
                        || (ch == ']' && leftChars.peekFirst() != '[')
                        || (ch == '}' && leftChars.peekFirst() != '{')) {
                    return false;
                }
                leftChars.removeFirst();
            }
        }
        return leftChars.isEmpty();
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsValidParenthesization.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
