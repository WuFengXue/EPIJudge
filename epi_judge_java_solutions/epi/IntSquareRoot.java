package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 12.4 COMPUTE THE INTEGER SQUARE ROOT
 * <p>
 * Write a program which takes a non-negative integer and returns the
 * largest integer whose square is less than or equal to the given
 * integer. For example, if the input is 16, return 4; if the input is 300,
 * return 17, since 17 ^ 2 = 289 < 300 and 18 ^ 2 = 324 > 300.
 * <p>
 * Hint: Look out for a corner-case.
 */
public class IntSquareRoot {
    @EpiTest(testDataFile = "int_square_root.tsv")

    public static int squareRoot(int k) {
        return solTwo(k);
    }

    /**
     * 思路二：二分查找法，利用如下特性：
     * <p>
     * 1、如果 x ^ 2 <= k，那么小于 x 的元素都不是目标元素
     * <p>
     * 2、如果 x ^ 2 > k，那么大于或等于 x 的元素都不是目标元素
     * <p>
     * 时间复杂度：O(log(k))
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solTwo(int k) {
        long lo = 0, hi = k;
        // Candidate interval [lo, hi] where everything before lo has
        // square <= k, and everything after hi has square > k.
        while (lo <= hi) {
            long mid = (lo + hi) >>> 1;
            long midSquared = mid * mid;
            if (midSquared <= k) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return (int) (lo - 1);
    }

    /**
     * 思路一：从 0 开始遍历直到遇到平方值大于 k 的元素
     * <p>
     * 时间复杂度：O(k)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(int k) {
        long result = -1;
        for (long i = 0; i <= k; i++) {
            if (i * i > k) {
                break;
            }
            result = i;
        }
        return (int) result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntSquareRoot.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
