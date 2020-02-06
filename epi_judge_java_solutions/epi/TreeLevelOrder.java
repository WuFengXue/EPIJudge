package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 9.7 COMPUTE BINARY TREE NODES IN ORDER OF INCREASING DEPTH
 * <p>
 * Binary trees are formally defined in Chapter 10. In particular, each node in a
 * binary tree has a depth, which is its distance from the root.
 * <p>
 * Given a binary tree, return an array consisting of the keys at the same level.
 * Keys should appear in the order of the corresponding nodes' depths, breaking ties
 * from left to right. For example, you should return ((314), (6, 6),
 * (271, 561, 2, 271), (28, 0, 3,1, 28), (17,401, 257), (641)} for the binary tree
 * in Figure 10.1 on Page 150.
 * <p>
 * Hint: First think about solving this problem with a pair of queues.
 */
public class TreeLevelOrder {
    @EpiTest(testDataFile = "tree_level_order.tsv")

    public static List<List<Integer>>
    binaryTreeDepthOrder(BinaryTreeNode<Integer> tree) {
        return solTwo(tree);
    }

    /**
     * 思路二：借助队列（用于缓存当前层和下一层的全部节点），采用迭代方式实现
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(m)，m 为单层的最大节点数
     */
    private static List<List<Integer>> solTwo(BinaryTreeNode<Integer> tree) {
        Queue<BinaryTreeNode<Integer>> currDepthNodes = new LinkedList<>();
        currDepthNodes.add(tree);
        List<List<Integer>> result = new ArrayList<>();

        while (!currDepthNodes.isEmpty()) {
            Queue<BinaryTreeNode<Integer>> nextDepthNodes = new LinkedList<>();
            List<Integer> thisLevel = new ArrayList<>();
            while (!currDepthNodes.isEmpty()) {
                BinaryTreeNode<Integer> curr = currDepthNodes.poll();
                if (curr != null) {
                    thisLevel.add(curr.data);

                    // Defer the null checks to the null test above.
                    nextDepthNodes.add(curr.left);
                    nextDepthNodes.add(curr.right);
                }
            }

            if (!thisLevel.isEmpty()) {
                result.add(thisLevel);
            }
            currDepthNodes = nextDepthNodes;
        }
        return result;
    }

    /**
     * 思路一：使用递归的方式实现，在遍历时传入结果列表和高度
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)，h为树的高度 【不确定】
     */
    private static List<List<Integer>> solOne(BinaryTreeNode<Integer> tree) {
        List<List<Integer>> result = new ArrayList();
        traversalTree(tree, result, 0);
        return result;
    }

    private static void traversalTree(BinaryTreeNode<Integer> tree,
                                      List<List<Integer>> result, int depth) {
        if (tree == null) {
            return;
        }

        if (result.size() == depth) {
            result.add(new ArrayList<>());
        }
        List<Integer> currLevel = result.get(depth);
        currLevel.add(tree.data);
        traversalTree(tree.left, result, depth + 1);
        traversalTree(tree.right, result, depth + 1);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeLevelOrder.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
