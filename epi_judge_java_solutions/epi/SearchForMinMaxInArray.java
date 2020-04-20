package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 12.7 FIND THE MIN AND MAX SIMULTANEOUSLY
 * <p>
 * Given an array of comparable objects, you can find either the min or
 * the max of the elements in the array with n - 1 comparisons, where n is
 * the length of the array.
 * <p>
 * Comparing elements may be expensive, e.g., a comparison may involve a
 * number of nested calls or the elements being compared may be long strings.
 * Therefore, it is natural to ask if both the min and the max can be
 * computed with less than the 2(n - 1) comparisons required to compute the
 * min and the max independently.
 * <p>
 * Design an algorithm to find the min and max elements in an array. For
 * example, if A =(3,2,5,1,2,4), you should return 1 for the min and 5 for
 * the max.
 * <p>
 * Hint: Use the fact that a < b and b < c implies a < c to reduce the number
 * of compares used by the brute-force approach.
 */
public class SearchForMinMaxInArray {
    @EpiTest(testDataFile = "search_for_min_max_in_array.tsv")

    public static MinMax findMinMax(List<Integer> A) {
        return solThree(A);
    }

    /**
     * 思路三：在思路二的基础上进行优化，采用流模式，实时计算当前的最大值和最小值
     * <p>
     * 时间复杂度：O(3n / 2 - 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static MinMax solThree(List<Integer> A) {
        if (A == null || A.isEmpty()) {
            return null;
        } else if (A.size() == 1) {
            return new MinMax(A.get(0), A.get(0));
        }

        MinMax globalMinMax = MinMax.minMax(A.get(0), A.get(1));
        // Process two elements at a time.
        for (int i = 2; i + 1 < A.size(); i += 2) {
            MinMax minMax = MinMax.minMax(A.get(i), A.get(i + 1));
            globalMinMax.smallest = Math.min(globalMinMax.smallest, minMax.smallest);
            globalMinMax.largest = Math.max(globalMinMax.largest, minMax.largest);
        }
        // If there is odd number of elements in the array, we still
        // need to compare the last element with the existing answer.
        if (A.size() % 2 == 1) {
            globalMinMax.smallest = Math.min(globalMinMax.smallest, A.get(A.size() - 1));
            globalMinMax.largest = Math.max(globalMinMax.largest, A.get(A.size() - 1));
        }

        return globalMinMax;
    }

    /**
     * 思路二：将列表分成最大值候选集合和最小值候选集合两部分（通过两两比较实现
     * / 类似晋级赛，最强的肯定在晋级的八强里面，最弱的肯定在被淘汰的八弱里面），
     * 然后再分别计算两个候选集合的最大值和最小值
     * <p>
     * 时间复杂度：O(3n / 2 - 2)，O(n / 2) 用于计算最大值候选集合和最小值候选集合，
     * 各 O(n / 2 - 1) 用于从候选集合计算最大值和最小值
     * <p>
     * 空间复杂度：O（n）
     */
    private static MinMax solTwo(List<Integer> A) {
        if (A == null || A.isEmpty()) {
            return null;
        } else if (A.size() == 1) {
            return new MinMax(A.get(0), A.get(0));
        }

        List<Integer> minCandidates = new ArrayList<>();
        List<Integer> maxCandidates = new ArrayList<>();
        for (int i = 0; i + 1 < A.size(); i += 2) {
            if (A.get(i) <= A.get(i + 1)) {
                minCandidates.add(A.get(i));
                maxCandidates.add(A.get(i + 1));
            } else {
                minCandidates.add(A.get(i + 1));
                maxCandidates.add(A.get(i));
            }
        }

        Integer min = minCandidates.get(0);
        for (int i = 1; i < minCandidates.size(); i++) {
            min = Math.min(min, minCandidates.get(i));
        }
        Integer max = maxCandidates.get(0);
        for (int i = 1; i < maxCandidates.size(); i++) {
            max = Math.max(max, maxCandidates.get(i));
        }

        if (A.size() % 2 == 1) {
            min = Math.min(min, A.get(A.size() - 1));
            max = Math.max(max, A.get(A.size() - 1));
        }

        return new MinMax(min, max);
    }

    /**
     * 思路一：遍历数组进行比较，最大值和最小值分开算
     * <p>
     * 时间复杂度：O(2n)，最大值和最小值各 n
     * <p>
     * 空间复杂度：O(1)
     */
    private static MinMax solOne(List<Integer> A) {
        if (A == null || A.isEmpty()) {
            return null;
        } else if (A.size() == 1) {
            return new MinMax(A.get(0), A.get(0));
        }

        Integer min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Integer x : A) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        return new MinMax(min, max);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchForMinMaxInArray.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {Integer.class, Integer.class})

    public static class MinMax {
        public Integer smallest;
        public Integer largest;

        public MinMax(Integer smallest, Integer largest) {
            this.smallest = smallest;
            this.largest = largest;
        }

        private static MinMax minMax(Integer a, Integer b) {
            return Integer.compare(b, a) < 0 ? new MinMax(b, a) : new MinMax(a, b);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MinMax minMax = (MinMax) o;

            if (!smallest.equals(minMax.smallest)) {
                return false;
            }
            return largest.equals(minMax.largest);
        }

        @Override
        public String toString() {
            return "min: " + smallest + ", max: " + largest;
        }
    }
}
