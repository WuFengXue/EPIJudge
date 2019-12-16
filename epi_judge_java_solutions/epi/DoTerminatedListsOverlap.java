package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * 8.4 TEST FOR OVERLAPPING LISTS —— LISTS ARE CYCLE-FREE
 * <p>
 * Given two singly linked lists there may be list nodes that are common to both. (This may
 * not be a bug —— it may be desirable from the perspective of reducing memory footprint, as
 * in the flyweight pattern, or maintaining a canonical form.) For example, the lists in
 * Figure 8.6 overlap at Node 7.
 * <p>
 * Write a program that takes two cycle-free singly linked lists, and determines if there
 * exists a node that is common to both lists.
 * <p>
 * Hint: Solve the simple cases first.
 */
public class DoTerminatedListsOverlap {

    public static ListNode<Integer>
    overlappingNoCycleLists(ListNode<Integer> l0, ListNode<Integer> l1) {
        return solThree(l0, l1);
    }

    /**
     * 思路一：先将第一个链表的全部节点添加到集合中，再逐个将第二个链表的节点添加到集合，
     * 第一个重复的元素即为重叠部分的起始节点
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> l0, ListNode<Integer> l1) {
        Set<ListNode<Integer>> nodes = new HashSet<>();
        while (l0 != null) {
            nodes.add(l0);
            l0 = l0.next;
        }
        while (l1 != null) {
            if (!nodes.add(l1)) {
                return l1;
            }
            l1 = l1.next;
        }
        return null;
    }

    /**
     * 思路二：使用两层循环，每次从第一个链表取出一个元素，然后在第二个链表中搜索，搜索到的第一个
     * 元素即为重叠部分的起点
     * <p>
     * 时间复杂度：O(n ^ 2)
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> l0, ListNode<Integer> l1) {
        while (l0 != null) {
            ListNode<Integer> p1 = l1;
            while (p1 != null) {
                if (l0 == p1) {
                    return l0;
                }
                p1 = p1.next;
            }
            l0 = l0.next;
        }
        return null;
    }

    /**
     * 思路三：先分别计算两个链表的长度，然后移动长链表直到与短链表长度相同，之后比较两个链表的节点，
     * 如果不一样则同时移动两个链表，第一个相同的节点即为重叠部分的起点
     *
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solThree(ListNode<Integer> l0, ListNode<Integer> l1) {
        int l0Length = length(l0), l1Length = length(l1);
        // Advances the longer list to get equal length lists.
        if (l0Length > l1Length) {
            l0 = advanceListByKSteps(l0, l0Length - l1Length);
        } else {
            l1 = advanceListByKSteps(l1, l1Length - l0Length);
        }
        while (l0 != null && l1 != null && l0 != l1) {
            l0 = l0.next;
            l1 = l1.next;
        }
        // nullptr implies there is no overlap between LI and L2.
        return l0;
    }

    private static ListNode<Integer> advanceListByKSteps(ListNode<Integer> l, int k) {
        while (l != null && k-- > 0) {
            l = l.next;
        }
        return l;
    }

    private static int length(ListNode<Integer> l) {
        int length = 0;
        while (l != null) {
            length++;
            l = l.next;
        }
        return length;
    }

    @EpiTest(testDataFile = "do_terminated_lists_overlap.tsv")
    public static void
    overlappingNoCycleListsWrapper(TimedExecutor executor, ListNode<Integer> l0,
                                   ListNode<Integer> l1, ListNode<Integer> common)
            throws Exception {
        if (common != null) {
            if (l0 != null) {
                ListNode<Integer> i = l0;
                while (i.next != null) {
                    i = i.next;
                }
                i.next = common;
            } else {
                l0 = common;
            }

            if (l1 != null) {
                ListNode<Integer> i = l1;
                while (i.next != null) {
                    i = i.next;
                }
                i.next = common;
            } else {
                l1 = common;
            }
        }

        final ListNode<Integer> finalL0 = l0;
        final ListNode<Integer> finalL1 = l1;
        ListNode<Integer> result =
                executor.run(() -> overlappingNoCycleLists(finalL0, finalL1));

        if (result != common) {
            throw new TestFailure("Invalid result");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DoTerminatedListsOverlap.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
