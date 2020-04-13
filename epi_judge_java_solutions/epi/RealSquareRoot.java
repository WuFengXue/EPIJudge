package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 12.5 COMPUTE THE REAL SQUARE ROOT
 * <p>
 * Square root computations can be implemented using sophisticated numerical
 * techniques involving iterative methods and logarithms. However, if you were
 * asked to implement a square root function, you would not be expected to know
 * these techniques.
 * <p>
 * Implement a function which takes as input a floating point value and returns
 * its square root.
 * <p>
 * Hint: Iteratively compute a sequence of intervals, each contained in the
 * previous interval, that contain the result.
 */
public class RealSquareRoot {
    @EpiTest(testDataFile = "real_square_root.tsv")

    public static double squareRoot(double x) {
        return solOne(x);
    }

    /**
     * 思路一：二分查找法，利用如下特性：
     * <p>
     * 1、如果 a ^ 2 < x，那么小于 a 的元素肯定不是目标元素
     * <p>
     * 2、如果 a ^ 2 > x，那么大于 a 的元素肯定不是目标元素
     * <p>
     * 因为 x 是浮点数，所以上下边界不能直接取 [0, x]，否则可能会溢出，
     * 例如 1/4 的平方根为 1/2（大于 1/4）
     * <p>
     * 将其分为两种场景：
     * <p>
     * 1、x < 1.0 时，上下界取 [x, 1.0]
     * <p>
     * 2、x >= 1.0时，上下界取 [1.0, x]
     * <p>
     * 时间复杂度：O(log (x / s))，s 为公差（tolerance）（TODO 理解该计算公式）
     * <p>
     * 空间复杂度：O(1)
     */
    private static double solOne(double x) {
        // Decides the search range according to x’s value relative to 1.0.
        double lo, hi;
        if (x < 1.0) {
            lo = x;
            hi = 1.0;
        }
        // x >= 1.0.
        else {
            lo = 1.0;
            hi = x;
        }

        // Keeps searching as long as lo < hi, within tolerance.
        while (compare(lo, hi) < 0) {
            double mid = lo + 0.5 * (hi - lo);
            double midSquared = mid * mid;
            if (compare(midSquared, x) < 0) {
                lo = mid;
            } else if (compare(midSquared, x) > 0) {
                hi = mid;
            } else {
                return mid;
            }
        }
        return lo;
    }

    private static int compare(double a, double b) {
        final double EPSILON = 0.000001;
        // Uses normalization for precision problem.
        double diff = (a - b) / Math.max(Math.abs(a), Math.abs(b));
        return diff < -EPSILON
                ? -1
                : (diff > EPSILON ? 1 : 0);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RealSquareRoot.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
