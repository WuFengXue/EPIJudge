package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 13.9 FIND THE LONGEST SUBARRAY WITH DISTINCT ENTRIES
 * <p>
 * Write a program that takes an array and returns the length of a longest
 * subarray with the property that all its elements are distinct. For
 * example, if the array is (/,s,/,e,t,w,e,n,w,e) then a longest subarray
 * all of whose elements are distinct is (s,f,e,t,w).
 * <p>
 * Hint: What should you do if the subarray from indices i to j satisfies
 * the property, but the subarray from i to j + 1 does not?
 */
public class LongestSubarrayWithDistinctValues {
    @EpiTest(testDataFile = "longest_subarray_with_distinct_values.tsv")

    public static int longestSubarrayWithDistinctEntries(List<Integer> A) {
        return solTwo(A);
    }

    /**
     * 思路二：用哈希表记录每种元素的最新下标，用另一个字段 startIdx 记录子串的
     * 起始下标，然后遍历数组，记 i 为当前遍历元素的下标：
     * <p>
     * 如果元素为非重复元素，将其添加到哈希表
     * <p>
     * 如果元素为重复元素，当前子串为 [startIdx, i - 1]，则比较更新最长子串，并更新
     * startIdx 为重复元素最新的下标 + 1
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(m)，m 为数组中整形的种类数
     */
    private static int solTwo(List<Integer> A) {
        int result = 0;
        // Records the most recent occurrences of each entry.
        Map<Integer, Integer> mostRecentOccurrence = new HashMap<>();
        int longestDupFreeSubarrayStartIdx = 0;
        for (int i = 0; i < A.size(); i++) {
            Integer dupIdx = mostRecentOccurrence.put(A.get(i), i);
            // A.get(i) appeared before. Did it appear in the longest current
            // subarray?
            if (dupIdx != null) {
                if (dupIdx >= longestDupFreeSubarrayStartIdx) {
                    result = Math.max(result, i - longestDupFreeSubarrayStartIdx);
                    longestDupFreeSubarrayStartIdx = dupIdx + 1;
                }
            }
        }
        return Math.max(result, A.size() - longestDupFreeSubarrayStartIdx);
    }

    /**
     * 思路一：遍历全部正向子串，读取子串的首个不重复子串，记录并更新最大不重复子串
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(m)，m 为最大不重复子数组的大小
     */
    private static int solOne(List<Integer> A) {
        int result = 0;
        Set<Integer> valueSet = new HashSet<>();
        for (int i = 0; i < A.size(); i++) {
            valueSet.clear();
            for (int j = i; j < A.size(); j++) {
                if (!valueSet.add(A.get(j))) {
                    result = Math.max(result, j - i);
                    break;
                }
            }
            if (valueSet.size() == A.size() - i) {
                result = Math.max(result, A.size() - i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "LongestSubarrayWithDistinctValues.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
