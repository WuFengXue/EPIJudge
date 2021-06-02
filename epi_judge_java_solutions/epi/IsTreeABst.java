package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 15.1 TEST IF A BINARY TREE SATISFIES THE BST PROPERTY
 * <p>
 * Write a program that takes as input a binary tree and checks if the tree
 * satisfies the BST property.
 * <p>
 * Hint: Is it correct to check for each node that its key is greater than
 * or equal to the key at its left child and less than or equal to the key
 * at its right child?
 */
public class IsTreeABst {
    @EpiTest(testDataFile = "is_tree_a_bst.tsv")

    public static boolean isBinaryTreeBST(BinaryTreeNode<Integer> tree) {
        return solThree(tree);
    }

    /**
     * 思路三：思路二的迭代法实现，借助一个队列实现 BFS 遍历，封装一个队列的元素类，
     * 包含树，下限和上限
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(2 ^ h)
     */
    private static boolean solThree(BinaryTreeNode<Integer> tree) {
        Queue<QueueEntry> bfsQueue = new LinkedList<>();
        bfsQueue.add(new QueueEntry(tree, Integer.MIN_VALUE, Integer.MAX_VALUE));
        while (!bfsQueue.isEmpty()) {
            QueueEntry entry = bfsQueue.poll();
            if (entry.tree == null) {
                continue;
            } else if (entry.tree.data < entry.lower || entry.tree.data > entry.upper) {
                return false;
            } else {
                bfsQueue.add(new QueueEntry(entry.tree.left, entry.lower, entry.tree.data));
                bfsQueue.add(new QueueEntry(entry.tree.right, entry.tree.data, entry.upper));
            }
        }
        return true;
    }

    /**
     * 思路二：递归法，利用二叉搜索树的特性：
     * <p>
     * 1、左子树 <= 父树 <= 右子树
     * <p>
     * 2、假定父树的值为 w，边界为[l, u]，则：
     * <p>
     * 左子树的边界为 [l，w]
     * <p>
     * 右子树的边界为 [w, u]
     * <p>
     * 前序遍历树，在遍历子树的时候，更新边界，如果发现有某个节点违反了上述特性，
     * 则返回 false
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(h)
     */
    private static boolean solTwo(BinaryTreeNode<Integer> tree) {
        return isBinaryTreeBST(tree, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static boolean isBinaryTreeBST(BinaryTreeNode<Integer> tree,
                                           int lower,
                                           int upper) {
        if (tree == null) {
            return true;
        } else if (tree.data < lower || tree.data > upper) {
            return false;
        }

        return isBinaryTreeBST(tree.left, lower, tree.data)
                && isBinaryTreeBST(tree.right, tree.data, upper);
    }

    /**
     * 思路一：中序遍历树，将树的数据转换成对应的数组，然后再检查数组是否从小到大排列
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(n)
     */
    private static boolean solOne(BinaryTreeNode<Integer> tree) {
        List<Integer> dataList = new ArrayList<>();
        bstToDataList(tree, dataList);
        for (int i = 0; i + 1 < dataList.size(); i++) {
            if (dataList.get(i) > dataList.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    private static void bstToDataList(BinaryTreeNode<Integer> tree,
                                      List<Integer> dataList) {
        if (tree == null) {
            return;
        }

        bstToDataList(tree.left, dataList);
        dataList.add(tree.data);
        bstToDataList(tree.right, dataList);
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "IsTreeABst.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    private static class QueueEntry {
        BinaryTreeNode<Integer> tree;
        int lower;
        int upper;

        public QueueEntry(BinaryTreeNode<Integer> tree,
                          int lower,
                          int upper) {
            this.tree = tree;
            this.lower = lower;
            this.upper = upper;
        }
    }
}
