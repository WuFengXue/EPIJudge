package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 6.16 THE SUDOKU CHECKER PROBLEM
 * <p>
 * Sudoku is a popular logic-based combinatorial number placement puzzle. The
 * objective is to fill a 9 X 9 grid with digits subject to the constraint that each
 * column, each row, and each of the nine 3x3 sub-grids that compose the grid contains
 * unique integers in [1, 9]. The grid is initialized with a partial assignment as shown
 * in Figure 6.2(a) on the following page; a complete solution is shown in Figure 6.2(b)
 * on the next page.
 * <p>
 * Check whether a 9 X 9 2D array representing a partially completed Sudoku is valid.
 * Specifically, check that no row, column, or 3 X 3 2D sub-array contains duplicates.
 * A 0-value in the 2D array indicates that entry is blank; every other entry is in
 * [1,9].
 * <p>
 * Hint: Directly test the constraints. Use an array to encode sets.
 */
public class IsValidSudoku {
    @EpiTest(testDataFile = "is_valid_sudoku.tsv")

    // Check if a partially filled matrix has any conflicts.
    public static boolean isValidSudoku(List<List<Integer>> partialAssignment) {
        return solOne(partialAssignment);
    }

    /**
     * 思路一：分三次检测，分别检测行、列和方块，利用数字不可重复的特性进行检测
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(n)
     */
    private static boolean solOne(List<List<Integer>> partialAssignment) {
        final int SIZE = partialAssignment.size();
        // Check row constraints.
        for (int i = 0; i < SIZE; i++) {
            if (hasDuplicate(partialAssignment, i, i + 1, 0, SIZE)) {
                return false;
            }
        }

        // Check column constraints.
        for (int i = 0; i < SIZE; i++) {
            if (hasDuplicate(partialAssignment, 0, SIZE, i, i + 1)) {
                return false;
            }
        }

        // Check region constraints.
        int regionSize = (int) Math.sqrt(SIZE);
        for (int i = 0; i < regionSize; i++) {
            for (int j = 0; j < regionSize; j++) {
                if (hasDuplicate(partialAssignment,
                        regionSize * i, regionSize * (i + 1),
                        regionSize * j, regionSize * (j + 1))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return true if sub-array partialAssignment[rowStart : rowEnd - 1][colStart
     * : colEnd-1]contains any duplicates in {1,2,...,partialAssignment.size()};
     * otherwise return false.
     *
     * @param partialAssignment 要检测的二维数组
     * @param rowStart          起始行
     * @param rowEnd            终止行
     * @param colStart          起始列
     * @param colEnd            终止列
     */
    private static boolean hasDuplicate(List<List<Integer>> partialAssignment,
                                        int rowStart, int rowEnd, int colStart, int colEnd) {
        List<Boolean> isPresent = new ArrayList<>(
                Collections.nCopies(partialAssignment.size() + 1, false));
        for (int i = rowStart; i < rowEnd; i++) {
            for (int j = colStart; j < colEnd; j++) {
                int val = partialAssignment.get(i).get(j);
                if (val != 0 && isPresent.get(val)) {
                    return true;
                }
                isPresent.set(val, true);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsValidSudoku.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
