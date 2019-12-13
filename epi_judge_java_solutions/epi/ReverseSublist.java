package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 8.2 REVERSE A SINGLE SUBLIST
 * <p>
 * This problem is concerned with reversing a sublist within a list. See Figure 8.4 for an
 * example of sublist reversal.
 * <p>
 * Write a program which takes a singly linked list L and two integers s and f as arguments,
 * and reverses the order of the nodes from the sth node to fth node, inclusive. The numbering
 * begins at 1, i.e., the head node is the first node. Do not allocate additional nodes.
 * <p>
 * Hint: Focus on the successor fields which have to be updated.
 */
public class ReverseSublist {
    @EpiTest(testDataFile = "reverse_sublist.tsv")

    public static ListNode<Integer> reverseSublist(ListNode<Integer> L, int start,
                                                   int finish) {
        return solTwo(L, start, finish);
    }

    /**
     * 思路一：先遍历整个链表，记录 start - 1 节点和 finish + 1 节点，并将 [start, finish] 中的节点
     * 添加到集合中，之后再反转 [start, finish] 中的节点
     * <p>
     * 时间复杂度：O(n + (finish - start))
     * <p>
     * 空间复杂度：O(finish - start)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L, int start, int finish) {
        if (start == finish) {
            return L;
        }

        ListNode<Integer> dummyHead = new ListNode<Integer>(0, L);
        ListNode<Integer> p = L;
        // start - 1 节点
        ListNode<Integer> preStart = dummyHead;
        // finish + 1 节点
        ListNode<Integer> nextFinish = null;
        List<ListNode<Integer>> nodes = new ArrayList<>();
        int pos = 1;
        while (p != null) {
            if (pos == start - 1) {
                preStart = p;
            }
            if (pos == finish + 1) {
                nextFinish = p;
            }
            if (start <= pos && pos <= finish) {
                nodes.add(p);
            }
            p = p.next;
            pos++;
        }
        // 反转 [start, finish] 节点
        ListNode<Integer> current = preStart;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            current.next = nodes.get(i);
            current = current.next;
        }
        if (current != null) {
            current.next = nextFinish;
        }
        return dummyHead.next;
    }

    /**
     * 思路二：先遍历链表找到 start - 1 （记为 preStart）节点和 start 节点，再逐个将 start 后面的
     * 节点添加到 preStart 节点的后面，遇到 finish 节点结束
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L, int start, int finish) {
        // No need to reverse since start == finish.
        if (start == finish) {
            return L;
        }

        ListNode<Integer> dummyHead = new ListNode<>(0, L);
        ListNode<Integer> p = L;
        ListNode<Integer> preStart = dummyHead;
        int pos = 1;
        while (p != null && pos < start) {
            if (pos == start - 1) {
                preStart = p;
            }
            p = p.next;
            pos++;
        }

        // Reverse sublist.
        ListNode<Integer> startNode = preStart.next;
        while (startNode != null && startNode.next != null && pos++ < finish) {
            ListNode<Integer> temp = startNode.next;
            startNode.next = temp.next;
            temp.next = preStart.next;
            preStart.next = temp;
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ReverseSublist.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
