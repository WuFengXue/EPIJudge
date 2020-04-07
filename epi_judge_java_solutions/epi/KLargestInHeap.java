package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiTestComparator;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;

/**
 * 11.6 COMPUTE THE k LARGEST ELEMENTS IN A MAX-HEAP
 * <p>
 * A heap contains limited information about the ordering of elements, so
 * unlike a sorted array or a balanced BST, naive algorithms for computing
 * the k largest elements have a time complexity that depends linearly on the
 * number of elements in the collection.
 * <p>
 * Given a max-heap, represented as an array A,design an algorithm that computes
 * the k largest elements stored in the max-heap. You cannot modify the heap.
 * For example, if the heap is the one shown in Figure11.1(a) on Page175, then
 * the array representation is (561,314,401,28,156,359,271,11,3), the four
 * largest elements are 561,314,401,and 359.
 */
public class KLargestInHeap {
    @EpiTestComparator
    public static BiPredicate<List<Integer>, List<Integer>> comp =
            (expected, result) -> {
                if (result == null) {
                    return false;
                }
                Collections.sort(expected);
                Collections.sort(result);
                return expected.equals(result);
            };

    @EpiTest(testDataFile = "k_largest_in_heap.tsv")

    public static List<Integer> kLargestInBinaryHeap(List<Integer> A, int k) {
        return solTwo(A, k);
    }

    /**
     * 思路二：利用大根堆部分排序的特性（父节点比子节点要大），首个元素肯定是最大值，
     * 将其添加到另一个大根堆，之后进行 k 次循环操作：
     * <p>
     * 1、从新的大根堆中取出一个元素，将其添加到结果集
     * <p>
     * 2、将其左、右子节点添加到新的大根堆
     * <p>
     * 3、重复上述操作
     * <p>
     * 时间复杂度：O(k * log(k))
     * <p>
     * 空间复杂度：O(k)
     */
    private static List<Integer> solTwo(List<Integer> A, int k) {
        if (A.isEmpty()) {
            return Collections.emptyList();
        }

        // Stores the (index, value)-pair in candidateMaxHeap. This heap is
        // ordered by the value field.
        PriorityQueue<HeapEntry> candidateMaxHeap
                = new PriorityQueue<>(Collections.reverseOrder());
        candidateMaxHeap.add(new HeapEntry(0, A.get(0)));
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < k && !candidateMaxHeap.isEmpty(); i++) {
            HeapEntry entry = candidateMaxHeap.remove();
            result.add(entry.value);

            int leftChildIdx = 2 * entry.index + 1;
            if (leftChildIdx < A.size()) {
                candidateMaxHeap.add(new HeapEntry(leftChildIdx,
                        A.get(leftChildIdx)));
            }
            int rightChildIdx = 2 * entry.index + 2;
            if (rightChildIdx < A.size()) {
                candidateMaxHeap.add(new HeapEntry(rightChildIdx,
                        A.get(rightChildIdx)));
            }
        }
        return result;
    }

    /**
     * 思路一：拷贝一份，对其进行降序排序，然后进行截取
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static List<Integer> solOne(List<Integer> A, int k) {
        List<Integer> aCopy = new ArrayList<>(A);
        Collections.sort(aCopy, Collections.reverseOrder());
        return aCopy.size() > k
                ? aCopy.subList(0, k)
                : aCopy;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "KLargestInHeap.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class HeapEntry implements Comparable<HeapEntry> {
        public Integer index;
        public Integer value;

        public HeapEntry(Integer index, Integer value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(HeapEntry o) {
            return Integer.compare(value, o.value);
        }
    }
}
