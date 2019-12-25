package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 8.9 IMPLEMENT CYCLIC RIGHT SHIFT FOR SINGLY LINKED LISTS
 * <p>
 * This problem is concerned with performing a cyclic right shift on a list.
 * <p>
 * Write a program that takes as input a singly linked list and a non-negative
 * integer k, and returns the list cyclically shifted to the right by k. See
 * Figure 8.9 for an example of a cyclic right shift.
 * <p>
 * Hint: How does this problem differ from rotating an array?
 */
public class ListCyclicRightShift {
    @EpiTest(testDataFile = "list_cyclic_right_shift.tsv")

    public static ListNode<Integer> cyclicallyRightShiftList(ListNode<Integer> L,
                                                             int k) {
        return solThree(L, k);
    }

    /**
     * 思路一：先将链表的全部元素添加到列表，再重构链表：<br>
     * 1、尾节点的 next 设为头节点<br>
     * 2、倒数第 k + 1 个节点为新的尾节点，将其 next 设为 null<br>
     * 3、倒数第 k 个节点为新的头节点，返回该节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L, int k) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        List<ListNode<Integer>> nodes = new ArrayList<>();
        while (L != null) {
            nodes.add(L);
            L = L.next;
        }
        int size = Math.max(1, nodes.size());
        k %= size;
        if (k == 0 || size <= 1) {
            return dummyHead.next;
        }

        nodes.get(size - 1).next = nodes.get(0);
        nodes.get(size - k - 1).next = null;
        return nodes.get(size - k);
    }

    /**
     * 思路二：每次移动一个节点，移动 k 次
     * <p>
     * 时间复杂度：O(k * n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L, int k) {
        int size = size(L);
        k %= Math.max(1, size);
        if (k == 0 || size < 2) {
            return L;
        }

        while (k-- > 0) {
            L = cyclicallyRightShiftOneNode(L);
        }
        return L;
    }

    private static int size(ListNode<Integer> L) {
        int size = 0;
        while (L != null) {
            size++;
            L = L.next;
        }
        return size;
    }

    private static ListNode<Integer> cyclicallyRightShiftOneNode(ListNode<Integer> L) {
        if (L == null || L.next == null) {
            return L;
        }

        ListNode<Integer> newTail = L;
        while (newTail != null
                && newTail.next != null
                && newTail.next.next != null) {
            newTail = newTail.next;
        }
        ListNode<Integer> newHead = newTail.next;
        newHead.next = L;
        newTail.next = null;
        return newHead;
    }

    /**
     * 思路三：先遍历链表取得尾节点，然后将尾节点的 next 设为头节点，形成环，
     * 然后从尾节点移动 n - k 个节点（即倒数第 k + 1 个节点），其为新的尾节点，
     * 它的下一个节点（即倒数第 k 个节点）为新的头节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solThree(ListNode<Integer> L, int k) {
        // Computes the length of L and the tail.
        ListNode<Integer> tail = L;
        int size = 1;
        while (tail != null
                && tail.next != null) {
            size++;
            tail = tail.next;
        }
        k %= size;
        if (k == 0 || size < 2) {
            return L;
        }

        // Makes a cycle by connecting the tail to the head.
        tail.next = L;
        int stepsToNewHead = size - k;
        ListNode<Integer> newTail = tail;
        while (stepsToNewHead-- > 0) {
            newTail = newTail.next;
        }
        ListNode<Integer> newHead = newTail.next;
        newTail.next = null;
        return newHead;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ListCyclicRightShift.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
