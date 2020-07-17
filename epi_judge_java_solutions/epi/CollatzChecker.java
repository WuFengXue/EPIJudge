package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.HashSet;
import java.util.Set;

/**
 * 13.13 TEST THE COLLATZ CONJECTURE
 * <p>
 * The Collatz conjecture is the following: Take any natural number. If it is odd,
 * triple it and add one; if it is even, halve it. Repeat the process indefinitely.
 * No matter what number you begin with, you will eventually arrive at 1.
 * <p>
 * As an example, if we start with 11 we get the sequence 11,34,17,52,26,13,40,20,
 * 10,5,16,8,4,2,1. Despite intense efforts, the Collatz conjecture has not been
 * proved or disproved.
 * <p>
 * Suppose you were given the task of checking the Collatz conjecture for the first
 * billion integers. A direct approach would be to compute the convergence sequence
 * for each number in this set.
 * <p>
 * Test the Collatz conjecture for the first n positive integers.
 * <p>
 * Hint: How would you efficiently check the conjecture for n assuming it holds for
 * all m < n?
 */
public class CollatzChecker {
    @EpiTest(testDataFile = "collatz_checker.tsv")

    public static boolean testCollatzConjecture(int n) {
        return solTwo(n);
    }

    /**
     * 思路二：迭代方式实现。
     * <p>
     * 1、用集合缓存已校验过的数字
     * <p>
     * 2、只缓存奇数
     * <p>
     * 3、需考虑整形溢出的场景，10亿 * 3 会超过整形的上限，所以采用长整型
     * <p>
     * 4、在校验过程，如果数字小于当前校验的数，则直接判定校验通过（从小到大校验）
     * <p>
     * 5、用另一个集合缓存单次校验过程的数字，以快速判定校验失败（否则要等堆栈溢出）
     * <p>
     * 6、如果乘法或除法的成本比较高，可以改用位移实现（未实现）
     * <p>
     * 7、可以通过多线程方式加速校验速度（未实现）
     * <p>
     * 时间复杂度：至少 O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solTwo(int n) {
        // Stores odd numbers already tested to converge to 1.
        Set<Long> verifiedNumbers = new HashSet<>();
        verifiedNumbers.add(Long.valueOf(1));
        // Starts from 3, since hypothesis holds trivially for 1 and 2.
        for (int i = 3; i <= n; i++) {
            Set<Long> sequence = new HashSet<>();
            long testI = i;
            while (testI >= i) {
                if (!sequence.add(testI)) {
                    // We previously encountered testI, so the Collatz sequence
                    // has fallen into a loop. This disproves the hypothesis, so
                    // we short-circuit, returning false.
                    return false;
                }

                // Even number, halve it.
                if (testI % 2 == 0) {
                    testI /= 2;
                }
                // Odd number
                else {
                    if (!verifiedNumbers.add(testI)) {
                        // testI has already been verified to converge to 1.
                        break;
                    } else {
                        // Multiply by 3 and add 1.
                        testI = testI * 3 + 1;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 思路一：递归方式，校验成功会正常遍历结束，校验失败会出现堆栈溢出。
     * <p>
     * 1、使用集合缓存已校验过的数字
     * <p>
     * 2、只缓存奇数
     * <p>
     * 3、需考虑整形溢出的场景，10亿 * 3 会超过整形的上限，所以采用长整型
     * <p>
     * 时间复杂度：预测至少 O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solOne(int n) {
        Set<Long> verifiedNumbers = new HashSet<>();
        verifiedNumbers.add(Long.valueOf(1));
        for (long i = 2; i <= n; i++) {
            if (!check(i, verifiedNumbers)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "CollatzChecker.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static boolean check(long num, Set<Long> verifiedNumbers) {
        if (num % 2 == 0) {
            return check(num / 2, verifiedNumbers);
        } else {
            if (!verifiedNumbers.add(num)) {
                return true;
            } else {
                return check(num * 3 + 1, verifiedNumbers);
            }
        }
    }
}
