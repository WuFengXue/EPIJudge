package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

/**
 * 10.9 COMPUTE THE kTH NODE IN AN INORDER TRAVERSAL
 * <p>
 * It is trivial to find the kth node that appears in an inorder traversal
 * with 0(n) time complexity, where n is the number of nodes. However,
 * with additional information on each node, you can do better.
 * <p>
 * Write a program that efficiently computes the kth node appearing in
 * an inorder traversal. Assume that each node stores the number of nodes
 * in the subtree rooted at that node.
 * <p>
 * Hint: Use the divide and conquer principle.
 */
public class KthNodeInTree {
    public static BinaryTreeNode<Integer>
    findKthNodeBinaryTree(BinaryTreeNode<Integer> tree, int k) {
        return solTwo(tree, k);
    }

    /**
     * 思路二：分治法，迭代方式实现：
     * <p>
     * 如果左子树的节点数大于或等于k，则目标节点在左子树
     * <p>
     * 如果左子树的节点数加一等于k，则目标节点为当前节点
     * <p>
     * 如果左子树的节点数加一小于k，则目标节点在右子树
     * <p>
     * 时间复杂度：O(h)
     * <p>
     * 空间复杂度：O(1)
     */
    private static BinaryTreeNode<Integer> solTwo(BinaryTreeNode<Integer> tree, int k) {
        BinaryTreeNode<Integer> iter = tree;
        while (iter != null) {
            int leftSize = (iter.left != null ? iter.left.size : 0);
            // k-th node must be in left subtree of iter.
            if (leftSize >= k) {
                iter = iter.left;
            }
            // k-th node is iter itself.
            else if (leftSize + 1 == k) {
                return iter;
            }
            // k-th node must be in right subtree of iter.
            else {
                k -= (leftSize + 1);
                iter = iter.right;
            }
        }
        // If k is between 1 and the tree size, this line is unreachable.
        return null;
    }


    /**
     * 思路一：分治法，递归方式实现：
     * <p>
     * 如果左子树的节点数大于或等于k，则目标节点在左子树
     * <p>
     * 如果左子树的节点数加一等于k，则目标节点为当前节点
     * <p>
     * 如果左子树的节点数加一小于k，则目标节点在右子树
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static BinaryTreeNode<Integer> solOne(BinaryTreeNode<Integer> tree, int k) {
        if (tree == null) {
            return null;
        }

        int leftSize = tree.left != null ? tree.left.size : 0;
        if (leftSize >= k) {
            return solOne(tree.left, k);
        } else if (leftSize + 1 == k) {
            return tree;
        } else {
            return solOne(tree.right, k - leftSize - 1);
        }
    }

    public static BinaryTreeNode<Integer>
    convertToTreeWithSize(BinaryTree<Integer> original) {
        if (original == null)
            return null;
        BinaryTreeNode<Integer> left = convertToTreeWithSize(original.left);
        BinaryTreeNode<Integer> right = convertToTreeWithSize(original.right);
        int lSize = left == null ? 0 : left.size;
        int rSize = right == null ? 0 : right.size;
        return new BinaryTreeNode<>(original.data, left, right, 1 + lSize + rSize);
    }

    @EpiTest(testDataFile = "kth_node_in_tree.tsv")
    public static int findKthNodeBinaryTreeWrapper(TimedExecutor executor,
                                                   BinaryTree<Integer> tree,
                                                   int k) throws Exception {
        BinaryTreeNode<Integer> converted = convertToTreeWithSize(tree);

        BinaryTreeNode<Integer> result =
                executor.run(() -> findKthNodeBinaryTree(converted, k));

        if (result == null) {
            throw new TestFailure("Result can't be null");
        }
        return result.data;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "KthNodeInTree.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public static class BinaryTreeNode<T> {
        public T data;
        public BinaryTreeNode<T> left, right;
        public int size;

        public BinaryTreeNode(T data, BinaryTreeNode<T> left,
                              BinaryTreeNode<T> right, int size) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.size = size;
        }
    }
}
