package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 10.6 FIND A ROOT TO LEAF PATH WITH SPECIFIED SUM
 * <p>
 * You are given a binary tree where each node is labeled with an integer.
 * The path weight of a node in such a tree is the sum of the integers on
 * the unique path from the root to that node. For the example shown in
 * Figure 10.1 on Page 150, the path weight of E is 591.
 * <p>
 * Write a program which takes as input an integer and a binary tree with
 * integer node weights, and checks if there exists a leaf whose path
 * weight equals the given integer.
 * <p>
 * Hint: What do you need to know about the rest of the tree when checking
 * a specific subtree?
 */
public class PathSum {
    @EpiTest(testDataFile = "path_sum.tsv")

    public static boolean hasPathSum(BinaryTreeNode<Integer> tree,
                                     int remainingWeight) {
        return solThree(tree, remainingWeight);
    }

    /**
     * 思路三：在思路二的基础上进行优化，改成遍历树的同时计算剩余的节点和
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static boolean solThree(BinaryTreeNode<Integer> tree,
                                    int remainingWeight) {
        if (tree == null) {
            return false;
        }
        remainingWeight -= tree.data;
        if (tree.left == null && tree.right == null) {
            return remainingWeight == 0;
        }
        return solThree(tree.left, remainingWeight)
                || solThree(tree.right, remainingWeight);
    }

    /**
     * 思路二：遍历树的同时计算路径的和，计算公式如下：
     * <p>
     * 子节点对应的路径和 = 父节点对应的路径和 + 子节点的数据值
     * <p>
     * 遇到叶子节点，直接返回路径和是否和目标路径和一致
     * <p>
     * 遇到非叶子节点，则判断其左右子节点的结果，有一个找到了即返回true
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static boolean solTwo(BinaryTreeNode<Integer> tree,
                                  int targetSum) {
        return pathSumHelper(tree, targetSum, 0);
    }

    /**
     * 思路一：遍历树，利用堆栈计算各条路径的和，遇到某个路径的和与目标路径和一致，
     * 则返回true
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static boolean solOne(BinaryTreeNode<Integer> tree,
                                  int targetSum) {
        Deque<BinaryTreeNode<Integer>> pathDeque = new LinkedList<>();
        return pathSumHelper(tree, targetSum, pathDeque);
    }

    private static boolean pathSumHelper(BinaryTreeNode<Integer> tree,
                                         int targetSum,
                                         Deque<BinaryTreeNode<Integer>> pathDeque) {
        if (tree == null) {
            return false;
        }
        pathDeque.addFirst(tree);
        if (tree.left == null && tree.right == null) {
            int pathSum = getPathSum(pathDeque);
            pathDeque.removeFirst();
            return pathSum == targetSum;
        }
        boolean result = pathSumHelper(tree.left, targetSum, pathDeque)
                || pathSumHelper(tree.right, targetSum, pathDeque);
        pathDeque.removeFirst();
        return result;
    }

    private static int getPathSum(Deque<BinaryTreeNode<Integer>> pathDeque) {
        int pathSum = 0;
        for (BinaryTreeNode<Integer> node : pathDeque) {
            pathSum += node.data;
        }
        return pathSum;
    }

    private static boolean pathSumHelper(BinaryTreeNode<Integer> tree,
                                         int targetSum,
                                         int partialPathSum) {
        if (tree == null) {
            return false;
        }
        partialPathSum += tree.data;
        // Leaf.
        if (tree.left == null && tree.right == null) {
            return partialPathSum == targetSum;
        }
        // Non-leaf.
        return pathSumHelper(tree.left, targetSum, partialPathSum)
                || pathSumHelper(tree.right, targetSum, partialPathSum);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "PathSum.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
