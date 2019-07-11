package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * TODO 更优的加法
 * <p>
 * 5.5 COMPUTE x * y WITHOUT ARITHMETICAL OPERATORS
 * <p>
 * Write a program that multiplies two nonnegative integers. The only operators you are allowed to use are
 * • assignment,
 * • the bitwise operators >, <<,|, &, ~, ^ and
 * • equality checks and Boolean combinations thereof.
 * You may use loops and functions that you write yourself. These constraints imply, for example, that you
 * cannot use increment or decrement, or test if x < y.
 * <p>
 * Hint: Add using bitwise operations; multiply using shift-and-add.
 */
public class PrimitiveMultiply {
    @EpiTest(testDataFile = "primitive_multiply.tsv")
    public static long multiply(long x, long y) {
        return solTwo(x, y);
    }

    /**
     * 利用乘法分配律：(a + b) * c = a * c + b * c，将 x 拆成 x 个 1 相加
     * <p>
     * 时间复杂度：O(x * n)，n = O(add)
     */
    private static long solOne(long x, long y) {
        long sum = 0;
        for (int i = 0; i < x; i++) {
            sum = add(sum, y);
        }
        return sum;
    }

    /**
     * 利用乘法分配律和进制乘法公式，以十进制为例：123 * 456 = 3 * 456 + 2 * 4560 + 1 * 45600;
     * <p>
     * 时间复杂度：O（m * n）, m = width(x), max(m) = Long.SIZE, n = O(add)
     */
    private static long solTwo(long x, long y) {
        long sum = 0;
        while (x != 0) {
            // Examines each bit of x.
            if ((x & 1L) != 0) {
                sum = add(sum, y);
            }
            x >>>= 1;
            y <<= 1;
        }
        return sum;
    }

    /**
     * 利用位操作实现加法
     * <p>
     * 时间复杂度：O(n), n = width(a), max(n) = Long.SIZE
     */
    private static long add(long a, long b) {
        long sum = 0;
        long carry = 0;
        int k = 0;
        while (a != 0 || b != 0) {
            long a1 = a & 1L;
            long b1 = b & 1L;
            sum |= (a1 ^ b1 ^ carry) << k;
            carry = (a1 & b1) | (a1 & carry) | (b1 & carry);
            k++;
            a >>>= 1;
            b >>>= 1;
        }
        return sum | (carry << k);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PrimitiveMultiply.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
