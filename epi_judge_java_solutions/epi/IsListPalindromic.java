package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 8.11 TEST WHETHER A SINGLY LINKED LIST IS PALINDROMIC
 * <p>
 * It is straightforward to check whether the sequence stored in an array is
 * a palindrome. However, if this sequence is stored as a singly linked list,
 * the problem of detecting palindromicity becomes more challenging. See Figure
 * 8.1 on Page 112 for an example of a palindromic singly linked list.
 * <p>
 * Write a program that tests whether a singly linked list is palindromic.
 * <p>
 * Hint: It's easy if you can traverse the list forwards and backwards
 * simultaneously.
 */
public class IsListPalindromic {
    private static int count = 0;

    @EpiTest(testDataFile = "is_list_palindromic.tsv")

    public static boolean isLinkedListAPalindrome(ListNode<Integer> L) {
        if (++count == 96) {
            System.out.println();
        }
        return solThree(L);
    }

    /**
     * 思路一：将链表的全部元素添加到一个列表中，然后依次比较列表的首尾元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solOne(ListNode<Integer> L) {
        List<ListNode<Integer>> nodes = new ArrayList<>();
        while (L != null) {
            nodes.add(L);
            L = L.next;
        }
        for (int i = 0, j = nodes.size() - 1; i < j; i++, j--) {
            // 不能直接比较 data，因其是包装类型
            if (!nodes.get(i).data.equals(nodes.get(j).data)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 思路二：将链表平分成两个子链表，然后第一个链表从头部开始，第二个链表从尾部开始（在获取
     * 对应节点时，每次都需从头节点移动过去），依次比较元素
     * <p>
     * 时间复杂度：O(n ^ 2)
     * <p>
     * 空间复杂度：O(1)
     */
    private static boolean solTwo(ListNode<Integer> L) {
        // 查找第二个子链表的头节点并计算子链表的长度
        ListNode<Integer> fast = L;
        ListNode<Integer> slow = L;
        int halfSize = 0;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            halfSize += 1;
        }

        // 比较两个子链表
        ListNode<Integer> firstHalfHead = L;
        // 链表长度为奇数时，需要移动一个节点才是第二个子链表的头部（跳过中间元素）
        ListNode<Integer> secondHalfHead = (fast == null ? slow : slow.next);
        for (int i = 0; i < halfSize; i++) {
            ListNode<Integer> firstHalfIt = advanceListByK(firstHalfHead, i);
            ListNode<Integer> secondHalfIt = advanceListByK(secondHalfHead, halfSize - 1 - i);
            if (!firstHalfIt.data.equals(secondHalfIt.data)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 思路三：将链表拆分成两个子链表，然后翻转第二个子链表，再依次比较两个子链表的元素
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static boolean solThree(ListNode<Integer> L) {
        // Finds the second half of L.
        ListNode<Integer> fast = L;
        ListNode<Integer> slow = L;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        // Compare the first half and the reversed second half lists.
        ListNode<Integer> firstHalfIt = L;
        ListNode<Integer> secondHalfIt = reverseList(slow);
        while (firstHalfIt != null && secondHalfIt != null) {
            if (!firstHalfIt.data.equals(secondHalfIt.data)) {
                return false;
            }
            firstHalfIt = firstHalfIt.next;
            secondHalfIt = secondHalfIt.next;
        }
        return true;
    }

    private static ListNode<Integer> reverseList(ListNode<Integer> L) {
        ListNode<Integer> dummyHead = new ListNode(0, L);
        while (L != null && L.next != null) {
            ListNode<Integer> nextNode = L.next;
            L.next = nextNode.next;
            nextNode.next = dummyHead.next;
            dummyHead.next = nextNode;
        }
        return dummyHead.next;
    }

    private static ListNode<Integer> advanceListByK(ListNode<Integer> L, int k) {
        while (L != null && k-- > 0) {
            L = L.next;
        }
        return L;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsListPalindromic.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
