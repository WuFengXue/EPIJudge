package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 15.7 ENUMERATE NUMBERS OF THE FORM a + b * sqrt(2)
 * Numbers of the form a + b where a and b are non-negative integers, and q is an integer
 * which is not the square of another integer, have special properties, e.g., they
 * are-closed under addition and multiplication. Some of the first few numbers of this form
 * are given in Figure 15.4.
 * <p>
 * Figure 15.4: Some points of the form a + b * sqrt(2). (For typographical reasons, this figure
 * does not include all numbers of the form a + b  * sqrt(2) between 0 and 2 + 2  * sqrt(2), e.g.,
 * 3 + 0  * sqrt(2), 4+  0  * sqrt(2), 0 + 3  * sqrt(2), 3 + 1  * sqrt(2) lie in the interval but are not included.)
 * <p>
 * Design an algorithm for efficiently computing the k smallest numbers of the form
 * a + b  * sqrt(2) for non-negative integers a and b.
 * <p>
 * Hint: Systematically enumerate points.
 */
public class ABSqrt2 implements Comparable<ABSqrt2> {
    private int a;
    private int b;
    private double val;

    public ABSqrt2(int a, int b) {
        this.a = a;
        this.b = b;
        this.val = a + b * Math.sqrt(2);
    }

    @EpiTest(testDataFile = "a_b_sqrt2.tsv")

    public static List<Double> generateFirstKABSqrt2(int k) {
        return solOne(k);
    }

    /**
     * 思路三：观察法，在思路二的基础上进一步优化，下一个候选数一定是在上一个数的基础上加 1 或加 sqrt(2)，
     * 用 i 记录加 1 的数的下标，用 j 记录加 sqrt(2) 的数的下标，每次取较小的数并移动下标，
     * 直到取满 k 个数
     * <p>
     * 时间复杂度：O(k)
     * <p>
     * 空间复杂度：O(k)
     */
    private static List<Double> solThree(int k) {
        // Will store the first k numbers of the form a + b * sqrt(2).
        List<ABSqrt2> result = new ArrayList<>();
        result.add(new ABSqrt2(0, 0));
        int i = 0, j = 0;
        while (result.size() < k) {
            ABSqrt2 resultIPlusOne = new ABSqrt2(result.get(i).a + 1, result.get(i).b);
            ABSqrt2 resultJPlusSqrt2 = new ABSqrt2(result.get(j).a, result.get(j).b + 1);
            result.add(resultIPlusOne.compareTo(resultJPlusSqrt2) < 0 ? resultIPlusOne : resultJPlusSqrt2);
            if (result.get(result.size() - 1).compareTo(resultIPlusOne) == 0) {
                i++;
            }
            if (result.get(result.size() - 1).compareTo(resultJPlusSqrt2) == 0) {
                j++;
            }
        }
        return toDoubleList(result);
    }

    /**
     * 思路二：观察法，从 0 + 0 * sqrt(2) 开始，下一个最小数一定在上一个数加 1 或加 sqrt(2)之间，
     * 用一个 SortedSet 存储候选数，每次取出最小的数，然后新加上两个候选数，直到取出 k 个数
     * <p>
     * 时间复杂度：O(k * log(k))
     * <p>
     * 空间复杂度：O(2 * k)
     */
    private static List<Double> solTwo(int k) {
        List<ABSqrt2> result = new ArrayList<>();
        SortedSet<ABSqrt2> candidates = new TreeSet<>();
        // Initial for 0 + 0 * sqrt(2).
        candidates.add(new ABSqrt2(0, 0));
        while (result.size() < k) {
            ABSqrt2 nextSmallest = candidates.first();
            result.add(nextSmallest);
            // Add the next two numbers derived from nextSmallest.
            candidates.add(new ABSqrt2(nextSmallest.a + 1, nextSmallest.b));
            candidates.add(new ABSqrt2(nextSmallest.a, nextSmallest.b + 1));
            candidates.remove(nextSmallest);
        }
        return toDoubleList(result);
    }

    /**
     * 思路一：将 0 <= a, b <= k -1 的所有组合进行排序，返回最小的 k 个数
     * <p>
     * 时间复杂度：O(k ^ 2 * log(k ^ 2))
     * <p>
     * 空间复杂度：O(k ^ 2)
     */
    private static List<Double> solOne(int k) {
        List<ABSqrt2> result = new ArrayList<>();
        SortedSet<ABSqrt2> candidates = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                candidates.add(new ABSqrt2(i, j));
            }
        }
        while (result.size() < k) {
            ABSqrt2 nextSmallest = candidates.first();
            result.add(nextSmallest);
            candidates.remove(nextSmallest);
        }
        return toDoubleList(result);
    }

    private static List<Double> toDoubleList(List<ABSqrt2> abSqrt2List) {
        List<Double> result = new ArrayList<>();
        for (ABSqrt2 abSqrt2 : abSqrt2List) {
            result.add(abSqrt2.val);
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ABSqrt2.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @Override
    public int compareTo(ABSqrt2 o) {
        return Double.compare(this.val, o.val);
    }
}
