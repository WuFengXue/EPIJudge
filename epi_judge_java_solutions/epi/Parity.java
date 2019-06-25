package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.1 COMPUTING THE PARITY OF A WORD
 * <p>
 * The parity of a binary word is 1 if the number of Is in the word is odd; otherwise,
 * it is 0. For example, the parity of 1011 is 1, and the parity of 10001000 is 0. Parity
 * checks are used to detect single bit errors in data storage and communication. It is
 * fairly straightforward to write code that computes the parity of a single 64-bit word.
 */
@SuppressWarnings("unused")
public class Parity {
    private static final int NUM = 1 << 16;
    private static final short[] PRE_COMPUTED_PARITIES = new short[NUM];

    static {
        for (int i = 0; i < NUM; i++) {
            PRE_COMPUTED_PARITIES[i] = solFour(i);
        }
    }

    @EpiTest(testDataFile = "parity.tsv")
    public static short parity(long x) {
        // TODO - you fill in here.
        return solFive(x);
    }

    /**
     * 先计算 1 的个数，再计算其奇偶性
     * <p>
     * 时间复杂度：O(n)，n 为 64
     */
    private static short solOne(long x) {
        short bitCount = 0;
        while (x > 0) {
            if ((x & 1) == 1) {
                bitCount++;
            }
            x >>>= 1;
        }
        return (short) (bitCount % 2);
    }

    /**
     * 利用异或的特性（1 ^ 1 = 0, 0 ^ 1 = 1; 1 ^ 0 = 1, 0 ^ 0 = 0;），直接计算奇偶性
     * <p>
     * 时间复杂度：O(n)，n 为 64
     */
    private static short solTwo(long x) {
        short parity = 0;
        while (x > 0) {
            parity ^= x & 1;
            x >>>= 1;
        }
        return parity;
    }

    /**
     * 利用清最低位 1 的特性（x & (x - 1)），跳过值为 0 的位
     * <p>
     * 时间复杂度：O(k)，k 为 1 的个数
     */
    private static short solThree(long x) {
        short parity = 0;
        while (x > 0) {
            parity ^= 1;
            x &= x - 1;
        }
        return parity;
    }

    /**
     * 利用异或的特性（交换律和结合律），每次将高位部分与低位部分进行异或，结果存在低位，直到只剩 1 位
     * <p>
     * 时间复杂度：O(log2 n)，n 为 64
     */
    private static short solFour(long x) {
        x ^= x >>> 32;
        x ^= x >>> 16;
        x ^= x >>> 8;
        x ^= x >>> 4;
        x ^= x >>> 2;
        x ^= x >>> 1;
        return (short) (x & 1);
    }

    /**
     * 利用异或的特性（结合律），将 long 拆为 4 个 16 位数，分别计算它们的奇偶性，最后再对 4 个数的奇偶性进行异或
     * <p>
     * 时间复杂度：O(n / L)，n 为 64，L 为 16
     * <p>
     * 分治、缓存
     */
    private static short solFive(long x) {
        final int WORD_SIZE = 16;
        final int BIT_MASK = 0xFFFF;
        return (short) (PRE_COMPUTED_PARITIES[(int) ((x >>> 3 * WORD_SIZE) & BIT_MASK)]
                ^ PRE_COMPUTED_PARITIES[(int) ((x >>> 2 * WORD_SIZE) & BIT_MASK)]
                ^ PRE_COMPUTED_PARITIES[(int) ((x >>> WORD_SIZE) & BIT_MASK)]
                ^ PRE_COMPUTED_PARITIES[(int) (x & BIT_MASK)]);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "Parity.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
