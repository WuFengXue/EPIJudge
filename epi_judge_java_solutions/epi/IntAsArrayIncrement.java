package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;

/**
 * 6.2 INCREMENT AN ARBITRARY-PRECISION INTEGER
 * <p>
 * Write a program which takes as input an array of digits encoding a decimal number
 * D and updates the array to represent the number D + 1. For example, if the input
 * is (1,2,9) then you should update the array to (1,3,0). Your algorithm should work
 * even if it is implemented in a language that has finite-precision arithmetic.
 * <p>
 * Hint: Experiment with concrete examples.
 */
public class IntAsArrayIncrement {
    @EpiTest(testDataFile = "int_as_array_increment.tsv")
    public static List<Integer> plusOne(List<Integer> A) {
        return solOne(A);
    }

    /**
     * 思路一：末位先加1，再处理进位
     * <p>
     * 时间复杂度：O(n)，n 为数组的大小
     */
    private static List<Integer> solOne(List<Integer> A) {
        // 末位加1
        int n = A.size() - 1;
        A.set(n, A.get(n) + 1);
        // 进位处理
        for (int i = n; i > 0 && A.get(i) == 10; i--) {
            A.set(i, 0);
            A.set(i - 1, A.get(i - 1) + 1);
        }
        // 溢出处理
        if (A.get(0) == 10) {
            // Need additional digit as the most significant digit (i.e A.get(9))
            // has a carry-out .
            A.set(0, 0);
            A.add(0, 1);
        }
        return A;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntAsArrayIncrement.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
