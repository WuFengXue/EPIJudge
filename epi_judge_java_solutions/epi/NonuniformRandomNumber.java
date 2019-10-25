package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.RandomSequenceChecker;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 6.15 GENERATE NONUNIFORM RANDOM NUMBERS
 * <p>
 * Suppose you need to write a load test for a server. You have studied the inter-arrival
 * time of requests to the server over a period of one year. From this data you have
 * computed a histogram of the distribution of the inter-arrival time of requests. In the
 * load test you would like to generate requests for the server such that the inter-arrival
 * times come from the same distribution that was observed in the historical data. The
 * following problem formalizes the generation of inter-arrival times.
 * <p>
 * You are given n numbers as well as probabilities p0,p1,...,p(n-1) which sum up to
 * 1. Given a random number generator that produces values in [0,1] uniformly, how
 * would you generate one of the n numbers according to the specified probabilities? For
 * example, if the numbers are 3, 5, 7, 11, and the probabilities are 9/18, 6/18, 2/18,
 * 1/18, then in 1000000 calls to your program, 3 should appear roughly 500000 times, 5
 * should appear roughly 333333 times, 7 should appear roughly 111111 times, and 11 should
 * appear roughly 55555 times.
 * <p>
 * Hint: Look at the graph of the probability that the selected number is less than or
 * equal to a. What do the jumps correspond to?
 */
public class NonuniformRandomNumber {

    public static int
    nonuniformRandomNumberGeneration(List<Integer> values,
                                     List<Double> probabilities) {
        return solOne(values, probabilities);
    }

    /**
     * 思路一：将 [0.0, 1.0] 区间进行切割，然后按生成的随机数落到哪个区域获取到对应的随机数
     * 对应的下标，然后再到值集里面取对应的值
     * <p>
     * 时间复杂度：O(n) - 创建数组的时间， O(n * log(n)) - 二分查找的时间
     * 空间复杂度：O(n)
     */
    private static int solOne(List<Integer> values, List<Double> probabilities) {
        List<Double> prefixSumOfProbabilities = new ArrayList<>();
        prefixSumOfProbabilities.add(0.0);
        // Creating the endpoints for the intervals corresponding to the
        // probabilities.
        for (double p : probabilities) {
            prefixSumOfProbabilities.add(
                    prefixSumOfProbabilities.get(prefixSumOfProbabilities.size() - 1)
                            + p);
        }
        Random r = new Random();
        // Get a random number in [0.0, 1.0)
        final double uniform01 = r.nextDouble();
        // Find the index of the interval that uniform01 lies in.
        int it = Collections.binarySearch(prefixSumOfProbabilities, uniform01);
        if (it < 0) {
            // We want the index of the first element in the array which is
            // greater than the key.
            //
            // When a key is not present in the array, Collections.binarySearch()
            // returns the negative of 1 plus the smallest index whose entry
            // is greater than the key.
            //
            // Therefore , if the return value is negative , by taking its insertion
            // point and minus 1 to it , we get the desired index.
            final int intervalIdx = Math.abs(it + 1) - 1;
            return values.get(intervalIdx);
        } else {
            // We have it >= 0, i.e., uniform01 equals an entry
            // in prefixSumOfProbabilities.
            //
            // Because we uniform01 is a random double, the probability of it
            // equalling an endpoint in prefixSumOfProbabilities is exceedingly low.
            // However, it is not 0, so to be robust we must consider this case.
            return values.get(it);
        }
    }

    private static boolean nonuniformRandomNumberGenerationRunner(
            TimedExecutor executor, List<Integer> values, List<Double> probabilities)
            throws Exception {
        final int N = 1000000;
        List<Integer> results = new ArrayList<>(N);

        executor.run(() -> {
            for (int i = 0; i < N; ++i) {
                results.add(nonuniformRandomNumberGeneration(values, probabilities));
            }
        });

        Map<Integer, Integer> counts = new HashMap<>();
        for (Integer result : results) {
            counts.put(result, counts.getOrDefault(result, 0) + 1);
        }
        for (int i = 0; i < values.size(); ++i) {
            final int v = values.get(i);
            final double p = probabilities.get(i);
            if (N * p < 50 || N * (1.0 - p) < 50) {
                continue;
            }
            final double sigma = Math.sqrt(N * p * (1.0 - p));
            if (counts.get(v) == null || Math.abs(counts.get(v) - (p * N)) > 5 * sigma) {
                return false;
            }
        }
        return true;
    }

    @EpiTest(testDataFile = "nonuniform_random_number.tsv")
    public static void nonuniformRandomNumberGenerationWrapper(
            TimedExecutor executor, List<Integer> values, List<Double> probabilities)
            throws Exception {
        RandomSequenceChecker.runFuncWithRetries(
                ()
                        -> nonuniformRandomNumberGenerationRunner(executor, values,
                        probabilities));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "NonuniformRandomNumber.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
