package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 6.3 MULTIPLY TWO ARBITRARY-PRECISION INTEGERS
 * <p>
 * Certain applications require arbitrary precision arithmetic. One way to achieve
 * this is to use arrays to represent integers, e.g., with one digit per array entry,
 * with the most significant digit appearing first, and a negative leading digit denoting
 * a negative integer. For example, (1,9,3,7,0,7,7,2,1) represents 193707721 and
 * (-7,6,1,8,3,8,2,5,7,2,8,7) represents -761838257287.
 * <p>
 * Write a program that takes two arrays representing integers, and returns an integer
 * representing their product. For example, since
 * 193707721 x -761838257287 = -147573952589676412927, if the inputs are
 * (1,9,3,7,0,7,7,2,1} and (-7,6,1,8,3,8,2,5,7,2,8,7), your function should return
 * (-1,4,7,5,7,3,9,5,2,5,8,9,6,7,6,4,1,2,9,2,7).
 * <p>
 * Hint: Use arrays to simulate the grade-school multiplication algorithm.
 */
public class IntAsArrayMultiply {
    @EpiTest(testDataFile = "int_as_array_multiply.tsv")
    public static List<Integer> multiply(List<Integer> num1, List<Integer> num2) {
        return solOne(num1, num2);
    }

    /**
     * 思路一：使用小学的乘法运算规则，逐位相乘，然后再将结果相加
     * <p>
     * 时间复杂度：O(m * n), m 为 num1 的大小， n 为 num2 的大小
     */
    private static List<Integer> solOne(List<Integer> num1, List<Integer> num2) {
        // 记录结果正负
        final int sign = num1.get(0) * num2.get(0) < 0 ? -1 : 1;
        // 清符号位
        num1.set(0, Math.abs(num1.get(0)));
        num2.set(0, Math.abs(num2.get(0)));
        // 结果存储在新的数组中，数组大小为原来的两个数组大小之和（以避免溢出场景）
        List<Integer> result =
                new ArrayList<>(Collections.nCopies(num1.size() + num2.size(), 0));
        // 执行乘法运算
        for (int i = num1.size() - 1; i >= 0; i--) {
            for (int j = num2.size() - 1; j >= 0; j--) {
                result.set(i + j + 1,
                        result.get(i + j + 1) + num1.get(i) * num2.get(j));
                result.set(i + j, result.get(i + j) + result.get(i + j + 1) / 10);
                result.set(i + j + 1, result.get(i + j + 1) % 10);
            }
        }

        // Remove the leading zeroes.
        int firstNotZero = 0;
        while (firstNotZero < result.size() && result.get(firstNotZero) == 0) {
            firstNotZero++;
        }
        result = result.subList(firstNotZero, result.size());
        if (result.isEmpty()) {
            return Arrays.asList(0);
        }
        result.set(0, sign * result.get(0));
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntAsArrayMultiply.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
