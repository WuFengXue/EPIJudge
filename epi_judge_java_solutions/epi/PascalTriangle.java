package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.19 COMPUTE ROWS IN PASCAL'S TRIANGLE
 * <p>
 * Figure 6.5 shows the first five rows of a graphic that is known as Pascal's triangle.
 * Each row contains one more entry than the previous one. Except for entries in the last
 * row, each entry is adjacent to one or two numbers in the row below it. The first row
 * holds 1. Each entry holds the sum of the numbers in the adjacent entries above it.
 * <p>
 * Write a program which takes as input a nonnegative integer n and returns the first n
 * rows of Pascal's triangle.
 * <p>
 * Hint: Write the given fact as an equation.
 */
public class PascalTriangle {
    @EpiTest(testDataFile = "pascal_triangle.tsv")

    public static List<List<Integer>> generatePascalTriangle(int numRows) {
        return solOne(numRows);
    }

    /**
     * 思路一：第 i 行的第 1 个元素和最后 1 个元素的值为1，其余元素为上一行相邻两个元素的和
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(n ^ 2)
     */
    private static List<List<Integer>> solOne(int numRows) {
        List<List<Integer>> pascalTriangle = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            List<Integer> currRow = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                // Set this entry to the sum of the two above adjacent entries if they
                // exist.
                currRow.add(0 < j && j < i
                        ? pascalTriangle.get(i - 1).get(j - 1) + pascalTriangle.get(i - 1).get(j)
                        : 1);
            }
            pascalTriangle.add(currRow);
        }
        return pascalTriangle;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PascalTriangle.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
