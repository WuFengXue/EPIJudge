package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.List;

/**
 * 12.2 SEARCH A SORTED ARRAY FOR ENTRY EQUAL TO ITS INDEX
 * <p>
 * Design an efficient algorithm that takes a sorted array of distinct integers,
 * and returns an index i such that the element at index i equals i. For example,
 * when the input is (-2,0,2,3,6,7,9) your algorithm should return 2 or 3.
 * <p>
 * Hint: Reduce this problem to ordinary binary search.
 */
public class SearchEntryEqualToIndex {

    public static int searchEntryEqualToItsIndex(List<Integer> A) {
        return solTwo(A);
    }

    /**
     * 思路二：观察法，有序、唯一的数组具有如下特性：
     * <p>
     * 1、如果 A[i] < i，那么 i 左侧的元素都不满足
     * <p>
     * 2、如果 A[i] > i，那么 i 右侧的元素都不满足
     * <p>
     * 也可将问题做一层转换：转换数组，新数组的元素 = A[i] - i，然后查找值为 0 的元素
     * <p>
     * 时间复杂度：O(log(n))
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solTwo(List<Integer> A) {
        int lo = 0, hi = A.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int difference = A.get(mid) - mid;
            if (difference < 0) {
                lo = mid + 1;
            } else if (difference > 0) {
                hi = mid - 1;
            }
            // A.get(mid) == mid if and only if difference == 0.
            else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 思路一：遍历数组，找到第一个满足条件的元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static int solOne(List<Integer> A) {
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i) == i) {
                return i;
            }
        }
        return -1;
    }

    @EpiTest(testDataFile = "search_entry_equal_to_index.tsv")
    public static void searchEntryEqualToItsIndexWrapper(TimedExecutor executor,
                                                         List<Integer> A)
            throws Exception {
        int result = executor.run(() -> searchEntryEqualToItsIndex(A));

        if (result != -1) {
            if (A.get(result) != result) {
                throw new TestFailure("Entry does not equal to its index");
            }
        } else {
            for (int i = 0; i < A.size(); ++i) {
                if (A.get(i) == i) {
                    throw new TestFailure("There are entries which equal to its index");
                }
            }
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchEntryEqualToIndex.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
