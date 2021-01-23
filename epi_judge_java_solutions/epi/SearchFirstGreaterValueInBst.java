package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

/**
 * 15.2 FIND THE FIRST KEY GREATER THAN A GIVEN VALUE IN A BST
 * <p>
 * Write a program that takes as input a BST and a value, and returns
 * the first key that would appear in an inorder traversal which is
 * greater than the input value. For example, when applied to the BST
 * in Figure 15.1 on Page 255 you should return 29 for input 23.
 * <p>
 * Hint: Perform binary search, keeping some additional state.
 */
public class SearchFirstGreaterValueInBst {

    public static BstNode<Integer> findFirstGreaterThanK(BstNode<Integer> tree,
                                                         Integer k) {
        return solTwo(tree, k);
    }

    /**
     * 思路二：利用二叉搜索树的特性，从根节点开始查找：
     * <p>
     * 如果当前节点的值比 k 大，则更新目标节点为当前节点，然后遍历当前节点的左子节点
     * <p>
     * 如果当前节点的值比 k 小，则遍历当前节点的右子节点
     * <p>
     * 时间复杂度：O(h)，h 为树高
     * <p>
     * 空间复杂度：O(1)
     */
    private static BstNode<Integer> solTwo(BstNode<Integer> tree,
                                           Integer k) {
        BstNode<Integer> first = null;
        while (tree != null) {
            if (tree.data > k) {
                first = tree;
                tree = tree.left;
            } else {
                tree = tree.right;
            }
        }
        return first;
    }

    /**
     * 思路一：中序遍历树（递归法），利用二叉搜索树有序的特性，遇到第一个大于 k 的
     * 节点即是目标节点
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(log(n))
     */
    private static BstNode<Integer> solOne(BstNode<Integer> tree,
                                           Integer k) {
        if (tree == null) {
            return null;
        }

        BstNode<Integer> first = solOne(tree.left, k);
        if (first != null) {
            return first;
        } else if (tree.data > k) {
            return tree;
        } else {
            return solOne(tree.right, k);
        }
    }

    @EpiTest(testDataFile = "search_first_greater_value_in_bst.tsv")
    public static int findFirstGreaterThanKWrapper(BstNode<Integer> tree,
                                                   Integer k) {
        BstNode<Integer> result = findFirstGreaterThanK(tree, k);
        return result != null ? result.data : -1;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "SearchFirstGreaterValueInBst.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
