package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 10.14 FORM A LINKED LIST FROM THE LEAVES OF A BINARY TREE
 * <p>
 * In some applications of a binary tree, only the leaf nodes contain actual information.
 * For example, the outcomes of matches in a tennis tournament can be represented by a
 * binary tree where leaves are players. The internal nodes correspond to matches, with a
 * single winner advancing. For such a tree, we can link the leaves to get a list of
 * participants.
 * <p>
 * Given a binary tree, compute a linked list from the leaves of the binary tree. The
 * leaves should appear in left-to-right order. For example, when applied to the binary
 * tree in Figure 10.1 on Page 150, your function should return (D, E, H, M, N, P).
 * <p>
 * Hint: Build the list incrementally—it's easy if the partial list is a global.
 */
public class TreeConnectLeaves {

    public static List<BinaryTreeNode<Integer>>
    createListOfLeaves(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路二：前序遍历树，依次添加全部的叶子节点，利用堆栈实现迭代方式遍历。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<BinaryTreeNode<Integer>> solTwo(BinaryTreeNode<Integer> tree) {
        List<BinaryTreeNode<Integer>> leaves = new LinkedList<>();
        Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
        deque.addFirst(tree);
        while (!deque.isEmpty()) {
            BinaryTreeNode<Integer> node = deque.removeFirst();
            if (node == null) {
                continue;
            }
            if (node.left == null && node.right == null) {
                leaves.add(node);
                continue;
            }
            deque.addFirst(node.right);
            deque.addFirst(node.left);
        }
        return leaves;
    }

    /**
     * 思路一：前序遍历树，依次添加全部的叶子节点，递归方式实现。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<BinaryTreeNode<Integer>> solOne(BinaryTreeNode<Integer> tree) {
        List<BinaryTreeNode<Integer>> leaves = new LinkedList<>();
        addLeavesLeftToRight(tree, leaves);
        return leaves;
    }

    private static void addLeavesLeftToRight(BinaryTreeNode<Integer> tree,
                                             List<BinaryTreeNode<Integer>> leaves) {
        if (tree == null) {
            return;
        }

        if (tree.left == null && tree.right == null) {
            leaves.add(tree);
            return;
        }

        addLeavesLeftToRight(tree.left, leaves);
        addLeavesLeftToRight(tree.right, leaves);
    }

    @EpiTest(testDataFile = "tree_connect_leaves.tsv")
    public static List<Integer>
    createListOfLeavesWrapper(TimedExecutor executor,
                              BinaryTreeNode<Integer> tree) throws Exception {
        List<BinaryTreeNode<Integer>> result =
                executor.run(() -> createListOfLeaves(tree));

        if (result.stream().anyMatch(x -> x == null || x.data == null)) {
            throw new TestFailure("Result can't contain null");
        }

        List<Integer> extractedRes = new ArrayList<>();
        for (BinaryTreeNode<Integer> x : result) {
            extractedRes.add(x.data);
        }
        return extractedRes;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeConnectLeaves.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
