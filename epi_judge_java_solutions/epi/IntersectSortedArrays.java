package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 14.1 COMPUTE THE INTERSECTION OF TWO SORTED ARRAYS
 * <p>
 * A natural implementation for a search engine is to retrieve documents that match the set of
 * words in a query by maintaining an inverted index. Each page is assigned an integer
 * identifier, its document-ID. An inverted index is a mapping that takes a word w and returns
 * a sorted array of page-ids which contain w—the sort order could be, for example, the page
 * rank in descending order. When a query contains multiple words, the search engine finds the
 * sorted array for each word and then computes the intersection of these arrays—these are the
 * pages containing all the words in the query. The most computationally intensive step of
 * doing this is finding the intersection of the sorted arrays.
 * <p>
 * Write a program which takes as input two sorted arrays, and returns a new array containing
 * elements that are present in both of the input arrays. The input arrays may have duplicate
 * entries, but the returned array should be free of duplicates. For example, the input is
 * (2,3,3,5,5,6,7,7,8,12} and (5,5,6,8,8,9,10,10), your output should be (5,6,8).
 * <p>
 * Hint: Solve the problem if the input array lengths differ by orders of magnitude. What if
 * they are approximately equal?
 */
public class IntersectSortedArrays {
    @EpiTest(testDataFile = "intersect_sorted_arrays.tsv")

    public static List<Integer> intersectTwoSortedArrays(List<Integer> A,
                                                         List<Integer> B) {
        return solFour(A, B);
    }

    /**
     * 思路四：使用两个指针分别指向两个集合的首个元素，如果两个指针指向的对象相等，则将其加到结果集合
     * 并同时移动两个指针，否则移动指向比较小的对象的指针
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：
     */
    private static List<Integer> solFour(List<Integer> A,
                                         List<Integer> B) {
        List<Integer> intersectionAB = new ArrayList<>();
        int i = 0, j = 0;
        while (i < A.size() && j < B.size()) {
            if (A.get(i).equals(B.get(j))) {
                if (!intersectionAB.contains(A.get(i))) {
                    intersectionAB.add(A.get(i));
                }
                i++;
                j++;
            } else if (A.get(i) < B.get(j)) {
                i++;
            } else { // A.get(i) > B.get(j)
                j++;
            }
        }
        return intersectionAB;
    }

    /**
     * 思路三：用一层循环，在循环内利用二分查找在另一个集合中搜索元素
     * <p>
     * 时间复杂度：O(m * log(n))
     * <p>
     * 空间复杂度：
     */
    private static List<Integer> solThree(List<Integer> A,
                                          List<Integer> B) {
        List<Integer> intersectionAB = new ArrayList<>();
        // 确保外层循环比较小
        if (A.size() > B.size()) {
            List<Integer> tmp = A;
            A = B;
            B = tmp;
        }
        for (int i = 0; i < A.size(); i++) {
            if ((i == 0 || A.get(i) != A.get(i - 1))
                    && Collections.binarySearch(B, A.get(i)) >= 0
                    && !intersectionAB.contains(A.get(i))) {
                intersectionAB.add(A.get(i));
            }
        }
        return intersectionAB;
    }

    /**
     * 思路二：用一层循环，在循环内利用集合的是否包含接口判断是否重叠元素
     * <p>
     * 时间复杂度：O(m * n)
     * <p>
     * 空间复杂度：
     */
    private static List<Integer> solTwo(List<Integer> A,
                                        List<Integer> B) {
        List<Integer> intersectionAB = new ArrayList<>();
        // 确保外层循环比较小
        if (A.size() > B.size()) {
            List<Integer> tmp = A;
            A = B;
            B = tmp;
        }
        for (Integer a : A) {
            if (B.contains(a) && !intersectionAB.contains(a)) {
                intersectionAB.add(a);
            }
        }
        return intersectionAB;
    }

    /**
     * 思路一：用两层循环逐个比较两个集合的元素
     * <p>
     * 时间复杂度：O(m * n)
     * <p>
     * 空间复杂度：
     */
    private static List<Integer> solOne(List<Integer> A,
                                        List<Integer> B) {
        List<Integer> intersectionAB = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            for (int j = 0; j < B.size(); j++) {
                if ((j == 0 || B.get(j) != B.get(j - 1))
                        && A.get(i).equals(B.get(j))
                        && !intersectionAB.contains(A.get(i))) {
                    intersectionAB.add(A.get(i));
                }
            }
        }
        return intersectionAB;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntersectSortedArrays.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
