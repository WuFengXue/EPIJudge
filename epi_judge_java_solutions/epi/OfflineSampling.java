package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.RandomSequenceChecker;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 6.11 SAMPLE OFFLINE DATA
 * <p>
 * This problem is motivated by the need for a company to select a random subset of
 * its customers to roll out a new feature to. For example, a social networking company
 * may want to see the effect of a new UI on page visit duration without taking the
 * chance of alienating all its users if the rollout is unsuccessful.
 * <p>
 * Implement an algorithm that takes as input an array of distinct elements and a size,
 * and returns a subset of the given size of the array elements. All subsets should be
 * equally likely. Return the result in input array itself.
 * <p>
 * Hint: How would you construct a random subset of size k + 1 given a random subset of size k?
 */
public class OfflineSampling {
    public static void randomSampling(int k, List<Integer> A) {
        solOne(k, A);
        return;
    }

    /**
     * 未验证思路：
     * <p>
     * 1、迭代 A，每次根据 k / n 的概率决定是否添加到结果列表，重复上述操作直到取满 k 个随机数
     * <p>
     * 2、随机选取数组下标，过滤重复的元素，直到取满 k 个下标，将小标对应的元素添加到结果列表
     * <p>
     * 3、生成全部可能的排列，然后从中选择一个
     */
    private static void solZero(int k, List<Integer> A) {

    }

    /**
     * 思路一：每次取一个随机数，放到数组的头部，取 k 次
     * <p>
     * 空间复杂度：O(1)
     * 时间复杂度：O(k)
     */
    private static void solOne(int k, List<Integer> A) {
        Random gen = new Random();
        for (int i = 0; i < k; i++) {
            //Generate a random int in [i, A.size() - 1].
            Collections.swap(A, i, i + gen.nextInt(A.size() - i));
        }
    }

    /**
     * 思路二：在思路一的基础上进行优化，当 k > n / 2 时，更新 k 为 n - k，并将随机数放到尾部
     * <p>
     * 空间复杂度：O(1)
     * 时间复杂度：O(k)
     */
    private static void solTwo(int k, List<Integer> A) {
        Random gen = new Random();
        if (k <= A.size() / 2) {
            for (int i = 0; i < k; i++) {
                Collections.swap(A, i, i + gen.nextInt(A.size() - i));
            }
        } else {
            k = A.size() - k;
            for (int i = 0; i < k; i++) {
                Collections.swap(A, A.size() - 1 - i, A.size() - 1 - gen.nextInt(A.size() - i));
            }
        }
    }

    private static boolean randomSamplingRunner(TimedExecutor executor, int k,
                                                List<Integer> A)
            throws Exception {
        List<List<Integer>> results = new ArrayList<>();

        executor.run(() -> {
            for (int i = 0; i < 1000000; ++i) {
                randomSampling(k, A);
                results.add(new ArrayList<>(A.subList(0, k)));
            }
        });

        int totalPossibleOutcomes =
                RandomSequenceChecker.binomialCoefficient(A.size(), k);
        Collections.sort(A);
        List<List<Integer>> combinations = new ArrayList<>();
        for (int i = 0; i < RandomSequenceChecker.binomialCoefficient(A.size(), k);
             ++i) {
            combinations.add(
                    RandomSequenceChecker.computeCombinationIdx(A, A.size(), k, i));
        }
        List<Integer> sequence = new ArrayList<>();
        for (List<Integer> result : results) {
            Collections.sort(result);
            sequence.add(combinations.indexOf(result));
        }
        return RandomSequenceChecker.checkSequenceIsUniformlyRandom(
                sequence, totalPossibleOutcomes, 0.01);
    }

    @EpiTest(testDataFile = "offline_sampling.tsv")
    public static void randomSamplingWrapper(TimedExecutor executor, int k,
                                             List<Integer> A) throws Exception {
        RandomSequenceChecker.runFuncWithRetries(
                () -> randomSamplingRunner(executor, k, A));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "OfflineSampling.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
