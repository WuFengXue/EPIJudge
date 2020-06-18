package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 13.10 FIND THE LENGTH OF A LONGEST CONTAINED INTERVAL
 * <p>
 * Write a program which takes as input a set of integers represented
 * by an array, and returns the size of a largest subset of integers
 * in the array having the property that if two integers are in the
 * subset, then so are all integers between them. For example, if the
 * input is (3,-2,7,9,8,1,2,0,-1,5,8), the largest such subset is
 * {-2,-1,0,1,2,3), so you should return 6.
 * <p>
 * Hint: Do you really need a total ordering on the input?
 */
public class LongestContainedInterval {
    @EpiTest(testDataFile = "longest_contained_interval.tsv")

    public static int longestContainedRange(List<Integer> A) {
        return solTwo(A);
    }

    /**
     * 思路二：用一个哈希集合存储数组元素，然后遍历数组，在遍历时，
     * 以元素为中心分别向上下边界进行延伸搜索以定位区间大小，为了避免重复
     * 计算，在搜索的同时将元素从哈希集合中移除
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static int solTwo(List<Integer> A) {
        // unprocessedEntries records the existence of each entry in A.
        Set<Integer> unprocessedEntries = new HashSet<>(A);

        int maxIntervalSize = 0;
        for (int i = 0; i < A.size(); i++) {
            if (!unprocessedEntries.remove(A.get(i))) {
                continue;
            }

            int length = 1;
            // Finds the lower bound of the largest range containing A.get(i).
            int lowerBound = A.get(i) - 1;
            while (unprocessedEntries.remove(lowerBound--)) {
                length++;
            }
            // Finds the upper bound of the largest range containing A.get(i).
            int upperBound = A.get(i) + 1;
            while (unprocessedEntries.remove(upperBound++)) {
                length++;
            }
            maxIntervalSize = Math.max(maxIntervalSize, length);
        }
        return maxIntervalSize;
    }

    /**
     * 思路一：对输入进行排序，然后遍历集合依次计算每个区间的大小
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(List<Integer> A) {
        Collections.sort(A);

        int result = 0;
        int intervalSize = 1;
        for (int i = 0; i + 1 < A.size(); i++) {
            if (A.get(i) + 1 == A.get(i + 1)) {
                intervalSize++;
            } else if (A.get(i) + 1 < A.get(i + 1)) {
                result = Math.max(result, intervalSize);
                intervalSize = 1;
            }
        }
        result = Math.max(result, intervalSize);
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "LongestContainedInterval.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
