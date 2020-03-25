package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.*;

/**
 * 10.16 COMPUTE THE RIGHT SIBLING TREE
 * <p>
 * For this problem, assume that each binary tree node has a extra field, call it
 * level-next, that holds a binary tree node (this field is distinct from the fields
 * for the left and right children). The level-next field will be used to compute
 * a map from nodes to their right siblings. The input is assumed to be perfect
 * binary tree. See Figure 10.6 for an example.
 * <p>
 * Write a program that takes a perfect binary tree, and sets each node's level-next
 * field to the node on its right, if one exists.
 * <p>
 * Hint: Think of an appropriate traversal order.
 */
public class TreeRightSibling {
    public static void constructRightSibling(BinaryTreeNode<Integer> tree) {
        solOne(tree);
        return;
    }

    /**
     * 思路二：观察法，完全二叉树的节点具有如下特性：
     * <p>
     * 左子树节点的 next 节点为右子树节点
     * <p>
     * 右子树节点的 next 节点为 next 节点的左子树节点
     * <p>
     * 逐层遍历树，每层都从最左侧的节点开始
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static void solTwo(BinaryTreeNode<Integer> tree) {
        while (tree != null && tree.left != null) {
            populateLowerLevelNextField(tree);
            tree = tree.left;
        }
    }

    private static void populateLowerLevelNextField(BinaryTreeNode<Integer> tree) {
        while (tree != null) {
            // Populate left child’s next field.
            tree.left.next = tree.right;
            // Populate right child’s next field if iter is not the last node of this
            // level.
            if (tree.next != null) {
                tree.right.next = tree.next.left;
            }
            tree = tree.next;
        }
    }

    /**
     * 思路一：逐层遍历树，利用两个队列缓存当前层和下一层的节点，下一层的队列构造完成后，
     * 就从左往右写 next 字段
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O（l），l为叶子节点数
     */
    private static void solOne(BinaryTreeNode<Integer> tree) {
        Queue<BinaryTreeNode<Integer>> currLevel = new LinkedList<>();
        if (tree != null) {
            currLevel.offer(tree);
        }
        while (!currLevel.isEmpty()) {
            Queue<BinaryTreeNode<Integer>> nextLevel = new LinkedList<>();
            while (!currLevel.isEmpty()) {
                BinaryTreeNode<Integer> node = currLevel.poll();
                if (node.left != null) {
                    nextLevel.offer(node.left);
                }
                if (node.right != null) {
                    nextLevel.offer(node.right);
                }
            }
            populateLevelNextField(nextLevel);
            currLevel = nextLevel;
        }
    }

    private static void populateLevelNextField(Queue<BinaryTreeNode<Integer>> level) {
        if (level.isEmpty()) {
            return;
        }

        Iterator<BinaryTreeNode<Integer>> it = level.iterator();
        BinaryTreeNode<Integer> curr = it.next();
        while (it.hasNext()) {
            BinaryTreeNode<Integer> next = it.next();
            curr.next = next;
            curr = next;
        }
    }

    private static BinaryTreeNode<Integer>
    cloneTree(BinaryTree<Integer> original) {
        if (original == null) {
            return null;
        }
        BinaryTreeNode<Integer> cloned = new BinaryTreeNode<>(original.data);
        cloned.left = cloneTree(original.left);
        cloned.right = cloneTree(original.right);
        return cloned;
    }

    @EpiTest(testDataFile = "tree_right_sibling.tsv")
    public static List<List<Integer>>
    constructRightSiblingWrapper(TimedExecutor executor, BinaryTree<Integer> tree)
            throws Exception {
        BinaryTreeNode<Integer> cloned = cloneTree(tree);

        executor.run(() -> constructRightSibling(cloned));

        List<List<Integer>> result = new ArrayList<>();
        BinaryTreeNode<Integer> levelStart = cloned;
        while (levelStart != null) {
            List<Integer> level = new ArrayList<>();
            BinaryTreeNode<Integer> levelIt = levelStart;
            while (levelIt != null) {
                level.add(levelIt.data);
                levelIt = levelIt.next;
            }
            result.add(level);
            levelStart = levelStart.left;
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeRightSibling.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public static class BinaryTreeNode<T> {
        public T data;
        public BinaryTreeNode<T> left, right;
        public BinaryTreeNode<T> next = null; // Populates this field.

        public BinaryTreeNode(T data) {
            this.data = data;
        }
    }
}
