package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 11.1 MERGE SORTED FILES
 * <p>
 * This problem is motivated by the following scenario. You are given 500 files,
 * each containing stock trade information for an S&P 500 company. Each trade is
 * encoded bya line in the following format: 1232111,AAPL,30,456.12.
 * <p>
 * The first number is the time of the trade expressed as the number of milliseconds
 * since the start of the day's trading. Lines within each file are sorted in
 * increasing order of time. The remaining values are the stock symbol, number of
 * shares, and price. You are to create a single file containing all the trades from
 * the 500 files, sorted in order of increasing trade times. The individual files
 * are of the order of 5-100 megabytes; the combined file will be of the order of
 * five gigabytes. In the abstract, we are trying to solve the following problem.
 * <p>
 * Write a program that takes as input a set of sorted sequences and computes the
 * union of these sequences as a sorted sequence. For example, if the input is
 * (3, 5, 7), (0, 6), and (0, 6, 28), then the output is (0, 0, 3, 5, 6, 6, 7, 28).
 * <p>
 * Hint: Which part of each sequence is significant as the algorithm executes?
 */
public class SortedArraysMerge {
    @EpiTest(testDataFile = "sorted_arrays_merge.tsv")

    public static List<Integer>
    mergeSortedArrays(List<List<Integer>> sortedArrays) {
        return solTwo(sortedArrays);
    }

    /**
     * 思路二：利用输入的集合都是已经排序过的特性，每次将全部集合的首个元素中最小的
     * 那个取出来（使用堆实现），添加到结果集合中。
     * <p>
     * 时间复杂度：O(n * log(k))，k为集合数
     * <p>
     * 空间复杂度：O(k)
     */
    private static List<Integer> solTwo(List<List<Integer>> sortedArrays) {
        List<Iterator<Integer>> its = new ArrayList<>(sortedArrays.size());
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
                minHeap.add(new ArrayEntry(its.get(entry.arrayId).next(),
                        entry.arrayId));
            }
        }
        return result;
    }

    /**
     * 思路一：将全部的元素添加到一个集合，然后对这个集合做排序
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static List<Integer> solOne(List<List<Integer>> sortedArrays) {
        List<Integer> result = new ArrayList<>();
        for (List<Integer> array : sortedArrays) {
            result.addAll(array);
        }
        Collections.sort(result);
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SortedArraysMerge.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
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
