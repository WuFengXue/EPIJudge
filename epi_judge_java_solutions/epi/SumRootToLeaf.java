package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.*;

/**
 * 10.5 SUM THE ROOT-TO-LEAF PATHS IN A BINARY TREE
 * <p>
 * Consider a binary tree in which each node contains a binary digit. A
 * root-to-leaf path can be associated with a binary number—the MSB is at
 * the root. As an example, the binary tree in Figure 10.4 represents the
 * numbers (1000)2, (1001)2, (10110)2, (110011)2, (11000)2, and (1100)2.
 * <p>
 * Design an algorithm to compute the sum of the binary numbers represented
 * by the root-to-leaf paths.
 * <p>
 * Hint: Think of an appropriate way of traversing the tree.
 */
public class SumRootToLeaf {
    @EpiTest(testDataFile = "sum_root_to_leaf.tsv")

    public static int sumRootToLeaf(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路二：遍历树的同时计算每个节点对应的值，计算公式如下：
     * <p>
     * 子节点的值 = 父节点的值 * 2 + 子节点的数据值
     * <p>
     * 遇到叶子节点时，直接返回路径对应的值
     * <p>
     * 遇到非叶子节点时，返回两个子节点的计算结果之和
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static int solTwo(BinaryTreeNode<Integer> tree) {
        return sumRootToLeafHelper(tree, 0);
    }

    private static int sumRootToLeafHelper(BinaryTreeNode<Integer> tree,
                                           int partialPathSum) {
        if (tree == null) {
            return 0;
        }

        partialPathSum = partialPathSum * 2 + tree.data;
        // Leaf.
        if (tree.left == null && tree.right == null) {
            return partialPathSum;
        }
        // Non-leaf.
        return sumRootToLeafHelper(tree.left, partialPathSum)
                + sumRootToLeafHelper(tree.right, partialPathSum);
    }

    /**
     * 思路一：遍历树，利用堆栈依次计算出每条路径的值，结果即为全部路径的值之和
     * <p>
     * 遇到叶子节点时，返回路径对应的值
     * <p>
     * 遇到非叶子节点时，返回两个子节点的结果之和
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static int solOne(BinaryTreeNode<Integer> tree) {
        Deque<BinaryTreeNode<Integer>> pathDeque = new LinkedList<>();
        return sumRootToLeafHelper(tree, pathDeque);
    }

    /**
     * 思路零：遍历树，用哈希表记录子节点对应的父节点，最后再从叶子节点逐级向上读取哈希表
     * 即可获得完整的路径
     * <p>
     * 说明：因为存在哈希冲突的场景，该思路无法通过验证
     * <p>
     * 时间复杂度：O(Lh)，L为叶子节点的数量，h为树高
     * <p>
     * 空间复杂度：O(n)
     */
    private static int solZero(BinaryTreeNode<Integer> tree) {
        Map<BinaryTreeNode<Integer>, BinaryTreeNode<Integer>> childParentMap = new HashMap<>();
        List<BinaryTreeNode<Integer>> leafList = new ArrayList<>();
        traversal(tree, childParentMap, leafList);
        int sum = 0;
        for (BinaryTreeNode<Integer> leaf : leafList) {
            sum += getPathSum(childParentMap, leaf);
        }
        return sum;
    }

    private static void traversal(BinaryTreeNode<Integer> tree,
                                  Map<BinaryTreeNode<Integer>, BinaryTreeNode<Integer>> childParentMap,
                                  List<BinaryTreeNode<Integer>> leafList) {
        if (tree == null) {
            return;
        }
        if (tree.left == null && tree.right == null) {
            leafList.add(tree);
            return;
        }
        if (tree.left != null) {
            childParentMap.put(tree.left, tree);
            traversal(tree.left, childParentMap, leafList);
        }
        if (tree.right != null) {
            childParentMap.put(tree.right, tree);
            traversal(tree.right, childParentMap, leafList);
        }
    }

    private static int getPathSum(Map<BinaryTreeNode<Integer>, BinaryTreeNode<Integer>> childParentMap,
                                  BinaryTreeNode<Integer> leaf) {
        int pathSum = leaf.data;
        long power = 2;
        while (childParentMap.containsKey(leaf)) {
            BinaryTreeNode<Integer> parent = childParentMap.get(leaf);
            pathSum += parent.data * power;
            leaf = parent;
            power *= 2;
        }
        return pathSum;
    }

    private static int sumRootToLeafHelper(BinaryTreeNode<Integer> tree,
                                           Deque<BinaryTreeNode<Integer>> pathDeque) {
        if (tree == null) {
            return 0;
        }
        pathDeque.addFirst(tree);
        if (tree.left == null && tree.right == null) {
            int pathSum = getPathSum(pathDeque);
            pathDeque.removeFirst();
            return pathSum;
        }
        int result = sumRootToLeafHelper(tree.left, pathDeque)
                + sumRootToLeafHelper(tree.right, pathDeque);
        pathDeque.removeFirst();
        return result;
    }

    private static int getPathSum(Deque<BinaryTreeNode<Integer>> pathDeque) {
        int pathSum = 0;
        Iterator<BinaryTreeNode<Integer>> it = pathDeque.descendingIterator();
        while (it.hasNext()) {
            pathSum = pathSum * 2 + it.next().data;
        }
        return pathSum;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SumRootToLeaf.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
