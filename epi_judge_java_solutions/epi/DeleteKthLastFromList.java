package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 8.7 REMOVE THE kTH LAST ELEMENT FROM A LIST
 * <p>
 * Without knowing the length of a linked list, it is not trivial to delete the kth
 * last element in a singly linked list.
 * <p>
 * Given a singly linked list and an integer k, write a program to remove the kth last
 * element from the list. Your algorithm cannot use more than a few words of storage,
 * regardless of the length of the list. In particular, you cannot assume that it is
 * possible to record the length of the list.
 * <p>
 * Hint: If you know the length of the list, can you find the kth last node using
 * two iterators?
 */
public class DeleteKthLastFromList {
    @EpiTest(testDataFile = "delete_kth_last_from_list.tsv")

    // Assumes L has at least k nodes, deletes the k-th last node in L.
    public static ListNode<Integer> removeKthLast(ListNode<Integer> L, int k) {
        return solTwo(L, k);
    }

    /**
     * 思路一：先遍历一次链表计算出链表的长度，然后找到倒数第 k + 1 个节点，最后移除它后面的节点
     * <p>
     * 时间复杂度：O(2n - k)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L, int k) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        int size = 0;
        while (L != null) {
            size++;
            L = L.next;
        }

        L = dummyHead;
        while (size-- > k) {
            L = L.next;
        }
        L.next = L.next.next;
        return dummyHead.next;
    }

    /**
     * 思路二：使用两个节点，使其相距 k 个节点，然后同时移动两个节点直到尾部，此时左侧的节点为
     * 倒数第 k + 1 个节点，之后移除它后面的节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L, int k) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        ListNode<Integer> first = dummyHead.next;
        while (k-- > 0) {
            first = first.next;
        }

        ListNode<Integer> second = dummyHead;
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        // second points to the (k + 1)-th last node, deletes its successor.
        second.next = second.next.next;
        return dummyHead.next;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DeleteKthLastFromList.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
