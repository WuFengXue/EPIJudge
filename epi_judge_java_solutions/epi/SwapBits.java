package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.2 SWAP BITS
 * <p>
 * A 64-bit integer can be viewed as an array of 64 bits, with the bit at index 0 corre¬
 * sponding to the least significant bit (LSB), and the bit at index 63 corresponding to
 * the most significant bit (MSB). Implement code that takes as input a 64-bit integer
 * and swaps the bits at indices i and j.
 */
public class SwapBits {
    @EpiTest(testDataFile = "swap_bits.tsv")
    public static long swapBits(long x, int i, int j) {
        return solTow(x, i, j);
    }

    /**
     * 读取位 i 和位 j，不相等再进行交换，一次修改一位
     * <p>
     * 时间复杂度：O(1)
     */
    private static long solOne(long x, int i, int j) {
        int bitI = (int) ((x >>> i) & 1L);
        int bitJ = (int) ((x >>> j) & 1L);
        if (bitI == bitJ) {
            return x;
        }

        if (bitI == 1) {
            x |= 1L << j;
        } else {
            x &= ~(1L << j);
        }
        if (bitJ == 1) {
            x |= 1L << i;
        } else {
            x &= ~(1l << i);
        }
        return x;
    }

    /**
     * 读取位 i 和位 j，不相等再进行交换，利用异或的特性，使用位掩码一次完成全部修改
     * <p>
     * 时间复杂度：O(1)
     */
    private static long solTow(long x, int i, int j) {
        if (((x >>> i) & 1L) != ((x >>> j) & 1L)) {
            long bitMask = (1L << i) | (1L << j);
            x ^= bitMask;
        }
        return x;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SwapBits.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
