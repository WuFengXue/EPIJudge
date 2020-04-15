package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;

/**
 * 12.6 SEARCH IN A 2D SORTED ARRAY
 * <p>
 * Call a 2D array sorted if its rows and its columns are non-decreasing.
 * See Figure 12.3 on the facing page for an example of a 2D sorted array.
 * <p>
 * Design an algorithm that takes a 2D sorted array and a number and checks
 * whether that number appears in the array. For example, if the input is
 * the 2D sorted array in Figure 12.3 on the next page, and the number is
 * 7, your algorithm should return false; if the number is 8, your algorithm
 * should return true.
 * <p>
 * Hint: Can you eliminate a row or a column per comparison?
 */
public class SearchRowColSortedMatrix {
    @EpiTest(testDataFile = "search_row_col_sorted_matrix.tsv")

    public static boolean matrixSearch(List<List<Integer>> A, int x) {
        return solThree(A, x);
    }


    /**
     * 思路三：利用有序矩阵的行和列都是有序的特性，比较首行的尾元素：
     * <p>
     * 1、如果尾元素等于 x，尾元素为目标元素，返回 true
     * <p>
     * 2、如果尾元素小于 x，那么尾元素所在行都小于 x，直接过滤该行
     * <p>
     * 3、如果尾元素大于 x，那么尾元素所在列都大于 x，直接过滤该列
     * <p>
     * 重复上述步骤，直到找到目标元素或者搜索结束
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static boolean solThree(List<List<Integer>> A, int x) {
        if (A.isEmpty()) {
            return false;
        }

        // Start from the top-right corner.
        int row = 0, col = A.get(0).size() - 1;
        // Keeps searching while there are unclassified rows and columns.
        while (row < A.size() && col >= 0) {
            Integer item = A.get(row).get(col);
            if (item < x) {
                // Eliminate this row.
                row++;
            } else if (item > x) {
                // Eliminate this column.
                col--;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 思路二：逐行（或逐列）搜索，搜索行（或列）时用二分查找法
     * <p>
     * 时间复杂度：O(m * log(n)) （或 O(n * log(m))），m 为行、n 为列
     * <p>
     * 空间复杂度：O(1)
     */
    private static boolean solTwo(List<List<Integer>> A, int x) {
        for (List<Integer> row : A) {
            int lo = 0, hi = row.size() - 1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (row.get(mid) < x) {
                    lo = mid + 1;
                } else if (row.get(mid) > x) {
                    hi = mid - 1;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 思路一：遍历矩阵查找
     * <p>
     * 时间复杂度：O(m * n)
     * <p>
     * 空间复杂度：O（1）
     */
    private static boolean solOne(List<List<Integer>> A, int x) {
        for (List<Integer> row : A) {
            for (Integer item : row) {
                if (item == x) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchRowColSortedMatrix.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
