package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 10.7 IMPLEMENT AN INORDER TRAVERSAL WITHOUT RECURSION
 * <p>
 * This problem is concerned with traversing nodes in a binary tree in an
 * inorder fashion. See Page 151 for details and examples of these
 * traversals. Generally speaking, a traversal computation is easy to
 * implement if recursion is allowed.
 * <p>
 * Write a program which takes as input a binary tree and performs an
 * inorder traversal of the tree. Do not use recursion. Nodes do not contain
 * parent references.
 * <p>
 * Hint: Simulate the function call stack.
 */
public class TreeInorder {
    @EpiTest(testDataFile = "tree_inorder.tsv")

    public static List<Integer> inorderTraversal(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路二：利用堆栈实现迭代方式遍历树，不扩展是否访问过字段，在遍历树的时候
     * 只将父节点和左子树节点加入堆栈
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<Integer> solTwo(BinaryTreeNode<Integer> tree) {
        List<Integer> result = new ArrayList<>();
        Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
        BinaryTreeNode<Integer> curr = tree;
        while (!deque.isEmpty() || curr != null) {
            if (curr != null) {
                deque.addFirst(curr);
                // Going left.
                curr = curr.left;
            } else {
                // Going up.
                curr = deque.removeFirst();
                result.add(curr.data);
                // Going right.
                curr = curr.right;
            }
        }
        return result;
    }

    /**
     * 思路一：利用堆栈实现迭代方式遍历树，对树的节点进行封装，扩展一个是否访问过
     * 字段，在遍历树的时候，依次将右子树节点、父节点（将是否访问过字段置为true）
     * 和左子树节点加入堆栈
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<Integer> solOne(BinaryTreeNode<Integer> tree) {
        List<Integer> result = new ArrayList<>();
        Deque<BinaryTreeNodeEx<Integer>> deque = new LinkedList<>();
        deque.addFirst(new BinaryTreeNodeEx<>(tree, false));
        while (!deque.isEmpty()) {
            BinaryTreeNodeEx<Integer> nodeEx = deque.removeFirst();
            if (nodeEx == null || nodeEx.node == null) {
                continue;
            }
            if (nodeEx.visited) {
                result.add(nodeEx.node.data);
                continue;
            }
            deque.addFirst(new BinaryTreeNodeEx<>(nodeEx.node.right, false));
            nodeEx.visited = true;
            deque.addFirst(nodeEx);
            deque.addFirst(new BinaryTreeNodeEx<>(nodeEx.node.left, false));
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeInorder.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class BinaryTreeNodeEx<T> {
        BinaryTreeNode<T> node;
        boolean visited;

        BinaryTreeNodeEx(BinaryTreeNode<T> node, boolean visited) {
            this.node = node;
            this.visited = visited;
        }
    }
}
