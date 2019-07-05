package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.3 REVERSE BITS
 * <p>
 * Write a program that takes a 64-bit word and returns the 64-bit word consisting of
 * the bits of the input word in reverse order. For example, if the input is alternating
 * Is and Os,i.e.,(1010...10),the output should be alternating Os and Is,i.e.,(0101...01).
 */
public class ReverseBits {
    private static final int NUM = 1 << 16;
    private static final long[] PRE_COMPUTED_REVERSE = new long[NUM];

    static {
        for (int i = 0; i < NUM; i++) {
            PRE_COMPUTED_REVERSE[i] = reverseBits(i, 16);
        }
    }

    private static long reverseBits(long x, int n) {
        for (int i = 0; i < n / 2; i++) {
            x = SwapBits.swapBits(x, i, n - 1 - i);
        }
        return x;
    }

    @EpiTest(testDataFile = "reverse_bits.tsv")
    public static long reverseBits(long x) {
        return solTwo(x);
    }

    /**
     * 遍历低32位，逐个位与高32位交换
     * <p>
     * 时间复杂度：O(n/2)，n = 64
     */
    private static long solOne(long x) {
        return reverseBits(x, 64);
    }

    /**
     * 缓存16位的翻转结果，将64位拆分为4个翻转的16位的拼接
     * <p>
     * 时间复杂度：O(n/L)，n = 64, L = 16
     */
    private static long solTwo(long x) {
        final int MASK_SIZE = 16;
        final int BIT_MASK = 0xFFFF;
        return PRE_COMPUTED_REVERSE[(int) (x & BIT_MASK)] << (3 * MASK_SIZE)
                | PRE_COMPUTED_REVERSE[(int) ((x >>> MASK_SIZE) & BIT_MASK)] << (2 * MASK_SIZE)
                | PRE_COMPUTED_REVERSE[(int) ((x >>> (2 * MASK_SIZE)) & BIT_MASK)] << MASK_SIZE
                | PRE_COMPUTED_REVERSE[(int) ((x >>> (3 * MASK_SIZE)) & BIT_MASK)];
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ReverseBits.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
