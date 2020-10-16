package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Collections;
import java.util.List;

/**
 * 14.2 MERGE TWO SORTED ARRAYS
 * <p>
 * Suppose you are given two sorted arrays of integers. If one array has
 * enough empty entries at its end, it can be used to store the combined
 * entries of the two arrays in sorted order. For example, consider
 * (5,13,17,_,_,_,_,_) and (3,7,11,19), where _ denotes an empty entry. Then
 * the combined sorted entries can be stored in the first array as
 * (3,5,7,11,13,17,19,_).
 * <p>
 * Write a program which takes as input two sorted arrays of integers, and
 * updates the first to the combined entries of the two arrays in sorted
 * order. Assume the first array has enough empty entries at its end to hold
 * the result.
 * <p>
 * Hint: Avoid repeatedly moving entries.
 */
public class TwoSortedArraysMerge {

    public static void mergeTwoSortedArrays(List<Integer> A, int m,
                                            List<Integer> B, int n) {
        solTwo(A, m, B, n);
        return;
    }

    /**
     * 思路二：反向比较，从大到小，将两个数组比较大的数值写到第一个数组的尾部，
     * 需注意第一个数组的元素遍历结束后第二个数组的元素还未遍历结束的场景。
     * <p>
     * 时间复杂度：O(m + n)
     * 空间复杂度：O(1)
     */
    private static void solTwo(List<Integer> A, int m,
                               List<Integer> B, int n) {
        int a = m - 1, b = n - 1, writeIdx = m + n - 1;
        while (a >= 0 && b >= 0) {
            A.set(writeIdx--, A.get(a) > B.get(b) ? A.get(a--) : B.get(b--));
        }
        while (b >= 0) {
            A.set(writeIdx--, B.get(b--));
        }
    }

    /**
     * 思路一：将第二个数组的元素追加到第一个数组的尾部，然后再对第一个数组
     * 执行排序操作。
     * <p>
     * 时间复杂度：O(n * log(n))
     * 空间复杂度：O(1)
     */
    private static void solOne(List<Integer> A, int m,
                               List<Integer> B, int n) {
        for (int i = 0; i < B.size() && m + i < A.size(); i++) {
            A.set(m + i, B.get(i));
        }
        Collections.sort(A);
    }

    @EpiTest(testDataFile = "two_sorted_arrays_merge.tsv")
    public static List<Integer>
    mergeTwoSortedArraysWrapper(List<Integer> A, int m, List<Integer> B, int n) {
        mergeTwoSortedArrays(A, m, B, n);
        return A;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TwoSortedArraysMerge.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
