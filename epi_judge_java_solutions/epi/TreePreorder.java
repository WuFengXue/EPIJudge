package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 10.8 IMPLEMENT A PREORDER TRAVERSAL WITHOUT RECURSION
 * <p>
 * This problem is concerned with traversing nodes in a binary tree in
 * preorder fashion. See Page 151 for details and examples of these
 * traversals. Generally speaking, a traversal computation is easy to
 * implement if recursion is allowed.
 * <p>
 * Write a program which takes as input a binary tree and performs a
 * preorder traversal of the tree. Do not use recursion. Nodes do not
 * contain parent references.
 */
public class TreePreorder {
    @EpiTest(testDataFile = "tree_preorder.tsv")

    public static List<Integer> preorderTraversal(BinaryTreeNode<Integer> tree) {
        return solOne(tree);
    }

    /**
     * 思路一：利用堆栈实现迭代方式遍历树，在遍历的时候，先将当前节点的值添加到结果，
     * 再依次将右子树的节点和左子树的节点压入堆栈
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<Integer> solOne(BinaryTreeNode<Integer> tree) {
        List<Integer> result = new LinkedList<>();
        Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
        deque.addFirst(tree);
        while (!deque.isEmpty()) {
            BinaryTreeNode<Integer> curr = deque.removeFirst();
            if (curr != null) {
                result.add(curr.data);
                deque.addFirst(curr.right);
                deque.addFirst(curr.left);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreePreorder.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
