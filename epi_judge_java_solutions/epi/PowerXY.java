package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 5.7 COMPUTE X ^ y
 * <p>
 * Write a program that takes a double x and an integer y and returns x ^ y. You can ignore
 * overflow and underflow.
 * <p>
 * Hint: Exploit mathematical properties of exponentiation.
 */
public class PowerXY {
    @EpiTest(testDataFile = "power_x_y.tsv")
    public static double power(double x, int y) {
        return solThree(x, y);
    }

    /**
     * 迭代版本
     * <p>
     * y 为奇数时，power = x * ((x ^ 2) ^ ((y - 1) / 2))
     * <p>
     * y 为偶数时，power = (x ^ 2) ^ (y / 2)
     * <p>
     * 时间复杂度：O(n), n = log2(y)
     */
    private static double solThree(double x, int y) {
        // 负值处理
        if (y < 0) {
            y = -y;
            x = 1.0 / x;
        }
        double result = 1;
        while (y > 0) {
            if ((y & 1) != 0) {
                result *= x;
            }
            x *= x;
            y >>>= 1;
        }
        return result;
    }

    /**
     * 递归版本
     * <p>
     * y 为奇数时，power = x * ((x ^ 2) ^ ((y - 1) / 2))
     * <p>
     * y 为偶数时，power = (x ^ 2) ^ (y / 2)
     * <p>
     * 时间复杂度：O(n), n = log2(y)
     */
    private static double solTwo(double x, int y) {
        // 负值处理
        if (y < 0) {
            y = -y;
            x = 1.0 / x;
        }
        if (y == 0) {
            return 1;
        } else if (y == 1) {
            return x;
        } else if ((y & 1) != 0) {
            return x * solTwo(x * x, (y - 1) / 2);
        } else {
            return solTwo(x * x, y / 2);
        }
    }

    /**
     * 普通解：执行 y 次乘以 x 操作
     * <p>
     * 时间复杂度：O(y)
     */
    private static double solOne(double x, int y) {
        // 负值处理
        if (y < 0) {
            y = -y;
            x = 1.0 / x;
        }
        double result = 1;
        for (int i = 0; i < y; i++) {
            result *= x;
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PowerXY.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
