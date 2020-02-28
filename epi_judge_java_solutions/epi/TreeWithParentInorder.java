package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 10.11 IMPLEMENT AN INORDER TRAVERSAL WITH 0(1) SPACE
 * <p>
 * The direct implementation of an inorder traversal using recursion has 0(h)
 * space complexity, where h is the height of the tree. Recursion can be
 * removed with an explicit stack, but the space complexity remains 0(h).
 * <p>
 * Write a nonrecursive program for computing the inorder traversal sequence
 * for a binary tree. Assume nodes have parent fields.
 * <p>
 * Hint: How can you tell whether a node is a left child or right child of
 * its parent?
 */
public class TreeWithParentInorder {
    @EpiTest(testDataFile = "tree_with_parent_inorder.tsv")

    public static List<Integer> inorderTraversal(BinaryTree<Integer> tree) {
        return solOne(tree);
    }

    /**
     * 思路一：关键在于如何识别当前是向下遍历、从左子树向上遍历还是从右子树向上遍历，
     * 本思路利用3个指针（prev、curr 和 next）实现。
     * <p>
     * 时间复杂度：O(n)
     * <p>
     * 空间复杂度：O(1)
     */
    private static List<Integer> solOne(BinaryTree<Integer> tree) {
        List<Integer> result = new ArrayList<>();
        BinaryTree<Integer> prev = null, curr = tree;
        while (curr != null) {
            BinaryTree<Integer> next;
            // We came down to curr from prev.
            if (curr.parent == prev) {
                if (curr.left != null) {
                    // Keep going left.
                    next = curr.left;
                } else {
                    result.add(curr.data);
                    // Done with left, so go right if right is not empty.
                    // Otherwise , go up.
                    next = (curr.right != null) ? curr.right : curr.parent;
                }
            } else if (curr.left == prev) {
                result.add(curr.data);
                // Done with left,so go right if right is not empty. Otherwise, go up.
                next = (curr.right != null) ? curr.right : curr.parent;
            } else {
                // Done with both children , so move up.
                next = curr.parent;
            }
            prev = curr;
            curr = next;
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "TreeWithParentInorder.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }
}
