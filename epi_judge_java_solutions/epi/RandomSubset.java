package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.RandomSequenceChecker;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 6.14 COMPUTE A RANDOM SUBSET
 * <p>
 * The set {0,1,2,...,n-1) has(n k)=n!/((n-k)!k!) subsets of size k.We seek to design
 * an algorithm that returns any one of these subsets with equal probability.
 * <p>
 * Write a program that takes as input a positive integer n and a size k < n,and returns
 * a size-k subset of {0,1, 2,..., n - 1). The subset should be represented as an array.
 * All subsets should be equally likely and, in addition, all permutations of elements
 * of the array should be equally likely. You may assume you have a function which takes
 * as input a nonnegative integer t and returns an integer in the set {0, 1, ..., t - 1)
 * with uniform probability.
 * <p>
 * Hint: Simulate Solution 6.11 on Page 78, using an appropriate data structure to
 * reduce space.
 */
public class RandomSubset {

    // Returns a random k-sized subset of {0, 1, ..., n - 1}.
    public static List<Integer> randomSubset(int n, int k) {
        return solOne(n, k);
    }

    /**
     * 思路一：每次生成一个[0, n - 1]的随机数，如果当前还未添加，则添加到列表中，
     * 如果已添加，则重试，使用{@link HashSet}检测是否已添加
     * <p>
     * 时间复杂度：O(n * log(n))
     * 空间复杂度：O(k)
     */
    private static List<Integer> solOne(int n, int k) {
        List<Integer> result = new ArrayList<>(k);
        Set<Integer> set = new HashSet<>(k);
        Random randIntGen = new Random();
        while (set.size() < k) {
            int randInt = randIntGen.nextInt(n);
            if (set.add(randInt)) {
                result.add(randInt);
            }
        }
        return result;
    }

    /**
     * 思路二：利用{@link OfflineSampling}的算法
     * <p>
     * 时间复杂度：O(k)
     * 空间复杂度：O(n)
     */
    private static List<Integer> solTwo(int n, int k) {
        List<Integer> A = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            A.add(i);
        }
        Random randIdxGen = new Random();
        for (int i = 0; i < k; i++) {
            Collections.swap(A, i, i + randIdxGen.nextInt(n - i));
        }
        return A.subList(0, k);
    }

    /**
     * 思路三：在{@link OfflineSampling}的算法基础上进行优化，用一个哈希表存储交换过的元素
     * <p>
     * 时间复杂度：O(k)
     * 空间复杂度：O(k)
     */
    private static List<Integer> solThree(int n, int k) {
        Map<Integer, Integer> changedElements = new HashMap<>();
        Random randIdxGen = new Random();
        for (int i = 0; i < k; i++) {
            // Generate random int in [i, n - 1].
            int randIdx = i + randIdxGen.nextInt(n - i);
            Integer ptr1 = changedElements.get(i);
            Integer ptr2 = changedElements.get(randIdx);
            if (ptr1 == null && ptr2 == null) {
                changedElements.put(i, randIdx);
                changedElements.put(randIdx, i);
            } else if (ptr1 != null && ptr2 == null) {
                changedElements.put(i, randIdx);
                changedElements.put(randIdx, ptr1);
            } else if (ptr1 == null && ptr2 != null) {
                changedElements.put(i, ptr2);
                changedElements.put(randIdx, i);
            } else {
                changedElements.put(i, ptr2);
                changedElements.put(randIdx, ptr1);
            }
        }
        List<Integer> result = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            result.add(changedElements.get(i));
        }
        return result;
    }

    private static boolean randomSubsetRunner(TimedExecutor executor, int n,
                                              int k) throws Exception {
        List<List<Integer>> results = new ArrayList<>();

        executor.run(() -> {
            for (int i = 0; i < 1000000; ++i) {
                results.add(randomSubset(n, k));
            }
        });

        int totalPossibleOutcomes = RandomSequenceChecker.binomialCoefficient(n, k);
        List<Integer> A = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            A.add(i);
        }
        List<List<Integer>> combinations = new ArrayList<>();
        for (int i = 0; i < RandomSequenceChecker.binomialCoefficient(n, k); ++i) {
            combinations.add(RandomSequenceChecker.computeCombinationIdx(A, n, k, i));
        }
        List<Integer> sequence = new ArrayList<>();
        for (List<Integer> result : results) {
            Collections.sort(result);
            sequence.add(combinations.indexOf(result));
        }
        return RandomSequenceChecker.checkSequenceIsUniformlyRandom(
                sequence, totalPossibleOutcomes, 0.01);
    }

    @EpiTest(testDataFile = "random_subset.tsv")
    public static void randomSubsetWrapper(TimedExecutor executor, int n, int k)
            throws Exception {
        RandomSequenceChecker.runFuncWithRetries(
                () -> randomSubsetRunner(executor, n, k));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RandomSubset.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
