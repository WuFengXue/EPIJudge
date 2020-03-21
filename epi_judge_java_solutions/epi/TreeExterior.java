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
 * 10.15 COMPUTE THE EXTERIOR OF A BINARY TREE
 * <p>
 * The exterior of a binary tree is the following sequence of nodes: the nodes from
 * the root to the leftmost leaf, followed by the leaves in left-to-right order,
 * followed by the nodes from the rightmost leaf to the root. (By leftmost
 * (rightmost) leaf, we mean the leaf that appears first (last) in an inorder
 * traversal.) For example, the exterior of the binary tree in Figure10.1 on Page150
 * is {A,B,C,D,E,H,M,N,P,O,I).
 * <p>
 * Write a program that computes the exterior of a binary tree.
 * <p>
 * Hint: Handle the root's left child and right child in mirror fashion.
 */
public class TreeExterior {

    public static List<BinaryTreeNode<Integer>>
    exteriorBinaryTree(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路二：将树的轮廓分为3部分：根节点，左子树的左下轮廓和右子树的下右轮廓，依次添加
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<BinaryTreeNode<Integer>> solTwo(BinaryTreeNode<Integer> tree) {
        List<BinaryTreeNode<Integer>> exterior = new ArrayList<>();
        if (tree != null) {
            exterior.add(tree);
            addLeftBoundaryAndLeaves(tree.left, exterior, true);
            addRightLeavesAndBoundary(tree.right, exterior, true);
        }
        return exterior;
    }

    /**
     * Computes the nodes from the root to the leftmost leaf followed by all the
     * leaves in subtreeRoot.
     */
    private static void addLeftBoundaryAndLeaves(BinaryTreeNode<Integer> tree,
                                                 List<BinaryTreeNode<Integer>> exterior,
                                                 boolean isBoundary) {
        if (tree == null) {
            return;
        }
        if (tree.left == null && tree.right == null) {
            exterior.add(tree);
            return;
        }
        if (isBoundary) {
            exterior.add(tree);
        }
        addLeftBoundaryAndLeaves(tree.left, exterior, isBoundary);
        addLeftBoundaryAndLeaves(tree.right, exterior, isBoundary && tree.left == null);
    }

    /**
     * Computes the leaves in left-to-right order followed by the rightmost leaf
     * to the root path in subtreeRoot.
     */
    private static void addRightLeavesAndBoundary(BinaryTreeNode<Integer> tree,
                                                  List<BinaryTreeNode<Integer>> exterior,
                                                  boolean isBoundary) {
        if (tree == null) {
            return;
        }
        if (tree.left == null && tree.right == null) {
            exterior.add(tree);
            return;
        }
        addRightLeavesAndBoundary(tree.left, exterior, isBoundary && tree.right == null);
        addRightLeavesAndBoundary(tree.right, exterior, isBoundary);
        if (isBoundary) {
            exterior.add(tree);
        }
    }

    /**
     * 思路一：将树的轮廓分为4个部分：根节点，左侧轮廓，底部的叶子，右侧轮廓，依次添加
     * <p>
     * 时间复杂度：O（h + n + h）
     * <p>
     * 空间复杂度：O(h)
     */
    private static List<BinaryTreeNode<Integer>> solOne(BinaryTreeNode<Integer> tree) {
        List<BinaryTreeNode<Integer>> exterior = new ArrayList<>();
        if (tree != null) {
            if (tree.left != null || tree.right != null) {
                exterior.add(tree);
            }
            addLeftBoundary(tree.left, exterior);
            addLeaves(tree, exterior);
            addRightBoundary(tree.right, exterior);
        }
        return exterior;
    }

    private static void addLeftBoundary(BinaryTreeNode<Integer> tree,
                                        List<BinaryTreeNode<Integer>> exterior) {
        if (tree == null) {
            return;
        }

        while (tree != null) {
            if (tree.left != null || tree.right != null) {
                exterior.add(tree);
            }
            tree = (tree.left != null) ? tree.left : tree.right;
        }
    }

    private static void addLeaves(BinaryTreeNode<Integer> tree,
                                  List<BinaryTreeNode<Integer>> exterior) {
        Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
        deque.addFirst(tree);
        while (!deque.isEmpty()) {
            BinaryTreeNode<Integer> node = deque.removeFirst();
            if (node == null) {
                continue;
            }
            if (node.left == null && node.right == null) {
                exterior.add(node);
                continue;
            }
            deque.addFirst(node.right);
            deque.addFirst(node.left);
        }
    }

    private static void addRightBoundary(BinaryTreeNode<Integer> tree,
                                         List<BinaryTreeNode<Integer>> exterior) {
        if (tree == null) {
            return;
        }

        Deque<BinaryTreeNode<Integer>> rightBoundary = new LinkedList<>();
        while (tree != null) {
            if (tree.left != null || tree.right != null) {
                rightBoundary.addFirst(tree);
            }
            tree = (tree.right != null) ? tree.right : tree.left;
        }
        exterior.addAll(rightBoundary);
    }

    private static List<Integer> createOutputList(List<BinaryTreeNode<Integer>> L)
            throws TestFailure {
        if (L.contains(null)) {
            throw new TestFailure("Resulting list contains null");
        }
        List<Integer> output = new ArrayList<>();
        for (BinaryTreeNode<Integer> l : L) {
            output.add(l.data);
        }
        return output;
    }

    @EpiTest(testDataFile = "tree_exterior.tsv")
    public static List<Integer>
    exteriorBinaryTreeWrapper(TimedExecutor executor,
                              BinaryTreeNode<Integer> tree) throws Exception {
        List<BinaryTreeNode<Integer>> result =
                executor.run(() -> exteriorBinaryTree(tree));

        return createOutputList(result);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeExterior.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
