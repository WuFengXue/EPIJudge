package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * Define the weight of a nonnegative integer x to be the number of bits that are set to 1 in its binary
 * representation. For example, since 92 in base-2 equals (1011100)2, the weight of 92 is 4.
 * <p>
 * Write a program which takes as input a nonnegative integer x and returns a number y which is not equal to x,
 * but has the same weight as x and their difference, |y - x|, is as small as possible. You can assume x is not 0,
 * or all Is. For example, if x = 6, you should return 5.
 * <p>
 * Hint: Start with the least significant bit.
 */
public class ClosestIntSameWeight {
    /**
     * 正长整形的位数
     */
    private static final int NUM_UNSIGNED_BITS = 63;

    @EpiTest(testDataFile = "closest_int_same_weight.tsv")
    public static long closestIntSameBitCount(long x) {
        return solTwo(x);
    }

    /**
     * 以 x 为中心点向两侧移动进行检测
     * <p>
     * 时间复杂度：O(2^(n-1))，n = log2(x)
     */
    private static long solOne(long x) {
        final int BIT_COUNT = bitCount(x);
        for (int i = 1; (x - i) > 0 || (x + i) < Long.MAX_VALUE; i++) {
            if (bitCount(x - i) == BIT_COUNT) {
                return x - i;
            } else if (bitCount(x + i) == BIT_COUNT) {
                return x + i;
            }
        }
        throw new IllegalArgumentException("All bits are 0 or 1");
    }

    /**
     * 计算1的个数
     */
    private static int bitCount(long x) {
        int count = 0;
        while (x > 0) {
            count++;
            x &= x - 1;
        }
        return count;
    }

    /**
     * 从 LSB 开始检测，遇到连续两个位不一致时，则结果为这两个位对调后的值
     * <p>
     * 时间复杂度：O(n)，n = ({@link #NUM_UNSIGNED_BITS} - 1)
     */
    private static long solTwo(long x) {
        // x is assumed to be non-negative so we know the leading bit is 0. We
        // restrict to our attention to 63 LSBs.
        for (int i = 0; i < NUM_UNSIGNED_BITS - 1; i++) {
            if (((x >>> i) & 1L) != ((x >>> (i + 1)) & 1L)) {
                // Swaps bit-i and bit-(i + 1).
                final long BIT_MASK = (1L << i) | (1L << (i + 1));
                return x ^ BIT_MASK;
            }
        }
        // Throw error if all bits of x are 0 or 1.
        throw new IllegalArgumentException("All bits are 0 or 1");
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ClosestIntSameWeight.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
