package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 14.9 IMPLEMENT A FAST SORTING ALGORITHM FOR LISTS
 * <p>
 * Implement a routine which sorts lists efficiently. It should be a stable
 * sort, i.e., the relative positions of equal elements must remain unchanged.
 * <p>
 * Hint: In what respects are lists superior to arrays?
 */
public class SortList {
    @EpiTest(testDataFile = "sort_list.tsv")

    public static ListNode<Integer> stableSortList(ListNode<Integer> L) {
        return solFour(L);
    }


    /**
     * 思路四：将问题拆解成合并两个有序列表，将链表平均分为两个链表，然后再递归调用
     * <p>
     * 时间复杂度：O(log(n))
     * <p>
     * 空间复杂度：O(n * log(n))
     */
    private static ListNode<Integer> solFour(ListNode<Integer> L) {
        // Base cases: L is empty or a single node, nothing to do.
        if (L == null || L.next == null) {
            return L;
        }

        // Find the midpoint of L using a slow and a fast pointer.
        ListNode<Integer> preSlow = null, slow = L, fast = L;
        while (fast != null && fast.next != null) {
            preSlow = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        // Splits the list into two equal-sized lists.
        preSlow.next = null;
        return mergeTwoSortedList(solFour(L), solFour(slow));
    }

    private static ListNode<Integer> mergeTwoSortedList(ListNode<Integer> n1,
                                                        ListNode<Integer> n2) {
        ListNode<Integer> dummyHead = new ListNode<>(0, null);
        ListNode<Integer> tail = dummyHead;
        while (n1 != null && n2 != null) {
            if (n1.data < n2.data) {
                tail.next = n1;
                n1 = n1.next;
            } else {
                tail.next = n2;
                n2 = n2.next;
            }
            tail = tail.next;
        }
        tail.next = (n1 != null) ? n1 : n2;
        return dummyHead.next;
    }

    /**
     * 思路三：在思路二的基础上进行优化，将链表分为两个部分，有序部分和无序部分，
     * 遍历链表，当发现有节点不满足有序特性时，将其移到有序部分（每次都重新从
     * 头结点开始查找插入点）
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solThree(ListNode<Integer> L) {
        ListNode<Integer> dummyHead = new ListNode<>(0, L);
        ListNode<Integer> iter = dummyHead.next;
        // The sublist consisting of nodes up to and including iter is sorted in
        // increasing order. We need to ensure that after we move to iter.next
        // this property continues to hold. We do this by swapping iter.next
        // with its predecessors in the list till it’s in the right place.
        while (iter != null && iter.next != null) {
            if (iter.data > iter.next.data) {
                ListNode<Integer> target = iter.next, preTarget = dummyHead;
                while (preTarget.next.data < target.data) {
                    preTarget = preTarget.next;
                }
                iter.next = target.next;
                target.next = preTarget.next;
                preTarget.next = target;
            } else {
                iter = iter.next;
            }
        }
        return dummyHead.next;
    }

    /**
     * 思路二：新建一个链表，遍历旧的链表找到最小的元素，将其移到新链表中，
     * 重复上述操作直到旧的链表为空
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static ListNode<Integer> solTwo(ListNode<Integer> L) {
        ListNode<Integer> dummyHead = new ListNode(0, null);
        ListNode<Integer> newTail = dummyHead;
        ListNode<Integer> dummyHeadOld = new ListNode(0, L);
        ListNode<Integer> preL, preMin, min;
        while (dummyHeadOld.next != null) {
            preL = dummyHeadOld;
            L = preL.next;
            preMin = preL;
            min = preMin.next;
            while (L != null) {
                if (L.data < min.data) {
                    preMin = preL;
                    min = L;
                }
                preL = L;
                L = L.next;
            }
            preMin.next = min.next;
            min.next = null;
            newTail.next = min;
            newTail = newTail.next;
        }
        return dummyHead.next;
    }

    /**
     * 思路一：先遍历链表，将全部节点添加到一个集合中，然后对集合中的节点进行排序，
     * 最后再重构链表
     * <p>
     * 时间复杂度：O(n * log(n))
     * <p>
     * 空间复杂度：O(n)
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L) {
        List<ListNode<Integer>> nodes = new ArrayList<>();
        while (L != null) {
            nodes.add(L);
            L = L.next;
        }
        Collections.sort(nodes, new Comparator<ListNode<Integer>>() {
            @Override
            public int compare(ListNode<Integer> n1, ListNode<Integer> n2) {
                return Integer.compare(n1.data, n2.data);
            }
        });
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).next = (i + 1 < nodes.size()) ? nodes.get(i + 1) : null;
        }
        return !nodes.isEmpty() ? nodes.get(0) : null;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SortList.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
