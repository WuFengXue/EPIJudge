package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.RandomSequenceChecker;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 5.10 GENERATE UNIFORM RANDOM NUMBERS
 * <p>
 * This problem is motivated by the following scenario. Six friends have to select
 * a designated driver using a single unbiased coin. The process should be fair to everyone.
 * <p>
 * How would you implement a random number generator that generates a random integer i
 * between a and b, inclusive, given a random number generator that produces zero or one
 * with equal probability? All values in [a,b] should be equally likely.
 * <p>
 * Hint: How would you mimic a three-sided coin with a two-sided coin?
 */
public class UniformRandomNumber {
    private static int zeroOneRandom() {
        Random gen = new Random();
        return gen.nextInt(2);
    }

    public static int uniformRandom(int lowerBound, int upperBound) {
        return solOne(lowerBound, upperBound);
    }

    /**
     * 思路一：使用二进制编码实现。
     * <p>
     * 时间复杂度：O(lg(upperBound - lowerBound + 1))
     */
    private static int solOne(int lowerBound, int upperBound) {
        // 可能的数值总数
        int numOutcomes = upperBound - lowerBound + 1, result;
        do {
            result = 0;
            // 二进制的位数（即 zeroOneRandom 的调用次数）：2 ^ (n - 1) < numOutcomes <= 2 ^ n，n 为位数
            for (int i = 0; (1 << i) < numOutcomes; i++) {
                // zeroOneRandom() is the provided random number generator.
                result = (result << 1) | zeroOneRandom();
            }
        }
        // 结果超出上界，重试
        while (result >= numOutcomes);

        return lowerBound + result;
    }

    private static boolean uniformRandomRunner(TimedExecutor executor,
                                               int lowerBound, int upperBound)
            throws Exception {
        List<Integer> results = new ArrayList<>();

        executor.run(() -> {
            for (int i = 0; i < 100000; ++i) {
                results.add(uniformRandom(lowerBound, upperBound));
            }
        });

        List<Integer> sequence = new ArrayList<>();
        for (Integer result : results) {
            sequence.add(result - lowerBound);
        }
        return RandomSequenceChecker.checkSequenceIsUniformlyRandom(
                sequence, upperBound - lowerBound + 1, 0.01);
    }

    @EpiTest(testDataFile = "uniform_random_number.tsv")
    public static void uniformRandomWrapper(TimedExecutor executor,
                                            int lowerBound, int upperBound)
            throws Exception {
        RandomSequenceChecker.runFuncWithRetries(
                () -> uniformRandomRunner(executor, lowerBound, upperBound));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "UniformRandomNumber.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
