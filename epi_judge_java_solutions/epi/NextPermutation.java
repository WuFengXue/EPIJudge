package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Collections;
import java.util.List;

/**
 * 6.10 COMPUTE THE NEXT PERMUTATION
 * <p>
 * There exist exactly n\ permutations of n elements. These can be totally ordered using
 * the dictionary ordering—define permutation p to appear before permutation q if in the
 * first place where p and q differ in their array representations, starting from index 0, the
 * corresponding entry for p is less than that for q. For example, (2,0,1} < (2,1,0). Note
 * that the permutation (0,1, 2) is the smallest permutation under dictionary ordering,
 * and (2,1,0) is the largest permutation under dictionary ordering.
 * <p>
 * Write a program that takes as input a permutation, and returns the next permutation
 * under dictionary ordering. If the permutation is the last permutation, return the
 * empty array. For example, if the input is (1,0,3,2) your function should return
 * (1, 2, 0, 3). If the input is (3, 2, 1, 0), return ().
 * <p>
 * Hint: Study concrete examples.
 */
public class NextPermutation {
    @EpiTest(testDataFile = "next_permutation.tsv")
    public static List<Integer> nextPermutation(List<Integer> perm) {
        return solTwo(perm);
    }

    /**
     * 思路一：
     * <p>
     * (1.) Find k such that p[k] < p[k + 1] and entries after index k appear in decreasing
     * order.
     * <p>
     * (2.) Find the smallest p[l] such that p[l] > p[k] (such an / must exist since p[k] <
     * p[k+l]).
     * <p>
     * (3.) Swap p[l] and p[k] (note that the sequence after position k remains in decreasing
     * order).
     * <p>
     * (4.) Sort the sequence after position k.
     * <p>
     * 时间复杂度：O(Collections.sort)
     * 空间复杂度：O(Collections.sort)
     */
    private static List<Integer> solOne(List<Integer> perm) {
        // Find the first entry from the right that is smaller than the entry
        // immediately after it.
        int k = perm.size() - 2;
        while (k >= 0 && perm.get(k) >= perm.get(k + 1)) {
            k--;
        }
        // perm is the last permutation.
        if (k < 0) {
            return Collections.emptyList();
        }
        // Swap the smallest entry after index k that is greater than perm[k] . We
        // exploit the fact that perm.subList(k + 1, perm.sizeO) is decreasing so
        // if we search in reverse order, the first entry that is greater than
        // perm[k] is the smallest such entry.
        for (int i = perm.size() - 1; i > k; i--) {
            if (perm.get(k) < perm.get(i)) {
                Collections.swap(perm, k, i);
                break;
            }
        }
        // Build the smallest dictionary ordering of this subarray by sorting it.
        Collections.sort(perm.subList(k + 1, perm.size()));
        return perm;
    }

    /**
     * 思路二：
     * <p>
     * (1.) Find k such that p[k] < p[k + 1] and entries after index k appear in decreasing
     * order.
     * <p>
     * (2.) Find the smallest p[l] such that p[l] > p[k] (such an / must exist since p[k] <
     * p[k+l]).
     * <p>
     * (3.) Swap p[l] and p[k] (note that the sequence after position k remains in decreasing
     * order).
     * <p>
     * (4.) Reverse the sequence after position k.
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static List<Integer> solTwo(List<Integer> perm) {
        // Find the first entry from the right that is smaller than the entry
        // immediately after it.
        int k = perm.size() - 2;
        while (k >= 0 && perm.get(k) >= perm.get(k + 1)) {
            k--;
        }
        // perm is the last permutation.
        if (k < 0) {
            return Collections.emptyList();
        }
        // Swap the smallest entry after index k that is greater than perm[k] . We
        // exploit the fact that perm.subList(k + 1, perm.sizeO) is decreasing so
        // if we search in reverse order, the first entry that is greater than
        // perm[k] is the smallest such entry.
        for (int i = perm.size() - 1; i > k; i--) {
            if (perm.get(k) < perm.get(i)) {
                Collections.swap(perm, k, i);
                break;
            }
        }
        // Since perm.subList[k + 1, perm.size()) is in decreasing order, we can
        // build the smallest dictionary ordering of this subarray by reversing it.
        Collections.reverse(perm.subList(k + 1, perm.size()));
        return perm;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "NextPermutation.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
