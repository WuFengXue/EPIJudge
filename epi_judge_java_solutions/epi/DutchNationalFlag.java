package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 6.1 THE DUTCH NATIONAL FLAG PROBLEM
 * <p>
 * The quicksort algorithm for sorting arrays proceeds recursively—it selects an element
 * (the "pivot"), reorders the array to make all the elements less than or equal to the pivot
 * appear first, followed by all the elements greater than the pivot. The two subarrays
 * are then sorted recursively.
 * <p>
 * Implemented naively, quicksort has large run times and deep function call stacks
 * on arrays with many duplicates because the subarrays may differ greatly in size. One
 * solution is to reorder the array so that all elements less than the pivot appear first,
 * followed by elements equal to the pivot, followed by elements greater than the pivot.
 * This is known as Dutch national flag partitioning, because the Dutch national flag
 * consists of three horizontal bands, each in a different color.
 * <p>
 * As an example, assuming that black precedes white and white precedes gray,
 * Figure 6.1(b) on the facing page is a valid partitioning for Figure 6.1(a) on the next
 * page. If gray precedes black and black precedes white, Figure 6.1(c) on the facing
 * page is a valid partitioning for Figure 6.1(a) on the next page.
 * <p>
 * Generalizing, suppose A = (0,l,2,0,2,l,l),andthepivotindexis3. ThenA[3] = 0,
 * so (0,0,1,2,2,1,1) is a valid partitioning. For the same array, if the pivot index is 2,
 * then A[2] = 2, so the arrays (0,1,0,1,1, 2,2) as well as langle0,0,1,1,1, 2,2) are valid
 * partitionings.
 * <p>
 * Write a program that takes an array A and an index i into A, and rearranges the
 * elements such that all elements less than A[i] (the "pivot") appear first, followed by
 * elements equal to the pivot, followed by elements greater than the pivot.
 * <p>
 * Hint: Think about the partition step in quicksort.
 */
public class DutchNationalFlag {
    public static void dutchFlagPartition(int pivotIndex, List<Color> A) {
        solFour(pivotIndex, A);
        return;
    }

    /**
     * 思路一：创建一个新的数组，分三次操作，最后再将新数组的内容拷贝到原来的数组
     * <p>
     * 第一次，将小的元素添加到新数组
     * <p>
     * 第二次，将相等的元素添加到新数组
     * <p>
     * 第三次，将大的元素添加到新数组
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static void solOne(int pivotIndex, List<Color> A) {
        final Color PIVOT = A.get(pivotIndex);
        List<Color> newA = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i).ordinal() < PIVOT.ordinal()) {
                newA.add(A.get(i));
            }
        }
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i).ordinal() == PIVOT.ordinal()) {
                newA.add(A.get(i));
            }
        }
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i).ordinal() > PIVOT.ordinal()) {
                newA.add(A.get(i));
            }
        }
        Collections.copy(A, newA);
    }

    /**
     * 思路二：分两次操作，首先将小的元素排到数组前面，然后再将大的元素排在数组后面
     * <p>
     * 每次操作时，都会遍历一次，直到找到匹配的元素
     * <p>
     * 第二次操作时，如果遇到小的元素则提前结束
     * <p>
     * 时间复杂度：O(n^2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solTwo(int pivotIndex, List<Color> A) {
        final Color PIVOT = A.get(pivotIndex);
        // First pass: group elements smaller than pivot.
        for (int i = 0; i < A.size(); i++) {
            // Look for a smaller element.
            for (int j = i + 1; j < A.size(); j++) {
                if (A.get(j).ordinal() < PIVOT.ordinal()) {
                    Collections.swap(A, i, j);
                    break;
                }
            }
        }
        // Second pass: group elements larger than pivot.
        for (int i = A.size() - 1; i >= 0 && A.get(i).ordinal() >= PIVOT.ordinal(); i--) {
            // Look for a larger element. Stop when we reach an element less
            // than pivot, since first pass has moved them to the start of A.
            for (int j = i - 1; j >= 0 && A.get(j).ordinal() >= PIVOT.ordinal(); j--) {
                if (A.get(j).ordinal() > PIVOT.ordinal()) {
                    Collections.swap(A, i, j);
                    break;
                }
            }
        }
    }

    /**
     * 思路三：分两次操作，首先将小的元素排到数组前面，然后再将大的元素排在数组后面
     * <p>
     * 每次操作时，只在元素匹配时才做交换
     * <p>
     * 第二次操作时，如果遇到小的元素则提前结束
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solThree(int pivotIndex, List<Color> A) {
        final Color PIVOT = A.get(pivotIndex);
        // First pass: group elements smaller than pivot.
        int smaller = 0;
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i).ordinal() < PIVOT.ordinal()) {
                Collections.swap(A, i, smaller++);
            }
        }
        // First pass: group elements smaller than pivot.
        int larger = A.size() - 1;
        for (int i = A.size() - 1; i >= 0 && A.get(i).ordinal() >= PIVOT.ordinal(); i--) {
            if (A.get(i).ordinal() > PIVOT.ordinal()) {
                Collections.swap(A, i, larger--);
            }
        }
    }

    /**
     * 思路四：将数组分成三段，小的元素，相等的元素和大的元素
     * <p>
     * 遍历数组，然后将元素调换到目标段
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solFour(int pivotIndex, List<Color> A) {
        final Color PIVOT = A.get(pivotIndex);
        // Keep the following invariants during partitioning:
        // bottom group: A.subList(0, smaller).
        // middle group: A.subList (smaller , equal).
        // unclassified group: A.subList(equal , larger).
        // top group: A .subList (larger , A .size ()) .
        int smaller = 0, equal = 0, larger = A.size() - 1;
        // Keep iterating as long as there is an unclassified element.
        while (equal <= larger) {
            // A.get (equal) is the incoming unclassified element.
            if (A.get(equal).ordinal() < PIVOT.ordinal()) {
                Collections.swap(A, smaller++, equal++);
            } else if (A.get(equal).ordinal() == PIVOT.ordinal()) {
                equal++;
            } else { // A.get(equal) > pivot.
                Collections.swap(A, equal, larger--);
            }
        }
    }

    @EpiTest(testDataFile = "dutch_national_flag.tsv")
    public static void dutchFlagPartitionWrapper(TimedExecutor executor,
                                                 List<Integer> A, int pivotIdx)
            throws Exception {
        List<Color> colors = new ArrayList<>();
        int[] count = new int[3];

        Color[] C = Color.values();
        for (int i = 0; i < A.size(); i++) {
            count[A.get(i)]++;
            colors.add(C[A.get(i)]);
        }

        Color pivot = colors.get(pivotIdx);
        executor.run(() -> dutchFlagPartition(pivotIdx, colors));

        int i = 0;
        while (i < colors.size() && colors.get(i).ordinal() < pivot.ordinal()) {
            count[colors.get(i).ordinal()]--;
            ++i;
        }

        while (i < colors.size() && colors.get(i).ordinal() == pivot.ordinal()) {
            count[colors.get(i).ordinal()]--;
            ++i;
        }

        while (i < colors.size() && colors.get(i).ordinal() > pivot.ordinal()) {
            count[colors.get(i).ordinal()]--;
            ++i;
        }

        if (i != colors.size()) {
            throw new TestFailure("Not partitioned after " + Integer.toString(i) +
                    "th element");
        } else if (count[0] != 0 || count[1] != 0 || count[2] != 0) {
            throw new TestFailure("Some elements are missing from original array");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DutchNationalFlag.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public enum Color {RED, WHITE, BLUE}
}
