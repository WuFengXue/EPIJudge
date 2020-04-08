package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;

/**
 * 12.1 SEARCH A SORTED ARRAY FOR FIRST OCCURRENCE OF k
 * <p>
 * Binary search commonly asks for the index of any element of a sorted array
 * that is equal to a specified element. The following problem has a slight twist
 * on this.
 * <p>
 * Write a method that takes a sorted array and a key and returns the index of the
 * first occurrence of that key in the array. For example, when applied to the
 * array in Figure 12.1 your algorithm should return 3 if the given key is 108;
 * if it is 285, your algorithm should return 6.
 * <p>
 * Hint: What happens when every entry equals k? Don't stop when you first see k.
 */
public class SearchFirstKey {
    @EpiTest(testDataFile = "search_first_key.tsv")

    public static int searchFirstOfK(List<Integer> A, int k) {
        return solThree(A, k);
    }

    /**
     * 思路三：先用二分查找法找到一个元素，记录其下标，然后更新区间为 [lo, mid - 1]
     * 继续二分查找，如果有找到就更新为新的下标，重复上述步骤直到查找结束
     * <p>
     * 时间复杂度：O(log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solThree(List<Integer> A, int k) {
        int result = -1, lo = 0, hi = A.size() - 1;
        // A.subList(lo , hi + 1) is the candidate set.
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (A.get(mid) < k) {
                lo = mid + 1;
            } else if (A.get(mid) > k) {
                hi = mid - 1;
            } else {
                result = mid;
                hi = mid - 1;
            }
        }
        return result;
    }

    /**
     * 思路二：先用二分查找法找到一个元素，再反向查找第一个出现的元素
     * <p>
     * 时间复杂度：二分查找 O(log(n))，反向查找第一个时最多 O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solTwo(List<Integer> A, int k) {
        int result = -1, lo = 0, hi = A.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (A.get(mid) < k) {
                lo = mid + 1;
            } else if (A.get(mid) > k) {
                hi = mid - 1;
            } else {
                result = mid;
                break;
            }
        }
        while (result > 0 && A.get(result - 1).equals(A.get(result))) {
            result--;
        }
        return result;
    }

    /**
     * 思路一：从第一个元素开始查找
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(List<Integer> A, int k) {
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i) == k) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchFirstKey.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
