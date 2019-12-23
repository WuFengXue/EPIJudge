package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 8.8 REMOVE DUPLICATES FROM A SORTED LIST
 * <p>
 * This problem is concerned with removing duplicates from a sorted list of integers.
 * See Figure 8.8 for an example.
 * <p>
 * Write a program that takes as input a singly linked list of integers in sorted
 * order, and removes duplicates from it. The list should be sorted.
 * <p>
 * Hint: Focus on the successor fields which have to be updated.
 */
public class RemoveDuplicatesFromSortedList {
    @EpiTest(testDataFile = "remove_duplicates_from_sorted_list.tsv")

    public static ListNode<Integer> removeDuplicates(ListNode<Integer> L) {
        return solTwo(L);
    }


    /**
     * 思路一：利用集合的元素唯一性过滤重复的元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        // ListNode 没有重写哈希方法，所以集合存储的是节点的 data 字段，节点用另外的列表存储
        Set<Integer> dataSet = new HashSet<>();
        List<ListNode<Integer>> nodes = new ArrayList<>();
        while (L != null) {
            if (dataSet.add(L.data)) {
                nodes.add(L);
            }
            L = L.next;
        }

        // 重新构造链表
        ListNode<Integer> current = dummyHead;
        for (ListNode<Integer> node : nodes) {
            node.next = null;
            current.next = node;
            current = current.next;
        }
        return dummyHead.next;
    }

    /**
     * 思路二：遍历链表，遇到重复的元素则从链表中删除，要删除时移动下一个不同的节点而不是当前节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L) {
        ListNode<Integer> iter = L;
        while (iter != null) {
            // Uses nextDistinct to find the next distinct value.
            ListNode<Integer> nextDistinct = iter.next;
            while (nextDistinct != null && nextDistinct.data == iter.data) {
                nextDistinct = nextDistinct.next;
            }
            iter.next = nextDistinct;
            iter = iter.next;
        }
        return L;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "RemoveDuplicatesFromSortedList.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
