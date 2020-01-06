package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 8.13 ADD LIST-BASED INTEGERS
 * <p>
 * A singly linked list whose nodes contain digits can be viewed as an integer,
 * with the least significant digit coming first. Such a representation can be used
 * to represent unbounded integers. This problem is concerned with adding integers
 * represented in this fashion. See Figure 8.12 for an example.
 * <p>
 * Write a program which takes two singly linked lists of digits, and returns the
 * list corresponding to the sum of the integers they represent. The least
 * significant digit comes first.
 * <p>
 * Hint: First, solve the problem assuming no pair of corresponding digits sum to
 * more than 9.
 */
public class IntAsListAdd {
    @EpiTest(testDataFile = "int_as_list_add.tsv")

    public static ListNode<Integer> addTwoNumbers(ListNode<Integer> L1,
                                                  ListNode<Integer> L2) {
        return solOne(L1, L2);
    }

    /**
     * 思路一：模拟小学的加法运算，依次计算每一位的和，然后添加对应的节点到结果链表，
     * 需注意最后的进位处理
     * <p>
     * 时间复杂度：O(m + n)
     * <p>
     * 空间复杂度：O(max(m, n))
     */
    private static ListNode<Integer> solOne(ListNode<Integer> L1,
                                            ListNode<Integer> L2) {
        ListNode<Integer> dummyHead = new ListNode(0, null);
        ListNode<Integer> placeIt = dummyHead;
        int carry = 0;
        while (L1 != null || L2 != null || carry != 0) {
            int sum = carry;
            if (L1 != null) {
                sum += L1.data;
                L1 = L1.next;
            }
            if (L2 != null) {
                sum += L2.data;
                L2 = L2.next;
            }
            carry = sum / 10;
            placeIt.next = new ListNode(sum % 10, null);
            placeIt = placeIt.next;
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IntAsListAdd.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
