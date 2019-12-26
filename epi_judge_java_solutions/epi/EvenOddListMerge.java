package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 8.10 IMPLEMENT EVEN-ODD MERGE
 * <p>
 * Consider a singly linked list whose nodes are numbered starting at 0. Define
 * the even-odd merge of the list to be the list consisting of the even-numbered
 * nodes followed by the odd-numbered nodes. The even-odd merge is illustrated
 * in Figure 8.10.
 * <p>
 * Write a program that computes the even-odd merge.
 * <p>
 * Hint: Use temporary additional storage.
 */
public class EvenOddListMerge {
    @EpiTest(testDataFile = "even_odd_list_merge.tsv")

    public static ListNode<Integer> evenOddMerge(ListNode<Integer> L) {
        return solThree(L);
    }

    /**
     * 思路一：将链表的全部元素添加到列表中，再重构链表，先添加偶数项再添加奇数项
     * <p>
     * 时间复杂度：O(2 * n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        List<ListNode<Integer>> nodes = new ArrayList<>();
        while (L != null) {
            nodes.add(L);
            L = L.next;
        }
        ListNode<Integer> current = dummyHead;
        for (int i = 0; i < nodes.size(); i += 2) {
            ListNode<Integer> node = nodes.get(i);
            node.next = null;
            current.next = node;
            current = current.next;
        }
        for (int i = 1; i < nodes.size(); i += 2) {
            ListNode<Integer> node = nodes.get(i);
            node.next = null;
            current.next = node;
            current = current.next;
        }
        return dummyHead.next;
    }

    /**
     * 思路二：先将链表拆分成奇偶两个链表，再将偶数链表的尾部指向奇数链表的头部
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L) {
        ListNode<Integer> evenDummyHead = new ListNode(0, null);
        ListNode<Integer> oddDummyHead = new ListNode(0, null);
        List<ListNode<Integer>> tails = Arrays.asList(evenDummyHead, oddDummyHead);
        int turn = 0;
        while (L != null) {
            tails.get(turn).next = L;
            tails.set(turn, tails.get(turn).next);
            turn ^= 1;
            L = L.next;
        }
        tails.get(1).next = null;
        tails.get(0).next = oddDummyHead.next;
        return evenDummyHead.next;
    }

    /**
     * 思路三：每次移动两个节点到下一个偶数节点，然后将其添加到当前偶数节点的后面，操作完成后
     * 更新当前偶数节点和迭代器
     * <p>
     * 时间复杂度：O(n / 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solThree(ListNode<Integer> L) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        ListNode<Integer> currentEven = dummyHead.next;
        ListNode<Integer> it = dummyHead.next;
        while (it != null
                && it.next != null
                && it.next.next != null) {
            ListNode<Integer> nextOdd = it.next;
            ListNode<Integer> nextEven = it.next.next;
            nextOdd.next = nextEven.next;
            nextEven.next = currentEven.next;
            currentEven.next = nextEven;
            currentEven = currentEven.next;
            it = nextOdd;
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "EvenOddListMerge.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
