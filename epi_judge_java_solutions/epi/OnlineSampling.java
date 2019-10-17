package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.RandomSequenceChecker;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 6.12 SAMPLE ONLINE DATA
 * <p>
 * This problem is motivated by the design of a packet sniffer that provides a uniform
 * sample of packets for a network session.
 * <p>
 * Design a program that takes as input a size k, and reads packets, continuously
 * maintaining a uniform random subset of size k of the read packets.
 * <p>
 * Hint: Suppose you have a procedure which selects k packets from the first n > k
 * packets as specified. How would you deal with the (n + l)th packet?
 */
public class OnlineSampling {

    // Assumption: there are at least k elements in the stream.
    public static List<Integer> onlineRandomSample(Iterator<Integer> stream,
                                                   int k) {
        return solTwo(stream, k);
    }

    /**
     * 思路一：存储全部数据，然后利用{@link OfflineSampling}的算法抽样
     * <p>
     * 时间复杂度：O(nk)
     * 空间复杂度：O(n)
     */
    private static List<Integer> solOne(Iterator<Integer> stream, int k) {
        List<Integer> A = new ArrayList<>();
        while (stream.hasNext()) {
            A.add(stream.next());
            if (A.size() <= k) {
                continue;
            }
            Random gen = new Random();
            for (int i = 0; i < k; i++) {
                Collections.swap(A, i, i + gen.nextInt(A.size() - i));
            }
        }
        return A.subList(0, k);
    }

    /**
     * 思路二：先存储头 k 个元素，之后每次都按 k / n 的概率对采样集中的元素进行替换
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(k)
     */
    private static List<Integer> solTwo(Iterator<Integer> stream, int k) {
        List<Integer> runningSamples = new ArrayList<>(k);
        // Stores the first k elements.
        for (int i = 0; i < k && stream.hasNext(); i++) {
            runningSamples.add(stream.next());
        }

        // Have read the first k elements.
        int numSeenSoFar = k;
        Random randIdxGen = new Random();
        while (stream.hasNext()) {
            Integer x = stream.next();
            ++numSeenSoFar;
            // Generate a random number in [0, numSeenSoFar], and if this number is in
            // [0, k - 1], we replace that element from the sample with x.
            final int idxToReplace = randIdxGen.nextInt(numSeenSoFar);
            if (idxToReplace < k) {
                runningSamples.set(idxToReplace, x);
            }
        }
        return runningSamples;
    }

    private static boolean onlineRandomSampleRunner(TimedExecutor executor,
                                                    List<Integer> A, int k)
            throws Exception {
        List<List<Integer>> results = new ArrayList<>();

        executor.run(() -> {
            for (int i = 0; i < 1000000; ++i) {
                results.add(onlineRandomSample(A.iterator(), k));
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

    @EpiTest(testDataFile = "online_sampling.tsv")
    public static void onlineRandomSampleWrapper(TimedExecutor executor,
                                                 List<Integer> stream, int k)
            throws Exception {
        RandomSequenceChecker.runFuncWithRetries(
                () -> onlineRandomSampleRunner(executor, stream, k));
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "OnlineSampling.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
