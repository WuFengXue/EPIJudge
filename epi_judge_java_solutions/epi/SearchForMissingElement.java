package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 12.10 FIND THE DUPLICATE AND MISSING ELEMENTS
 * <p>
 * If an array contains n — 1 integers, each between 0 and n —1, inclusive,
 * and all numbers in the array are distinct, then it must be the case that
 * exactly one number between 0 and n-1 is absent.
 * <p>
 * We can determine the missing number in 0(n) time and 0(1) space by
 * computing the sum of the elements in the array. Since the sum of all the
 * numbers from 0 to n - 1, inclusive, is (n - 1) * n / 2, we can subtract
 * the sum of the numbers in the array from (n - 1) * n / 2 to get the
 * missing number.
 * <p>
 * For example, if the array is (5,3,0,1,2), then n = 6. We subtract
 * (5+3+0+1+2) = 11 from 5 * (6) / 2 = 15, and the result, 4, is the missing
 * number.
 * <p>
 * Similarly, if the array contains n + 1 integers, each between 0 and
 * n — 1, inclusive, with exactly one element appearing twice, the duplicated
 * integer will be equal to the sum of the elements of the array minus
 * (n - 1) * n / 2.
 * <p>
 * Alternatively, for the first problem, we can compute the missing number
 * by computing the XOR of all the integers from 0 to n- 1, inclusive, and
 * XORing that with the XOR of all the elements in the array. Every element
 * in the array, except for the missing element, cancels out with an integer
 * from the first set. Therefore, the resulting XOR equals the missing
 * element. The same approach works for the problem of finding the duplicated
 * element. For example, the array (5,3,0,1,2} represented in binary is
 * <(101)2, (011)2, (000)2, (001)2, (010)2>. The XOR of these entries is
 * (101)2. The XOR of all numbers from 0 to 5, inclusive, is (001)2. The XOR
 * of (101)2 and (001)2 is (100)2 = 4, which is the missing number.
 * <p>
 * We now turn to a related, though harder, problem.
 * <p>
 * You are given an array of n integers, each between 0 and n - 1, inclusive.
 * Exactly one element appears twice, implying that exactly one number
 * between 0 and n - 1 is missing from the array. How would you compute the
 * duplicate and missing numbers?
 * <p>
 * Hint: Consider performing multiple passes through the array.
 */
public class SearchForMissingElement {
    @EpiTest(testDataFile = "find_missing_and_duplicate.tsv")

    public static DuplicateAndMissing findDuplicateMissing(List<Integer> A) {
        return solFour(A);
    }

    /**
     * 思路四：观察法：
     * <p>
     * [0, n - 1] 全部元素 ^ 数组 A 全部元素 = 重复项 ^ 缺失项，记为 dupXorMiss
     * <p>
     * dupXorMiss 值为 1 的位，重复项和缺失项肯定不一样，此处取值为 1 的最低位，
     * 记为 differBit，遍历 [0, n - 1] 和 数组 A 的全部元素，对其中 differBit 位
     * 为 1 的全部元素进行异或，最终的结果一定是重复项或缺失项，记为 dupOrMiss
     * <p>
     * 遍历数组 A 的全部元素，与 dupOrMiss 进行比较，如果发现相等的项，则 dupOrMiss
     * 为重复项，否则其为缺失项
     * <p>
     * 将 dupOrMiss 与 dupXorMiss 进行异或，结果就是另一项
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static DuplicateAndMissing solFour(List<Integer> A) {
        int dupXorMiss = 0;
        for (int i = 0; i < A.size(); i++) {
            dupXorMiss ^= (i ^ A.get(i));
        }

        int differBit = dupXorMiss & ~(dupXorMiss - 1);
        int dupOrMiss = 0;
        for (int i = 0; i < A.size(); i++) {
            if ((i & differBit) != 0) {
                dupOrMiss ^= i;
            }
            if ((A.get(i) & differBit) != 0) {
                dupOrMiss ^= A.get(i);
            }
        }

        for (int a : A) {
            if (a == dupOrMiss) {
                return new DuplicateAndMissing(dupOrMiss, dupOrMiss ^ dupXorMiss);
            }
        }

        return new DuplicateAndMissing(dupOrMiss ^ dupXorMiss, dupOrMiss);
    }

    /**
     * 思路三：使用集合来鉴别重复项，之后利用公式计算出缺失项：
     * <p>
     * 缺失项 = [0, n - 1] 全部元素 ^ （数组 A 全部元素 ^ 重复项）
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static DuplicateAndMissing solThree(List<Integer> A) {
        Set<Integer> set = new HashSet<>();
        Integer duplicate = null;
        for (Integer a : A) {
            if (!set.add(a)) {
                duplicate = a;
                break;
            }
        }

        if (duplicate == null) {
            throw new IllegalArgumentException("No duplicate");
        }

        int nXorA = 0;
        for (int i = 0; i < A.size(); i++) {
            nXorA ^= (i ^ A.get(i));
        }
        int missing = nXorA ^ duplicate;
        return new DuplicateAndMissing(duplicate, missing);
    }

    /**
     * 思路二：使用集合来鉴别重复项，之后利用公式计算出缺失项：
     * <p>
     * 缺失项 = [0, n - 1] 全部元素之和 - （数组 A 全部元素之和 - 重复项）
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static DuplicateAndMissing solTwo(List<Integer> A) {
        Set<Integer> set = new HashSet<>();
        Integer duplicate = null;
        for (Integer a : A) {
            if (!set.add(a)) {
                duplicate = a;
                break;
            }
        }

        if (duplicate == null) {
            throw new IllegalArgumentException("No duplicate");
        }

        int nSum = 0, aSum = 0;
        for (int i = 0; i < A.size(); i++) {
            nSum += i;
            aSum += A.get(i);
        }
        int missing = nSum - (aSum - duplicate);
        return new DuplicateAndMissing(duplicate, missing);
    }

    /**
     * 思路一：使用一个集合来鉴别重复项和缺失项：
     * <p>
     * 遍历数组 A 的全部元素，依次将它们添加到集合中，添加接口返回 false 表明
     * 该元素为重复项（注意：发现重复元素后，需要继续遍历）
     * <p>
     * 依次将 [0, n - 1] 添加到上述集合中，添加接口返回 true 表明该元素为缺失项
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static DuplicateAndMissing solOne(List<Integer> A) {
        Set<Integer> set = new HashSet<>();
        Integer duplicate = null;
        for (Integer a : A) {
            if (!set.add(a)) {
                duplicate = a;
            }
        }

        if (duplicate == null) {
            throw new IllegalArgumentException("No duplicate");
        }

        int miss = 0;
        for (int i = 0; i < A.size(); i++) {
            if (set.add(i)) {
                miss = i;
                break;
            }
        }

        return new DuplicateAndMissing(duplicate, miss);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchForMissingElement.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    @EpiUserType(ctorParams = {Integer.class, Integer.class})

    public static class DuplicateAndMissing {
        public Integer duplicate;
        public Integer missing;

        public DuplicateAndMissing(Integer duplicate, Integer missing) {
            this.duplicate = duplicate;
            this.missing = missing;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DuplicateAndMissing that = (DuplicateAndMissing) o;

            if (!duplicate.equals(that.duplicate)) {
                return false;
            }
            return missing.equals(that.missing);
        }

        @Override
        public int hashCode() {
            int result = duplicate.hashCode();
            result = 31 * result + missing.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "duplicate: " + duplicate + ", missing: " + missing;
        }
    }
}
