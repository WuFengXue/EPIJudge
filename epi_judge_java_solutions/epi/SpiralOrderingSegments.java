package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 6.17 COMPUTE THE SPIRAL ORDERING OF A 2D ARRAY
 * <p>
 * A 2D array can be written as a sequence in several orders—-the most natural ones being
 * row-by-row or column-by-column. In this problem we explore the problem of writing the
 * 2D array in spiral order. For example, the spiral ordering for the 2D array in Figure
 * 6.3(a) is (1,2,3, 6,9,8, 7,4,5). For Figure 6.3(b), the spiral ordering is
 * <1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10).
 * <p>
 * Write a program which takes an nxn 2D array and returns the spiral ordering of the array.
 * <p>
 * Hint: Use case analysis and divide-and-conquer.
 */
public class SpiralOrderingSegments {
    @EpiTest(testDataFile = "spiral_ordering_segments.tsv")

    public static List<Integer>
    matrixInSpiralOrder(List<List<Integer>> squareMatrix) {
        return solTwo(squareMatrix);
    }

    /**
     * 思路一：逐层添加（顺时针），每层分四次添加：上、右、下、左，每次添加个数：边长 - 1，
     * 层数为奇数时，需要专门处理最里层（只有它一个元素）
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(1)
     */
    private static List<Integer> solOne(List<List<Integer>> squareMatrix) {
        List<Integer> spiralOrdering = new ArrayList<>();
        for (int offset = 0; offset < Math.ceil(0.5 * squareMatrix.size()); offset++) {
            addMatrixLayerInClockwise(squareMatrix, offset, spiralOrdering);
        }
        return spiralOrdering;
    }

    private static void addMatrixLayerInClockwise(List<List<Integer>> squareMatrix,
                                                  int offset, List<Integer> spiralOrdering) {
        if (offset == squareMatrix.size() - 1 - offset) {
            // squareMatrix has odd dimension, and we are at its center.
            spiralOrdering.add(squareMatrix.get(offset).get(offset));
            return;
        }

        // top
        for (int i = offset; i < squareMatrix.size() - offset - 1; i++) {
            spiralOrdering.add(squareMatrix.get(offset).get(i));
        }

        // right
        for (int j = offset; j < squareMatrix.size() - offset - 1; j++) {
            spiralOrdering.add(squareMatrix.get(j).get(squareMatrix.size() - offset - 1));
        }

        // bottom
        for (int k = squareMatrix.size() - offset - 1; k > offset; k--) {
            spiralOrdering.add(squareMatrix.get(squareMatrix.size() - offset - 1).get(k));
        }

        // left
        for (int l = squareMatrix.size() - offset - 1; l > offset; l--) {
            spiralOrdering.add(squareMatrix.get(l).get(offset));
        }
    }

    /**
     * 思路二：遍历所有元素，定义四个边对应的移动步伐：上、右、下、左，每次移动一步，到达边界就转向
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(1)
     */
    private static List<Integer> solTwo(List<List<Integer>> squareMatrix) {
        final int[][] SHIFT = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int dir = 0, row = 0, col = 0;
        List<Integer> spiralOrdering = new ArrayList<>();

        for (int i = 0; i < squareMatrix.size() * squareMatrix.size(); i++) {
            spiralOrdering.add(squareMatrix.get(row).get(col));
            squareMatrix.get(row).set(col, 0);
            int nextRow = row + SHIFT[dir][0], nextCol = col + SHIFT[dir][1];
            if (nextCol < 0 || nextCol >= squareMatrix.size()
                    || nextRow < 0 || nextRow >= squareMatrix.size()
                    || squareMatrix.get(nextRow).get(nextCol) == 0) {
                dir = (dir + 1) % 4;
                nextRow = row + SHIFT[dir][0];
                nextCol = col + SHIFT[dir][1];
            }
            row = nextRow;
            col = nextCol;
        }
        return spiralOrdering;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SpiralOrderingSegments.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
