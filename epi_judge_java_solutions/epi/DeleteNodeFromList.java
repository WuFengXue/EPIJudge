package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

/**
 * 8.6 DELETE A NODE FROM A SINGLY LINKED LIST
 * <p>
 * Given a node in a singly linked list, deleting it in 0(1) time appears impossible
 * because its predecessor's next field has to be updated. Surprisingly, it can be done
 * with one small caveat —— the node to delete cannot be the last one in the list and it
 * is easy to copy the value part of a node.
 * <p>
 * Write a program which deletes a node in a singly linked list. The input node is
 * guaranteed not to be the tail node.
 * <p>
 * Hint: Instead of deleting the node, can you delete its successor and still achieve the
 * desired configuration?
 */
public class DeleteNodeFromList {

    // Assumes nodeToDelete is not tail.
    public static void deletionFromList(ListNode<Integer> nodeToDelete) {
        solTwo(nodeToDelete);
        return;
    }

    /**
     * 思路一：将后续节点的值逐个赋值到前一个节点，最后再移除尾节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solOne(ListNode<Integer> nodeToDelete) {
        while (nodeToDelete != null && nodeToDelete.next != null) {
            nodeToDelete.data = nodeToDelete.next.data;
            // 当前位于倒数第二个节点，移除尾节点
            if (nodeToDelete.next.next == null) {
                nodeToDelete.next = null;
                break;
            }
            nodeToDelete = nodeToDelete.next;
        }
    }

    /**
     * 思路二：将下个节点的值赋给当前节点，然后删除下个节点
     * <p>
     * 时间复杂度：O(1)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solTwo(ListNode<Integer> nodeToDelete) {
        nodeToDelete.data = nodeToDelete.next.data;
        nodeToDelete.next = nodeToDelete.next.next;
    }

    @EpiTest(testDataFile = "delete_node_from_list.tsv")
    public static ListNode<Integer> deleteListWrapper(TimedExecutor executor,
                                                      ListNode<Integer> head,
                                                      int nodeToDeleteIdx)
            throws Exception {
        ListNode<Integer> nodeToDelete = head;
        if (nodeToDelete == null)
            throw new RuntimeException("List is empty");
        while (nodeToDeleteIdx-- > 0) {
            if (nodeToDelete.next == null)
                throw new RuntimeException("Can't delete last node");
            nodeToDelete = nodeToDelete.next;
        }

        final ListNode<Integer> finalNodeToDelete = nodeToDelete;
        executor.run(() -> deletionFromList(finalNodeToDelete));

        return head;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DeleteNodeFromList.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
