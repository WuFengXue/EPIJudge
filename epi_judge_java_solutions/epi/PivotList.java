package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 8.12 IMPLEMENT LIST PIVOTING
 * <p>
 * For any integer k, the pivot of a list of integers with respect to k is that list
 * with its nodes reordered so that all nodes containing keys less than k appear
 * before nodes containing k, and all nodes containing keys greater than k appear
 * after the nodes containing k. See Figure 8.11 for an example of pivoting.
 * <p>
 * Implement a function which takes as input a singly linked list and an integer k
 * and performs a pivot of the list with respect to k. The relative ordering of nodes
 * that appear before k, and after k, must remain unchanged; the same must hold for
 * nodes holding keys equal to k.
 * <p>
 * Hint: Form the three regions independently.
 */
public class PivotList {

    public static ListNode<Integer> listPivoting(ListNode<Integer> l, int x) {
        return solTwo(l, x);
    }

    /**
     * 思路一：遍历链表，将小于、等于和大于 x 的元素添加到对应的列表中，然后再重构链表
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> l, int x) {
        ListNode<Integer> dummyHead = new ListNode(0, null);
        List<ListNode<Integer>> lessNodes = new ArrayList();
        List<ListNode<Integer>> equalNodes = new ArrayList();
        List<ListNode<Integer>> greaterNodes = new ArrayList();
        while (l != null) {
            if (l.data < x) {
                lessNodes.add(l);
            } else if (l.data > x) {
                greaterNodes.add(l);
            } else {
                equalNodes.add(l);
            }
            l = l.next;
        }
        ListNode<Integer> it = dummyHead;
        for (ListNode<Integer> node : lessNodes) {
            node.next = null;
            it.next = node;
            it = it.next;
        }
        for (ListNode<Integer> node : equalNodes) {
            node.next = null;
            it.next = node;
            it = it.next;
        }
        for (ListNode<Integer> node : greaterNodes) {
            node.next = null;
            it.next = node;
            it = it.next;
        }
        return dummyHead.next;
    }

    /**
     * 思路二：遍历链表，将链表拆分成小于、等于和大于的三个子链表，再重构链表
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> l, int x) {
        ListNode<Integer> lessDummyHead = new ListNode(0, null);
        ListNode<Integer> equalDummyHead = new ListNode(0, null);
        ListNode<Integer> greaterDummyHead = new ListNode(0, null);
        ListNode<Integer> lessIt = lessDummyHead;
        ListNode<Integer> equalIt = equalDummyHead;
        ListNode<Integer> greaterIt = greaterDummyHead;
        // Populates the three lists.
        while (l != null) {
            if (l.data < x) {
                lessIt.next = l;
                lessIt = lessIt.next;
            } else if (l.data > x) {
                greaterIt.next = l;
                greaterIt = greaterIt.next;
            } else { // it.data == x.
                equalIt.next = l;
                equalIt = equalIt.next;
            }
            l = l.next;
        }
        // Combines the three lists.
        greaterIt.next = null;
        equalIt.next = greaterDummyHead.next;
        lessIt.next = equalDummyHead.next;
        return lessDummyHead.next;
    }

    public static List<Integer> linkedToList(ListNode<Integer> l) {
        List<Integer> v = new ArrayList<>();
        while (l != null) {
            v.add(l.data);
            l = l.next;
        }
        return v;
    }

    @EpiTest(testDataFile = "pivot_list.tsv")
    public static void listPivotingWrapper(TimedExecutor executor,
                                           ListNode<Integer> l, int x)
            throws Exception {
        List<Integer> original = linkedToList(l);

        final ListNode<Integer> finalL = l;
        l = executor.run(() -> listPivoting(finalL, x));

        List<Integer> pivoted = linkedToList(l);

        int mode = -1;
        for (Integer i : pivoted) {
            switch (mode) {
                case -1:
                    if (i == x) {
                        mode = 0;
                    } else if (i > x) {
                        mode = 1;
                    }
                    break;
                case 0:
                    if (i < x) {
                        throw new TestFailure("List is not pivoted");
                    } else if (i > x) {
                        mode = 1;
                    }
                    break;
                case 1:
                    if (i <= x) {
                        throw new TestFailure("List is not pivoted");
                    }
            }
        }

        Collections.sort(original);
        Collections.sort(pivoted);
        if (!original.equals(pivoted))
            throw new TestFailure("Result list contains different values");
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PivotList.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
