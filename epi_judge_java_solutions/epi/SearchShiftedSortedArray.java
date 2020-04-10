package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;

/**
 * 12.3 SEARCH A CYCLICALLY SORTED ARRAY
 * <p>
 * An array is said to be cyclically sorted if it is possible to cyclically
 * shift its entries so that it becomes sorted. For example, the array in
 * Figure 12.2 on the facing page is cyclically sorted—a cyclic left shift
 * by 4 leads to a sorted array.
 * <p>
 * Design an O(log(n)) algorithm for finding the position of the smallest
 * element in a cyclically sorted array. Assume all elements are distinct.
 * For example, for the array in Figure 12.2 on the next page, your algorithm
 * should return 4.
 * <p>
 * Hint: Use the divide and conquer principle.
 */
public class SearchShiftedSortedArray {
    @EpiTest(testDataFile = "search_shifted_sorted_array.tsv")

    public static int searchSmallest(List<Integer> A) {
        return solTwo(A);
    }

    /**
     * 思路二：利用集合部分排序的特性：
     * <p>
     * 1、如果 A[m] < A[n - 1]，那么目标元素肯定不在区间 [m + 1, n - 1]
     * <p>
     * 2、如果 A[m] > A[n - 1]，那么目标元素肯定在区间 [m + 1, n - 1]
     * <p>
     * 3、因为元素是唯一的，所以 A[m] == A[n - 1] 是不可能成立的
     * <p>
     * 时间复杂度：O(log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solTwo(List<Integer> A) {
        int left = 0, right = A.size() - 1;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (A.get(mid) > A.get(right)) {
                // Minimum must be in A.subList(mid+1,right+1).
                left = mid + 1;
            }
            // A.get(mid) < A.get(right).
            else {
                // Minimum cannot be in A.subList(mid + 1, right + 1) so it
                // must be in A.sublist(left, mid + 1).
                right = mid;
            }
        }
        // Loop ends when left == right.
        return left;
    }

    /**
     * 思路一：遍历集合，找到最小的元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(List<Integer> A) {
        int result = 0;
        for (int i = 1; i < A.size(); i++) {
            if (A.get(i) < A.get(result)) {
                result = i;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchShiftedSortedArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
