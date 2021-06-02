package epi;

import epi.test_framework.*;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 10.3 COMPUTE THE LOWEST COMMON ANCESTOR IN A BINARY TREE
 * <p>
 * Any two nodes in a binary tree have a common ancestor, namely the root.
 * The lowest common ancestor (LCA) of any two nodes in a binary tree is the
 * node furthest from the root that is an ancestor of both nodes. For example,
 * the LCA of M and N in Figure 10.1 on Page 150 is K.
 * <p>
 * Computing the LCA has important applications. For example, it is an essential
 * calculation when rendering web pages, specifically when computing the
 * Cascading Style Sheet (CSS) that is applicable to a particular Document
 * Object Model (DOM) element.
 * <p>
 * Design an algorithm for computing the LCA of two nodes in a binary tree in
 * which nodes do not have a parent field.
 * <p>
 * Hint: When is the root the LCA?
 */
public class LowestCommonAncestor {
    public static BinaryTreeNode<Integer> LCA(BinaryTreeNode<Integer> tree,
                                              BinaryTreeNode<Integer> node0,
                                              BinaryTreeNode<Integer> node1) {
        return solTwo(tree, node0, node1);
    }

    /**
     * 思路二：用一个类存储结果（包含祖先节点和目标节点数量），后序（左、右、父）遍历树
     * 进行查找
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTreeNode<Integer> solTwo(BinaryTreeNode<Integer> tree,
                                                  BinaryTreeNode<Integer> node0,
                                                  BinaryTreeNode<Integer> node1) {
        return LCAHelper(tree, node0, node1).ancestor;
    }

    // Returns an object consisting of an int and a node. The int field is
    // ®, 1, or 2 depending on how many of {node®, nodel} are present in
    // the tree. If both are present in the tree, when ancestor is
    // assigned to a non-null value, it is the LCA.
    private static Status LCAHelper(BinaryTreeNode<Integer> tree,
                                    BinaryTreeNode<Integer> node0,
                                    BinaryTreeNode<Integer> node1) {
        if (tree == null) {
            return new Status(0, null);
        }

        Status leftResult = LCAHelper(tree.left, node0, node1);
        if (leftResult.numTargetNodes == 2) {
            // Found both nodes in the left subtree.
            return leftResult;
        }
        Status rightResult = LCAHelper(tree.right, node0, node1);
        if (rightResult.numTargetNodes == 2) {
            // Found both nodes in the right subtree.
            return rightResult;
        }
        int numTargetNodes = leftResult.numTargetNodes + rightResult.numTargetNodes
                + (tree == node0 ? 1 : 0) + (tree == node1 ? 1 : 0);
        return new Status(numTargetNodes, numTargetNodes == 2 ? tree : null);
    }

    /**
     * 思路一：查找根节点到目标节点的路径（利用堆栈），然后再比较两条路径，
     * 最后一个相同节点即为LCA
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTreeNode<Integer> solOne(BinaryTreeNode<Integer> tree,
                                                  BinaryTreeNode<Integer> node0,
                                                  BinaryTreeNode<Integer> node1) {
        Deque<BinaryTreeNode<Integer>> deque0 = new LinkedList<>();
        if (!findPath(tree, node0, deque0)) {
            return null;
        }

        Deque<BinaryTreeNode<Integer>> deque1 = new LinkedList<>();
        if (!findPath(tree, node1, deque1)) {
            return null;
        }

        BinaryTreeNode<Integer> result = null;
        while (!deque0.isEmpty() && !deque1.isEmpty()) {
            BinaryTreeNode<Integer> n0 = deque0.removeLast();
            BinaryTreeNode<Integer> n1 = deque1.removeLast();
            if (n0 == n1) {
                result = n0;
            }
        }
        return result;
    }

    private static boolean findPath(BinaryTreeNode<Integer> tree,
                                    BinaryTreeNode<Integer> node,
                                    Deque<BinaryTreeNode<Integer>> deque) {
        if (tree == null) {
            return false;
        }

        deque.addFirst(tree);
        if (tree == node
                || findPath(tree.left, node, deque)
                || findPath(tree.right, node, deque)) {
            return true;
        }

        deque.removeFirst();
        return false;
    }

    @EpiTest(testDataFile = "lowest_common_ancestor.tsv")
    public static int lcaWrapper(TimedExecutor executor,
                                 BinaryTreeNode<Integer> tree, Integer key0,
                                 Integer key1) throws Exception {
        BinaryTreeNode<Integer> node0 = BinaryTreeUtils.mustFindNode(tree, key0);
        BinaryTreeNode<Integer> node1 = BinaryTreeUtils.mustFindNode(tree, key1);

        BinaryTreeNode<Integer> result =
                executor.run(() -> LCA(tree, node0, node1));

        if (result == null) {
            throw new TestFailure("Result can not be null");
        }
        return result.data;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "LowestCommonAncestor.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class Status {
        BinaryTreeNode<Integer> ancestor;
        int numTargetNodes;

        Status(int numTargetNodes, BinaryTreeNode<Integer> ancestor) {
            this.numTargetNodes = numTargetNodes;
            this.ancestor = ancestor;
        }
    }
}
