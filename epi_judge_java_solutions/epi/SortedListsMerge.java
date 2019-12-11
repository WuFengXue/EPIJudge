package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 8.1 MERGE TWO SORTED LISTS
 * <p>
 * Consider two singly linked lists in which each node holds a number. Assume the lists are
 * sorted, i.e., numbers in the lists appear in ascending order within each list. The merge of
 * the two lists is a list consisting of the nodes of the two lists in which numbers appear in
 * ascending order. Merge is illustrated in Figure 8.3.
 * <p>
 * Write a program that takes two lists, assumed to be sorted, and returns their merge. The only
 * field your program can change in a node is its next field.
 * <p>
 * Hint: Two sorted arrays can be merged using two indices. For lists, take care when one
 * iterator reaches the end.
 */
public class SortedListsMerge {
    private static Comparator<ListNode<Integer>> sNodeComparator = new Comparator<ListNode<Integer>>() {
        @Override
        public int compare(ListNode<Integer> o1, ListNode<Integer> o2) {
            return o1.data - o2.data;
        }
    };

    @EpiTest(testDataFile = "sorted_lists_merge.tsv")
    //@include
    public static ListNode<Integer> mergeTwoSortedLists(ListNode<Integer> L1,
                                                        ListNode<Integer> L2) {
        return solTwo(L1, L2);
    }

    /**
     * 思路一：将所有节点添加到一个数组中，然后对数组进行排序，再逐个取出构建新的列表。
     * <p>
     * 时间复杂度：O((m + n) * log (m + n))
     * <p>
     * 空间复杂度：O(m + n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L1,
                                            ListNode<Integer> L2) {
        List<ListNode<Integer>> nodes = new ArrayList<>();
        ListNode<Integer> p1 = L1, p2 = L2;
        while (p1 != null) {
            nodes.add(p1);
            p1 = p1.next;
        }
        while (p2 != null) {
            nodes.add(p2);
            p2 = p2.next;
        }
        Collections.sort(nodes, sNodeComparator);

        // 创建一个占位节点，以减少判空场景
        ListNode<Integer> dummyHead = new ListNode<>(0, null);
        ListNode<Integer> current = dummyHead;
        for (int i = 0; i < nodes.size(); i++) {
            current.next = nodes.get(i);
            current = current.next;
        }
        return dummyHead.next;
    }

    /**
     * 思路二：创建一个占位的节点，然后依次将两个链表中较小的节点添加到新的链表
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L1,
                                            ListNode<Integer> L2) {
        // Creates a placeholder for the result.
        ListNode<Integer> dummyHead = new ListNode<>(0, null);
        ListNode<Integer> current = dummyHead;
        ListNode<Integer> p1 = L1, p2 = L2;
        while (p1 != null && p2 != null) {
            if (p1.data <= p2.data) {
                current.next = p1;
                p1 = p1.next;
            } else {
                current.next = p2;
                p2 = p2.next;
            }
            current = current.next;
        }
        // Appends the remaining nodes of pi or p2.
        current.next = (p1 != null) ? p1 : p2;
        return dummyHead.next;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SortedListsMerge.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
