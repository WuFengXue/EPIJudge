package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 11.2 SORT AN INCREASING-DECREASING ARRAY
 * <p>
 * An array is said to be k-increasing-decreasing if elements repeatedly increase
 * up to a certain index after which they decrease, then again increase, a total
 * of k times. This is illustrated in Figure 11.2.
 * <p>
 * Design an efficient algorithm for sorting a k-increasing-decreasing array.
 * <p>
 * Hint: Can you cast this in terms of combining k sorted arrays?
 */
public class SortIncreasingDecreasingArray {
    @EpiTest(testDataFile = "sort_increasing_decreasing_array.tsv")

    public static List<Integer> sortKIncreasingDecreasingArray(List<Integer> A) {
        return solTwo(A);
    }

    /**
     * 思路二：将数组拆成多个有序数组，然后翻转降序数组，最后再采用 11.1 的算法
     * （合并多个有序数组）
     * <p>
     * 时间复杂度：O(n * log(k))
     * <p>
     * 空间复杂度：O(k)
     */
    private static List<Integer> solTwo(List<Integer> A) {
        // Decomposes A into a set of sorted arrays.
        List<List<Integer>> sortedSubarrays = new ArrayList<>();
        SubarrayType subarrayType = SubarrayType.INCREASING;
        int startIdx = 0;
        for (int i = 1; i <= A.size(); i++) {
            // A is ended. Adds the last subarray
            if (i == A.size()
                    || (A.get(i - 1) < A.get(i) && subarrayType == SubarrayType.DECREASING)
                    || (A.get(i - 1) > A.get(i) && subarrayType == SubarrayType.INCREASING)) {
                List<Integer> subList = A.subList(startIdx, i);
                if (subarrayType == SubarrayType.DECREASING) {
                    Collections.reverse(subList);
                }
                sortedSubarrays.add(subList);
                startIdx = i;
                subarrayType = (subarrayType == SubarrayType.INCREASING)
                        ? SubarrayType.DECREASING
                        : SubarrayType.INCREASING;
            }
        }
        return mergeSortedArrays(sortedSubarrays);
    }

    /**
     * 思路一：直接对数组进行排序
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static List<Integer> solOne(List<Integer> A) {
        Collections.sort(A);
        return A;
    }

    private static List<Integer> mergeSortedArrays(List<List<Integer>> sortedArrays) {
        List<Iterator<Integer>> its = new ArrayList<>();
        for (List<Integer> array : sortedArrays) {
            its.add(array.iterator());
        }
        PriorityQueue<ArrayEntry> minHeap = new PriorityQueue<>(its.size(), new Comparator<ArrayEntry>() {
            @Override
            public int compare(ArrayEntry o1, ArrayEntry o2) {
                return Integer.compare(o1.value, o2.value);
            }
        });
        for (int i = 0; i < its.size(); i++) {
            if (its.get(i).hasNext()) {
                minHeap.add(new ArrayEntry(its.get(i).next(), i));
            }
        }
        List<Integer> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            ArrayEntry entry = minHeap.poll();
            result.add(entry.value);
            if (its.get(entry.arrayId).hasNext()) {
                minHeap.add(new ArrayEntry(its.get(entry.arrayId).next(), entry.arrayId));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SortIncreasingDecreasingArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static enum SubarrayType {
        INCREASING,
        DECREASING
    }

    private static class ArrayEntry {
        public Integer value;
        public Integer arrayId;

        public ArrayEntry(Integer value, Integer arrayId) {
            this.value = value;
            this.arrayId = arrayId;
        }
    }
}
