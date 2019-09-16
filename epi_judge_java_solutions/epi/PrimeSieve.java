package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 6.8 ENUMERATE ALL PRIMES TO n
 * <p>
 * A natural number is called a prime if it is bigger than 1 and has no divisors other
 * than 1 and itself.
 * <p>
 * Write a program that takes an integer argument and returns all the primes between 1
 * and that integer. For example,if the input is 18, you should return(2,3,5,7,11,13,17).
 * <p>
 * Hint: Exclude the multiples of primes.
 */
public class PrimeSieve {
    @EpiTest(testDataFile = "prime_sieve.tsv")
    // Given n, return all primes up to and including n.
    public static List<Integer> generatePrimes(int n) {
        return solSieveThree(n);
    }

    /**
     * 试除法-思路一：根据质数的定义，除以 2 到 x - 1 之间的全部数字
     */
    private static List<Integer> solDivOne(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean isPrime = true;
        for (int i = 3; i <= n; i++) {
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
                isPrime = true;
            }
            if (isPrime) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * 试除法-思路二：除以 2 到 x / 2 之间的全部数字
     */
    private static List<Integer> solDivTwo(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean isPrime = true;
        for (int i = 3; i <= n; i++) {
            final int size = (int) Math.floor(0.5 * i) + 1;
            for (int j = 2; j < size; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
                isPrime = true;
            }
            if (isPrime) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * 试除法-思路三：只考虑奇数（2 把全部偶数都过滤掉了）
     */
    private static List<Integer> solDivThree(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        final int size = (int) Math.floor(0.5 * (n - 3)) + 1;
        boolean isPrime = true;
        for (int i = 0; i < size; i++) {
            int p = 2 * i + 3;
            final int pSize = (int) Math.floor(0.5 * p) + 1;
            for (int j = 2; j < pSize; j++) {
                if (p % j == 0) {
                    isPrime = false;
                    break;
                }
                isPrime = true;
            }
            if (isPrime) {
                primes.add(p);
            }
        }
        return primes;
    }

    /**
     * 试除法-思路四：除以 2 到 sqrt(x) 之间的全部数字
     */
    private static List<Integer> solDivFour(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean isPrime = true;
        for (int i = 3; i <= n; i++) {
            final int size = (int) Math.sqrt(i) + 1;
            for (int j = 2; j < size; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
                isPrime = true;
            }
            if (isPrime) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * 试除法-思路五：只考虑奇数，且除以 2 到 sqrt(x) 之间的全部数字
     */
    private static List<Integer> solDivFive(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        final int size = (int) Math.floor(0.5 * (n - 3)) + 1;
        boolean isPrime = true;
        for (int i = 0; i < size; i++) {
            int p = 2 * i + 3;
            final int pSize = (int) Math.sqrt(p) + 1;
            for (int j = 2; j < pSize; j++) {
                if (p % j == 0) {
                    isPrime = false;
                    break;
                }
                isPrime = true;
            }
            if (isPrime) {
                primes.add(p);
            }
        }
        return primes;
    }

    /**
     * 筛法-思路一：逐个标记筛除
     */
    private static List<Integer> solSieveOne(int n) {
        List<Integer> primes = new ArrayList<>();
        // isPrime.get(p) represents if p is prime or not. Initially, set each
        // to true, excepting ® and 1. Then use sieving to eliminate nonprimes.
        List<Boolean> isPrimes = new ArrayList<>(Collections.nCopies(n + 1, true));
        isPrimes.set(0, false);
        isPrimes.set(1, false);
        for (int i = 0; i <= n; i++) {
            if (isPrimes.get(i)) {
                primes.add(i);
                // Sieve p’s multiples.
                for (int j = 2 * i; j <= n; j += i) {
                    isPrimes.set(j, false);
                }
            }
        }
        return primes;
    }

    /**
     * 筛法-思路二：筛选的时候，从 p ^ 2 开始
     * <p>
     * 说明：考虑到溢出的情况，循环体中的类型必须为长整型
     */
    private static List<Integer> solSieveTwo(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        List<Boolean> isPrimes = new ArrayList<>(Collections.nCopies(n + 1, true));
        for (long i = 2; i <= n; i++) {
            if (isPrimes.get((int) i)) {
                primes.add((int) i);
                for (long j = i * i; j <= n; j += i) {
                    isPrimes.set((int) j, false);
                }
            }
        }
        return primes;
    }

    /**
     * 筛法-思路三：只考虑奇数，且筛选的时候，从 p ^ 2 开始
     * <p>
     * 说明：考虑到溢出的情况，循环体中的类型必须为长整型
     */
    private static List<Integer> solSieveThree(int n) {
        if (n < 2) {
            return Collections.emptyList();
        }
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        final int size = (int) Math.floor(0.5 * (n - 3)) + 1;
        // isPrime.get(i) represents whether (2i + 3) is prime or not.
        // Initially, set each to true. Then use sieving to eliminate nonprimes.
        List<Boolean> isPrimes = new ArrayList<>(Collections.nCopies(size, true));
        for (long i = 0; i < size; i++) {
            if (isPrimes.get((int) i)) {
                long p = 2 * i + 3;
                primes.add((int) p);
                // Sieving from pA2, whose value is (4iA2 + 12i + 9). The index of this
                // value in isPrime is (2iA2 + 6i + 3) because isPrime.get(i) represents
                //2i + 3.
                //
                // Note that we need to use long type for j because p ^ 2 might overflow .
                for (long j = 2 * i * i + 6 * i + 3; j < size; j += p) {
                    isPrimes.set((int) j, false);
                }
            }
        }
        return primes;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PrimeSieve.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
