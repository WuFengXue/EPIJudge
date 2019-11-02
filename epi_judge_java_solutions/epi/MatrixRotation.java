package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 6.18 ROTATE A 2D ARRAY
 * <p>
 * Image rotation is a fundamental operation in computer graphics. Figure 6.4 illustrates
 * the rotation operation on a 2D array representing a bit-map of an image. Specifically,
 * the image is rotated by 90 degrees clockwise.
 * <p>
 * Write a function that takes as input an n X n 2D array, and rotates the array by 90
 * degrees clockwise.
 * <p>
 * Hint: Focus on the boundary elements.
 */
public class MatrixRotation {

    public static void rotateMatrix(List<List<Integer>> squareMatrix) {
        solTwo(squareMatrix);
        return;
    }

    /**
     * 思路一：经观察可知，顺时针旋转 90 度后，原来的第 i 行，会变成新矩阵的第 n - i 列，
     * 借助一个新的数组，逐行（从最后一行开始）添加到新数组对应的列中，最后再将新数组复制回原来的数组
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(n ^ 2)
     */
    private static void solOne(List<List<Integer>> squareMatrix) {
        List<List<Integer>> rotatedMatrix = new ArrayList<>(squareMatrix.size());
        for (int i = 0; i < squareMatrix.size(); i++) {
            rotatedMatrix.add(new ArrayList<>());
        }
        for (int row = squareMatrix.size() - 1; row >= 0; row--) {
            for (int col = 0; col < squareMatrix.size(); col++) {
                rotatedMatrix.get(col).add(squareMatrix.get(row).get(col));
            }
        }
        Collections.copy(squareMatrix, rotatedMatrix);
    }

    /**
     * 思路二：将矩阵分成多层，每次处理一层，逐个处理每层的单边对应的元素，每次处理时调换
     * 上、右、下、左对应的元素（单边要处理的元素数量为：边长 - 1）
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solTwo(List<List<Integer>> squareMatrix) {
        final int matrixSize = squareMatrix.size() - 1;
        for (int layer = 0; layer < (squareMatrix.size() / 2); layer++) {
            for (int col = layer; col < matrixSize - layer; col++) {
                int up = squareMatrix.get(layer).get(col);
                int right = squareMatrix.get(col).get(matrixSize - layer);
                int bottom = squareMatrix.get(matrixSize - layer).get(matrixSize - col);
                int left = squareMatrix.get(matrixSize - col).get(layer);
                squareMatrix.get(layer).set(col, left);
                squareMatrix.get(col).set(matrixSize - layer, up);
                squareMatrix.get(matrixSize - layer).set(matrixSize - col, right);
                squareMatrix.get(matrixSize - col).set(layer, bottom);
            }
        }
    }

    @EpiTest(testDataFile = "matrix_rotation.tsv")
    public static List<List<Integer>>
    rotateMatrixWrapper(List<List<Integer>> squareMatrix) {
        rotateMatrix(squareMatrix);
        return squareMatrix;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "MatrixRotation.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
