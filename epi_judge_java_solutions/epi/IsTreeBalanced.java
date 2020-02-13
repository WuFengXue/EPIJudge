package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 10.1 TEST IF A BINARY TREE IS HEIGHT-BALANCED
 * <p>
 * A binary tree is said to be height-balanced if for each node in the tree, the
 * difference in the height of its left and right subtrees is at most one. A perfect
 * binary tree is height-balanced, as is a complete binary tree. A height-balanced
 * binary tree does not have to be perfect or complete—see Figure 10.2 on the facing
 * page for an example.
 * <p>
 * Write a program that takes as input the root of a binary tree and checks whether
 * the tree is height-balanced.
 * <p>
 * Hint: Think of a classic binary tree algorithm.
 */
public class IsTreeBalanced {

    @EpiTest(testDataFile = "is_tree_balanced.tsv")

    public static boolean isBalanced(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路二：用一个类缓存平衡状态和高度，后序遍历树，用递归的方式获取树高
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static boolean solTwo(BinaryTreeNode<Integer> tree) {
        return checkBalanced(tree).balanced;
    }

    private static BalanceStatusWithHeight checkBalanced(BinaryTreeNode<Integer> tree) {
        // Base case.
        if (tree == null) {
            return new BalanceStatusWithHeight(true, -1);
        }

        BalanceStatusWithHeight leftResult = checkBalanced(tree.left);
        // Left subtree is not balanced.
        if (!leftResult.balanced) {
            return leftResult;
        }

        // Right subtree is not balanced.
        BalanceStatusWithHeight rightResult = checkBalanced(tree.right);
        if (!rightResult.balanced) {
            return rightResult;
        }

        boolean isBalanced = Math.abs(leftResult.height - rightResult.height) <= 1;
        int height = Math.max(leftResult.height, rightResult.height) + 1;
        return new BalanceStatusWithHeight(isBalanced, height);
    }

    /**
     * 思路一：后序遍历树，用迭代的方式获取树高
     * <p>
     * 时间复杂度：> O(n)
     * <p>
     * 空间复杂度：O(2 ^ h)
     */
    private static boolean solOne(BinaryTreeNode<Integer> tree) {
        if (tree == null) {
            return true;
        }

        return solOne(tree.left)
                && solOne(tree.right)
                && Math.abs(getTreeHeight(tree.left) - getTreeHeight(tree.right)) <= 1;
    }

    private static int getTreeHeight(BinaryTreeNode<Integer> tree) {
        int height = -1;
        Queue<BinaryTreeNode<Integer>> currDepthNodes = new LinkedList<>();
        currDepthNodes.add(tree);
        while (!currDepthNodes.isEmpty()) {
            height++;
            Queue<BinaryTreeNode<Integer>> nextDepthNodes = new LinkedList<>();
            while (!currDepthNodes.isEmpty()) {
                BinaryTreeNode<Integer> node = currDepthNodes.remove();
                if (node != null) {
                    nextDepthNodes.add(node.left);
                    nextDepthNodes.add(node.right);
                }
            }
            currDepthNodes = nextDepthNodes;
        }
        return height;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsTreeBalanced.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class BalanceStatusWithHeight {
        boolean balanced;
        int height;

        public BalanceStatusWithHeight(boolean balanced, int height) {
            this.balanced = balanced;
            this.height = height;
        }
    }
}
