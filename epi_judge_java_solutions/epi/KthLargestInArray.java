package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 12.8 FIND THE kTH LARGEST ELEMENT
 * <p>
 * Many algorithms require as a subroutine the computation of the kth largest
 * element of an array. The first largest element is simply the largest element.
 * The nth largest element is the smallest element, where n is the length of the
 * array.
 * <p>
 * For example, if the input array A = (3,2,1,5,4), then A[3] is the first largest
 * element in A, A[0] is the third largest element in A, and A[2] is the fifth
 * largest element in A.
 * <p>
 * Design an algorithm for computing the kth largest element in an array. Assume
 * entries are distinct.
 * <p>
 * Hint: Use divide and conquer in conjunction with randomization.
 */
public class KthLargestInArray {
    // The numbering starts from one, i.e., if A = [3,1,-1,2] then
    // findKthLargest(A, 1) returns 3, findKthLargest(A, 2) returns 2,
    // findKthLargest(A, 3) returns 1, and findKthLargest(A, 4) returns -1.
    @EpiTest(testDataFile = "kth_largest_in_array.tsv")
    public static int findKthLargest(int k, List<Integer> A) {
        return solThree(k, A);
    }

    /**
     * 思路三：每次随机从数组选取一个数 x，然后遍历数组，将大于 x 的数移到左边（交换算法），
     * 遍历结束后，x 左侧的元素全部大于 x，右侧的元素全部小于 x（数唯一），此时：
     * <p>
     * 如果 x 的下标刚好等于 k - 1，则直接返回 x
     * <p>
     * 如果 x 的下标小于 k - 1，则目标元素在其右侧，更新下限后重新选取一个数进行排序
     * <p>
     * 如果 x 的下标大于 k - 1，则目标元素在其左侧，更新上限后重新选取一个数进行排序
     * <p>
     * 时间复杂度：O(n)，最糟的情况为 O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solThree(int k, List<Integer> A) {
        if (A == null || A.size() < k) {
            throw new IllegalArgumentException("A is null or A.size() < k");
        }

        int left = 0, right = A.size() - 1;
        Random r = new Random(0);
        while (left <= right) {
            // Generates a random integer in [left, right].
            int pivotIdx = left + r.nextInt(right - left + 1);
            int newPivotIdx = partitionAroundPivot(left, right, pivotIdx, A);
            if (newPivotIdx < k - 1) {
                left = newPivotIdx + 1;
            } else if (newPivotIdx > k - 1) {
                right = newPivotIdx - 1;
            } else {
                break;
            }
        }
        return A.get(k - 1);
    }

    /**
     * Partitions A.subList(left, right + 1) around pivotIdx, returns the new index
     * of the pivot, newPivotIdx, after partition. After partitioning,
     * A.subList(left, newPivotIdx) contains elements that are greater than the
     * pivot, and A.subList(newPivotIdx + 1 , right + 1) contains elements that
     * are less than the pivot.
     * <p>
     * Returns the new index of the pivot element after partition.
     */
    private static int partitionAroundPivot(int left, int right, int pivotIdx,
                                            List<Integer> A) {
        int pivotValue = A.get(pivotIdx);
        int newPivotIdx = left;

        Collections.swap(A, pivotIdx, right);
        for (int i = left; i < right; i++) {
            if (A.get(i) > pivotValue) {
                Collections.swap(A, i, newPivotIdx++);
            }
        }
        Collections.swap(A, newPivotIdx, right);
        return newPivotIdx;
    }

    /**
     * 思路二：用一个小根堆来存储最大的 k 个数，遍历数组然后更新小根堆，
     * 遍历完成后返回小根堆的首个元素
     * <p>
     * 时间复杂度：O(n * log(k))
     * <p>
     * 空间复杂度：O(k)
     */
    private static int solTwo(int k, List<Integer> A) {
        if (A == null || A.size() < k) {
            throw new IllegalArgumentException("A is null or A.size() < k");
        }

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (Integer a : A) {
            minHeap.add(a);
            if (minHeap.size() == k + 1) {
                minHeap.remove();
            }
        }
        return minHeap.peek();
    }

    /**
     * 思路一：对数组进行降序排序，然后返回第 k - 1 个元素
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(int k, List<Integer> A) {
        if (A == null || A.size() < k) {
            throw new IllegalArgumentException("A is null or A.size() < k");
        }

        Collections.sort(A, Collections.reverseOrder());
        return A.get(k - 1);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "KthLargestInArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
