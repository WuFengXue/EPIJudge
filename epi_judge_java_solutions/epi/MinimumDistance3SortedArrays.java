package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * 15.6 FIND THE CLOSEST ENTRIES IN THREE SORTED ARRAYS
 * <p>
 * Design an algorithm that takes three sorted arrays and returns one entry from each
 * such that the minimum interval containing these three entries is as small as possible.
 * For example, if the three arrays are (5,10,15), (3,6,9,12,15), and (8,16,24), then
 * 15,15,16 lie in the smallest possible interval.
 * <p>
 * Hint: How would you proceed if you needed to pick three entries in a single sorted array?
 */
public class MinimumDistance3SortedArrays {

    @EpiTest(testDataFile = "minimum_distance_3_sorted_arrays.tsv")

    public static int
    findMinDistanceSortedArrays(List<List<Integer>> sortedArrays) {
        return solOne(sortedArrays);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "MinimumDistance3SortedArrays.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    /**
     * 思路一：从每个数组中的最小元素的组合开始，在该组合中，最大元素所在的数组中，
     * 因为其他元素都大于最小的元素，所以肯定不是解，可以直接过滤掉
     * <p>
     * 之后，每次移除组合中最小的元素，并从最小元素所在的数组中，取下个元素加入重新计算，
     * 重复上述操作，直到有某个数组的全部元素都被遍历过
     * <p>
     * 时间复杂度：O(n * log(k))，k 为数组的数量
     * <p>
     * 空间复杂度：O(k)
     */
    private static int solOne(List<List<Integer>> sortedArrays) {
        // Indices into each of the arrays.
        List<Integer> heads = new ArrayList<>();
        for (int i = 0; i < sortedArrays.size(); i++) {
            heads.add(0);
        }

        int result = Integer.MAX_VALUE;
        NavigableSet<ArrayData> currentHeads = new TreeSet<>();
        // Adds the minimum element of each array in to currentHeads.
        for (int i = 0; i < sortedArrays.size(); i++) {
            currentHeads.add(new ArrayData(i, sortedArrays.get(i).get(heads.get(i))));
        }

        while (true) {
            result = Math.min(result, currentHeads.last().val - currentHeads.first().val);
            int idxNextMin = currentHeads.first().idx;
            heads.set(idxNextMin, heads.get(idxNextMin) + 1);
            // Return if some array has no remaining elements.
            if (heads.get(idxNextMin) >= sortedArrays.get(idxNextMin).size()) {
                return result;
            }

            currentHeads.pollFirst();
            currentHeads.add(new ArrayData(idxNextMin, sortedArrays.get(idxNextMin).get(heads.get(idxNextMin))));
        }
    }

    /**
     * 思路零：用三层循环遍历所有的组合，因为测试数据的数组数量大于3，所以无法通过测试
     * <p>
     * 时间复杂度：O(l * m * n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solZero(List<List<Integer>> sortedArrays) {
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < sortedArrays.get(0).size(); i++) {
            int min = sortedArrays.get(0).get(i);
            int max = sortedArrays.get(0).get(i);
            for (int j = 0; j < sortedArrays.get(1).size(); j++) {
                min = Math.min(min, sortedArrays.get(1).get(j));
                max = Math.max(max, sortedArrays.get(1).get(j));
                for (int k = 0; k < sortedArrays.get(2).size(); k++) {
                    min = Math.min(min, sortedArrays.get(2).get(k));
                    max = Math.max(max, sortedArrays.get(2).get(k));
                    result = Math.min(result, max - min);
                }
            }
        }
        return result;
    }

    public static class ArrayData implements Comparable<ArrayData> {
        public int val;
        public int idx;

        public ArrayData(int idx, int val) {
            this.val = val;
            this.idx = idx;
        }

        @Override
        public int compareTo(ArrayData o) {
            int result = Integer.compare(val, o.val);
            if (result == 0) {
                result = Integer.compare(idx, o.idx);
            }
            return result;
        }
    }
}
