package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 11.3 SORT AN ALMOST-SORTED ARRAY
 * <p>
 * Often data is almost-sorted——for example, a server receives timestamped stock
 * quotes and earlier quotes may arrive slightly after later quotes because of
 * differences in server loads and network routes. In this problem we address
 * efficient ways to sort such data.
 * <p>
 * Write a program which takes as input a very long sequence of numbers and prints
 * the numbers in sorted order. Each number is at most k away from its correctly
 * sorted position. (Such an array is sometimes referred to as being For example,
 * no number in the sequence (3,-1,2,6,4,5,8} is more than 2 away from its final
 * sorted position.
 * <p>
 * Hint: How many numbers must you read after reading the ith number to be sure
 * you can place it in the correct location?
 */
public class SortAlmostSortedArray {

    public static List<Integer>
    sortApproximatelySortedData(Iterator<Integer> sequence, int k) {
        return solTwo(sequence, k);
    }

    /**
     * 思路二：利用数据近乎已排序的特性，用一个游标（最小堆）来提取 k+1 个元素中的最小值，
     * 向右移动游标直至遍历结束，最后再将堆中的剩余元素添加到结果集合
     * <p>
     * 时间复杂度：O(n * log(k))
     * <p>
     * 空间复杂度：O(k + 1)
     */
    private static List<Integer> solTwo(Iterator<Integer> sequence, int k) {
        // Adds the first k elements into minHeap. Stop if there are fewer than k
        // elements.
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k + 1);
        for (int i = 0; i < k && sequence.hasNext(); i++) {
            minHeap.add(sequence.next());
        }
        List<Integer> result = new ArrayList<>();
        // For every new element, add it to minHeap and extract the smallest.
        while (sequence.hasNext()) {
            minHeap.add(sequence.next());
            result.add(minHeap.remove());
        }
        // sequence is exhausted, iteratively extracts the remaining elements.
        while (!minHeap.isEmpty()) {
            result.add(minHeap.remove());
        }
        return result;
    }

    /**
     * 思路一：将数据存储到一个数组，然后再对数组做排序
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static List<Integer> solOne(Iterator<Integer> sequence, int k) {
        List<Integer> result = new ArrayList<>();
        while (sequence.hasNext()) {
            result.add(sequence.next());
        }
        Collections.sort(result);
        return result;
    }

    @EpiTest(testDataFile = "sort_almost_sorted_array.tsv")
    public static List<Integer>
    sortApproximatelySortedDataWrapper(List<Integer> sequence, int k) {
        return sortApproximatelySortedData(sequence.iterator(), k);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SortAlmostSortedArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
