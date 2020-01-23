package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 9.2 EVALUATE RPN EXPRESSIONS
 * <p>
 * A string is said to be an arithmetical expression in Reverse Polish notation (RPN) if:
 * (1.) It is a single digit or a sequence of digits, prefixed with an option -, e.g., "6",
 * "123", "-42".
 * (2.) It is of the form “A,B,o" where A and B are RPN expressions and o is one of
 * +,-,x,/.
 * For example, the following strings satisfy these rules: "1729", "3,4,+,2,X,1,+",
 * "1,1,+,-2,x","-641,6,/,28,/".
 * An RPN expression can be evaluated uniquely to an integer, which is determined
 * recursively. The base case corresponds to Rule (1.), which is an integer expressed in
 * base-10 positional system. Rule (2.)corresponds to the recursive case, and the RPNs
 * are evaluated in the natural way, e.g., if A evaluates to 2 and B evaluates to 3, then
 * "A,B,x" evaluates to 6.
 * <p>
 * Write a program that takes an arithmetical expression in RPN and returns the number that
 * the expression evaluates to.
 * <p>
 * Hint: Process subexpressions, keeping values in a stack. How should operators be handled?
 */
public class EvaluateRpn {
    @EpiTest(testDataFile = "evaluate_rpn.tsv")

    public static int eval(String expression) {
        return solOne(expression);
    }

    /**
     * 思路一：遍历全部元素，遇到数字时，将其推到堆栈中，遇到运算符时，从堆栈中取出两个数字
     * 进行相应的运算，然后再将结果推到堆栈中，遍历结束后留存在堆栈中的数字即为结果。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：
     */
    private static int solOne(String expression) {
        Deque<Integer> intermediateResults = new LinkedList<>();
        String[] symbols = expression.split(",");
        for (String symbol : symbols) {
            if (symbol.length() == 1 && "+-*/".contains(symbol)) {
                int y = intermediateResults.removeFirst();
                int x = intermediateResults.removeFirst();
                switch (symbol.charAt(0)) {
                    case '+':
                        intermediateResults.addFirst(x + y);
                        break;
                    case '-':
                        intermediateResults.addFirst(x - y);
                        break;
                    case '*':
                        intermediateResults.addFirst(x * y);
                        break;
                    case '/':
                        intermediateResults.addFirst(x / y);
                        break;
                    default:
                        // should never happen
                        throw new IllegalStateException("Malformed RPN at: " + symbol);
                }
            } else {
                // symbol is a number.
                intermediateResults.addFirst(Integer.parseInt(symbol));
            }
        }
        return intermediateResults.removeFirst();
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "EvaluateRpn.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
