package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.6 COMPUTE x/y
 * <p>
 * Given two positive integers, compute their quotient, using only the addition, sub¬
 * traction, and shifting operators.
 * <p>
 * Hint: Relate x/y to (a - b)/y.
 */
public class PrimitiveDivide {

    @EpiTest(testDataFile = "primitive_divide.tsv")
    public static int divide(int x, int y) {
        return solThree(x, y);
    }

    /**
     * x 每次减 y，直到 x < y，减法操作的总次数即为商
     * <p>
     * 时间复杂度：O(x / y)
     */
    private static int solOne(int x, int y) {
        int quotient = 0;
        while (x >= y) {
            x -= y;
            quotient++;
        }
        return quotient;
    }

    /**
     * 将 x 展开（2 ^ a * y + ...），然后利用乘法分配律的推论, (a + b) / c = a / c + b / c，
     * 每次减去最高位的 2 ^ a * y（其商为 2 ^ a），直到 x < y，汇总后的商即为结果
     * <p>
     * 在计算最高位的 2 ^ a * y 时，每次都从最小值 y 开始查找
     * <p>
     * 时间复杂度：O(n ^ 2), n 为 x 的位数
     */
    private static int solTwo(int x, int y) {
        int quotient = 0;
        while (x >= y) {
            int power = 0;
            long yPower = y;
            while (yPower <= x) {
                yPower <<= 1;
                power++;
            }
            if (power > 0) {
                yPower >>>= 1;
                power--;
            }
            quotient += 1 << power;
            x -= yPower;
        }
        return quotient;
    }

    /**
     * 将 x 展开（2 ^ a * y + ...），然后利用乘法分配律的推论, (a + b) / c = a / c + b / c，
     * 每次减去最高位的 2 ^ a * y（其商为 2 ^ a），直到 x < y，汇总后的商即为结果
     * <p>
     * 在计算最高位的 2 ^ a * y 时，从最大值开始查找，利用次低位的指数肯定比最高位低的特性，无需每次重新计算
     * <p>
     * 时间复杂度：O(n), n 为 x 的位数
     */
    private static int solThree(int x, int y) {
        int quotient = 0;
        int power = 31;
        long yPower = ((long) y) << power;
        while (x >= y) {
            while (yPower > x) {
                yPower >>>= 1;
                power--;
            }
            quotient += 1 << power;
            x -= yPower;
        }
        return quotient;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PrimitiveDivide.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
