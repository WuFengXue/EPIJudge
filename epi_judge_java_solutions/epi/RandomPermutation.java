package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.RandomSequenceChecker;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 6.13 COMPUTE A RANDOM PERMUTATION
 * <p>
 * Generating random permutations is not as straightforward as it seems. For example,
 * iterating through (0,1, 1} and swapping each element with another randomly
 * selected element does not generate all permutations with equal probability. One way
 * to see this is to consider the case n = 3. The number of permutations is 3! = 6. The
 * total number of ways in which we can choose the elements to swap is 33 = 27 and
 * all are equally likely. Since 27 is not divisible by 6, some permutations correspond
 * to more ways than others, so not all permutations are equally likely.
 * <p>
 * Design an algorithm that creates uniformly random permutations of {0,1,...,n-1).
 * You are given a random number generator that returns integers in the set
 * {0,1,...,n-1) with equal probability; use as few calls to it as possible.
 * <p>
 * Hint: If the result is stored in A, how would you proceed once A[n - 1] is assigned
 * correctly?
 */
public class RandomPermutation {

    public static List<Integer> computeRandomPermutation(int n) {
        return solTwo(n);
    }

    /**
     * 思路一：每次生成一个 0 到 n-1 的随机数，利用哈希表进行重复检测，
     * 如果未重复则添加到列表，如果重复则重试
     * <p>
     * 时间复杂度：O(n log(n))
     * 空间复杂度：O(n)，需要一个额外的哈希表
     */
    private static List<Integer> solOne(int n) {
        List<Integer> randPer = new ArrayList<>();
        Set<Integer> set = new HashSet<>(n);
        Random randIntGen = new Random();
        while (set.size() < n) {
            int x = randIntGen.nextInt(n);
            if (set.add(x)) {
                randPer.add(x);
            }
        }
        return randPer;
    }

    /**
     * 思路二：使用{@link OfflineSampling}的算法
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)，无需额外的空间
     */
    private static List<Integer> solTwo(int n) {
        List<Integer> permutation = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            permutation.add(i);
        }
        Random randIdxGen = new Random();
        for (int i = 0; i < n; i++) {
            Collections.swap(permutation, i, i + randIdxGen.nextInt(n - i));
        }
        return permutation;
    }

    private static int factorial(int n) {
        return n <= 1 ? 1 : n * factorial(n - 1);
    }

    private static int permutationIndex(List<Integer> perm) {
        int idx = 0;
        int n = perm.size();
        for (int i = 0; i < perm.size(); ++i) {
            int a = perm.get(i);
            idx += a * factorial(n - 1);
            for (int j = i + 1; j < perm.size(); ++j) {
                if (perm.get(j) > a) {
                    perm.set(j, perm.get(j) - 1);
                }
            }
            --n;
        }
        return idx;
    }

    private static boolean computeRandomPermutationRunner(TimedExecutor executor,
                                                          int n)
            throws Exception {
        List<List<Integer>> results = new ArrayList<>();

        executor.run(() -> {
            for (int i = 0; i < 1000000; ++i) {
                results.add(computeRandomPermutation(n));
            }
        });

        List<Integer> sequence = new ArrayList<>();
        for (List<Integer> result : results) {
            sequence.add(permutationIndex(result));
        }
        return RandomSequenceChecker.checkSequenceIsUniformlyRandom(
                sequence, factorial(n), 0.01);
    }

    @EpiTest(testDataFile = "random_permutation.tsv")
    public static void computeRandomPermutationWrapper(TimedExecutor executor,
                                                       int n) throws Exception {
        RandomSequenceChecker.runFuncWithRetries(
                () -> computeRandomPermutationRunner(executor, n));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RandomPermutation.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
