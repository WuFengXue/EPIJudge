package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 10.2 TEST IF A BINARY TREE IS SYMMETRIC
 * <p>
 * A binary tree is symmetric if you can draw a vertical line through the root and
 * then the left subtree is the mirror image of the right subtree. The concept of
 * a symmetric binary tree is illustrated in Figure 10.3 on the facing page.
 * <p>
 * Write a program that checks whether a binary tree is symmetric.
 * <p>
 * Hint: The definition of symmetry is recursive.
 */
public class IsTreeSymmetric {
    @EpiTest(testDataFile = "is_tree_symmetric.tsv")

    public static boolean isSymmetric(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路一：构造一棵镜像树，然后与原来的树进行比较
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solOne(BinaryTreeNode<Integer> tree) {
        return tree == null || compareTree(tree, mirrorCopy(tree));
    }

    private static BinaryTreeNode<Integer> mirrorCopy(BinaryTreeNode<Integer> tree) {
        BinaryTreeNode<Integer> copy = new BinaryTreeNode<>();
        mirrorCopy(tree, copy);
        return copy;
    }

    private static void mirrorCopy(BinaryTreeNode<Integer> ori,
                                   BinaryTreeNode<Integer> copy) {
        copy.data = ori.data;
        if (ori.left != null) {
            copy.right = new BinaryTreeNode<>();
            mirrorCopy(ori.left, copy.right);
        }
        if (ori.right != null) {
            copy.left = new BinaryTreeNode<>();
            mirrorCopy(ori.right, copy.left);
        }
    }

    private static boolean compareTree(BinaryTreeNode<Integer> tree0,
                                       BinaryTreeNode<Integer> tree1) {
        if (tree0 == null && tree1 == null) {
            return true;
        } else if (tree0 != null && tree1 != null) {
            return tree0.data.equals(tree1.data)
                    && compareTree(tree0.left, tree1.left)
                    && compareTree(tree0.right, tree1.right);
        }

        return false;
    }

    /**
     * 思路二：根据定义，同时遍历左边和右边的子树，依次对节点进行比较
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 控件复杂度：O(h), h 为树的高度
     */
    private static boolean solTwo(BinaryTreeNode<Integer> tree) {
        return tree == null || checkSymmetric(tree.left, tree.right);
    }

    private static boolean checkSymmetric(BinaryTreeNode<Integer> subtree0,
                                          BinaryTreeNode<Integer> subtree1) {
        if (subtree0 == null && subtree1 == null) {
            return true;
        } else if (subtree0 != null && subtree1 != null) {
            return subtree0.data.equals(subtree1.data)
                    && checkSymmetric(subtree0.left, subtree1.right)
                    && checkSymmetric(subtree0.right, subtree1.left);
        }

        // One subtree is empty, and the other is not.
        return false;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsTreeSymmetric.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
